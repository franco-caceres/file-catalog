package pe.fcg.kth.id1212.hw3.server.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

class BaseDao {
    private final static EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("fileCatalogPersistenceUnit");
    private final ThreadLocal<EntityManager> threadLocalEm = new ThreadLocal<>();

    EntityManager beginTransaction() {
        EntityManager em = emFactory.createEntityManager();
        threadLocalEm.set(em);
        EntityTransaction txn = em.getTransaction();
        if(!txn.isActive()) {
            txn.begin();
        }
        return em;
    }

    void commitTransaction() {
        threadLocalEm.get().getTransaction().commit();
    }
}
