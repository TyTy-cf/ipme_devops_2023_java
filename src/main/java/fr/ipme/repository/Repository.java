package fr.ipme.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Repository {

    private final String url;

    public Repository(String url) {
        this.url = url;
    }

    public void executeQuery(String query) {
        try {
            Connection connection = DriverManager.getConnection(url, "root", "");
            Statement statement = connection.createStatement();
            statement.execute(query);
            System.out.println("Query : " + query + " - DONE");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet findAll(String table) {
        try {
            Connection connection = DriverManager.getConnection(url, "root", "");
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + table;
            System.out.println("Query : " + sql);
            return statement.executeQuery(sql);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
