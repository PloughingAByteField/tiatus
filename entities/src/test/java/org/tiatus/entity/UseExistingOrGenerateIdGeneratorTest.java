package org.tiatus.entity;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.enhanced.DatabaseStructure;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.EntityPersister;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by johnreynolds on 14/09/2016.
 */
@ExtendWith(MockitoExtension.class)
public class UseExistingOrGenerateIdGeneratorTest {

    @Mock
    private SequenceStyleGenerator sequenceStyleGeneratorMock;
    
    @Mock 
    private SessionImplementor sessionMock;

    @Mock 
    private ClassMetadata classMetadataMock;

    @Mock 
    private EntityPersister entityPersisterMock;
    
    @Mock
    private DatabaseStructure databaseStructure;

    @Test
    public void testExistingId() {
        when(classMetadataMock.getIdentifier(any(Object.class), any(SessionImplementor.class))).thenReturn(1L);
        when(entityPersisterMock.getClassMetadata()).thenReturn(classMetadataMock);
        when(sessionMock.getEntityPersister(any(), any())).thenReturn(entityPersisterMock);

        UseExistingOrGenerateIdGenerator generator = new UseExistingOrGenerateIdGenerator();
        
        generator.generate(sessionMock, new User());
    }

    @Test
    public void testGenerateNullObject() {

        Assertions.assertThrows(HibernateException.class, () -> {
            UseExistingOrGenerateIdGenerator generator = new UseExistingOrGenerateIdGenerator();
            generator.generate(null, null);
        });

    }
}
