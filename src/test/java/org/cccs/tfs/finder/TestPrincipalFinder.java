package org.cccs.tfs.finder;

import org.cccs.tfs.domain.Principal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * User: Craig Cook
 * Date: Apr 1, 2010
 * Time: 7:00:36 PM
 */
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestPrincipalFinder {

    @Autowired
    private PrincipalFinder finder;

    @Test
    public void getAllShouldWork() throws Exception {
        assertThat(finder.all().size(), is(equalTo(3)));
    }

    @Test
    public void findShouldWork() throws Exception {
        Principal p = finder.find("BoyCook");
        assertNotNull(p);
        assertThat(p.getId(), is(greaterThan(0l)));
    }

    @Test
    public void findUpperCaseShouldWork() throws Exception {
        Principal p = finder.find("BOYCOOK");
        assertNotNull(p);
        assertThat(p.getId(), is(greaterThan(0l)));
    }

    @Test
    public void findLowerCaseShouldWork() throws Exception {
        Principal p = finder.find("boycook");
        assertNotNull(p);
        assertThat(p.getId(), is(greaterThan(0l)));
    }

    @Test(expected = EntityNotFoundException.class)
    public void findInvalidShouldThrowException() throws Exception {
        finder.find("nonsense");
    }

    @Test
    public void searchByShortNameShouldWork() throws Exception {
        List<Principal> principals = finder.search("cook");
        assertThat(principals.size(), is(equalTo(2)));
    }

    @Test
    public void searchByParamsShouldWork() throws Exception {
        Map<String, String[]> parameters = new HashMap<String, String[]>();
        parameters.put("foreName", new String[] {"craig"});
        parameters.put("surName", new String[] {"cook"});

        List<Principal> principals = finder.search(parameters);
        assertThat(principals.size(), is(equalTo(1)));
    }

    @Test
    public void filterByParamsShouldWork() throws Exception {
        Map<String, String[]> parameters = new HashMap<String, String[]>();
        parameters.put("surName", new String[] {"cook", "bar"});

        List<Principal> principals = finder.filter(parameters);
        assertThat(principals.size(), is(equalTo(3)));
    }
}
