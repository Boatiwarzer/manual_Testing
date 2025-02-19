package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.UseCase;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;

public class UseCaseRepository {
    private final EntityManager entityManager;

    // Constructor to inject EntityManager using JpaUtil
    public UseCaseRepository() {
        this.entityManager = JpaUtil.getEntityManager();
    }

    // Create a new UseCase
    public void addUseCase(UseCase useCase) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(useCase);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Retrieve a UseCase by ID
    public UseCase findById(String id) {
        try {
            return entityManager.find(UseCase.class, id);
        } catch (NoResultException e) {
            return null; // Return null if no result found
        }
    }

    // Retrieve all UseCases
    public List<UseCase> getAllUseCases() {
        String query = "SELECT t FROM UseCase t";
        try {
            return entityManager.createQuery(query, UseCase.class).getResultList();
        }
    }

    // Update an existing UseCase
    public void updateUseCase(UseCase useCase) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(useCase); // Use merge for updating existing entities
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Delete a UseCase by ID
    public void deleteUseCase(String idUC) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            UseCase useCase = entityManager.find(UseCase.class, idUC);
            if (useCase != null) {
                entityManager.remove(useCase); // Remove the entity from the database
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
    private void close() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
