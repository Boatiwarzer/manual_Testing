package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.IRreport;
import ku.cs.testTools.Models.TestToolModels.IRreportDetail;

import java.util.List;

public class IRDetailRepository {
    private final EntityManager entityManager;
    private final EntityManagerFactory emf;

    // Constructor to inject EntityManager

    public IRDetailRepository() {
        this.emf = Persistence.createEntityManagerFactory("test_db");
        this.entityManager = this.emf.createEntityManager();
    }

    public IRDetailRepository(String pu) {
        this.emf = Persistence.createEntityManagerFactory(pu);
        this.entityManager = this.emf.createEntityManager();    }

    // Create a new TestScript
    public void addIRRepository(IRreportDetail iRreportDetail) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(iRreportDetail);
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }
    public void findById(String id){
        Query query = entityManager.createNamedQuery("find IRreportDetail by id");
        query.setParameter("id",id);
        query.getSingleResult();
    }

    // Retrieve all TestScripts
    public List<IRreportDetail> getAllIRReportDetIL() {
        String query = "SELECT t FROM IRreportDetail t";
        TypedQuery<IRreportDetail> typedQuery = entityManager.createQuery(query, IRreportDetail.class);
        return typedQuery.getResultList();
    }

    // Retrieve a TestScript by ID
    public IRreportDetail getIRreportDetailById(String idIRD) {
        return entityManager.find(IRreportDetail.class, idIRD);
    }

    // Update an existing TestScript
    public void updateIRReportDetail(IRreportDetail iRreportDetail) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(iRreportDetail);  // Use merge for updating existing entities
            entityManager.getTransaction().commit();
        }catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }

    }

    // Delete a TestScript by ID
    public void deleteIRreportdetail(String idIR) {
        try {
            entityManager.getTransaction().begin();
            IRreportDetail iRreportDetail = entityManager.find(IRreportDetail.class, idIR);
            if (iRreportDetail != null) {
                entityManager.remove(iRreportDetail);  // Remove the entity from the database
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
