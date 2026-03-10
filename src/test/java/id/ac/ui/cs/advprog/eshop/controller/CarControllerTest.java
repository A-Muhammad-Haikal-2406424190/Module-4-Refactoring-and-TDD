package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CarService service;

    @InjectMocks
    private CarController controller;

    @Test
    void createCarPageShouldReturnCreateViewAndSetCarModel() {
        Model model = new ExtendedModelMap();

        String viewName = controller.createCarPage(model);

        assertEquals("createCar", viewName);
        assertNotNull(model.getAttribute("car"));
    }

    @Test
    void createCarShouldCallServiceAndRedirectToList() {
        Car car = new Car();

        String redirect = controller.createCarPost(car, new ExtendedModelMap());

        verify(service).create(car);
        assertEquals("redirect:listCar", redirect);
    }

    @Test
    void editCarPageShouldSetCarInModelAndReturnEditView() {
        String carId = "car-1";
        Car car = new Car();
        car.setCarId(carId);
        when(service.findById(carId)).thenReturn(car);
        Model model = new ExtendedModelMap();

        String viewName = controller.editCarPage(carId, model);

        verify(service).findById(carId);
        assertEquals("editCar", viewName);
        assertEquals(car, model.getAttribute("car"));
    }

    @Test
    void editCarShouldCallServiceAndRedirectToList() {
        Car car = new Car();
        car.setCarId("car-1");

        String redirect = controller.editCarPost(car, new ExtendedModelMap());

        verify(service).update("car-1", car);
        assertEquals("redirect:listCar", redirect);
    }

    @Test
    void deleteCarShouldCallServiceAndRedirectToListPage() {
        String redirect = controller.deleteCar("car-1");

        verify(service).deleteById("car-1");
        assertEquals("redirect:listCar", redirect);
    }

    @Test
    void carListPageShouldSetAllCarsInModelAndReturnCarListView() {
        Car car = new Car();
        car.setCarId("car-1");
        List<Car> cars = List.of(car);
        when(service.findAll()).thenReturn(cars);
        Model model = new ExtendedModelMap();

        String viewName = controller.carListPage(model);

        verify(service).findAll();
        assertEquals("carList", viewName);
        assertEquals(cars, model.getAttribute("cars"));
    }
}
