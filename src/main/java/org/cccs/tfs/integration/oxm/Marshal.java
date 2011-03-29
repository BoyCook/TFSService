package org.cccs.tfs.integration.oxm;

import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * User: boycook
 * Date: 15/02/2011
 * Time: 18:32
 */
public class Marshal {

    private XStreamMarshaller marshaller;

    public Marshal(XStreamMarshaller marshaller) {
        this.marshaller = marshaller;
    }

    public String marshal(final Object obj) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        marshal(obj, new BufferedOutputStream(bos));
        return bos.toString();
    }

    private void marshal(final Object obj, final OutputStream stream) throws IOException {
        final Result result = new StreamResult(stream);
        marshaller.marshal(obj, result);
    }

    public Object unmarshal(final String data) throws IOException {
        return unmarshal(new ByteArrayInputStream(data.getBytes()));
    }

    private Object unmarshal(final InputStream stream) throws IOException {
        return marshaller.unmarshal(new StreamSource(stream));
    }
}
