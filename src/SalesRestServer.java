import static spark.Spark.*;
import com.google.gson.Gson;
import java.sql.*;
import java.util.*;

public class SalesRestServer {

    public static void main(String[] args) {
        port(4567); // REST API will run on http://localhost:4567

        // Enable CORS so PHP can fetch from localhost:4567
        options("/*", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            return "OK";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        get("/sales", (req, res) -> {
            res.type("application/json");

            List<Map<String, Object>> salesData = new ArrayList<>();

            String url = "jdbc:mysql://localhost:3306/mapfintech_exercise";
            String user = "java_user"; // use the one you created
            String password = "StrongPassword123!";

            String query = "SELECT p.product_name, c.continent_name, s.quarter, s.year, s.sales_amount " +
                    "FROM Sales s " +
                    "JOIN Product p ON s.product_id = p.product_id " +
                    "JOIN Continent c ON s.continent_id = c.continent_id";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("product", rs.getString("product_name"));
                    row.put("continent", rs.getString("continent_name"));
                    row.put("quarter", rs.getString("quarter"));
                    row.put("year", rs.getInt("year"));
                    row.put("sales_amount", rs.getDouble("sales_amount"));
                    salesData.add(row);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return new Gson().toJson(salesData);
        });
    }
}
