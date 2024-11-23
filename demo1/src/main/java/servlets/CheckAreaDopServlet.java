package servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;
import com.google.gson.*;

@WebServlet("/dop")
public class CheckAreaDopServlet extends HttpServlet {

    private String alp = "0123456789";
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        Map<String, Object> requestData = gson.fromJson(reader, Map.class);


        List<String> functions = (List<String>) requestData.get("functions");

        double x = (double) requestData.get("x");
        double y;
        if(requestData.get("y") == null){
            y = 0;
        }
        else{
            y = (double) requestData.get("y");
        }

        String xStr = (x < 0) ? "(" + String.valueOf(x) + ")" : String.valueOf(x);

        List<Double> valuesFunc = new ArrayList<>();
        List<String> results = new ArrayList<>();

        for (String func : functions) {
            func = func.replaceAll("x", xStr);
            func = func.replaceAll(" ", "").replaceAll("y=", "");
            double doubleX = func.charAt(0)-'0';
            for(int i=1;i< func.length()-1;i++){
                if(func.charAt(i)=='+'){
                    doubleX += func.charAt(i+1)-'0';
                }
                else if(func.charAt(i)=='-'){
                    doubleX -= func.charAt(i+1)-'0';
                }
                else if (func.charAt(i)=='*'){
                    doubleX *= func.charAt(i+1)-'0';
                }
            }
            valuesFunc.add(doubleX);
        }
        double maxY = Collections.max(valuesFunc);
        double minY = Collections.min(valuesFunc);

        double intersectionY;
        double intersectionX;

        if(maxY >= y && y >= minY){
            int indexMax = valuesFunc.indexOf(maxY);
            int indexMin = valuesFunc.indexOf(minY);

            int a = 0;
            int b = 0;
            int c = 0;
            int d = 0;

            String maxFunc = functions.get(indexMax);
            String minFunc = functions.get(indexMin);
            List<Integer> l = new ArrayList<>();

            maxFunc = maxFunc.replaceAll(" ", "").replaceAll("y=", "").replaceAll("x","");


            minFunc = minFunc.replaceAll(" ", "").replaceAll("y=", "").replaceAll("x","");


            String line = maxFunc + minFunc;

            boolean flag = false;
            for(int i= 0 ; i<line.length();i++){
                int c1 = line.charAt(i)-'0';


                if(alp.contains(c1+"")){
                    if(flag){
                        c1 *= -1;
                        flag = false;
                    }
                    l.add((int) c1);
                }
                if(c1 == -3){
                    flag = true;
                }

            }

            a = l.get(0);
            b = l.get(1);
            c = l.get(2);
            d = l.get(3);

            double kA = d - b;
            double kB = a -c  ;
            int k = 1;
            double ansX = kA/kB;

            maxFunc = functions.get(indexMax);
            maxFunc = maxFunc.replaceAll(" ", "").replaceAll("y=", "").replaceAll("x", String.valueOf(ansX));

            double doubleY = maxFunc.charAt(0) - '0';
            if(doubleY == -3){
                doubleY = maxFunc.charAt(1) - '0';
                doubleY *= -1;
                k = 2;
            }

            for( ;k<maxFunc.length()-1;k++){
                int fawa = maxFunc.charAt(k) - '0';
                int fawaw = maxFunc.charAt(k+1) - '0';
                if(maxFunc.charAt(k)=='+'){
                    doubleY += maxFunc.charAt(k+1)-'0';
                }
                else if(maxFunc.charAt(k)=='-'){
                    doubleY -= maxFunc.charAt(k+1)-'0';
                }
                else if ((fawa == -6) && (!(fawaw == -2))){
                    doubleY *= maxFunc.charAt(k+1)-'0';
                }
                else if((fawa== -6) && (fawaw == -2)){
                    doubleY *= maxFunc.charAt(k+2)-'0';
                    doubleY *= -1;
                    k+=1;
                }
            }
            intersectionY = doubleY;
            intersectionX = ansX;

            maxFunc = functions.get(indexMax);
            minFunc = functions.get(indexMin);

            functions.remove(maxFunc);
            functions.remove(minFunc);

            for (String func : functions) {

                String EquationOfALine = "y + bx +c";
                func = func.replaceAll(" ", "");
                double a1 = 0;
                double b1 = 0;
                double c1 = 0;
                if (func.startsWith("y=")) {
                    String rightPart = func.substring(2);

                    String[] terms = rightPart.split("(?=[+-])");
                    for (String term : terms) {
                        if (term.contains("x")) { // Обрабатываем термы с x
                            String cofStr = term.replace("x", "");
                            if (cofStr.isEmpty() || cofStr.equals("+")) {
                                b1 = 1;
                            } else if (cofStr.equals("-")) {
                                b1 = -1;
                            } else {
                                if (cofStr.contains("*")) {
                                    cofStr = cofStr.replace("*", "");
                                }
                                b1 += parseCoefficient(cofStr); // Добавляем к b1, так как может быть несколько термов с x
                            }
                        } else { // Обрабатываем термы без x (свободный член)
                            c1 += parseCoefficient(term); // Добавляем к c1, так как может быть несколько свободных членов
                        }
                    }
                }
                func = func.replaceAll(" ", "").replaceAll("y=", "").replaceAll("x", String.valueOf(x));
                a1 = 1;
                int counter = 1;
                double xLast = func.charAt(0)-'0';
                if(xLast == - 3){
                    xLast = func.charAt(1) - '0';
                    xLast *= -1;
                    counter +=1;
                }

                for( ;counter<func.length()-1;counter++){
                    int gate = func.charAt(counter)-'0';
                    int gate2 = func.charAt(counter+1)-'0';
                    if(func.charAt(counter)=='+'){
                        xLast += func.charAt(counter+1)-'0';
                    }
                    else if(func.charAt(counter)=='-'){
                        xLast -= func.charAt(counter+1)-'0';
                    }
                    else if((gate == -6) && (!(gate2 == -2))){
                        xLast *= func.charAt(counter+1)-'0';
                    }
                    else if((gate == -6) && (gate2 == -2)){
                        xLast *= func.charAt(counter+2)-'0';
                        xLast *= -1;
                        counter+=1;
                    }
                }




                if(a1>0){
                    if((a1 * y + b1*x + c1 >=0) && (a1 * intersectionY + b1*intersectionX + c1 >= 0) && y<=maxY && y>=minY && x<=intersectionX && y<=xLast){
                        System.out.println("победа");
                    } else{
                        System.out.println("не попал");
                    }
                }
                else{
                    if ((a1 * y + b1*x + c1 >=0) && (a1 * intersectionY + b1*intersectionX + c1 >=0) && y<=maxY && y>=minY && x>=intersectionX && y>=xLast) {
                        System.out.println("победа");
                    }   else{
                        System.out.println("не попал");
                    }
                }

            }
        }





        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("success", true);
        responseData.put("results", results);

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(responseData));
        out.flush();
    }

    private static double parseCoefficient(String term){
        if(term.equals("+") || term.equals("-") || term.isEmpty()){
            return term.equals("-") ? -1:1;
        }
        return Double.parseDouble(term);
    }
}
