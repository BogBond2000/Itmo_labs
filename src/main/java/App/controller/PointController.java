package App.controller;

import App.model.DotDTO;
import App.model.PointData;
import App.service.DotService;
import App.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/points")
public class PointController {

    private final DotService dotService;

    @Autowired
    public PointController(DotService dotService, UserService userService) {
        this.dotService = dotService;
    }


    @PostMapping("/addPoints")
    public ResponseEntity<DotDTO> receivePoint(@RequestBody PointData pointData) {

        // Добавляем логику проверки попадания в область

        boolean isInArea = checkPointInArea(pointData.getX(), pointData.getY(), pointData.getR());
        pointData.setInArea(isInArea);
        System.out.println("Полученные данные: " + pointData);
        return dotService.addDot(pointData.getX(), pointData.getY(), pointData.getR(), isInArea, pointData.getUserId());
    }

    // Эндпоинт для получения всех точек пользователя
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DotDTO>> getUserDots(@PathVariable int userId) {
        return dotService.getUserDots(userId);
    }

    // Логика проверки точки
    private boolean checkPointInArea(double x, double y, double r) {
        if (x >= 0 && y >= 0 && x <= r && y <= r / 2) return true; // Прямоугольник
        if (x <= 0 && y >= 0 && x * x + y * y <= (r / 2) * (r / 2)) return true; // Четверть круга
        if (x <= 0 && y <= 0 && y >= -x - r) return true; // Прямоугольный треугольник
        return false;
    }
}