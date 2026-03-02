package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void createShouldDelegateToRepository() {
        Product product = new Product();
        product.setProductId("p-1");

        Product result = productService.create(product);

        verify(productRepository).create(product);
        assertSame(product, result);
    }

    @Test
    void findAllShouldConvertIteratorToList() {
        Product first = new Product();
        first.setProductId("p-1");
        Product second = new Product();
        second.setProductId("p-2");
        Iterator<Product> iterator = List.of(first, second).iterator();
        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> result = productService.findAll();

        assertEquals(2, result.size());
        assertEquals("p-1", result.get(0).getProductId());
        assertEquals("p-2", result.get(1).getProductId());
    }

    @Test
    void findByIdShouldDelegateToRepository() {
        Product product = new Product();
        product.setProductId("p-1");
        when(productRepository.findById("p-1")).thenReturn(product);

        Product result = productService.findById("p-1");

        assertEquals("p-1", result.getProductId());
    }

    @Test
    void editShouldDelegateToRepository() {
        Product product = new Product();
        product.setProductId("p-1");
        when(productRepository.update("p-1", product)).thenReturn(product);

        productService.update("p-1", product);

        verify(productRepository).update("p-1", product);
    }

    @Test
    void deleteShouldDelegateToRepository() {
        productService.deleteById("p-1");

        verify(productRepository).delete("p-1");
    }
}
