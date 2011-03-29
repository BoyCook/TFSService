package org.cccs.tfs.web;

import org.cccs.tfs.domain.Principal;
import org.cccs.tfs.finder.PrincipalFinder;
import org.cccs.tfs.service.PrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * User: Craig Cook
 * Date: Jun 11, 2010
 * Time: 9:22:30 AM
 */
@Deprecated //Signup not required
@Controller
@Scope("request")
public class SignUpController extends BaseController<Principal>{

    @Autowired
    private PrincipalFinder finder;

    @Autowired
    private PrincipalService service;

    //TODO: allow unsecured access to this url
    @RequestMapping(value = "/signup/new/{uid}", method = RequestMethod.PUT)
    public String requestSignup(@PathVariable("uid") final String uid,
                          @RequestBody final Principal user,
                          final Model model) {
        log.debug("SignUpController.requestSignup");
        user.setActive(false);
        model.addAttribute(DOMAIN_DATA, service.create(user));
        return MARSHALLER;        
    }

    @RequestMapping(value = "/signup/confirm/{uid}", method = RequestMethod.POST)
    public String confirmSignup(@PathVariable("uid") final String uid,
                          final Model model) {
        log.debug("SignUpController.confirmSignup");
        Principal p = finder.find(uid);
        p.setActive(true);
        model.addAttribute(DOMAIN_DATA, service.update(p));
        return MARSHALLER;
    }
}
