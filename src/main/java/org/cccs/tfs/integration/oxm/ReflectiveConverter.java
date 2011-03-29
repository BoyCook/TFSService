package org.cccs.tfs.integration.oxm;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * User: Craig Cook
 * Date: May 31, 2010
 * Time: 2:39:34 PM
 */
public class ReflectiveConverter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void marshalObject(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        Class clazz = o.getClass();

        for (Field f : clazz.getFields()) {
            try {
                Object value = f.get(o);

                if (value == null) {
                    writer.startNode(f.getName());
                    writer.setValue("");
                    writer.endNode();
                } else {
                    if (value instanceof Collection) {
//                        if (!getFoo(context)) {
//                            context.put("FOO", "FOO");
//                            for (Object item: (List) value) {
//                                context.convertAnother(item);
//                            }
//                            context.put("FOO", null);
//                        }
                    } else { //TODO: deal with primitives
                        writer.startNode(f.getName());
                        writer.setValue((String) value);
                        writer.endNode();
                    }
                }
            } catch (IllegalAccessException e) {
                log.info("There was an error accessing class property value");
                e.printStackTrace();
            }

        }
    }

    private boolean getFoo(MarshallingContext context) {
        return context.get("FOO") != null;
    }

}
