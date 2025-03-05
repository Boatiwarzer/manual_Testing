package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.TestToolModels.IRreport;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;

public class IRReportRepository {
    private final EntityManager entityManager;

    // Constructor
    public IRReportRepository() {
        this.entityManager = JpaUtil.getEntityManager();
    }

    // Create a new IRReport
    public void saveOrUpdateIR(IRreport irReport) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(irReport);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Find an IRReport by ID
    public IRreport findById(String id) {
        try {
            TypedQuery<IRreport> query = entityManager.createNamedQuery("find IRreport by id", IRreport.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Retrieve all IRReports
    public List<IRreport> getAllIRReports() {
        String query = "SELECT t FROM IRreport t";
        return entityManager.createQuery(query, IRreport.class).getResultList();
    }

    // Retrieve an IRReport by ID
    public IRreport getIRReportById(String id) {
        return entityManager.find(IRreport.class, id);
    }

    // Update an existing IRReport
    public void updateIRReport(IRreport irReport) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(irReport);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Delete an IRReport by ID
    public void deleteIRReport(String id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            IRreport irReport = entityManager.find(IRreport.class, id);
            if (irReport != null) {
                entityManager.remove(irReport);
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
