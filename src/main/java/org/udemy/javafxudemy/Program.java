package org.udemy.javafxudemy;

import org.udemy.javafxudemy.db.DB;
import org.udemy.javafxudemy.db.DbException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Program {

    public static void main(String[] args) {

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DB.getConnection();

            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM Client");

            while (rs.next()) {
                int id = rs.getInt("id_client");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.println("ID: " + id + ", Nome: " + name + ", Email: " + email);
            }

        } catch (SQLException e) {
            throw new DbException("Erro ao consultar dados: " + e.getMessage(), e);
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
            DB.closeConnection();
        }
    }
}
