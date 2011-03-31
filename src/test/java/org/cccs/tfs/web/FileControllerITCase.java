package org.cccs.tfs.web;

import org.cccs.tfs.domain.File;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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
        assertEquals("https://github.com/BoyCook/JSLibs/raw/master/jquery.collapsible/lib/jquery.collapsible.js", file.getUrl());
    }

    @Test
    public void addFileShouldWork() {
        File f1 = new File("org.cccs.jslibs", "jsarray", "1.0", "js", "https://github.com/BoyCook/JSLibs/raw/master/array/lib/jsArray.js");
        http(serviceBaseURL + "files/" + f1.getKey() + "/", f1, HttpMethod.PUT);

        File f2 = (File) httpGet(serviceBaseURL + "files/" + f1.getKey() + "/");
        assertNotNull(f2);
        assertThat(f1.getGroupId(), is(equalTo(f2.getGroupId())));
        assertThat(f1.getArtefactId(), is(equalTo(f2.getArtefactId())));
        assertThat(f1.getVersion(), is(equalTo(f2.getVersion())));
        assertThat(f1.getExtension(), is(equalTo(f2.getExtension())));
        assertThat(f1.getUrl(), is(equalTo(f2.getUrl())));
    }

    @Ignore
    @Test
    public void updateFileShouldWork() {
        File f1 = new File("org.cccs.jslibs", "jsarray", "1.0", "js", "https://github.com/BoyCook/JSLibs/raw/master/array/lib/jsArray.js");
        http(serviceBaseURL + "files/" + f1.getKey() + "/", f1, HttpMethod.PUT);

        File f2 = (File) httpGet(serviceBaseURL + "files/" + f1.getKey() + "/");
        assertNotNull(f2);
        assertThat(f1.getGroupId(), is(equalTo(f2.getGroupId())));
        assertThat(f1.getArtefactId(), is(equalTo(f2.getArtefactId())));
        assertThat(f1.getVersion(), is(equalTo(f2.getVersion())));
        assertThat(f1.getExtension(), is(equalTo(f2.getExtension())));
        assertThat(f1.getUrl(), is(equalTo(f2.getUrl())));
    }
}
