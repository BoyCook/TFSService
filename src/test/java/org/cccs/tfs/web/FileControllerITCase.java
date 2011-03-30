package org.cccs.tfs.web;

import org.cccs.tfs.domain.File;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: boycook
 * Date: 30/03/2011
 * Time: 14:26
 */
@SuppressWarnings({"unchecked"})
public class FileControllerITCase extends JettyIntegrationTestEnvironment {

    static {
        overrideWebXML = true;
    }

    @Test
    public void getExampleShouldWork() {
        File file = (File) httpGet(serviceBaseURL + "examples/file");
        assertNotNull(file);
        assertEquals("exampleGroupID", file.getGroupId());
        assertEquals("exampleArtefactId", file.getArtefactId());
        assertEquals("exampleVersion", file.getVersion());
        assertEquals("exampleURL", file.getUrl());
    }

    @Test
    public void getFileByGroupIdShouldWork() {
        List<File> files = (List<File>) httpGet(serviceBaseURL + "files/org.cccs.jslibs/");
        assertNotNull(files);
        assertEquals(4, files.size());
    }

    @Test
    public void getFileByArtefactIdShouldWork() {
        List<File> files = (List<File>) httpGet(serviceBaseURL + "files/org.cccs.jslibs/jquery.collapsible/");
        assertNotNull(files);
        assertEquals(2, files.size());
    }

    @Test
    public void getFileShouldWork() {
        File file = (File) httpGet(serviceBaseURL + "files/org.cccs.jslibs/jquery.collapsible/1.0.0/");
        assertNotNull(file);

        assertEquals("org.cccs.jslibs", file.getGroupId());
        assertEquals("jquery.collapsible", file.getArtefactId());
        assertEquals("1.0.0", file.getVersion());
        assertEquals("https://github.com/BoyCook/JSLibs/raw/master/jquery.madeup/lib/jquery.collapsible.js", file.getUrl());
    }
}
