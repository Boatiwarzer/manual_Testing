package ku.cs.testTools.Services.Repository;

import jakarta.persistence.*;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;

public class ManagerRepository {
    private final EntityManager entityManager;

    // Constructor
    public ManagerRepository() {
        this.entityManager = JpaUtil.getEntityManager();
    }
    public Manager getManagerByProjectName(String projectName) {
        for (Manager manager : getAllManagers()) {
            if (manager.getProjectName().equals(projectName)) {
                return manager;  // คืนค่า Manager ที่ตรงกับโปรเจกต์
            }
        }
        return null; // ถ้าไม่พบ Manager
    }
    // Create a new Manager
    public void addManager(Manager manager) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(manager);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Find a Manager by ID
    public Manager findById(String id) {
        return entityManager.find(Manager.class, id);
    }

    // Retrieve all Managers
    public List<Manager> getAllManagers() {
        String query = "SELECT m FROM Manager m";
        return entityManager.createQuery(query, Manager.class).getResultList();
    }

    // Update an existing Manager
    public void updateManager(Manager manager) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(manager);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Delete a Manager by ID
    public void deleteManager(String id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Manager manager = entityManager.find(Manager.class, id);
            if (manager != null) {
                entityManager.remove(manager);
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
