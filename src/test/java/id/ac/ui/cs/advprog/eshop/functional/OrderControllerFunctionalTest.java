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
class OrderControllerFunctionalTest {

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
    void createOrderPageTitle_isCorrect(ChromeDriver driver) {
        driver.get(String.format("%s/order/create", baseUrl));
        String pageTitle = driver.getTitle();
        assertEquals("Create New Order", pageTitle);
    }

    @Test
    void orderHistoryPageTitle_isCorrect(ChromeDriver driver) {
        driver.get(String.format("%s/order/history", baseUrl));
        String pageTitle = driver.getTitle();
        assertEquals("Order History", pageTitle);
    }

    @Test
    void searchOrderHistoryByAuthor_isSuccessful(ChromeDriver driver) {
        driver.get(String.format("%s/order/history", baseUrl));
        WebElement authorInputField = driver.findElement(By.id("authorInput"));
        authorInputField.sendKeys("Safira Sudrajat");
        WebElement submitButton = driver.findElement(By.tagName("button"));
        submitButton.click();

        String historyUrl = driver.getCurrentUrl();
        assertEquals(String.format("%s/order/history", baseUrl), historyUrl);
    }

    @Test
    void orderPayPageTitle_isCorrect(ChromeDriver driver) {
        driver.get(String.format("%s/order/pay/13652556-012a-4c07-b546-54eb1396d79b", baseUrl));
        String pageTitle = driver.getTitle();
        assertEquals("Order Pay", pageTitle);
    }

    @Test
    void payOrder_isSuccessful(ChromeDriver driver) {
        driver.get(String.format("%s/order/pay/13652556-012a-4c07-b546-54eb1396d79b", baseUrl));
        WebElement methodInputField = driver.findElement(By.id("methodInput"));
        methodInputField.sendKeys("VOUCHER_CODE");
        WebElement voucherCodeInputField = driver.findElement(By.id("voucherCodeInput"));
        voucherCodeInputField.sendKeys("ESHOP1234ABC5678");
        WebElement submitButton = driver.findElement(By.tagName("button"));
        submitButton.click();

        String payResultUrl = driver.getCurrentUrl();
        assertEquals(
                String.format("%s/order/pay/13652556-012a-4c07-b546-54eb1396d79b", baseUrl),
                payResultUrl
        );
    }
}
