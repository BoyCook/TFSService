package org.cccs.tfs.integration.oxm;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.cccs.tfs.domain.File;

import static org.cccs.tfs.utils.RuntimeSupport.isNotEmpty;

/**
 * User: boycook
 * Date: 30/03/2011
 * Time: 12:19
 */
public class FileConverter implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        File f = (File) o;

        if (f.getId() != 0) {
            writer.addAttribute("id", String.valueOf(f.getId()));
            writer.startNode("id");
            writer.setValue(String.valueOf(f.getId()));
            writer.endNode();
        }

        writer.startNode("groupId");
        writer.setValue(f.getGroupId());
        writer.endNode();

        writer.startNode("artefactId");
        writer.setValue(f.getArtefactId());
        writer.endNode();

        writer.startNode("version");
        writer.setValue(f.getVersion());
        writer.endNode();

        writer.startNode("extension");
        writer.setValue(f.getExtension());
        writer.endNode();

        writer.startNode("url");
        writer.setValue(f.getUrl());
        writer.endNode();

        if (isNotEmpty(f.getName())) {
            writer.startNode("name");
            writer.setValue(f.getName());
            writer.endNode();
        }

        if (isNotEmpty(f.getDescription())) {
            writer.startNode("description");
            writer.setValue(f.getDescription());
            writer.endNode();
        }

        if (isNotEmpty(f.getStorageType())) {
            writer.startNode("storageType");
            writer.setValue(f.getStorageType());
            writer.endNode();
        }

        if (isNotEmpty(f.getWebsite())) {
            writer.startNode("website");
            writer.setValue(f.getWebsite());
            writer.endNode();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        final File f = new File();

        while (reader.hasMoreChildren()) {
            reader.moveDown();

            String node = reader.getNodeName();
            String value = reader.getValue();

            if (node.equalsIgnoreCase("id")) {
                f.setId(Long.valueOf(value));
            } else if (node.equalsIgnoreCase("groupId")) {
                f.setGroupId(value);
            } else if (node.equalsIgnoreCase("artefactId")) {
                f.setArtefactId(value);
            } else if (node.equalsIgnoreCase("version")) {
                f.setVersion(value);
            } else if (node.equalsIgnoreCase("extension")) {
                f.setExtension(value);
            } else if (node.equalsIgnoreCase("url")) {
                f.setUrl(value);
            } else if (node.equalsIgnoreCase("name")) {
                f.setName(value);
            } else if (node.equalsIgnoreCase("description")) {
                f.setDescription(value);
            } else if (node.equalsIgnoreCase("storageType")) {
                f.setStorageType(value);
            } else if (node.equalsIgnoreCase("website")) {
                f.setWebsite(value);
            }

            reader.moveUp();
        }

        return f;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass == File.class;
    }
}
