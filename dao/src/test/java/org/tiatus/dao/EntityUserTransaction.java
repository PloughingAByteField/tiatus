package org.tiatus.dao;

// import javax.persistence.EntityManager;
// import javax.transaction.*;

/**
 * Created by johnreynolds on 25/06/2016.
 */
public class EntityUserTransaction 
// implements UserTransaction 
{
    // private EntityManager em;

    // public EntityUserTransaction(EntityManager em) {
    //     this.em = em;
    // }

    // @Override
    // public void begin() throws NotSupportedException, SystemException {
    //     if (em.getTransaction() == null) {
    //         throw new SystemException();
    //     }
    //     if (em.getTransaction().isActive()) {
    //         throw new NotSupportedException();
    //     }
    //     em.getTransaction().begin();
    // }

    // @Override
    // public void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {
    //     if (em.getTransaction() == null) {
    //         throw new SystemException();
    //     }
    //     em.getTransaction().commit();
    // }

    // @Override
    // public int getStatus() throws SystemException {
    //     if (em.getTransaction() == null) {
    //         throw new SystemException();
    //     }
    //     if (em.getTransaction().isActive()) {
    //         return Status.STATUS_ACTIVE;
    //     } else {
    //         return Status.STATUS_NO_TRANSACTION;
    //     }
    // }

    // @Override
    // public void rollback() throws IllegalStateException, SecurityException, SystemException {
    //     if (em.getTransaction() == null) {
    //         throw new SystemException();
    //     }
    //     em.getTransaction().rollback();
    // }

    // @Override
    // public void setRollbackOnly() throws IllegalStateException, SystemException {
    //     if (em.getTransaction() == null) {
    //         throw new SystemException();
    //     }
    //     em.getTransaction().setRollbackOnly();
    // }

    // @Override
    // public void setTransactionTimeout(int i) throws SystemException {
    //     throw new UnsupportedOperationException();
    // }
}
