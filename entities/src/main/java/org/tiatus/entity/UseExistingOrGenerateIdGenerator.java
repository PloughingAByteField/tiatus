package org.tiatus.entity;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import java.io.Serializable;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public class UseExistingOrGenerateIdGenerator extends SequenceStyleGenerator {
    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        if (object == null) throw new HibernateException(new NullPointerException()) ;
        Serializable id = session.getEntityPersister(null, object).getClassMetadata().getIdentifier(object, session);

        if (id == null) {
            id = super.generate(session, object);
        }
        return id;
    }
}
