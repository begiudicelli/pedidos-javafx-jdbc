package org.udemy.javafxudemy.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DB {
    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                conn = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                throw new DbException("Error trying to connect: " + e.getMessage(), e);
            }
        }
        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DbException("Error closing the connection: " + e.getMessage(), e);
            }
        }
    }

    private static Properties loadProperties() {
        try (InputStream is = DB.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties props = new Properties();
            props.load(is);
            return props;
        } catch (IOException e) {
            throw new DbException("Error loading db.properties: " + e.getMessage(), e);
        }
    }


    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DbException("Error closing Statement: " + e.getMessage(), e);
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbException("Error closing ResultSet: " + e.getMessage(), e);
            }
        }
    }

}
