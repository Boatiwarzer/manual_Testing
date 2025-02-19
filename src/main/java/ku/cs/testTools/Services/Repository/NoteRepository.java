package ku.cs.testTools.Services.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import ku.cs.testTools.Models.TestToolModels.Connection;
import ku.cs.testTools.Models.TestToolModels.Note;
import ku.cs.testTools.Services.JpaUtil;

import java.util.List;

public class NoteRepository {
    private final EntityManager entityManager;

    // Constructor
    public NoteRepository() {
        this.entityManager = JpaUtil.getEntityManager();
    }

    // Create a new Connection
    public void addNote(Note note) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(note);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Find a Connection by ID
    public Note findById(String id) {
        try {
            TypedQuery<Note> query = entityManager.createNamedQuery("find Note by id", Note.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Retrieve all Connections
    public List<Note> getAllNote() {
        String query = "SELECT c FROM Note c";
        return entityManager.createQuery(query, Note.class).getResultList();
    }

    // Retrieve a Connection by ID
    public Note getNoteById(String id) {
        return entityManager.find(Note.class, id);
    }

    // Update an existing Connection
    public void updateNote(Note note) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(note);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Delete a Connection by ID
    public void deleteNote(String id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Note note = entityManager.find(Note.class, id);
            if (note != null) {
                entityManager.remove(note);
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
