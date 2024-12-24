package com.ghostnet.dao;

import com.ghostnet.entities.MeldendePerson;
import com.ghostnet.utils.JpaUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class MeldendePersonDao {

    public void speichern(MeldendePerson meldendePerson) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(meldendePerson);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}

