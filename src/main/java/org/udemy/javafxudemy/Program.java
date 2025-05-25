package org.udemy.javafxudemy;

import org.udemy.javafxudemy.db.DB;
import org.udemy.javafxudemy.model.dao.ClientDao;
import org.udemy.javafxudemy.model.dao.impl.ClientDaoJDBC;

import java.sql.Connection;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Connection conn = null;
        Scanner sc = new Scanner(System.in);

        try {
            conn = DB.getConnection();
            ClientDao clientDao = new ClientDaoJDBC(conn);

            System.out.print("Digite o ID do cliente que deseja deletar: ");
            int id = sc.nextInt();

            clientDao.deleteById(id);

            System.out.println("Cliente deletado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
            DB.closeConnection();
        }
    }
}
