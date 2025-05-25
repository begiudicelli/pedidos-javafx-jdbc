package org.udemy.javafxudemy.model.dao.impl;

import org.udemy.javafxudemy.db.DB;
import org.udemy.javafxudemy.db.DbException;
import org.udemy.javafxudemy.model.dao.ClientDao;
import org.udemy.javafxudemy.model.entities.Client;
import org.udemy.javafxudemy.model.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ClientDaoJDBC implements ClientDao {

    private final Connection conn;

    public ClientDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Client client) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "INSERT INTO client " +
                "(name, phone, email, address, cpf, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try{
            st = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setString(1, client.getName());
            st.setString(2, client.getPhone());
            st.setString(3, client.getEmail());
            st.setString(4, client.getAddress());
            st.setString(5, client.getCpf());
            st.setDate(6, java.sql.Date.valueOf(client.getCreatedAt()));

            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    client.setId(id);
                }
            }else{
                throw new DbException("Unexpected error. No Line affected in client table.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void update(Client client) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Client findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "SELECT * FROM client WHERE id_client = ? ORDER BY name";

        try{
            st = conn.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            if(rs.next()){
                return instantiateClient(rs);
            }

            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Client> findAll() {
        return List.of();
    }


    private Client instantiateClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id_client"));
        client.setName(rs.getString("name"));
        client.setPhone(rs.getString("phone"));
        client.setEmail(rs.getString("email"));
        client.setAddress(rs.getString("address"));
        client.setCpf(rs.getString("cpf"));
        client.setCreatedAt(rs.getDate("created_at").toLocalDate());
        return client;
    }

}
