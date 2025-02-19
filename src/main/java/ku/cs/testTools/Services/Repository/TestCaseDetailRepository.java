package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.TestCaseDetail;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;

public class TestCaseDetailRepository {
    private final EntityManager entityManager;

    // Constructor
    public TestCaseDetailRepository() {
        this.entityManager = JpaUtil.getEntityManager();
    }

    // Create a new TestCaseDetail
    public void addTestCaseDetail(TestCaseDetail testCaseDetail) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(testCaseDetail);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Find a TestCaseDetail by ID
    public TestCaseDetail findById(String id) {
        try {
            TypedQuery<TestCaseDetail> query = entityManager.createNamedQuery("find testcasesdetail by id", TestCaseDetail.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Retrieve all TestCaseDetails
    public List<TestCaseDetail> getAllTestCaseDetails() {
        String query = "SELECT t FROM TestCaseDetail t";
        return entityManager.createQuery(query, TestCaseDetail.class).getResultList();
    }

    // Retrieve a TestCaseDetail by ID
    public TestCaseDetail getTestCaseDetailById(String idTCD) {
        return entityManager.find(TestCaseDetail.class, idTCD);
    }

    // Update an existing TestCaseDetail
    public void updateTestCaseDetail(TestCaseDetail testCaseDetail) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(testCaseDetail);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Delete a TestCaseDetail by ID
    public void deleteTestCaseDetail(String idTS) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            TestCaseDetail testCaseDetail = entityManager.find(TestCaseDetail.class, idTS);
            if (testCaseDetail != null) {
                entityManager.remove(testCaseDetail);
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
