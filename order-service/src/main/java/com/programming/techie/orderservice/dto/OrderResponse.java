package com.programming.techie.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    public String orderNumber;
    private List<OrderLineItemsResponse> orderLineItemsResponseList;

}
