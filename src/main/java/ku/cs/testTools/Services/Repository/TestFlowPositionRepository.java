package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.TestFlowPosition;

import java.util.List;

public class TestFlowPositionRepository {
    private final EntityManager entityManager;
    private final EntityManagerFactory emf;

    // Constructor to inject EntityManager

    public TestFlowPositionRepository() {
        this.emf = Persistence.createEntityManagerFactory("test_db");
        this.entityManager = this.emf.createEntityManager();
    }

    public TestFlowPositionRepository(String pu) {
        this.emf = Persistence.createEntityManagerFactory(pu);
        this.entityManager = this.emf.createEntityManager();    }

    // Create a new TestFlowPosition
    public void addTestFlowPosition(TestFlowPosition testFlowPosition) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(testFlowPosition);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }
    public void findById(String id){
        Query query = entityManager.createNamedQuery("find testflowposition by id");
        query.setParameter("id",id);
        query.getSingleResult();
    }

    // Retrieve all TestFlowPositions
    public List<TestFlowPosition> getAllTestFlowPositions() {
        String query = "SELECT t FROM TestFlowPosition t";
        TypedQuery<TestFlowPosition> typedQuery = entityManager.createQuery(query, TestFlowPosition.class);
        return typedQuery.getResultList();
    }

    // Retrieve a TestFlowPosition by ID
    public TestFlowPosition getTestFlowPositionById(int idTS) {
        return entityManager.find(TestFlowPosition.class, idTS);
    }

    // Update an existing TestFlowPosition
    public void updateTestFlowPosition(TestFlowPosition TestFlowPosition) {
        TestFlowPosition TestFlowPositionUpdate = getTestFlowPositionById(TestFlowPosition.getPositionID());

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(TestFlowPositionUpdate);  // Use merge for updating existing entities
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }

    // Delete a TestFlowPosition by ID
    public void deleteTestFlowPosition(String idTS) {
        try {
            entityManager.getTransaction().begin();
            TestFlowPosition TestFlowPosition = entityManager.find(TestFlowPosition.class, idTS);
            if (TestFlowPosition != null) {
                entityManager.remove(TestFlowPosition);  // Remove the entity from the database
            }
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }

}
