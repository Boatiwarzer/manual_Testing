package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.TestScript;

import java.util.List;

public class TestScriptRepository {

    private final EntityManager entityManager;
    private final EntityManagerFactory emf;

    // Constructor to inject EntityManager

    public TestScriptRepository() {
        this.emf = Persistence.createEntityManagerFactory("test_db");
        this.entityManager = this.emf.createEntityManager();
    }

    public TestScriptRepository(String pu) {
        this.emf = Persistence.createEntityManagerFactory(pu);
        this.entityManager = this.emf.createEntityManager();    }

    // Create a new TestScript
    public void addTestScript(TestScript testScript) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(testScript);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }
    public void findById(String id){
        Query query = entityManager.createNamedQuery("find testscript by id");
        query.setParameter("id",id);
        query.getSingleResult();
    }

    // Retrieve all TestScripts
    public List<TestScript> getAllTestScripts() {
        String query = "SELECT t FROM TestScript t";
        TypedQuery<TestScript> typedQuery = entityManager.createQuery(query, TestScript.class);
        return typedQuery.getResultList();
    }

    // Retrieve a TestScript by ID
    public TestScript getTestScriptById(String idTS) {
        return entityManager.find(TestScript.class, idTS);
    }

    // Update an existing TestScript
    public void updateTestScript(TestScript testScript) {
        TestScript testScriptUpdate = getTestScriptById(testScript.getIdTS());

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(testScriptUpdate);  // Use merge for updating existing entities
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }

    // Delete a TestScript by ID
    public void deleteTestScript(String idTS) {
        try {
            entityManager.getTransaction().begin();
            TestScript testScript = entityManager.find(TestScript.class, idTS);
            if (testScript != null) {
                entityManager.remove(testScript);  // Remove the entity from the database
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
