package org.cccs.tfs.integration.oxm;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.cccs.tfs.domain.Location;

/**
 * User: boycook
 * Date: 18/02/2011
 * Time: 20:37
 */
public class LocationConverter implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        Location l = (Location) o;
        marshalLocation(l, writer);
    }

    private void marshalLocation(Location l, HierarchicalStreamWriter writer) {
        writer.startNode("latitude");
        writer.setValue(l.latitude());
        writer.endNode();

        writer.startNode("longitude");
        writer.setValue(l.longitude());
        writer.endNode();
    }


    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        final Location l = new Location();

        while (reader.hasMoreChildren()) {
            reader.moveDown();

            String node = reader.getNodeName();
            String value = reader.getValue();

            if (node.equalsIgnoreCase("latitude")) {
                l.setLatitude(Double.valueOf(value));
            } else if (node.equalsIgnoreCase("longitude")) {
                l.setLongitude(Double.valueOf(value));
            }

            reader.moveUp();
        }

        return l;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass == Location.class;
    }
}
