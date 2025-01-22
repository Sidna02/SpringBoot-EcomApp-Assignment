package com.aymankhachchab.ecommerce;

import com.aymankhachchab.ecommerce.dto.*;
import com.aymankhachchab.ecommerce.entity.*;
import com.aymankhachchab.ecommerce.repository.CategoryRepository;
import com.aymankhachchab.ecommerce.repository.ProductRepository;
import com.aymankhachchab.ecommerce.repository.UserRepository;
import com.aymankhachchab.ecommerce.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class EcommerceApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CategoryService categoryService;
    @Autowired

    private ProductService productService;
    @Autowired
    private UserService userService;


    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void contextLoads() {
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void testRegisterUser() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail("testuser@gmail.com");
        registerUserDto.setPassword("password123");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(registerUserDto);

        mockMvc.perform(post("/auth/signup")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("testuser@gmail.com"));
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void testLoginUser() throws Exception {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("testuser@gmail.com");
        loginUserDto.setPassword("password123");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(loginUserDto);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }


    @Test
    @WithMockUser(username = "testuser@gmail.com", roles = {"ADMIN"})
    void testCreateCategory() throws Exception {
        String categoryName = "Electronics";
        String categoryDescription = "Electronics items like laptops and phones";
        CreateCategoryDto createCategoryDto = new CreateCategoryDto(

                categoryName, categoryDescription
        );
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(createCategoryDto);

        mockMvc.perform(post("/api/categories")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(categoryName));
    }

    @Test
    @WithMockUser(username = "testuser@gmail.com")
    void testGetAllCategories() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }


    @Test
    @WithMockUser(username = "testuser@gmail.com", roles = "{ADMIN}")
    void testCreateProduct() throws Exception {
        Category category = this.categoryService.createCategory(
                new CreateCategoryDto(
                        "Electronics",
                        "Various electronics"
                )
        );
        CreateProductDto createProductDto = new CreateProductDto("Laptop", "",  BigDecimal.valueOf(1000), 50,  category.getId());

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonContent = objectMapper.writeValueAsString(createProductDto);



        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.stockQuantity").value(50))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));
    }
    @Test
    @WithMockUser(username = "testuser@gmail.com", roles = "{USER}")
    void testCreateProductForbidden() throws Exception {
        Category category = this.categoryService.createCategory(
                new CreateCategoryDto(
                        "Electronics",
                        "Various electronics"
                )
        );
        CreateProductDto createProductDto = new CreateProductDto("Laptop", "",  BigDecimal.valueOf(1000), 50,  category.getId());

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonContent = objectMapper.writeValueAsString(createProductDto);



        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "testuser@gmail.com")
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }


    @Test
    void testGetAuthenticatedUser() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail("testuser111@gmail.com");
        registerUserDto.setPassword("password123");
        this.authenticationService.signup(registerUserDto);

        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("testuser111@gmail.com");
        loginUserDto.setPassword("password123");

        ObjectMapper objectMapper = new ObjectMapper();
        String loginJson = objectMapper.writeValueAsString(loginUserDto);

        String loginResponseJson = mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse loginResponse = objectMapper.readValue(loginResponseJson, LoginResponse.class);

        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + loginResponse.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("testuser111@gmail.com"));
    }

    @Test
    @WithMockUser(username = "testuser@gmail.com")
    public void testAddToCart() throws Exception {
        Category category = this.categoryService.createCategory(
                new CreateCategoryDto(
                        "Electronics",
                        "Various electronics"
                )
        );
      Product product =   this.productService.createProduct(new CreateProductDto(
                "Laptop",
                "desc",
                BigDecimal.valueOf(1000),
                50,
                category.getId()

        ));

       this.cartService.addToCart(product, 3);
       RequestCartItemDto requestCartItemDto = new RequestCartItemDto(
               product.getId(),
               1
       );
        mockMvc.perform(post("/api/cart/add")
                        .content(new ObjectMapper().writeValueAsString(requestCartItemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(product.getId()))
                .andExpect(jsonPath("$.quantity").value(1));
    }

    @Test
    @WithMockUser(username = "testuser@gmail.com")
    public void testMakeOrder() throws Exception {
        User user = userService.getAuthenticatedUser();

        Category category = this.categoryService.createCategory(
                new CreateCategoryDto(
                        "Electronics",
                        "Various electronics"
                )
        );
        Product product = this.productService.createProduct(
                new CreateProductDto(
                        "Laptop",
                        "desc",
                        BigDecimal.valueOf(1000),
                        10,
                        category.getId()
                )
        );
        Cart cart = user.getCart();

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setCart(cart);
        cart.setCartItems(List.of(cartItem));


        Order order = this.cartService.makeOrder(cart);


        ResponseOrderDto responseOrderDto = orderService.transformOrderToDto(order);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestContent = objectMapper.writeValueAsString(responseOrderDto);

        mockMvc.perform(post("/api/cart/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.orderItems[0].productId").value(2))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.orderItems[0].productName").value("Laptop"))
                .andExpect(jsonPath("$.orderItems[0].quantity").value(1))
                .andExpect(jsonPath("$.orderItems[0].price").value(1000));
    }
    @Test
    @WithMockUser(username = "testuser@gmail.com", roles = "{USER}")

    public void testCancelOrder() throws Exception {
        User user = userService.getAuthenticatedUser();
        Cart cart = user.getCart();


        Category category = this.categoryService.createCategory(
                new CreateCategoryDto(
                        "Electronics",
                        "Various electronics"
                )
        );
        Product product = this.productService.createProduct(
                new CreateProductDto(
                        "Laptop",
                        "desc",
                        BigDecimal.valueOf(1000),
                        10,
                        category.getId()
                )
        );

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setCart(user.getCart());
        cart.setCartItems(List.of(cartItem));


        Order order = this.cartService.makeOrder(cart);

        mockMvc.perform(post("/api/cart/order/cancel/{orderId}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.orderId").value(order.getId()));
    }
}
