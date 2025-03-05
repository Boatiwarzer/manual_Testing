package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.TestFlowPosition;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;
import java.util.UUID;

public class TestFlowPositionRepository {
    private final EntityManager entityManager;

    // Constructor
    public TestFlowPositionRepository() {
        this.entityManager = JpaUtil.getEntityManager();
    }

    // Create a new TestFlowPosition
    public void saveOrUpdateTestFlowPosition(TestFlowPosition testFlowPosition) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(testFlowPosition);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Find a TestFlowPosition by ID
    public TestFlowPosition findById(UUID id) {
        try {
            TypedQuery<TestFlowPosition> query = entityManager.createNamedQuery("find testflowposition by id", TestFlowPosition.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Retrieve all TestFlowPositions
    public List<TestFlowPosition> getAllTestFlowPositions() {
        String query = "SELECT t FROM TestFlowPosition t";
        return entityManager.createQuery(query, TestFlowPosition.class).getResultList();
    }

    // Retrieve a TestFlowPosition by ID


    // Update an existing TestFlowPosition
    public void updateTestFlowPosition(TestFlowPosition testFlowPosition) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(testFlowPosition);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Delete a TestFlowPosition by ID
    public void deleteTestFlowPosition(UUID idTS) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            TestFlowPosition testFlowPosition = entityManager.find(TestFlowPosition.class, idTS);
            if (testFlowPosition != null) {
                entityManager.remove(testFlowPosition);
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
