package org.udemy.javafxudemy;

import org.udemy.javafxudemy.db.DB;
import org.udemy.javafxudemy.model.dao.ClientDao;
import org.udemy.javafxudemy.model.dao.impl.ClientDaoJDBC;
import org.udemy.javafxudemy.model.entities.Client;

import java.sql.Connection;
import java.time.LocalDate;

public class Program {
    public static void main(String[] args) {
        Connection conn = null;

        try {
            conn = DB.getConnection();
            ClientDao clientDao = new ClientDaoJDBC(conn);

            // Buscar um cliente existente
            Client client = clientDao.findById(8); // Atualize com o ID que você sabe que existe

            if (client != null) {
                System.out.println("Cliente original: " + client);

                // Atualizar os dados do cliente
                client.setName("Nome Atualizado");
                client.setPhone("31999999999");
                client.setEmail("atualizado@email.com");
                client.setAddress("Rua Atualizada 999");
                client.setCpf("99999999999");

                // Executar o update
                clientDao.update(client);

                System.out.println("Cliente atualizado com sucesso!");
                System.out.println("Dados atualizados: " + client);
            } else {
                System.out.println("Cliente com ID informado não encontrado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }
    }
}
