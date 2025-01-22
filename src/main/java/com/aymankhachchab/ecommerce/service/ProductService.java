package com.aymankhachchab.ecommerce.service;

import com.aymankhachchab.ecommerce.dto.CreateProductDto;
import com.aymankhachchab.ecommerce.dto.ResponseProductDto;
import com.aymankhachchab.ecommerce.entity.Category;
import com.aymankhachchab.ecommerce.entity.Product;
import com.aymankhachchab.ecommerce.repository.CategoryRepository;
import com.aymankhachchab.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product createProduct(CreateProductDto newProduct) {
        Product product = new Product();
        product.setName(newProduct.getName());
        product.setPrice(newProduct.getPrice());
        product.setStockQuantity(newProduct.getStockQuantity());


        Category category = categoryRepository.findById(newProduct.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        product.setCategory(category);

        return productRepository.saveAndFlush(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    public Product updateProduct(Long id, Product newProductDetails) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        existingProduct.setName(newProductDetails.getName());
        existingProduct.setDescription(newProductDetails.getDescription());
        existingProduct.setPrice(newProductDetails.getPrice());

        return productRepository.saveAndFlush(existingProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public ResponseProductDto transformToDto(Product product) {
        return new ResponseProductDto(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCreatedAt(),
                product.getCategory().getName()

        );

    }
}
