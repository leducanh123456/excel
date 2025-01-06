package org.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

@SpringBootApplication
@EntityScan(basePackages = "org.example")
public class Main {
    public static void main(String[] args) {
        String url = "jdbc:presto://localhost:8443";
        Properties properties = new Properties();
        properties.setProperty("user", "root"); // Username truy cập trên datalake
        properties.setProperty("password", "12345"); // Password truy cập trên datalake
        properties.setProperty("SSL", "true");
        properties.setProperty("SSLKeyStorePath", "D:\\keystore.jks");
        properties.setProperty("SSLKeyStorePassword", "123456");
        properties.setProperty("SSLTrustStorePath", "D:\\truststore.jks");
        properties.setProperty("SSLTrustStorePassword", "123456");
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        System.setProperty("javax.net.debug", "ssl");

        try {
            // Tải driver JDBC của PrestoSQL
            Class.forName("io.prestosql.jdbc.PrestoDriver");

            // Kết nối đến Trino (hoặc PrestoSQL)
            Connection connection = DriverManager.getConnection(url, properties);

            // Tạo đối tượng Statement
            Statement statement = connection.createStatement();

            // Thực hiện truy vấn
            String query = "SELECT * FROM mysql.test.my_table";
            ResultSet resultSet = statement.executeQuery(query);

            // Xử lý kết quả truy vấn
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.println("ID: " + id + ", Name: " + name);
            }

            // Đóng kết nối
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        SpringApplication.run(Main.class, args);
    }
}