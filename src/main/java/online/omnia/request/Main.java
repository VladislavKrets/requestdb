package online.omnia.request;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lollipop on 27.03.2018.
 */
public class Main {
    public static void main(String[] args) {
        String url = null;
        String username = null;
        String password = null;
        String query = null;
        Map<String, String> config = iniFileReader();
        url = config.get("url");
        username = config.get("username");
        password = config.get("password");
        query = config.get("query");
        System.out.println(query);
        try {
            FileWriter writer = new FileWriter("result.txt");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int size = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= size; i++) writer.write(resultSet.getString(i) + " | ");
                writer.write("\n");
                writer.flush();
            }
        }
        catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
    public static Map<String, String> iniFileReader() {
        Map<String, String> properties = new HashMap<>();
        StringBuilder line = new StringBuilder();
        try (BufferedReader iniFileReader = new BufferedReader(new FileReader("configuration.ini"))) {
            String property;
            String[] propertyArray;
            while ((property = iniFileReader.readLine()) != null) {
                propertyArray = property.split("=");
                if (property.contains("=")) {
                    for (int i = 1; i < propertyArray.length; i++) {

                        line.append(propertyArray[i]);
                        if (i != propertyArray.length - 1) line.append("=");
                    }
                    properties.put(propertyArray[0], line.toString());
                }
                line = new StringBuilder();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
