package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService service;

    @InjectMocks
    private ProductController controller;

    @Test
    void createProductPageShouldReturnCreateViewAndSetProductModel() {
        Model model = new ExtendedModelMap();

        String viewName = controller.createProductPage(model);

        assertEquals("createProduct", viewName);
        assertNotNull(model.getAttribute("product"));
    }

    @Test
    void createProductShouldCallServiceAndRedirectToList() {
        Product product = new Product();

        String redirect = controller.createProductPost(product, new ExtendedModelMap());

        verify(service).create(product);
        assertEquals("redirect:list", redirect);
    }

    @Test
    void editProductPageShouldSetProductInModelAndReturnEditView() {
        String productId = "p-1";
        Product product = new Product();
        product.setProductId(productId);
        when(service.findById(productId)).thenReturn(product);
        Model model = new ExtendedModelMap();

        String viewName = controller.editProductPage(productId, model);

        verify(service).findById(productId);
        assertEquals("editProduct", viewName);
        assertEquals(product, model.getAttribute("product"));
    }

    @Test
    void editProductShouldCallServiceAndRedirectToList() {
        Product product = new Product();
        product.setProductId("p-1");

        String redirect = controller.editProductPost(product, new ExtendedModelMap());

        verify(service).update("p-1", product);
        assertEquals("redirect:list", redirect);
    }

    @Test
    void deleteProductShouldCallServiceAndRedirectToListPage() {
        String redirect = controller.deleteProduct("p-1");

        verify(service).deleteById("p-1");
        assertEquals("redirect:list", redirect);
    }

    @Test
    void productListPageShouldSetAllProductInModelAndReturnProductListView() {
        Product product = new Product();
        product.setProductId("p-1");
        List<Product> products = List.of(product);
        when(service.findAll()).thenReturn(products);
        Model model = new ExtendedModelMap();

        String viewName = controller.productListPage(model);

        verify(service).findAll();
        assertEquals("productList", viewName);
        assertEquals(products, model.getAttribute("products"));
    }
}
