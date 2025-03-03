package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.TestCase;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;

public class TestCaseRepository {
    private final EntityManager entityManager;

    // Constructor
    public TestCaseRepository() {
        this.entityManager = JpaUtil.getEntityManager();
    }

    // Create a new TestCase
    public void addTestCase(TestCase testCase) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(testCase);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
    public void saveOrUpdateTestCase(TestCase testCase) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(testCase); // ✅ ใช้ merge() แทน persist()
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
    // Find a TestCase by ID
    public TestCase findById(String id) {
        try {
            TypedQuery<TestCase> query = entityManager.createNamedQuery("find testcase by id", TestCase.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Retrieve all TestCases
    public List<TestCase> getAllTestCases() {
        String query = "SELECT t FROM TestCase t";
        return entityManager.createQuery(query, TestCase.class).getResultList();
    }

    // Retrieve a TestCase by ID
    public TestCase getTestCaseById(String idTC) {
        return entityManager.find(TestCase.class, idTC);
    }

    // Update an existing TestCase
    public void updateTestCase(TestCase testCase) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(testCase);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Delete a TestCase by ID
    public void deleteTestCase(String idTC) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            TestCase testCase = entityManager.find(TestCase.class, idTC);
            if (testCase != null) {
                entityManager.remove(testCase);
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
