package org.cccs.tfs.service;

import org.cccs.tfs.finder.FileFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * User: boycook
 * Date: 29/03/2011
 * Time: 21:34
 */
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestFileService {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileFinder fileFinder;

    @Test
    public void createShouldWork() {

    }

    @Test
    public void createDuplicateShouldFail() {

    }

    @Test
    public void createWithMissingNotNullFieldsShouldFail() {

    }

    @Test
    public void updateShouldWork() {

    }

    @Test
    public void updateWithMissingNotNullFieldsShouldFail() {

    }

    @Test
    public void deleteShouldWork() {

    }
}
