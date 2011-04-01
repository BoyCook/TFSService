package org.cccs.tfs.utils;

import org.cccs.tfs.domain.File;
import org.cccs.tfs.domain.Principal;
import org.cccs.tfs.service.FileService;
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
    private static boolean installed = false;

    @Autowired
    public PrincipalService principalService;

    @Autowired
    public FileService fileService;

    public DummyDataInstaller() {}

    public DummyDataInstaller(PrincipalService principalService) {
        this.principalService = principalService;
    }

    public void install() {
        if (!installed) {
            try {
                principalService.create(new Principal("BoyCook", "Boy", "Cook", "password", "boycook@me.com", "07918880501"));
                principalService.create(new Principal("CraigCook", "Craig", "Cook", "password", "craig.cook@bt.com", "07918880501"));
                principalService.create(new Principal("FooBar", "Foo", "Bar", "password", "craig.cook@bt.com", "07918880501"));

                fileService.create(new File("org.cccs.jslibs", "jsArray", "1.0.0.0", "js", "https://github.com/BoyCook/JSLibs/raw/master/array/lib/jsArray.js"));
                fileService.create(new File("org.cccs.jslibs", "jsMap", "1.0.0.0", "js", "https://github.com/BoyCook/JSLibs/raw/master/map/lib/jsMap.js"));
                fileService.create(new File("org.cccs.jslibs", "jquery.hintbox", "1.0.0.0", "js", "https://github.com/BoyCook/JSLibs/raw/master/jquery.hintbox/lib/jquery.hintbox.js"));
                fileService.create(new File("org.cccs.jslibs", "jquery.collapsible", "1.0.0.0", "js", "https://github.com/BoyCook/JSLibs/raw/master/jquery.collapsible/lib/jquery.collapsible.js"));
                fileService.create(new File("org.cccs.jslibs", "jquery.collapsible", "1.0.0.1", "js", "https://github.com/BoyCook/JSLibs/raw/master/jquery.collapsible/lib/jquery.collapsible.js"));
                installed = true;
            } catch (Exception e) {
                log.debug("Error installing data");
                log.debug(e.getMessage());
            }
        }
    }
}
