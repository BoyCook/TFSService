package org.cccs.tfs.service;

import org.cccs.tfs.domain.File;
import org.cccs.tfs.finder.FileFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ValidationException;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * User: boycook
 * Date: 29/03/2011
 * Time: 21:34
 */
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestFileService {

    private File array = new File("org.cccs.jslibs", "jsarray", "1.0", "https://github.com/BoyCook/JSLibs/raw/master/array/lib/jsArray.js");

    @Autowired
    private FileService service;

    @Autowired
    private FileFinder finder;

    @Test
    public void b01CreateShouldWork() {
        assertSize(4);
        service.create(array);
        assertSize(5);
    }

    @Test(expected = ValidationException.class)
    public void b02CreateDuplicateShouldFail() {
        service.create(array);
    }

    @Test(expected = ValidationException.class)
    public void b03CreateWithEmptyNotNullFieldsShouldFail() {
        service.create(new File("org.cccs.jslibs", "somethingnew", "1.0", ""));
    }

    @Test(expected = ValidationException.class)
    public void b03CreateWithNullNotNullFieldsShouldFail() {
        service.create(new File("org.cccs.jslibs", "somethingnew", "1.0", null));
    }

    @Test
    public void b04UpdateShouldWork() {
        File jsArray1 = finder.search("jsarray").get(0);
        jsArray1.setUrl("http://craigcook.co.uk/svn");
        service.update(jsArray1);

        File jsArray2 = finder.search("jsarray").get(0);
        assertEquals(jsArray1.getUrl(), jsArray2.getUrl());
    }

    @Test(expected = ValidationException.class)
    public void b05UpdateWithMissingNotNullFieldsShouldFail() {
        File jsArray1 = finder.search("jsarray").get(0);
        jsArray1.setUrl(null);
        service.update(jsArray1);
    }

    @Test
    public void b06DeleteShouldWork() {
        assertSize(5);
        File jsArray1 = finder.search("jsarray").get(0);
        service.delete(jsArray1);
        assertSize(4);
    }

    private void assertSize(int size) {
        List<File> list = finder.all();
        assertThat(list.size(), is(equalTo(size)));
    }
}
