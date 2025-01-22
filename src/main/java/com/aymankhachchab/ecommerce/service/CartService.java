package com.aymankhachchab.ecommerce.service;

import com.aymankhachchab.ecommerce.dto.ResponseCartItem;
import com.aymankhachchab.ecommerce.entity.*;
import com.aymankhachchab.ecommerce.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;


    public CartService(CartRepository cartRepository, OrderRepository orderRepository, UserRepository userRepository, UserService userService, ProductRepository productRepository, OrderItemRepository orderItemRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public CartItem addToCart(Product product, int quantity) {
        User authenticatedUser = this.userService.getAuthenticatedUser();
        Cart cart = authenticatedUser.getCart();
        if (cart == null) {
            cart = new Cart();
            authenticatedUser.setCart(cart);
        }
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItem.setQuantity(quantity);



        return this.cartItemRepository.save(cartItem);

    }

    public Order makeOrder(Cart cart) {
        if (cart.getCartItems().isEmpty()) {
            return null;
        }
        Order order = new Order();
        order.setUser(this.userService.getAuthenticatedUser());
        order.setOrderItems(new ArrayList<>());

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem(cartItem.getProduct(), cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItem.setPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice().doubleValue());
            order.getOrderItems().add(orderItem);
        }

        order.setStatus(Order.OrderStatus.PENDING.name());
        this.orderRepository.save(order);

        return order;
    }

    public List<Order> getUserOrders(User user) {
        return user.getOrders();
    }

    public Order cancelOrder(Order order) {
        order.setStatus(Order.OrderStatus.CANCELLED.name());

        this.orderRepository.save(order);

        return order;
    }

    public Order getOrderById(Long id) {
        return this.orderRepository.findById(id).orElse(null);
    }


    public ResponseCartItem transformCartItemToDto(CartItem cartItem) {
        return new ResponseCartItem(cartItem.getProduct().getId(), cartItem.getProduct().getName(), cartItem.getQuantity());
    }
}
