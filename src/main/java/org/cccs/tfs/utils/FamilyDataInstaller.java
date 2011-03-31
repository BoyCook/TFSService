package org.cccs.tfs.utils;

import org.cccs.tfs.domain.File;
import org.cccs.tfs.domain.Location;
import org.cccs.tfs.domain.Principal;
import org.cccs.tfs.service.FileService;
import org.cccs.tfs.service.PrincipalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: boycook
 * Date: 18/02/2011
 * Time: 21:12
 */
public class FamilyDataInstaller implements Installer {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PrincipalService service;

    @Autowired
    public FileService fileService;

    public FamilyDataInstaller() {}

    public FamilyDataInstaller(PrincipalService service) {
        this.service = service;
    }

    public void install() {
        try {
            Principal principal = new Principal("JamesCook", "James", "Cook", "password", "james.cook@me.com", "07918880501", new Location(50.8486898, 0.4782826));
            Principal principal1 = new Principal("DulcieCook", "Dulcie", "Cook", "password", "dulcie.cook@me.com", "07918880501", new Location(50.8486898, 0.4782826));
            Principal principal2 = new Principal("JasonCook", "Jason", "Cook", "password", "jason.cook@me.com", "07918880501", new Location(33.4667784, -96.9183360));
            Principal principal3 = new Principal("DuncanCook", "Duncan", "Cook", "password", "duncan.cook@me.com", "07918880501", new Location(51.4073116, -0.2201433));
            Principal principal4 = new Principal("DouglasCook", "Douglas", "Cook", "password", "craig.cook@me.com", "07918880501", new Location(51.4627355, -0.0018480));
            Principal principal5 = new Principal("SuzannahCook", "Suzannah", "Cook", "password", "suzannah.cook@me.com", "07918880501", new Location(51.4656180, -0.0015475));
            Principal principal6 = new Principal("CraigCook", "Craig", "Cook", "password", "craig.cook@me.com", "07918880501", new Location(51.4580291, 0.1676881));

            service.create(principal);
            service.create(principal1);
            service.create(principal2);
            service.create(principal3);
            service.create(principal4);
            service.create(principal5);

            principal6.addFriend(principal);
            principal6.addFriend(principal1);
            principal6.addFriend(principal2);
            principal6.addFriend(principal3);
            principal6.addFriend(principal4);
            principal6.addFriend(principal5);

            service.create(principal6);

            fileService.create(new File("org.cccs.jslibs", "jsMap", "1.0", "js", "https://github.com/BoyCook/JSLibs/raw/master/map/lib/jsMap.js"));
            fileService.create(new File("org.cccs.jslibs", "jquery.hintbox", "1.0", "js", "https://github.com/BoyCook/JSLibs/raw/master/jquery.hintbox/lib/jquery.hintbox.js"));
            fileService.create(new File("org.cccs.jslibs", "jquery.collapsible", "1.0", "js", "https://github.com/BoyCook/JSLibs/raw/master/jquery.collapsible/lib/jquery.collapsible.js"));
            fileService.create(new File("org.cccs.jslibs", "jquery.collapsible", "1.0.0", "js", "https://github.com/BoyCook/JSLibs/raw/master/jquery.collapsible/lib/jquery.collapsible.js"));
        } catch (Exception e) {
            log.debug("Error installing data");
            log.debug(e.getMessage());
        }
    }
}
