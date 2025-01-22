package com.aymankhachchab.ecommerce.controller;

import com.aymankhachchab.ecommerce.dto.RequestCartItemDto;
import com.aymankhachchab.ecommerce.dto.ResponseCartItem;
import com.aymankhachchab.ecommerce.dto.ResponseOrderDto;
import com.aymankhachchab.ecommerce.entity.CartItem;
import com.aymankhachchab.ecommerce.entity.Order;
import com.aymankhachchab.ecommerce.entity.Product;
import com.aymankhachchab.ecommerce.entity.User;
import com.aymankhachchab.ecommerce.repository.ProductRepository;
import com.aymankhachchab.ecommerce.service.AuthorizationService;
import com.aymankhachchab.ecommerce.service.CartService;
import com.aymankhachchab.ecommerce.service.OrderService;
import com.aymankhachchab.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final OrderService orderService;
    @Autowired
    private final AuthorizationService authorizationService;

    public CartController(CartService cartService, UserService userService, ProductRepository productRepository, OrderService orderService, AuthorizationService authorizationService) {
        this.cartService = cartService;
        this.userService = userService;
        this.productRepository = productRepository;
        this.orderService = orderService;
        this.authorizationService = authorizationService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseCartItem> addToCart(@Valid @RequestBody RequestCartItemDto requestCartItemDto) {
        Product product = this.productRepository.findById(requestCartItemDto.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }

        CartItem cartItem = cartService.addToCart(product, requestCartItemDto.getQuantity());
        return ResponseEntity.ok(this.cartService.transformCartItemToDto(cartItem));
    }

    @PostMapping("/order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseOrderDto> makeOrder() {
        Order order = cartService.makeOrder(this.userService.getAuthenticatedUser().getCart());
        if (order == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.orderService.transformOrderToDto(order));
    }



    @GetMapping("/orders")
    public ResponseEntity<List<ResponseOrderDto>> getUserOrders() {
        User user = this.userService.getAuthenticatedUser();

        List<ResponseOrderDto> responseOrderDtos = user.getOrders().stream()
                .map(orderService::transformOrderToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseOrderDtos);
    }
    @GetMapping("/orders/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ResponseOrderDto>> getOrdersByUsername(@PathVariable String username) {
        User user = this.userService.getUserByUsername(username);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }


        List<ResponseOrderDto> responseOrderDtos = user.getOrders().stream()
                .map(orderService::transformOrderToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseOrderDtos);
    }

    @PostMapping("/order/cancel/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or @authorizationService.isOrderOwner(principal.username, #orderId)")
    public ResponseEntity<ResponseOrderDto> cancelOrder(@PathVariable Long orderId) {
        Order order = cartService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        Order cancelledOrder = cartService.cancelOrder(order);


        return ResponseEntity.ok(this.orderService.transformOrderToDto(cancelledOrder));
    }
}
