package com.aymankhachchab.ecommerce.service;

import com.aymankhachchab.ecommerce.dto.ResponseCartItem;
import com.aymankhachchab.ecommerce.dto.ResponseProductDto;
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
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    public CartService(CartRepository cartRepository, OrderRepository orderRepository, UserRepository userRepository, UserService userService, OrderItemRepository orderItemRepository, CartItemRepository cartItemRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

     
    public ResponseCartItem addToCart(ResponseProductDto productDto, int quantity) {
        Product p = this.productService.getEntityById(productDto.getId());
        User authenticatedUser = this.userService.getAuthenticatedUser();
        Cart cart = authenticatedUser.getCart();
        if (cart == null) {
            cart = new Cart();
            authenticatedUser.setCart(cart);
        }
        CartItem cartItem = new CartItem();
        cartItem.setProduct(p);
        cartItem.setCart(cart);
        cartItem.setQuantity(quantity);

        CartItem savedCartItem = this.cartItemRepository.save(cartItem);
        return transformCartItemToDto(savedCartItem);  
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

     
    public List<ResponseCartItem> transformCartItemsToDto(List<CartItem> cartItems) {
        List<ResponseCartItem> responseCartItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            responseCartItems.add(transformCartItemToDto(cartItem));
        }
        return responseCartItems;
    }
}
