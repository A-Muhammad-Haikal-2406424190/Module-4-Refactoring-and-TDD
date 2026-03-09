package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarTest {
    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("car-1");
        car.setCarName("Sedan");
        car.setCarColor("Blue");
        car.setCarQuantity(10);
    }

    @Test
    void getCarIdShouldReturnAssignedValue() {
        assertEquals("car-1", car.getCarId());
    }

    @Test
    void getCarNameShouldReturnAssignedValue() {
        assertEquals("Sedan", car.getCarName());
    }

    @Test
    void getCarColorShouldReturnAssignedValue() {
        assertEquals("Blue", car.getCarColor());
    }

    @Test
    void getCarQuantityShouldReturnAssignedValue() {
        assertEquals(10, car.getCarQuantity());
    }
}
