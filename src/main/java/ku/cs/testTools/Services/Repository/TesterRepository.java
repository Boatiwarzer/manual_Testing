package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.Manager.Tester;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;

public class TesterRepository {
    private final EntityManager entityManager;

    // Constructor
    public TesterRepository() {
        this.entityManager = JpaUtil.getEntityManager();
    }

    // Create a new Tester
    public void addTester(Tester tester) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(tester);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Find a Tester by ID
    public Tester findById(String id) {
        return entityManager.find(Tester.class, id);
    }

    // Retrieve all Testers
    public List<Tester> getAllTesters() {
        String query = "SELECT t FROM Tester t";
        return entityManager.createQuery(query, Tester.class).getResultList();
    }

    // Update an existing Tester
    public void updateTester(Tester tester) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(tester);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Delete a Tester by ID
    public void deleteTester(String id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Tester tester = entityManager.find(Tester.class, id);
            if (tester != null) {
                entityManager.remove(tester);
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
