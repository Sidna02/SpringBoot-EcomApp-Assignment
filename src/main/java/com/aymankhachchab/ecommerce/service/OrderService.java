package com.aymankhachchab.ecommerce.service;

import com.aymankhachchab.ecommerce.dto.ResponseOrderDto;
import com.aymankhachchab.ecommerce.entity.Order;
import com.aymankhachchab.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;


    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public ResponseOrderDto transformOrderToDto(Order order) {
        List<ResponseOrderDto.OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                .map(orderItem -> new ResponseOrderDto.OrderItemDto(
                        orderItem.getId(),
                        orderItem.getProduct().getId(),
                        orderItem.getProduct().getName(),
                        orderItem.getQuantity(),
                        orderItem.getPrice()
                ))
                .toList();

        return new ResponseOrderDto(
                order.getId(),
                order.getUser().getUsername(),
                order.getTotalAmount(),
                order.getStatus(),
                orderItemDtos
        );
    }

}
