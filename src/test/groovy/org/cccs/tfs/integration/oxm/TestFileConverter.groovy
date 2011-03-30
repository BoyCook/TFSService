package org.cccs.tfs.integration.oxm

import org.springframework.test.context.ContextConfiguration
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.custommonkey.xmlunit.XMLTestCase
import org.springframework.oxm.xstream.XStreamMarshaller
import org.springframework.beans.factory.annotation.Autowired
import org.cccs.tfs.finder.FileFinder
import org.custommonkey.xmlunit.XMLUnit
import org.junit.Test
import javax.xml.transform.stream.StreamSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.Result

/**
 * User: boycook
 * Date: 30/03/2011
 * Time: 12:24
 */
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
class TestFileConverter extends XMLTestCase {

    static {
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
    }

    @Autowired
    private FileFinder finder;

    @Autowired
    private XStreamMarshaller marshaller;

    private String expectedList = """
        <resources>
            <file id="12">
                <id>12</id>
                <groupId>org.cccs.jslibs</groupId>
                <artefactId>jquery.collapsible</artefactId>
                <version>1.0</version>
                <extension>js</extension>
                <url>https://github.com/BoyCook/JSLibs/raw/master/jquery.collapsible/lib/jquery.collapsible.js</url>
            </file>
            <file id="13">
                <id>13</id>
                <groupId>org.cccs.jslibs</groupId>
                <artefactId>jquery.collapsible</artefactId>
                <version>1.0.0</version>
                <extension>js</extension>
                <url>https://github.com/BoyCook/JSLibs/raw/master/jquery.collapsible/lib/jquery.collapsible.js</url>
            </file>
        </resources>
    """

    private String expectedFile = """
        <file id="12">
            <id>12</id>
            <groupId>org.cccs.jslibs</groupId>
            <artefactId>jquery.collapsible</artefactId>
            <version>1.0</version>
            <extension>js</extension>
            <url>https://github.com/BoyCook/JSLibs/raw/master/jquery.collapsible/lib/jquery.collapsible.js</url>
        </file>
    """

    @Test
    public void marshallingListShouldWork() throws IOException {
        String marshalled = marshal(finder.search("jquery.collapsible"));
        println marshalled;
        assertXMLEqual("Comparing marshalled xml to expected xml", expectedList, marshalled);
    }

    @Test
    public void marshallingAFileShouldWork() throws IOException {
        String marshalled = marshal(finder.search("jquery.collapsible").get(0));
        println marshalled;
        assertXMLEqual("Comparing marshalled xml to expected xml", expectedFile, marshalled);
    }

    private String marshal(final Object obj) throws IOException {
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

    public Object unmarshal(final InputStream stream) throws IOException {
        return marshaller.unmarshal(new StreamSource(stream));
    }
}
