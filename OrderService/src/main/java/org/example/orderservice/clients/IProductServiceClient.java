package org.example.orderservice.clients;

import org.example.orderservice.dtos.Product;

public interface IProductServiceClient {
    Product getProductById(Long productId);

    Product updateProduct(Product product);
}
