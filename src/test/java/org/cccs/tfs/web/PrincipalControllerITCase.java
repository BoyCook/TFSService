package org.cccs.tfs.web;

import org.cccs.tfs.domain.Location;
import org.cccs.tfs.domain.Principal;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpMethod;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * User: Craig Cook
 * Date: Jun 10, 2010
 * Time: 7:51:47 PM
 */

@SuppressWarnings({"unchecked"})
public class PrincipalControllerITCase extends JettyIntegrationTestEnvironment {

    static {
        overrideWebXML = true;
    }

    @Before
    public void login() {
        if (!getHeaders().containsKey("Cookie")) {
            login("boycook", "password");
        }
    }

    public void testNothing() {
        assertTrue(false);
    }

    @Test 
    public void getUserShouldWork() {
        Principal p = (Principal) httpGet(serviceBaseURL + "users/BoyCook");
        assertNotNull(p);
        assertEquals("BoyCook", p.getShortName());
    }

    @Test
    public void getInvalidUserShouldFail() {
        httpGetFail(serviceBaseURL + "users/ThisWill404", 404);
    }

    @Test
    public void getExampleUserShouldWork() {
        Principal p = (Principal) httpGet(serviceBaseURL + "examples/user");
        assertNotNull(p);
        assertEquals("ExampleUser", p.getShortName());
    }

    @Test
    public void getUsersShouldWork() {
        List<Principal> people = (List<Principal>) httpGet(serviceBaseURL + "users");
        assertNotNull(people);
    }

    @Test
    public void searchForUsersShouldWork() {
        String query = "?__bin_opt=AND&foreName=craig&surName=cook&";
        List<Principal> people = (List<Principal>) httpGet(serviceBaseURL + "users" + query);
        assertThat(people.size(), is(equalTo(1)));
    }

    @Test
    public void filterForUsersShouldWork() {
        String query = "?__bin_opt=OR&surName=bar&surName=cook&";
        List<Principal> people = (List<Principal>) httpGet(serviceBaseURL + "users" + query);
        assertThat(people.size(), is(equalTo(3)));
    }

    @Test
    public void addUserShouldWork() {
        Principal p1 = new Principal("ITTest", "IT", "Test", "password", "it.test@foo.com", "07918880501");
        http(serviceBaseURL + "users/" + p1.getShortName(), p1, HttpMethod.PUT);

        Principal p2 = (Principal) httpGet(serviceBaseURL + "users/" + p1.getShortName());
        assertNotNull(p2);
        assertThat(p1.getShortName(), is(equalTo(p2.getShortName())));
        assertThat(p1.getForeName(), is(equalTo(p2.getForeName())));
        assertThat(p1.getSurName(), is(equalTo(p2.getSurName())));
        assertThat(p1.getEmail(), is(equalTo(p2.getEmail())));
        assertThat(p1.getPhoneNumber(), is(equalTo(p2.getPhoneNumber())));
    }

    @Test
    public void addInvalidUserShouldFail() {
        Principal p1 = new Principal("ITTest", "", "", "password", "it.test@foo.com", "07918880501");
        httpFail(serviceBaseURL + "users/" + p1.getShortName(), p1, HttpMethod.PUT, 422);
        httpGetFail(serviceBaseURL + "users/" + p1.getShortName(), 404);
    }

    @Test
    public void updateUserShouldWork() {
        log.debug("PrincipalControllerITCase.updateUserShouldWork");
        login("ITTest", "password");
        Principal p1 = (Principal) httpGet(serviceBaseURL + "users/ITTest");
        p1.setForeName("ForeName");
        p1.setSurName("SurName");

        http(serviceBaseURL + "users/" + p1.getShortName(), p1, HttpMethod.POST);
        Principal p2 = (Principal) httpGet(serviceBaseURL + "users/" + p1.getShortName());

        assertThat(p1.getForeName(), is(equalTo(p2.getForeName())));
        assertThat(p1.getSurName(), is(equalTo(p2.getSurName())));
    }

    @Test
    public void updateInvalidUserShouldFail() {
        Principal control = (Principal) httpGet(serviceBaseURL + "users/ITTest");
        Principal p1 = (Principal) httpGet(serviceBaseURL + "users/ITTest");
        p1.setForeName("ForeName");
        p1.setSurName("SurName");

        httpFail(serviceBaseURL + "users/" + p1.getShortName(), p1, HttpMethod.POST, 422);
        Principal p2 = (Principal) httpGet(serviceBaseURL + "users/" + p1.getShortName());

        assertThat(control.getForeName(), is(equalTo(p2.getForeName())));
        assertThat(control.getSurName(), is(equalTo(p2.getSurName())));
    }

