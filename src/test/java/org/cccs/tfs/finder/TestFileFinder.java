package org.cccs.tfs.finder;

import org.cccs.tfs.domain.File;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: boycook
 * Date: 29/03/2011
 * Time: 23:41
 */
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestFileFinder {

    @Autowired
    private FileFinder finder;

    @Test
    public void getAllShouldWork() throws Exception {
        assertThat(finder.all().size(), is(equalTo(5)));
    }

    @Test
    public void searchByArtefactIdShouldWork() throws Exception {
        List<File> files = finder.search("jsMap");
        assertThat(files.size(), is(equalTo(1)));
    }

    @Test
    public void searchByParamsShouldWork() throws Exception {
        Map<String, String[]> parameters = new HashMap<String, String[]>();
        parameters.put("groupId", new String[] {"org.cccs.jslibs"});
        parameters.put("version", new String[] {"1.0.0"});

        List<File> files = finder.search(parameters);
        assertThat(files.size(), is(equalTo(1)));
    }

    @Test
    public void filterByParamsShouldWork() throws Exception {
        Map<String, String[]> parameters = new HashMap<String, String[]>();
        parameters.put("artefactId", new String[] {"jquery.collapsible", "jquery.hintbox"});

        List<File> files = finder.filter(parameters);
        assertThat(files.size(), is(equalTo(3)));
    }
}
