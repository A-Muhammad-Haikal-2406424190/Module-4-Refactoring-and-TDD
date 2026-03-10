package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ProductTest {
    Product product;
    @BeforeEach
    void setUp() {
        product = new Product();
        this.product.setProductId("94268615-caac-48d8-85cb-f0a6a761d8a9");
        this.product.setProductName("Ayam goreng");
        this.product.setProductQuantity(100);
    }

    @Test
    void getProductId() {
        assertEquals("94268615-caac-48d8-85cb-f0a6a761d8a9", this.product.getProductId());
    }

    @Test
    void getProductName() {
        assertEquals("Ayam goreng", this.product.getProductName());
    }

    @Test
    void getProductQuantity() {
        assertEquals(100, this.product.getProductQuantity());
    }
}