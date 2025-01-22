package com.aymankhachchab.ecommerce.controller;

import com.aymankhachchab.ecommerce.dto.CreateProductDto;
import com.aymankhachchab.ecommerce.dto.ResponseProductDto;
import com.aymankhachchab.ecommerce.entity.Product;
import com.aymankhachchab.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<List<ResponseProductDto>> allProducts() {
        List<ResponseProductDto> products = productService.getAllProducts();

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('{ADMIN}')")
    public ResponseEntity<ResponseProductDto> newProduct(@Valid @RequestBody CreateProductDto newProduct) {
        ResponseProductDto createdProductDto = productService.createProduct(newProduct);
        return new ResponseEntity<>(createdProductDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductDto> getProductById(@PathVariable Long id) {
        ResponseProductDto productDto = productService.getProductById(id);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseProductDto> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        ResponseProductDto updatedProductDto = productService.updateProduct(id, newProduct);
        return new ResponseEntity<>(updatedProductDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
