package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.UseCaseDetail;

import java.util.List;

public class UseCaseDetailRepository {
    private final EntityManager entityManager;
    private final EntityManagerFactory emf;

    // Constructor to inject EntityManager

    public UseCaseDetailRepository() {
        this.emf = Persistence.createEntityManagerFactory("test_db");
        this.entityManager = this.emf.createEntityManager();
    }

    public UseCaseDetailRepository(String pu) {
        this.emf = Persistence.createEntityManagerFactory(pu);
        this.entityManager = this.emf.createEntityManager();    }

    // Create a new UseCaseDetail
    public void addUseCaseDetail(UseCaseDetail useCaseDetail) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(useCaseDetail);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }
    public void findById(String id){
        Query query = entityManager.createNamedQuery("find usecasedetail by id");
        query.setParameter("id",id);
        query.getSingleResult();
    }

    // Retrieve all UseCaseDetails
    public List<UseCaseDetail> getAllUseCaseDetails() {
        String query = "SELECT t FROM UseCaseDetail t";
        TypedQuery<UseCaseDetail> typedQuery = entityManager.createQuery(query, UseCaseDetail.class);
        return typedQuery.getResultList();
    }

    // Retrieve a UseCaseDetail by ID
    public UseCaseDetail getUseCaseDetailById(String idUCD) {
        return entityManager.find(UseCaseDetail.class, idUCD);
    }

    // Update an existing UseCaseDetail
    public void updateUseCaseDetail(UseCaseDetail useCaseDetail) {
        UseCaseDetail useCaseDetailUpdate = getUseCaseDetailById(useCaseDetail.getUseCaseID());

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(useCaseDetailUpdate);  // Use merge for updating existing entities
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }

    // Delete a UseCaseDetail by ID
    public void deleteUseCaseDetail(String idUCD) {
        try {
            entityManager.getTransaction().begin();
            UseCaseDetail useCaseDetail = entityManager.find(UseCaseDetail.class, idUCD);
            if (useCaseDetail != null) {
                entityManager.remove(useCaseDetail);  // Remove the entity from the database
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
