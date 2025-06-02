package org.udemy.javafxudemy.model.dao.impl;

import org.udemy.javafxudemy.db.DbException;
import org.udemy.javafxudemy.model.dao.ClientDao;
import org.udemy.javafxudemy.model.entities.Client;

import java.sql.*;
import java.util.*;

public class ClientDaoJDBC implements ClientDao {

    private final Connection conn;
    private final Map<Integer, Client> clientCache = new HashMap<>();

    public ClientDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Client client) {
        String sql = "INSERT INTO client (name, phone, email, address, cpf) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setStatementParameters(st, client);

            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("Insert failed: no rows affected.");
            }

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    client.setId(id);
                    clientCache.put(id, client);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error inserting client: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Client client) {
        String sql = "UPDATE client SET name = ?, phone = ?, email = ?, address = ?, cpf = ? WHERE id_client = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            setStatementParameters(st, client);
            st.setInt(6, client.getId());
            st.executeUpdate();

            clientCache.put(client.getId(), client);
        } catch (SQLException e) {
            throw new DbException("Error updating client: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM client WHERE id_client = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            int rows = st.executeUpdate();

            if (rows == 0) {
                throw new DbException("Delete failed: no client found with ID " + id);
            }

            clientCache.remove(id);
        } catch (SQLException e) {
            throw new DbException("Error deleting client: " + e.getMessage(), e);
        }
    }

    @Override
    public Client findById(Integer id) {
        if (clientCache.containsKey(id)) {
            return clientCache.get(id);
        }

        String sql = "SELECT * FROM client WHERE id_client = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Client client = instantiateClient(rs);
                    clientCache.put(client.getId(), client);
                    return client;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DbException("Error finding client by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Client> findByName(Client client) {
        String sql = "SELECT * FROM client WHERE name LIKE ? ORDER BY name";
        List<Client> list = new ArrayList<>();

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, "%" + client.getName() + "%");

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id_client");

                    if (clientCache.containsKey(id)) {
                        list.add(clientCache.get(id));
                    } else {
                        Client newClient = instantiateClient(rs);
                        clientCache.put(id, newClient);
                        list.add(newClient);
                    }
                }
            }

            return list;
        } catch (SQLException e) {
            throw new DbException("Error finding client by name: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Client> findAll() {
        String sql = "SELECT * FROM client ORDER BY name";
        List<Client> list = new ArrayList<>();

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_client");

                if (clientCache.containsKey(id)) {
                    list.add(clientCache.get(id));
                } else {
                    Client client = instantiateClient(rs);
                    clientCache.put(id, client);
                    list.add(client);
                }
            }

            return list;
        } catch (SQLException e) {
            throw new DbException("Error fetching all clients: " + e.getMessage(), e);
        }
    }

    // Utilitários

    private void setStatementParameters(PreparedStatement st, Client client) throws SQLException {
        st.setString(1, client.getName());
        st.setString(2, client.getPhone());
        st.setString(3, client.getEmail());
        st.setString(4, client.getAddress());
        st.setString(5, client.getCpf());
    }

    private Client instantiateClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id_client"));
        client.setName(rs.getString("name"));
        client.setPhone(rs.getString("phone"));
        client.setEmail(rs.getString("email"));
        client.setAddress(rs.getString("address"));
        client.setCpf(rs.getString("cpf"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            client.setCreatedAt(ts.toLocalDateTime());
        }
        return client;
    }
}
