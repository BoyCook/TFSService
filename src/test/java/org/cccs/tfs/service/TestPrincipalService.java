package org.cccs.tfs.service;

import org.cccs.tfs.domain.Location;
import org.cccs.tfs.domain.Principal;
import org.cccs.tfs.finder.PrincipalFinder;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ValidationException;
import java.util.List;

import static org.cccs.tfs.service.SiteService.setLoggedInUser;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * User: boycook
 * Date: 10/02/2011
 * Time: 21:08
 */
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestPrincipalService {

    private Principal bobSmith = new Principal("BobSmith", "Bob", "Smith", "password", "craig.cook@bt.com", "07918880501");
    private Principal tomJones = new Principal("TomJones", "Tom", "Jones", "password", "tom.jones@me.com", "07918880501");
    private Principal johnDoe = new Principal("JohnDoe", "John", "Doe", "password", "tom.jones@me.com", "07918880501");
    private Principal janeDoe = new Principal("JaneDoe", "Jane", "Doe", "password", "tom.jones@me.com", "07918880501");
    private Principal gregJones = new Principal("GregJones", null, null, "password", "craig.cook@bt.com", "07918880501");

    @Autowired
    private PrincipalService service;

    @Autowired
    private PrincipalFinder finder;

    @After
    public void tearDown() {
        setLoggedInUser(null);
    }

    @Test
    public void a01CreateNewShouldWork() {
        assertSize(3);
        service.create(bobSmith);
        service.create(johnDoe);
        service.create(janeDoe);
        assertSize(6);
        assertThat(bobSmith.getId(), is(greaterThan(0l)));
    }

    @Test(expected = ValidationException.class)
    public void a02CreateDuplicateShouldFail() {
        service.create(tomJones);
        tomJones.setId(0);
        service.create(tomJones);
    }

    @Test(expected = ValidationException.class)
    public void a03CreateWithMissingNotNullFieldsShouldFail() {
        service.create(gregJones);
    }

    @Test
    public void a04UpdateShouldWork() {
        setLoggedInUser(bobSmith.getShortName());
        Principal p1 = finder.find(bobSmith.getShortName());
        p1.setEmail("foo");
        service.update(p1);
        Principal p2 = finder.find(bobSmith.getShortName());
        assertEquals(p1.getEmail(), p2.getEmail());
    }

    @Test
    public void a05UpdateLocationShouldWork() {
        setLoggedInUser(bobSmith.getShortName());
        Principal p1 = finder.find(bobSmith.getShortName());
        Location location = new Location(123, 456);
        service.updateLocation(p1, location);

        Principal p2 = finder.find(bobSmith.getShortName());
        assertThat(p2.getLocation().getLatitude(), is(equalTo(location.getLatitude())));
        assertThat(p2.getLocation().getLongitude(), is(equalTo(location.getLongitude())));
    }

    @Test(expected = SecurityException.class)
    public void a06UpdateLocationShouldFailForWrongUser() {
        setLoggedInUser(null);
        Principal p1 = finder.find(bobSmith.getShortName());
        service.updateLocation(p1, new Location(123, 456));
    }

    @Test(expected = SecurityException.class)
    public void a07UpdateShouldWorkFailForWrongUser() {
        setLoggedInUser(null);
        Principal p1 = finder.find(bobSmith.getShortName());
        p1.setEmail("foo");
        service.update(p1);
    }

    @Test(expected = ValidationException.class)
    public void a08AddFriendShouldFailWithoutFriendRequest() {
        Principal bs1 = finder.find(bobSmith.getShortName());
        Principal tj1 = finder.find(tomJones.getShortName());
        service.addFriend(bs1, tj1);
    }

    @Test
    public void a09AddFriendRequestShouldWork() {
        Principal bs1 = finder.find(bobSmith.getShortName());
        Principal tj1 = finder.find(tomJones.getShortName());
        service.addFriendRequest(bs1, tj1);

        Principal bs2 = finder.find(bobSmith.getShortName());
        Principal tj2 = finder.find(tomJones.getShortName());

        assertThat(bs2.getFriendRequests().size(), is(equalTo(1)));
        assertThat(tj2.getFriendRequests().size(), is(equalTo(0)));
        assertTrue(bs2.getFriendRequests().contains(tj2));
        assertFalse(tj2.getFriendRequests().contains(bs2));
    }

    @Test(expected = SecurityException.class)
    public void a10AddFriendShouldFailForWrongUser() {
        setLoggedInUser(null);
        Principal bs1 = finder.find(bobSmith.getShortName());
        Principal tj1 = finder.find(tomJones.getShortName());
        service.addFriend(bs1, tj1);
    }

    @Test
    public void a11AddFriendShouldWork() {
        setLoggedInUser(bobSmith.getShortName());
        Principal bs1 = finder.find(bobSmith.getShortName());
        Principal tj1 = finder.find(tomJones.getShortName());
        service.addFriend(bs1, tj1);

        Principal bs2 = finder.find(bobSmith.getShortName());
        Principal tj2 = finder.find(tomJones.getShortName());

        assertThat(bs2.getFriends().size(), is(equalTo(1)));
        assertThat(tj2.getFriends().size(), is(equalTo(1)));
        assertTrue(bs2.getFriends().contains(tj2));
        assertTrue(tj2.getFriends().contains(bs2));
        assertThat(bs2.getFriendRequests().size(), is(equalTo(0)));
        assertThat(tj2.getFriendRequests().size(), is(equalTo(0)));
        assertFalse(bs2.getFriendRequests().contains(tj2));
        assertFalse(tj2.getFriendRequests().contains(bs2));
    }

    @Test(expected = SecurityException.class)
    public void a12DeclineFriendRequestShouldFailForWrongUser() {
        setLoggedInUser(johnDoe.getShortName());
        Principal john1 = finder.find(johnDoe.getShortName());
        Principal jane1 = finder.find(janeDoe.getShortName());

        assertThat(john1.getFriendRequests().size(), is(equalTo(0)));
        assertThat(jane1.getFriendRequests().size(), is(equalTo(0)));
        assertFalse(john1.getFriendRequests().contains(jane1));
        assertFalse(jane1.getFriendRequests().contains(john1));

        service.addFriendRequest(john1, jane1);

        Principal john2 = finder.find(johnDoe.getShortName());
        Principal jane2 = finder.find(janeDoe.getShortName());

        assertThat(john2.getFriendRequests().size(), is(equalTo(1)));
        assertThat(jane2.getFriendRequests().size(), is(equalTo(0)));
        assertTrue(john2.getFriendRequests().contains(jane2));
        assertFalse(jane2.getFriendRequests().contains(john2));

        setLoggedInUser(null);
        service.removeFriendRequest(john2, jane2);
    }

    @Test
    public void a13DeclineFriendRequestShouldWork() {
        setLoggedInUser(johnDoe.getShortName());
        Principal john1 = finder.find(johnDoe.getShortName());
        Principal jane1 = finder.find(janeDoe.getShortName());

        service.removeFriendRequest(john1, jane1);

        Principal john2 = finder.find(johnDoe.getShortName());
        Principal jane2 = finder.find(janeDoe.getShortName());

        assertThat(john2.getFriendRequests().size(), is(equalTo(0)));
        assertThat(jane2.getFriendRequests().size(), is(equalTo(0)));
        assertFalse(john2.getFriendRequests().contains(jane2));
        assertFalse(jane2.getFriendRequests().contains(john2));
    }

    @Test(expected = ValidationException.class)
    public void a14DeclineFriendRequestShouldFailIfThereIsNoRequest() {
        Principal bs1 = finder.find(bobSmith.getShortName());
        Principal tj1 = finder.find(tomJones.getShortName());
        service.removeFriendRequest(bs1, tj1);
    }

    @Test(expected = SecurityException.class)
    public void a15RemoveFriendShouldFailForWrongUser() {
        setLoggedInUser(null);
        Principal bs1 = finder.find(bobSmith.getShortName());
        Principal tj1 = finder.find(tomJones.getShortName());
        service.removeFriend(bs1, tj1);
    }

    @Test
    public void a16RemoveFriendShouldWork() {
        setLoggedInUser(bobSmith.getShortName());
        Principal bs1 = finder.find(bobSmith.getShortName());
        Principal tj1 = finder.find(tomJones.getShortName());
        service.removeFriend(bs1, tj1);

        Principal bs2 = finder.find(bobSmith.getShortName());
        Principal tj2 = finder.find(tomJones.getShortName());

        assertThat(bs2.getFriends().size(), is(equalTo(0)));
        assertThat(tj2.getFriends().size(), is(equalTo(0)));
        assertFalse(bs2.getFriends().contains(tj2));
        assertFalse(tj2.getFriends().contains(bs2));
    }

    @Test
    public void a17DeleteShouldWork() {
        Principal p1 = finder.find(bobSmith.getShortName());
        Principal p2 = finder.find(tomJones.getShortName());
        Principal p3 = finder.find(johnDoe.getShortName());
        Principal p4 = finder.find(janeDoe.getShortName());
        List beforeList = finder.all();

        service.delete(p1);
        service.delete(p2);
        service.delete(p3);
        service.delete(p4);
        List afterList = finder.all();

        assertThat(afterList.size(), is(lessThan(beforeList.size())));
    }

    private void assertSize(int size) {
        List<Principal> list = finder.all();
        assertThat(list.size(), is(equalTo(size)));
    }
}
