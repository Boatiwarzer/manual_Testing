package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.TestResultDetail;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;

public class TestResultDetailRepository {
    private final EntityManager entityManager;

    // Constructor to inject EntityManager using JpaUtil
    public TestResultDetailRepository() {
        this.entityManager = JpaUtil.getEntityManager();
    }

    // Create a new TestResultDetail
    public void addTestResultDetail(TestResultDetail testResultDetail) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(testResultDetail);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            // Ensure that the EntityManager is closed after the transaction
            close();
        }
    }

    // Find a TestResultDetail by ID
    public TestResultDetail findById(String id) {
        try {
            return entityManager.find(TestResultDetail.class, id);
        } catch (NoResultException e) {
            return null; // Return null if no result found
        } finally {
            close();
        }
    }

    // Retrieve all TestResultDetails
    public List<TestResultDetail> getAllTestResultDetails() {
        String query = "SELECT t FROM TestResultDetail t";
        try {
            return entityManager.createQuery(query, TestResultDetail.class).getResultList();
        } finally {
            close();
        }
    }

    // Update an existing TestResultDetail
    public void updateTestResultDetail(TestResultDetail testResultDetail) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(testResultDetail);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            close();
        }
    }

    // Delete a TestResultDetail by ID
    public void deleteTestResultDetail(String idTC) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            TestResultDetail testResultDetail = entityManager.find(TestResultDetail.class, idTC);
            if (testResultDetail != null) {
                entityManager.remove(testResultDetail);
            }
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            close();
        }
    }

    // Close EntityManager after usage
    private void close() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