    @Test
    public void updateLocationShouldWork() {
        log.debug("PrincipalControllerITCase.updateLocationShouldWork");
        login("ITTest", "password");
        Principal p1 = (Principal) httpGet(serviceBaseURL + "users/ITTest");
        Location location = new Location(123, 456);

        http(serviceBaseURL + "users/" + p1.getShortName() + "/location", location, HttpMethod.POST);
        Principal p2 = (Principal) httpGet(serviceBaseURL + "users/" + p1.getShortName());

        assertThat(p2.getLocation().getLatitude(), is(equalTo(location.getLatitude())));
        assertThat(p2.getLocation().getLongitude(), is(equalTo(location.getLongitude())));
    }

    @Test
    public void addFriendRequestShouldWork() {
        Principal bc1 = (Principal) httpGet(serviceBaseURL + "users/BoyCook");
        Principal cc1 = (Principal) httpGet(serviceBaseURL + "users/CraigCook");

        assertThat(bc1.getFriendRequests().size(), is(equalTo(0)));
        assertThat(cc1.getFriendRequests().size(), is(equalTo(0)));

        String url = serviceBaseURL + "users/" + bc1.getShortName() + "/friendrequest/" + cc1.getShortName();
        http(url, null, HttpMethod.PUT);

        Principal bc2 = (Principal) httpGet(serviceBaseURL + "users/BoyCook");
        Principal cc2 = (Principal) httpGet(serviceBaseURL + "users/CraigCook");

        assertThat(bc2.getFriendRequests().size(), is(equalTo(1)));
        assertThat(cc2.getFriendRequests().size(), is(equalTo(0)));
        assertTrue(bc2.getFriendRequests().contains(cc2));
        assertFalse(cc2.getFriendRequests().contains(bc2));
    }

    @Test
    public void acceptFriendRequestShouldWork() {
        login("BoyCook", "password");
        Principal bc1 = (Principal) httpGet(serviceBaseURL + "users/BoyCook");
        Principal cc1 = (Principal) httpGet(serviceBaseURL + "users/CraigCook");

        assertThat(bc1.getFriends().size(), is(equalTo(0)));
        assertThat(cc1.getFriends().size(), is(equalTo(0)));

        String url = serviceBaseURL + "users/" + bc1.getShortName() + "/friends/" + cc1.getShortName();
        http(url, null, HttpMethod.PUT);

        Principal bc2 = (Principal) httpGet(serviceBaseURL + "users/BoyCook");
        Principal cc2 = (Principal) httpGet(serviceBaseURL + "users/CraigCook");

        assertThat(bc2.getFriends().size(), is(equalTo(1)));
        assertThat(cc2.getFriends().size(), is(equalTo(1)));
    }

    @Ignore //This is redundant
    @Test
    public void updateUserShouldNotRemoveFriends() {
        Principal bc1 = (Principal) httpGet(serviceBaseURL + "users/BoyCook");
        Principal cc1 = (Principal) httpGet(serviceBaseURL + "users/CraigCook");
        bc1.setSurName("Foo");
        cc1.setSurName("Bar");

        assertThat(bc1.getFriends().size(), is(equalTo(1)));
        assertThat(cc1.getFriends().size(), is(equalTo(1)));

        login("BoyCook", "password");
        http(serviceBaseURL + "users/" + bc1.getShortName(), bc1, HttpMethod.POST);
        login("CraigCook", "password");
        http(serviceBaseURL + "users/" + cc1.getShortName(), cc1, HttpMethod.POST);

        Principal bc2 = (Principal) httpGet(serviceBaseURL + "users/BoyCook");
        Principal cc2 = (Principal) httpGet(serviceBaseURL + "users/CraigCook");

        assertThat(bc2.getFriends().size(), is(equalTo(1)));
        assertThat(cc2.getFriends().size(), is(equalTo(1)));
    }

    @Test
    public void removeFriendShouldWork() {
        Principal bc1 = (Principal) httpGet(serviceBaseURL + "users/BoyCook");
        Principal cc1 = (Principal) httpGet(serviceBaseURL + "users/CraigCook");

        assertThat(bc1.getFriends().size(), is(equalTo(1)));
        assertThat(cc1.getFriends().size(), is(equalTo(1)));

        String url = serviceBaseURL + "users/" + bc1.getShortName() + "/friends/" + cc1.getShortName();
        httpDelete(url);

        Principal bc2 = (Principal) httpGet(serviceBaseURL + "users/BoyCook");
        Principal cc2 = (Principal) httpGet(serviceBaseURL + "users/CraigCook");

        assertThat(bc2.getFriends().size(), is(equalTo(0)));
        assertThat(cc2.getFriends().size(), is(equalTo(0)));
    }

    @Test
    public void deleteUserShouldWork() {
        login("ITTest", "password");
        httpDelete(serviceBaseURL + "users/ITTest");
        login("BoyCook", "password");
        httpGetFail(serviceBaseURL + "users/ITTest", 404);
    }

    @Test
    public void deleteInvalidUserShouldFail() {
        httpDeleteFail(serviceBaseURL + "users/ThisWill404", 404);
    }
}
