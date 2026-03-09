package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CarRepositoryTest {
    @InjectMocks
    private CarRepositoryImpl carRepository;

    @Test
    void createAndFindShouldStoreCarData() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("SUV");
        car.setCarColor("White");
        car.setCarQuantity(4);
        carRepository.create(car);

        Iterator<Car> iterator = carRepository.findAll();
        assertTrue(iterator.hasNext());
        Car saved = iterator.next();
        assertEquals("car-1", saved.getCarId());
        assertEquals("SUV", saved.getCarName());
        assertEquals("White", saved.getCarColor());
        assertEquals(4, saved.getCarQuantity());
    }

    @Test
    void createShouldGenerateIdWhenNull() {
        Car car = new Car();
        car.setCarName("Coupe");
        car.setCarColor("Black");
        car.setCarQuantity(2);

        Car created = carRepository.create(car);

        assertNotNull(created.getCarId());
        assertFalse(created.getCarId().isBlank());
    }

    @Test
    void findAllShouldReturnEmptyIteratorWhenNoData() {
        Iterator<Car> iterator = carRepository.findAll();
        assertFalse(iterator.hasNext());
    }

    @Test
    void findByIdShouldReturnNullWhenMissing() {
        assertNull(carRepository.findById("missing"));
    }

    @Test
    void updateShouldModifyExistingCar() {
        Car original = new Car();
        original.setCarId("car-1");
        original.setCarName("Old Name");
        original.setCarColor("Red");
        original.setCarQuantity(1);
        carRepository.create(original);

        Car updated = new Car();
        updated.setCarName("New Name");
        updated.setCarColor("Blue");
        updated.setCarQuantity(9);

        Car result = carRepository.update("car-1", updated);

        assertNotNull(result);
        assertEquals("car-1", result.getCarId());
        assertEquals("New Name", result.getCarName());
        assertEquals("Blue", result.getCarColor());
        assertEquals(9, result.getCarQuantity());
    }

    @Test
    void updateShouldReturnNullWhenIdMissing() {
        Car updated = new Car();
        updated.setCarName("Name");
        updated.setCarColor("Color");
        updated.setCarQuantity(3);

        Car result = carRepository.update("missing", updated);

        assertNull(result);
    }

    @Test
    void updateShouldReturnNullWhenExistingCarsDoNotMatchId() {
        Car existing = new Car();
        existing.setCarId("car-1");
        existing.setCarName("Existing");
        existing.setCarColor("Silver");
        existing.setCarQuantity(7);
        carRepository.create(existing);

        Car updated = new Car();
        updated.setCarName("Name");
        updated.setCarColor("Color");
        updated.setCarQuantity(3);

        Car result = carRepository.update("missing", updated);

        assertNull(result);
    }

    @Test
    void deleteShouldRemoveOnlyMatchingCar() {
        Car first = new Car();
        first.setCarId("car-1");
        first.setCarName("First");
        first.setCarColor("Gray");
        first.setCarQuantity(5);
        carRepository.create(first);

        Car second = new Car();
        second.setCarId("car-2");
        second.setCarName("Second");
        second.setCarColor("Green");
        second.setCarQuantity(6);
        carRepository.create(second);

        carRepository.delete("car-1");

        assertNull(carRepository.findById("car-1"));
        assertNotNull(carRepository.findById("car-2"));
    }
}
