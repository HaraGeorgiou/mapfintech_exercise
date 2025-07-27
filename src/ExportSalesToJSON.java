import java.sql.*;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportSalesToJSON {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mapfintech_exercise";
        String user = "java_user";
        String password = "StrongPassword123!"; // leave empty if using auth_socket (on Linux WSL)

        String query = "SELECT p.product_name, c.continent_name, s.quarter, s.year, s.sales_amount " +
                "FROM Sales s " +
                "JOIN Product p ON s.product_id = p.product_id " +
                "JOIN Continent c ON s.continent_id = c.continent_id";

        List<Map<String, Object>> salesData = new ArrayList<>();

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

            // Write JSON file
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter("sales_data.json")) {
                gson.toJson(salesData, writer);
                System.out.println("✅ JSON file created: sales_data.json");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
