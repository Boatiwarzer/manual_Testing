package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.TestCase;

import java.util.List;

public class TestCaseRepository {
    private final EntityManager entityManager;
    private final EntityManagerFactory emf;

    // Constructor to inject EntityManager

    public TestCaseRepository() {
        this.emf = Persistence.createEntityManagerFactory("test_db");
        this.entityManager = this.emf.createEntityManager();
    }

    public TestCaseRepository(String pu) {
        this.emf = Persistence.createEntityManagerFactory(pu);
        this.entityManager = this.emf.createEntityManager();    }

    // Create a new TestScript
    public void addTestCase(TestCase testcase) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(testcase);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }
    public void findById(String id){
        Query query = entityManager.createNamedQuery("find testcase by id");
        query.setParameter("id",id);
        query.getSingleResult();
    }

    // Retrieve all TestScripts
    public List<TestCase> getAllTestCases() {
        String query = "SELECT t FROM TestScript t";
        TypedQuery<TestCase> typedQuery = entityManager.createQuery(query, TestCase.class);
        return typedQuery.getResultList();
    }

    // Retrieve a TestScript by ID
    public TestCase getTestCaseById(String idTC) {
        return entityManager.find(TestCase.class, idTC);
    }

    // Update an existing TestScript
    public void updateTestCase(TestCase testCase) {
        TestCase testCaseUpdate = getTestCaseById(testCase.getIdTC());

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(testCaseUpdate);  // Use merge for updating existing entities
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }

    // Delete a TestScript by ID
    public void deleteTestCase(String idTC) {
        try {
            entityManager.getTransaction().begin();
            TestCase testCase = entityManager.find(TestCase.class, idTC);
            if (testCase != null) {
                entityManager.remove(testCase);  // Remove the entity from the database
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
