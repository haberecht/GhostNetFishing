package com.ghostnet.dao;

import com.ghostnet.entities.Geisternetz;
import com.ghostnet.utils.JpaUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class GeisternetzDao {
    private static final Logger logger = LoggerFactory.getLogger(GeisternetzDao.class);

    public List<Geisternetz> alleGeisternetze() {
        EntityManager entityManager = JpaUtil.getEntityManager();
        try {
            List<Geisternetz> result = entityManager.createQuery("SELECT g FROM Geisternetz g", Geisternetz.class).getResultList();
            logger.info("Geisternetze aus der Datenbank abgerufen: {}", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Fehler beim Abrufen der Geisternetze", e);
            return Collections.emptyList();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    // Geisternetz speichern
    public void speichern(Geisternetz geisternetz) {
        if (geisternetz == null) {
            logger.error("Das Geisternetz ist null und kann nicht gespeichert werden.");
            return;
        }
        EntityManager entityManager = JpaUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(geisternetz);
            entityManager.getTransaction().commit();
            logger.info("Geisternetz mit ID {} erfolgreich gespeichert.", geisternetz.getId());
        } catch (Exception e) {
            handleTransactionError(entityManager, e, "Fehler beim Speichern des Geisternetzes");
        } finally {
            closeEntityManager(entityManager);
        }
    }


    // Geisternetz aktualisieren
    public void aktualisieren(Geisternetz geisternetz) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(geisternetz);
            entityManager.getTransaction().commit();
            logger.info("Geisternetz mit ID {} erfolgreich aktualisiert.", geisternetz.getId());
        } catch (Exception e) {
            handleTransactionError(entityManager, e, "Fehler beim Aktualisieren des Geisternetzes");
        } finally {
            closeEntityManager(entityManager);
        }
    }

    // Private Hilfsmethode zur Behandlung von Transaktionsfehlern
    private void handleTransactionError(EntityManager entityManager, Exception e, String errorMessage) {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
        logger.error(errorMessage, e);
    }

    // Private Hilfsmethode zum Schließen des EntityManagers
    private void closeEntityManager(EntityManager entityManager) {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
