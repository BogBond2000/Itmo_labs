<%@ page import="pointManager.PointsArr" %>
<%@ page import="pointManager.PointManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Результаты</title>
</head>
<body>
<h1>Результаты проверки</h1>

<%
  PointsArr points = (PointsArr) session.getAttribute("points");
  if (points != null && !points.getPoints().isEmpty()) {
%>
<table>
  <thead>
  <tr>
    <th>X</th>
    <th>Y</th>
    <th>R</th>
    <th>Результат</th>
  </tr>
  </thead>
  <tbody>
  <%
    for (PointManager point : points.getPoints()) {
  %>
  <tr>
    <td><%= point.getX() %></td>
    <td><%= point.getY() %></td>
    <td><%= point.getR() %></td>
    <td><%= point.isInArea() ? "Попадание" : "Промах" %></td>
  </tr>
  <%
    }
  %>
  </tbody>
</table>
<%
} else {
%>
<p>Нет данных для отображения.</p>
<%
  }
%>
</body>
</html>