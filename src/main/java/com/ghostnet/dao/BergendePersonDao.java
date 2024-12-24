package com.ghostnet.dao;

import com.ghostnet.entities.BergendePerson;
import com.ghostnet.utils.JpaUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class BergendePersonDao {
    private static final Logger logger = LoggerFactory.getLogger(BergendePersonDao.class);

    public void speichern(BergendePerson bergendePerson) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(bergendePerson);
            entityManager.getTransaction().commit();
            logger.info("BergendePerson gespeichert: {}", bergendePerson);
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.error("Fehler beim Speichern der Bergenden Person", e);
        } finally {
            entityManager.close();
        }
    }

    public void aktualisieren(BergendePerson bergendePerson) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(bergendePerson);
            entityManager.getTransaction().commit();
            logger.info("BergendePerson aktualisiert: {}", bergendePerson);
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.error("Fehler beim Aktualisieren der Bergenden Person", e);
        } finally {
            entityManager.close();
        }
    }

    public boolean istEmailBereitsRegistriert(String email) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        try {
            Long count = entityManager.createQuery(
                            "SELECT COUNT(b) FROM BergendePerson b WHERE b.mail = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return count > 0;
        } finally {
            entityManager.close();
        }
    }

    public BergendePerson findByEmail(String email) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT b FROM BergendePerson b WHERE b.mail = :email", BergendePerson.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null; // Benutzer nicht gefunden
        } finally {
            entityManager.close();
        }
    }


}
