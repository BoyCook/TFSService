package org.cccs.tfs.configuration;

import org.cccs.tfs.service.PrincipalService;
import org.cccs.tfs.utils.DummyDataInstaller;
import org.cccs.tfs.utils.Installer;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: Craig Cook
 * Date: May 14, 2010
 * Time: 10:02:38 AM
 */
public class DatabaseTestEnvironment  {

    protected Installer installer;

    @Autowired
    PrincipalService service;

    @Before
    public void setUp(){
        if (installer == null) {
            installer = new DummyDataInstaller(service);
            installer.install();
        }
    }

}

