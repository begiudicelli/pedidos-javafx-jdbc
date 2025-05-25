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

            // Criando e configurando o cliente
            Client client = new Client();
            client.setName("Rafael Giudicelli");
            client.setPhone("31975295276");
            client.setEmail("rafael@gmail.com");
            client.setAddress("Rua da maldade 1200");
            client.setCpf("13051818690");
            client.setCreatedAt(LocalDate.now());

            // Inserindo cliente no banco de dados
            clientDao.insert(client);

            // Buscando o cliente pelo ID gerado
            Client insertedClient = clientDao.findById(client.getId());

            if (insertedClient != null) {
                System.out.println("Cliente inserido com sucesso:");
                System.out.println("ID: " + insertedClient.getId());
                System.out.println("Nome: " + insertedClient.getName());
                System.out.println("Email: " + insertedClient.getEmail());
                System.out.println("Telefone: " + insertedClient.getPhone());
                System.out.println("Endereço: " + insertedClient.getAddress());
                System.out.println("CPF: " + insertedClient.getCpf());
                System.out.println("Data de criação: " + insertedClient.getCreatedAt());
            } else {
                System.out.println("Erro: Cliente não foi encontrado após inserção.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }
    }
}
