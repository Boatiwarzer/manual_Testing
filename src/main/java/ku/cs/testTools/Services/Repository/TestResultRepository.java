package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.TestResult;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;

public class TestResultRepository {
    private final EntityManager entityManager;

    // Constructor to inject EntityManager
    public TestResultRepository() {
        this.entityManager = JpaUtil.getEntityManager();
    }



    // Create a new TestScript
    public void saveOrUpdateTestResult(TestResult testResult) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.merge(testResult);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
            }

    }
    public void findById(String id){
        Query query = entityManager.createNamedQuery("find testresults by id");
        query.setParameter("id",id);
        query.getSingleResult();
    }

    // Retrieve all TestScripts
    public List<TestResult> getAllTestResults() {
        String query = "SELECT t FROM TestResult t";
        TypedQuery<TestResult> typedQuery = entityManager.createQuery(query, TestResult.class);
        return typedQuery.getResultList();
    }

    // Retrieve a TestScript by ID
    public TestResult getTestResultById(String idTR) {
        return entityManager.find(TestResult.class, idTR);
    }

    // Update an existing TestScript
    public void updateTestResult(TestResult testResult) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.merge(testResult);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }

    }

    // Delete a TestScript by ID
    public void deleteTestResult(String idTC) {
        try {
            entityManager.getTransaction().begin();
            TestResult TestResult = entityManager.find(TestResult.class, idTC);
            if (TestResult != null) {
                entityManager.remove(TestResult);  // Remove the entity from the database
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
