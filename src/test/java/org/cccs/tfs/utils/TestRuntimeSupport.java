package org.cccs.tfs.utils;

import org.cccs.tfs.domain.Location;
import org.cccs.tfs.domain.Principal;
import org.junit.Test;

import static java.lang.String.format;
import static org.cccs.tfs.utils.RuntimeSupport.distanceBetween;
import static org.junit.Assert.assertNotNull;

/**
 * User: boycook
 * Date: 18/02/2011
 * Time: 19:59
 */
public class TestRuntimeSupport {

    /*

    - iPhone sends location to service
    - Service updates DB, and calculates distance between user and all friends
    - Service alerts user/friend(s) if the are close (iPhone event)

    - User asks service 'where are my friends'?
    - Service calculates distance between user and all friends and returns results

     */


    private Principal james = new Principal("JamesCook", "James", "Cook", "password", "james.cook@me.com", "07918880501", new Location(50.8486898, 0.4782826));
    private Principal dulcie = new Principal("DulcieCook", "Dulcie", "Cook", "password", "dulcie.cook@me.com", "07918880501", new Location(50.8486898, 0.4782826));
    private Principal jason = new Principal("JasonCook", "Jason", "Cook", "password", "jason.cook@me.com", "07918880501", new Location(33.4667784, -96.9183360));
    private Principal duncan = new Principal("DuncanCook", "Duncan", "Cook", "password", "duncan.cook@me.com", "07918880501", new Location(51.4073116, -0.2201433));
    private Principal douglas = new Principal("DouglasCook", "Douglas", "Cook", "password", "craig.cook@me.com", "07918880501", new Location(51.4627355, -0.0018480));
    private Principal suzannah = new Principal("SuzannahCook", "Suzannah", "Cook", "password", "suzannah.cook@me.com", "07918880501", new Location(51.4656180, -0.0015475));
    private Principal craig = new Principal("CraigCook", "Craig", "Cook", "password", "craig.cook@me.com", "07918880501", new Location(51.4580291, 0.1676881));

    @Test
    public void testGetDistance() {
        printDistance(craig, douglas);
        printDistance(craig, suzannah);
        printDistance(craig, duncan);
        printDistance(craig, james);
        printDistance(craig, jason);
    }

    private void printDistance(Principal p1, Principal p2) {
        double d = distanceBetween(p1.getLocation(), p2.getLocation());
        System.out.println(format("Distance between %s and %s - (%f) km", p1.getName(), p2.getName(), d/1000));
    }
}
