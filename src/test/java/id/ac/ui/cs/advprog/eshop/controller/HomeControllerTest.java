package id.ac.ui.cs.advprog.eshop.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeControllerTest {

    @Test
    void homePageShouldReturnHomePageView() {
        HomeController controller = new HomeController();

        String viewName = controller.homePage();

        assertEquals("HomePage", viewName);
    }
}
