package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.UseCaseDetail;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;

public class UseCaseDetailRepository {
    private final EntityManager entityManager;

    // Constructor to inject EntityManager using JpaUtil
    public UseCaseDetailRepository() {
        this.entityManager = JpaUtil.getEntityManager();
    }

    // Create a new UseCaseDetail
    public void addUseCaseDetail(UseCaseDetail useCaseDetail) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(useCaseDetail);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            // Ensure that the EntityManager is closed after the transaction
            close();
        }
    }

    // Find a UseCaseDetail by ID
    public UseCaseDetail findById(String id) {
        try {
            return entityManager.find(UseCaseDetail.class, id);
        } catch (NoResultException e) {
            return null; // Return null if no result found
        } finally {
            close();
        }
    }

    // Retrieve all UseCaseDetails
    public List<UseCaseDetail> getAllUseCaseDetails() {
        String query = "SELECT t FROM UseCaseDetail t";
        try {
            return entityManager.createQuery(query, UseCaseDetail.class).getResultList();
        } finally {
            close();
        }
    }

    // Update an existing UseCaseDetail
    public void updateUseCaseDetail(UseCaseDetail useCaseDetail) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(useCaseDetail);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Delete a UseCaseDetail by ID
    public void deleteUseCaseDetail(String idUCD) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            UseCaseDetail useCaseDetail = entityManager.find(UseCaseDetail.class, idUCD);
            if (useCaseDetail != null) {
                entityManager.remove(useCaseDetail);
            }
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            close();
        }
    }

    // Close EntityManager after usage
    private void close() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
