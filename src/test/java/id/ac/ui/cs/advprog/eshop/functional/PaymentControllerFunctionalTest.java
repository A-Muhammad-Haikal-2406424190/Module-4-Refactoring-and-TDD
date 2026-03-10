package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class PaymentControllerFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void paymentDetailPageTitle_isCorrect(ChromeDriver driver) {
        driver.get(String.format("%s/payment/detail", baseUrl));
        String pageTitle = driver.getTitle();
        assertEquals("Payment Detail", pageTitle);
    }

    @Test
    void paymentDetailByIdPageTitle_isCorrect(ChromeDriver driver) {
        driver.get(String.format("%s/payment/detail/pay-1", baseUrl));
        String pageTitle = driver.getTitle();
        assertEquals("Payment Detail", pageTitle);
    }

    @Test
    void paymentAdminListPageTitle_isCorrect(ChromeDriver driver) {
        driver.get(String.format("%s/payment/admin/list", baseUrl));
        String pageTitle = driver.getTitle();
        assertEquals("Payment Admin List", pageTitle);
    }

    @Test
    void paymentAdminDetailPageTitle_isCorrect(ChromeDriver driver) {
        driver.get(String.format("%s/payment/admin/detail/pay-1", baseUrl));
        String pageTitle = driver.getTitle();
        assertEquals("Payment Admin Detail", pageTitle);
    }

    @Test
    void setPaymentStatus_isSuccessful(ChromeDriver driver) {
        driver.get(String.format("%s/payment/admin/detail/pay-1", baseUrl));
        WebElement statusInput = driver.findElement(By.name("status"));
        statusInput.sendKeys("SUCCESS");
        WebElement submitButton = driver.findElement(By.tagName("button"));
        submitButton.click();

        String detailUrl = driver.getCurrentUrl();
        assertEquals(String.format("%s/payment/admin/detail/pay-1", baseUrl), detailUrl);
    }
}
