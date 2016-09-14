package org.tiatus.entity;

import mockit.Mock;
import mockit.MockUp;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.EntityPersister;
import org.junit.Test;

import java.io.Serializable;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class UseExistingOrGenerateIdGeneratorTest {

    @Test
    public void testGenerate() {
        new MockUp<SequenceStyleGenerator>() {
            @Mock
            public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
                return 1L;
            }
        };

        ClassMetadata classMetadata = new MockUp<ClassMetadata>() {
            @Mock
            Serializable getIdentifier(Object entity, SessionImplementor session) {
                return null;
            }
        }.getMockInstance();

        EntityPersister entityPersister = new MockUp<EntityPersister>() {
           @Mock
           public ClassMetadata getClassMetadata() {
               return classMetadata;
           }
        }.getMockInstance();

        SessionImplementor sesssion = new MockUp<SessionImplementor>() {
            @Mock
            EntityPersister getEntityPersister(String entityName, Object object) throws HibernateException {
                return entityPersister;
            }

        }.getMockInstance();

        UseExistingOrGenerateIdGenerator generator = new UseExistingOrGenerateIdGenerator();
        generator.generate(sesssion, new User());
    }

    @Test (expected = HibernateException.class)
    public void testGenerateNullObject() {
        UseExistingOrGenerateIdGenerator generator = new UseExistingOrGenerateIdGenerator();
        generator.generate(null, null);
    }
}
