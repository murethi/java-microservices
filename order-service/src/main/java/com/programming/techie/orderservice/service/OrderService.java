package com.programming.techie.orderservice.service;

import com.programming.techie.orderservice.dto.*;
import com.programming.techie.orderservice.model.Order;
import com.programming.techie.orderservice.model.OrderLineItems;
import com.programming.techie.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);
        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode)
                .toList();
        //Check inventory service and place order if
        //item is in stock
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build()
                .get()
                .uri("http://inventory-service/api/inventory",uriBuilder
                        -> uriBuilder.queryParam("skuCode",skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        //assert inventoryResponseArray != null;
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);
       if (allProductsInStock){
           orderRepository.save(order);
           return "Order placed successfully";
       }else {
           throw new IllegalArgumentException("Product is not in stock");
       }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

    public List<OrderResponse> listOrders() {
        List<Order> orders  = orderRepository.findAll();
        return orders.stream().map(this::mapToOrderResponse).toList();
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderNumber(order.getOrderNumber())
                .orderLineItemsResponseList(order.getOrderLineItemsList().stream().map(this::mapRoOrderLineItemResponse).toList())
                .build();
    }

    private OrderLineItemsResponse mapRoOrderLineItemResponse(OrderLineItems orderLineItems) {
        return OrderLineItemsResponse.builder()
                .skuCode(orderLineItems.getSkuCode())
                .quantity(orderLineItems.getQuantity())
                .price(orderLineItems.getPrice())
                .build();
    }
}
