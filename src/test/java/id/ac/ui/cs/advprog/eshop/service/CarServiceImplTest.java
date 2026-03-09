package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void createShouldDelegateToRepository() {
        Car car = new Car();
        car.setCarId("car-1");

        Car result = carService.create(car);

        verify(carRepository).create(car);
        assertSame(car, result);
    }

    @Test
    void findAllShouldConvertIteratorToList() {
        Car first = new Car();
        first.setCarId("car-1");
        Car second = new Car();
        second.setCarId("car-2");
        Iterator<Car> iterator = List.of(first, second).iterator();
        when(carRepository.findAll()).thenReturn(iterator);

        List<Car> result = carService.findAll();

        assertEquals(2, result.size());
        assertEquals("car-1", result.get(0).getCarId());
        assertEquals("car-2", result.get(1).getCarId());
    }

    @Test
    void findByIdShouldDelegateToRepository() {
        Car car = new Car();
        car.setCarId("car-1");
        when(carRepository.findById("car-1")).thenReturn(car);

        Car result = carService.findById("car-1");

        assertEquals("car-1", result.getCarId());
    }

    @Test
    void updateShouldDelegateToRepository() {
        Car car = new Car();
        car.setCarId("car-1");

        carService.update("car-1", car);

        verify(carRepository).update("car-1", car);
    }

    @Test
    void deleteByIdShouldDelegateToRepository() {
        carService.deleteById("car-1");

        verify(carRepository).delete("car-1");
    }
}
