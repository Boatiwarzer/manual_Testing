package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.TestCaseDetail;

import java.util.List;

public class TestCaseDetailRepository {
    private final EntityManager entityManager;
    private final EntityManagerFactory emf;

    // Constructor to inject EntityManager

    public TestCaseDetailRepository() {
        this.emf = Persistence.createEntityManagerFactory("test_db");
        this.entityManager = this.emf.createEntityManager();
    }

    public TestCaseDetailRepository(String pu) {
        this.emf = Persistence.createEntityManagerFactory(pu);
        this.entityManager = this.emf.createEntityManager();    }

    // Create a new TestScript
    public void addTestCaseDetail(TestCaseDetail testCaseDetail) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(testCaseDetail);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }
    public void findById(String id){
        Query query = entityManager.createNamedQuery("find testcasesdetail by id");
        query.setParameter("id",id);
        query.getSingleResult();
    }

    // Retrieve all TestScripts
    public List<TestCaseDetail> getAlltestCaseDetail() {
        String query = "SELECT t FROM TestCaseDetail t";
        TypedQuery<TestCaseDetail> typedQuery = entityManager.createQuery(query, TestCaseDetail.class);
        return typedQuery.getResultList();
    }

    // Retrieve a TestScript by ID
    public TestCaseDetail getTestScriptDetaolById(String idTCD) {
        return entityManager.find(TestCaseDetail.class, idTCD);
    }

    // Update an existing TestScript
    public void updatetestCaseDetail(TestCaseDetail testCaseDetail) {
        TestCaseDetail testCaseDetailUpdate = getTestScriptDetaolById(testCaseDetail.getIdTCD());

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(testCaseDetailUpdate);  // Use merge for updating existing entities
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }

    // Delete a TestScript by ID
    public void deletetestCaseDetail(String idTS) {
        try {
            entityManager.getTransaction().begin();
            TestCaseDetail testCaseDetail = entityManager.find(TestCaseDetail.class, idTS);
            if (testCaseDetail != null) {
                entityManager.remove(testCaseDetail);  // Remove the entity from the database
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
