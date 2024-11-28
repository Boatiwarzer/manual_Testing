package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.TestResultDetail;

import java.util.List;

public class TestResultDetailRepository {
    private final EntityManager entityManager;
    private final EntityManagerFactory emf;

    // Constructor to inject EntityManager

    public TestResultDetailRepository() {
        this.emf = Persistence.createEntityManagerFactory("test_db");
        this.entityManager = this.emf.createEntityManager();
    }

    public TestResultDetailRepository(String pu) {
        this.emf = Persistence.createEntityManagerFactory(pu);
        this.entityManager = this.emf.createEntityManager();    }

    // Create a new TestScript
    public void addTestResultDetail(TestResultDetail testResultDetail) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(testResultDetail);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }
    public void findById(String id){
        Query query = entityManager.createNamedQuery("find testresultdetail by id");
        query.setParameter("id",id);
        query.getSingleResult();
    }

    // Retrieve all TestScripts
    public List<TestResultDetail> getAllTestResultDetails() {
        String query = "SELECT t FROM TestResultDetail t";
        TypedQuery<TestResultDetail> typedQuery = entityManager.createQuery(query, TestResultDetail.class);
        return typedQuery.getResultList();
    }

    // Retrieve a TestScript by ID
    public TestResultDetail getTestResultDetailById(String idTRD) {
        return entityManager.find(TestResultDetail.class, idTRD);
    }

    // Update an existing TestScript
    public void updateTestResultDetail(TestResultDetail testResultDetail) {
        TestResultDetail testResultDetailUpdate = getTestResultDetailById(testResultDetail.getIdTRD());

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(testResultDetailUpdate);  // Use merge for updating existing entities
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }

    // Delete a TestScript by ID
    public void deleteTestResultDetail(String idTC) {
        try {
            entityManager.getTransaction().begin();
            TestResultDetail testResultDetail = entityManager.find(TestResultDetail.class, idTC);
            if (testResultDetail != null) {
                entityManager.remove(testResultDetail);  // Remove the entity from the database
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
