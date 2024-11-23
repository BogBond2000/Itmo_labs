package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pointManager.PointManager;
import pointManager.PointsArr;

import java.io.IOException;

@WebServlet("/checkArea")
public class AreaCheckServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Привет от сервлета проверки попадания");

        // Получаем значения из параметров запроса
        String xStr = (String) request.getAttribute("x");
        String yStr = (String) request.getAttribute("y");
        String rStr = (String) request.getAttribute("r");
        System.out.println(xStr);
        // Проверяем, что все параметры были переданы
        if (xStr == null || yStr == null || rStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не все параметры были переданы");
            return;
        }

        try {
            double x = Double.parseDouble(xStr);
            double y = Double.parseDouble(yStr);
            double r = Double.parseDouble(rStr);

            System.out.println("Получены значения: x = " + x + ", y = " + y + ", r = " + r);

            // Создаем точку и проверяем попадание
            PointManager point = new PointManager(x, y, r);

            // Получаем сессию и список точек
            HttpSession session = request.getSession();
            PointsArr points = (PointsArr) session.getAttribute("points");

            // Если списка точек нет в сессии, создаем новый
            if (points == null) {
                points = new PointsArr();
                session.setAttribute("points", points); // Сохраняем список в сессии
            }

            // Добавляем точку в список
            points.addPoint(point);

            // Сохраняем список точек в сессии (на случай, если он был создан)
            session.setAttribute("points", points);

            // Перенаправляем на result.jsp
            request.getRequestDispatcher("/result.jsp").forward(request, response);
            System.out.println("Перенаправление на result.jsp");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный формат данных. X, Y и R должны быть числами.");
            System.err.println("Ошибка парсинга чисел: " + e.getMessage());
        }
    }
}