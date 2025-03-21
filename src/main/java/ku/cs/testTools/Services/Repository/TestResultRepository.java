package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.TestResult;

import java.util.List;

public class TestResultRepository {
    private final EntityManager entityManager;
    private final EntityManagerFactory emf;

    // Constructor to inject EntityManager

    public TestResultRepository() {
        this.emf = Persistence.createEntityManagerFactory("test_db");
        this.entityManager = this.emf.createEntityManager();
    }

    public TestResultRepository(String pu) {
        this.emf = Persistence.createEntityManagerFactory(pu);
        this.entityManager = this.emf.createEntityManager();    }

    // Create a new TestScript
    public void saveOrUpdateTestResult(TestResult testResult) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(testResult);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
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
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(testResult);  // Use merge for updating existing entities
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
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
