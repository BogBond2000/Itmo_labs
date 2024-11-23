<%@ page import="java.awt.*" %>
<%@ page import="pointManager.PointsArr" %>
<%@ page import="pointManager.PointManager" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Бондарь Богдан 408305</title>
    <link rel="stylesheet" href=style.css>
    <script src=script.js defer></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<header>
    <H1>Лабораторная работа №1 | Веб-программирование</H1>
    <a>Бондарь Богдан Антонович</a><br>
    <a>Вариант №408305</a>
</header>

<table>
    <tr>
        <td><label>Выберите X:</label></td>
        <td>
            <input type="checkbox" name="xValue" value="-2">-2
            <input type="checkbox" name="xValue" value="-1.5">-1.5
            <input type="checkbox" name="xValue" value="-1">-1
            <input type="checkbox" name="xValue" value="-0.5">-0.5
            <br>
            <input type="checkbox" name="xValue" value="0">0
            <input type="checkbox" name="xValue" value="0.5">0.5
            <input type="checkbox" name="xValue" value="1">1
            <input type="checkbox" name="xValue" value="1.5">1.5
            <input type="checkbox" name="xValue" value="2">2
            <div id="xError" class="error"></div>
        </td>
    </tr>
    <tr>
        <td><label>Выберите Y (От -3 до 5):</label></td>
        <td>
            <input type="text" id="yInput">
            <div id="yError" class="error"></div>
        </td>
    </tr>
    <tr>
        <td><label>Выберите R:</label></td>
        <td>
            <input type="checkbox" name="rValue" value="1">1
            <input type="checkbox" name="rValue" value="2">2
            <input type="checkbox" name="rValue" value="3">3
            <input type="checkbox" name="rValue" value="4">4
            <input type="checkbox" name="rValue" value="5">5
            <div id="rError" class="error"></div>
        </td>
    </tr>
    <tr>
        <td><button onclick="drawGraph()">Проверить точку</button></td>
    </tr>
    <tr>
        <td><canvas id="graphCanvas" width="400" height="400"></canvas></td>

    </tr>

</table>
<tfoot>
<tr>
    <td colspan="5" id="outputContainer">
        <% PointsArr points = (PointsArr) request.getSession().getAttribute("points");
            if (points == null) {
        %>
        <h4>
            <span id="notifications" class="outputStub notification">Нет результатов</span>
        </h4>
        <table id="outputTable">
            <tr>
                <th>X</th>
                <th>Y</th>
                <th>R</th>
                <th>Точка входит в ОДЗ</th>
            </tr>
        </table>
        <% } else { %>
        <h4>
            <span class="notification"></span>
        </h4>
        <table id="outputTable">
            <tr>
                <th>X</th>
                <th>Y</th>
                <th>R</th>
                <th>Точка входит в ОДЗ</th>
            </tr>
            <% for (PointManager point : points.getPoints()) { %>
            <tr>
                <td>
                    <%= point.getX() %>
                </td>
                <td>
                    <%= point.getY() %>
                </td>
                <td>
                    <%= point.getR() %>
                </td>
                <td>
                    <%= point.isInArea() ? "<span class=\"success\">Попал</span>" : "<span class=\"fail\">Промазал</span>" %>
                </td>
            </tr>
            <% } %>
        </table>
        <% } %>
    </td>
</tr>
</tfoot>

<script src="script.js"></script>


<h1>График функций</h1>
<textarea id="functionInput" rows="5" cols="40" placeholder="Введите функции, по одной на строке (например: y = x)"></textarea>
<button id="plotButton">Построить график</button>
<div id="plot"></div>
<script src="https://cdn.plot.ly/plotly-2.24.1.min.js"></script>
<script src="graph.js"></script>



</body>
</html>