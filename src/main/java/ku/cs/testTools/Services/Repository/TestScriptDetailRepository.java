package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetail;

import java.util.List;

public class TestScriptDetailRepository {
    private final EntityManager entityManager;
    private final EntityManagerFactory emf;

    // Constructor to inject EntityManager

    public TestScriptDetailRepository() {
        this.emf = Persistence.createEntityManagerFactory("test_db");
        this.entityManager = this.emf.createEntityManager();
    }

    public TestScriptDetailRepository(String pu) {
        this.emf = Persistence.createEntityManagerFactory(pu);
        this.entityManager = this.emf.createEntityManager();    }

    // Create a new TestScript
    public void addTestScriptDetail(TestScriptDetail testScriptDetail) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(testScriptDetail);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }
    public void findById(String id){
        Query query = entityManager.createNamedQuery("find testscriptdetail by id");
        query.setParameter("id",id);
        query.getSingleResult();
    }

    // Retrieve all TestScripts
    public List<TestScriptDetail> getAllTestScriptDetail() {
        String query = "SELECT t FROM TestScriptDetail t";
        TypedQuery<TestScriptDetail> typedQuery = entityManager.createQuery(query, TestScriptDetail.class);
        return typedQuery.getResultList();
    }

    // Retrieve a TestScript by ID
    public TestScriptDetail getTestScriptDetaolById(String idTSD) {
        return entityManager.find(TestScriptDetail.class, idTSD);
    }

    // Update an existing TestScript
    public void updateTestScriptDetail(TestScriptDetail testScriptDetail) {
        TestScriptDetail testScriptDetailUpdate = getTestScriptDetaolById(testScriptDetail.getIdTSD());

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(testScriptDetailUpdate);  // Use merge for updating existing entities
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }

    // Delete a TestScript by ID
    public void deleteTestScriptDetail(String idTSD) {
        try {
            entityManager.getTransaction().begin();
            TestScriptDetail testScriptDetail = entityManager.find(TestScriptDetail.class, idTSD);
            if (testScriptDetail != null) {
                entityManager.remove(testScriptDetail);  // Remove the entity from the database
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
