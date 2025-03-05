package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.IRreport;
import ku.cs.testTools.Models.TestToolModels.IRreportDetail;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;

public class IRDetailRepository {
    private final EntityManager entityManager;

    // Constructor to inject EntityManager

    public IRDetailRepository() {
        this.entityManager = JpaUtil.getEntityManager();
    }

    // Create a new TestScript
    public void saveOrUpdateIRDetail(IRreportDetail iRreportDetail) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(iRreportDetail);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
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
