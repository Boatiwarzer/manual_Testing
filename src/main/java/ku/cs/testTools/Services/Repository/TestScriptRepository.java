package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.TestScript;
import ku.cs.testTools.Models.TestToolModels.TestScriptList;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;

public class TestScriptRepository {

    private final EntityManager entityManager;

    public TestScriptRepository() {
        this.entityManager = JpaUtil.getEntityManager();
    }

    // Create a new TestScript
    public void addTestScript(TestScript testScript) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(testScript);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Find a TestScript by ID
    public TestScript findById(String id) {
        try {
            TypedQuery<TestScript> query = entityManager.createNamedQuery("find testscript by id", TestScript.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Retrieve all TestScripts
    public List<TestScript> getAllTestScripts() {
        String query = "SELECT t FROM TestScript t";
        return entityManager.createQuery(query, TestScript.class).getResultList();
    }

    // Retrieve a TestScript by ID
    public TestScript getTestScriptById(String idTS) {
        return entityManager.find(TestScript.class, idTS);
    }

    // Update an existing TestScript
    public void updateTestScript(TestScript testScript) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(testScript);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Delete a TestScript by ID
    public void deleteTestScript(String idTS) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            TestScript testScript = entityManager.find(TestScript.class, idTS);
            if (testScript != null) {
                entityManager.remove(testScript);
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
    public void close() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
