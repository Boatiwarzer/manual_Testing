package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.UseCase;

import java.util.List;

public class UseCaseRepository {
    private final EntityManager entityManager;
    private final EntityManagerFactory emf;

    // Constructor to inject EntityManager

    public UseCaseRepository() {
        this.emf = Persistence.createEntityManagerFactory("test_db");
        this.entityManager = this.emf.createEntityManager();
    }

    public UseCaseRepository(String pu) {
        this.emf = Persistence.createEntityManagerFactory(pu);
        this.entityManager = this.emf.createEntityManager();    }

    // Create a new UseCase
    public void addUseCase(UseCase UseCase) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(UseCase);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }
    public void findById(String id){
        Query query = entityManager.createNamedQuery("find usecase by id");
        query.setParameter("id",id);
        query.getSingleResult();
    }

    // Retrieve all UseCases
    public List<UseCase> getAllUseCases() {
        String query = "SELECT t FROM UseCase t";
        TypedQuery<UseCase> typedQuery = entityManager.createQuery(query, UseCase.class);
        return typedQuery.getResultList();
    }

    // Retrieve a UseCase by ID
    public UseCase getUseCaseById(String idUC) {
        return entityManager.find(UseCase.class, idUC);
    }

    // Update an existing UseCase
    public void updateUseCase(UseCase useCase) {
        UseCase useCaseUpdate = getUseCaseById(useCase.getUseCaseID());

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(useCaseUpdate);  // Use merge for updating existing entities
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }

    // Delete a UseCase by ID
    public void deleteUseCase(String idUC) {
        try {
            entityManager.getTransaction().begin();
            UseCase useCase = entityManager.find(UseCase.class, idUC);
            if (useCase != null) {
                entityManager.remove(useCase);  // Remove the entity from the database
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
