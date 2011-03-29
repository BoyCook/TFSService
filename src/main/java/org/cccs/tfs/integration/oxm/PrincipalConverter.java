package org.cccs.tfs.integration.oxm;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.cccs.tfs.domain.Location;
import org.cccs.tfs.domain.Principal;

/**
 * User: Craig Cook
 * Date: Apr 3, 2010
 * Time: 2:15:42 PM
 */
public class PrincipalConverter implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        Principal p = (Principal) o;
        marshalPerson(p, writer, context);
    }

    private void marshalPerson(Principal p, HierarchicalStreamWriter writer, MarshallingContext context) {
        if (p.getId() != 0) {
            writer.addAttribute("id", String.valueOf(p.getId()));
            writer.startNode("id");
            writer.setValue(String.valueOf(p.getId()));
            writer.endNode();
        }

        writer.startNode("shortName");
        writer.setValue(p.getShortName());
        writer.endNode();

        writer.startNode("foreName");
        writer.setValue(p.getForeName());
        writer.endNode();

        writer.startNode("surName");
        writer.setValue(p.getSurName());
        writer.endNode();

        //TODO: conditional rendering of this
        writer.startNode("password");
        writer.setValue(p.getPassword());
        writer.endNode();

        writer.startNode("phoneNumber");
        writer.setValue(p.getPhoneNumber());
        writer.endNode();

        writer.startNode("email");
        writer.setValue(p.getEmail());
        writer.endNode();

        if (p.getDistance() != 0) {
            writer.startNode("distance");
            writer.setValue(String.valueOf(p.getDistance()));
            writer.endNode();
        }

        if (p.getLocation() != null) {
            writer.startNode("location");
            context.convertAnother(p.getLocation());
            writer.endNode();
        }

        if (!isDone(context)) {
            context.put("DONE", "done");
            writer.startNode("friends");
            for (Principal f : p.getFriends()) {
                writer.startNode("principal");
                context.convertAnother(f);
                writer.endNode();
            }
            writer.endNode();
            context.put("DONE", null);
        }

        if (!isDone(context)) {
            context.put("DONE", "done");
            writer.startNode("friendRequests");
            for (Principal f : p.getFriendRequests()) {
                writer.startNode("principal");
                context.convertAnother(f);
                writer.endNode();
            }
            writer.endNode();
            context.put("DONE", null);
        }
    }

    private boolean isDone(MarshallingContext context) {
        return context.get("DONE") != null;
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        final Principal p = new Principal();

        while (reader.hasMoreChildren()) {
            reader.moveDown();

            String node = reader.getNodeName();
            String value = reader.getValue();

            if (node.equalsIgnoreCase("id")) {
                p.setId(Long.valueOf(value));
            } else if (node.equalsIgnoreCase("shortName")) {
                p.setShortName(value);
            } else if (node.equalsIgnoreCase("foreName")) {
                p.setForeName(value);
            } else if (node.equalsIgnoreCase("surName")) {
                p.setSurName(value);
            } else if (node.equalsIgnoreCase("password")) {
                p.setPassword(value);
            } else if (node.equalsIgnoreCase("phoneNumber")) {
                p.setPhoneNumber(value);
            } else if (node.equalsIgnoreCase("email")) {
                p.setEmail(value);
            } else if (node.equalsIgnoreCase("location")) {
                Location l = (Location) context.convertAnother(null, Location.class);
                p.setLocation(l);
            } else if (node.equalsIgnoreCase("friends")) {
                while (reader.hasMoreChildren()) {
                    reader.moveDown();
                    Principal f = (Principal) context.convertAnother(null, Principal.class);
                    p.addFriend(f);
                    reader.moveUp();
                }
            } else if (node.equalsIgnoreCase("friendRequests")) {
                while (reader.hasMoreChildren()) {
                    reader.moveDown();
                    Principal f = (Principal) context.convertAnother(null, Principal.class);
                    p.addFriendRequest(f);
                    reader.moveUp();
                }
            }

            reader.moveUp();
        }

        return p;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass == Principal.class;
    }
}
