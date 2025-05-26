
package org.udemy.javafxudemy;

import org.udemy.javafxudemy.db.DB;
import org.udemy.javafxudemy.model.dao.ClientDao;
import org.udemy.javafxudemy.model.dao.impl.ClientDaoJDBC;
import org.udemy.javafxudemy.model.entities.Client;

import java.util.List;

public class Program {

    public static void main(String[] args) {

        // Suponha que DB.getConnection() já configura sua conexão com o banco.
        ClientDao clientDao = new ClientDaoJDBC(DB.getConnection());

        // Criando o objeto client com o nome que queremos buscar
        Client searchClient = new Client();
        searchClient.setName("b");

        // Buscando no banco de dados
        List<Client> result = clientDao.findByName(searchClient);

        // Exibindo o resultado
        if (result.isEmpty()) {
            System.out.println("Nenhum cliente encontrado com o nome 'b'.");
        } else {
            for (Client c : result) {
                System.out.println(c);
            }
        }
    }
}

