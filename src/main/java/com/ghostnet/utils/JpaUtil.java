package com.ghostnet.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JpaUtil {

    private static final Logger logger = Logger.getLogger(JpaUtil.class.getName());
    private static final EntityManagerFactory emf;

    static {
        EntityManagerFactory tempEmf = null;
        try {
            tempEmf = Persistence.createEntityManagerFactory("ghostnetPU");
            logger.info("EntityManagerFactory erfolgreich erstellt.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fehler bei der Initialisierung der EntityManagerFactory: " + e.getMessage(), e);
        }
        emf = tempEmf;
    }

    public static EntityManager getEntityManager() {
        if (emf == null) {
            logger.severe("EntityManagerFactory konnte nicht initialisiert werden.");
            throw new IllegalStateException("EntityManagerFactory konnte nicht initialisiert werden.");
        }
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf != null) {
            emf.close();
            logger.info("EntityManagerFactory wurde geschlossen.");
        }
    }
}
