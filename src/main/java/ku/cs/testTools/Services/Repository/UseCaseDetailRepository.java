package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.UseCaseDetail;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;
import java.util.UUID;

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

            UseCaseDetail existing = entityManager.find(UseCaseDetail.class, useCaseDetail.getUseCaseID());
            if (existing == null) {
                entityManager.persist(useCaseDetail); // Insert ถ้ายังไม่มี
            } else {
                entityManager.merge(useCaseDetail);   // Update ถ้ามีแล้ว
            }

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }


    // Find a UseCaseDetail by ID
    public UseCaseDetail findById(String id) {
        try {
            return entityManager.find(UseCaseDetail.class, id);
        } catch (NoResultException e) {
            return null; // Return null if no result found
        }
    }

    // Retrieve all UseCaseDetails
    public List<UseCaseDetail> getAllUseCaseDetails() {
        String query = "SELECT t FROM UseCaseDetail t";
        try {
            return entityManager.createQuery(query, UseCaseDetail.class).getResultList();
        } catch (NoResultException e) {
            return null; // Return null if no result found
        }
    }

    // Update an existing UseCaseDetail
    public void saveOrUpdateUseCaseDetail(UseCaseDetail useCaseDetail) {
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
    public void deleteUseCaseDetail(UUID idUCD) {
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
        }
    }

    // Close EntityManager after usage

}
