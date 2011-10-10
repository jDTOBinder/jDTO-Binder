package com.juancavallotti.jdto;

import java.util.List;
import com.juancavallotti.jdto.dtos.FormatDTO;
import com.juancavallotti.jdto.entities.SimpleEntity;
import java.util.LinkedList;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author juancavallotti
 */
public class TestBindCollection {

    private static DTOBinder binder;

    @BeforeClass
    public static void globalInit() {
        binder = DTOBinderFactory.buildBinder();
    }

    @Test
    public void testBindSimpleCollection() {
        LinkedList<SimpleEntity> simpleEntities = new LinkedList<SimpleEntity>();
        simpleEntities.add(new SimpleEntity("simple 1", 12, 45.56, true));
        simpleEntities.add(new SimpleEntity("simple 2", 34, 56.67, false));
        
        List<FormatDTO> dtos = binder.bindFromBusinessObjectList(FormatDTO.class, simpleEntities);
        
        assertEquals("both lists should have the same size", simpleEntities.size(), dtos.size());
    }
}
