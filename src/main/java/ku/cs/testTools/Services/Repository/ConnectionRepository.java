package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.Connection;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;
import java.util.UUID;

public class ConnectionRepository {
    private final EntityManager entityManager;

    // Constructor
    public ConnectionRepository() {
        this.entityManager = JpaUtil.getEntityManager();
    }

    // Create a new Connection
    public void addConnection(Connection connection) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(connection);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Find a Connection by ID
    public Connection findById(UUID id) {
        try {
            TypedQuery<Connection> query = entityManager.createNamedQuery("find Connection by id", Connection.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Retrieve all Connections
    public List<Connection> getAllConnections() {
        String query = "SELECT c FROM Connection c";
        return entityManager.createQuery(query, Connection.class).getResultList();
    }

    // Retrieve a Connection by ID
    public Connection getConnectionById(UUID id) {
        return entityManager.find(Connection.class, id);
    }

    // Update an existing Connection
    public void updateConnection(Connection connection) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(connection);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Delete a Connection by ID
    public void deleteConnection(String id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Connection connection = entityManager.find(Connection.class, id);
            if (connection != null) {
                entityManager.remove(connection);
            }
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Close EntityManager

}
