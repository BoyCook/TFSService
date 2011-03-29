package org.cccs.tfs.utils;

import org.cccs.tfs.domain.Principal;
import org.cccs.tfs.service.PrincipalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: boycook
 * Date: 11/02/2011
 * Time: 15:48
 */
public class DummyDataInstaller implements Installer {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PrincipalService service;

    public DummyDataInstaller() {}

    public DummyDataInstaller(PrincipalService service) {
        this.service = service;
    }

    public void install() {
        try {
            service.create(new Principal("BoyCook", "Boy", "Cook", "password", "boycook@me.com", "07918880501"));
            service.create(new Principal("CraigCook", "Craig", "Cook", "password", "craig.cook@bt.com", "07918880501"));
            service.create(new Principal("FooBar", "Foo", "Bar", "password", "craig.cook@bt.com", "07918880501"));
        } catch (Exception e) {
            log.debug("Error installing data");
            log.debug(e.getMessage());
        }
    }
}
