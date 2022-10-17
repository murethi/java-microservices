package com.programming.techie.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineItemsResponse {
    private String skuCode;
    private BigDecimal price;
    private int quantity;
}
