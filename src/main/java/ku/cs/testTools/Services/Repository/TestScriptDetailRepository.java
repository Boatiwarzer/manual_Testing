package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetail;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;

public class TestScriptDetailRepository {
    private final EntityManager entityManager;

    // Constructor
    public TestScriptDetailRepository() {
        this.entityManager = JpaUtil.getEntityManager();
    }

    // Create a new TestScriptDetail
    public void saveOrUpdateTestScriptDetail(TestScriptDetail testScriptDetail) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(testScriptDetail);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Find a TestScriptDetail by ID
    public TestScriptDetail findById(String id) {
        try {
            TypedQuery<TestScriptDetail> query = entityManager.createNamedQuery("find testscriptdetail by id", TestScriptDetail.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Retrieve all TestScriptDetails
    public List<TestScriptDetail> getAllTestScriptDetail() {
        String query = "SELECT t FROM TestScriptDetail t";
        return entityManager.createQuery(query, TestScriptDetail.class).getResultList();
    }

    // Retrieve a TestScriptDetail by ID
    public TestScriptDetail getTestScriptDetailById(String idTSD) {
        return entityManager.find(TestScriptDetail.class, idTSD);
    }


    // Update an existing TestScriptDetail
    public void updateTestScriptDetail(TestScriptDetail testScriptDetail) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(testScriptDetail);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Delete a TestScriptDetail by ID
    public void deleteTestScriptDetail(String idTSD) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            TestScriptDetail testScriptDetail = entityManager.find(TestScriptDetail.class, idTSD);
            if (testScriptDetail != null) {
                entityManager.remove(testScriptDetail);
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
