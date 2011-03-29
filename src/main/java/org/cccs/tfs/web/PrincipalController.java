package org.cccs.tfs.web;

import org.cccs.tfs.domain.Location;
import org.cccs.tfs.domain.Principal;
import org.cccs.tfs.finder.PrincipalFinder;
import org.cccs.tfs.service.PrincipalService;
import org.cccs.tfs.utils.RuntimeSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static java.lang.String.format;
import static org.cccs.tfs.utils.RuntimeSupport.convertSetToList;
import static org.cccs.tfs.utils.RuntimeSupport.populateDistances;

/**
 * User: Craig Cook
 * Date: Apr 2, 2010
 * Time: 4:10:53 PM
 */
@Controller
@Scope("request")
public class PrincipalController extends BaseController<Principal> {

    @Autowired
    private PrincipalFinder finder;

    @Autowired
    private PrincipalService service;

    @SuppressWarnings({"unchecked"})
    @RequestMapping(value = "/session", method = RequestMethod.GET)
    public String getUserSession(final Model model,
                                 final HttpServletRequest request,
                                 final HttpServletResponse response) {
        log.debug("PrincipalController.getUserSession");

        Cookie[] cookies = request.getCookies();

        for (Cookie cookie: cookies) {
            if (cookie.getName().equalsIgnoreCase("JSESSIONID")) {
                log.debug("Adding JESSIONID: " + cookie.getValue());
                response.addHeader("JESSIONID", cookie.getValue());
            }
        }

        model.addAttribute(DOMAIN_DATA, request.getCookies());
        return MARSHALLER;
    }

    @SuppressWarnings({"unchecked"})
    @RequestMapping(value = "/examples/user", method = RequestMethod.GET)
    public String getExampleUser(final Model model) {
        log.debug("PrincipalController.getExampleUser");
        Principal example = new Principal("ExampleUser", "Example", "User", "password", "example@user.com", "example");
        model.addAttribute(DOMAIN_DATA, example);
        return MARSHALLER;
    }

    @SuppressWarnings({"unchecked"})
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getUsers(final Model model,
                           final HttpServletRequest request) {
        log.debug("PrincipalController.getUsers");

        Map<String, String[]> params = strip(request.getParameterMap());

        if (params != null && params.size()>0) {
            if (isOr(request.getParameterMap())) {
                model.addAttribute(DOMAIN_DATA, finder.filter(params));
            } else {
                model.addAttribute(DOMAIN_DATA, finder.search(params));
            }
        } else {
            model.addAttribute(DOMAIN_DATA, finder.all());
        }

        return MARSHALLER;
    }

    @RequestMapping(value = "/users/{uid}", method = RequestMethod.GET)
    public String getUser(@PathVariable("uid") final String uid,
                          final Model model) {
        log.debug("PrincipalController.getUser");
        model.addAttribute(DOMAIN_DATA, finder.find(uid));
        return MARSHALLER;
    }

    @RequestMapping(value = "/users/{uid}", method = RequestMethod.PUT)
    public String addUser(@PathVariable("uid") final String uid,
                          @RequestBody final Principal user,
                          final Model model, final HttpServletResponse response) {
        log.debug("PrincipalController.addUser");
        response.setStatus(HttpServletResponse.SC_CREATED);
        model.addAttribute(DOMAIN_DATA, service.create(user));
        return MARSHALLER;
    }

    @RequestMapping(value = "/users/{uid}", method = RequestMethod.POST)
    public String updateUser(@PathVariable("uid") final String uid,
                             @RequestBody final Principal user,
                             final Model model, final HttpServletResponse response) {
        log.debug("PrincipalController.updateUser");
        //TODO: get user from finder - friends won't be posted
        response.setStatus(HttpServletResponse.SC_RESET_CONTENT);
        model.addAttribute(DOMAIN_DATA, service.update(user));
        return MARSHALLER;
    }

    @RequestMapping(value = "/users/{uid}/picture", method = RequestMethod.POST)
    public String updateUsersPicture(@PathVariable("uid") final String uid,
                             @RequestParam("picture") final MultipartFile picture,
                             final Model model, final HttpServletResponse response) {
        log.debug("PrincipalController.updateUsersPicture");
        log.debug("Received file: " + picture.getOriginalFilename());

        try {
            picture.transferTo(new File(RuntimeSupport.IMAGE_DIRECTORY + "/" + uid + ".jpg"));
        } catch (IOException e) {
            log.error(format("Error writing (%s) to disk", picture.getOriginalFilename()));
            e.printStackTrace();
        }

        response.setStatus(HttpServletResponse.SC_RESET_CONTENT);
        model.addAttribute(DOMAIN_DATA, "picture added");
        return MARSHALLER;
    }

    @RequestMapping(value = "/users/{uid}/picture", method = RequestMethod.GET)
    public String getUsersPicture(@PathVariable("uid") final String uid,
                             final Model model) {
        log.debug("PrincipalController.getUsersPicture");

        File file = new File(RuntimeSupport.IMAGE_DIRECTORY + "/" + uid + ".jpg");
        model.addAttribute(DOMAIN_DATA, file);
        return FILE;
    }

    @RequestMapping(value = "/users/{uid}/location", method = RequestMethod.POST)
    public String updateLocation(@PathVariable("uid") final String uid,
                             @RequestBody final Location location,
                             final Model model, final HttpServletResponse response) {
        log.debug("PrincipalController.updateLocation");
        Principal p = finder.find(uid);
        response.setStatus(HttpServletResponse.SC_RESET_CONTENT);
        model.addAttribute(DOMAIN_DATA, service.updateLocation(p, location));
        return MARSHALLER;
    }

    @RequestMapping(value = "/users/{uid}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable("uid") final String uid,
                             final Model model,
                             final HttpServletResponse response) {
        log.debug("PrincipalController.deleteUser");
        Principal p = finder.find(uid);
        service.delete(p);
        response.setStatus(HttpServletResponse.SC_RESET_CONTENT);
        model.addAttribute(DOMAIN_DATA, "deleted");
        return MARSHALLER;
    }

    @SuppressWarnings({"unchecked"})
    @RequestMapping(value = "/users/{uid}/friends", method = RequestMethod.GET)
    public String getUsersFriends(@PathVariable("uid") final String uid,
                                  final Model model) {
        log.debug("PrincipalController.getUsersFriends");
        model.addAttribute(DOMAIN_DATA, convertSetToList(finder.find(uid).getFriends()));
        return MARSHALLER;
    }

    @RequestMapping(value = "/users/{uid}/friends/near", method = RequestMethod.GET)
    public String getUsersLocalFriends(@PathVariable("uid") final String uid,
                                       final Model model) {
        log.debug("PrincipalController.getUsersLocalFriends");
        Principal principal = finder.find(uid);
        populateDistances(principal);
        model.addAttribute(DOMAIN_DATA, convertSetToList(principal.getFriends()));
        return MARSHALLER;
    }

    @RequestMapping(value = "/users/{uid}/friends/{fid}", method = RequestMethod.PUT)
    public String acceptFriendRequest(@PathVariable("uid") final String uid,
                            @PathVariable("fid") final String fid,
                            final Model model,
                            final HttpServletResponse response) {
        log.debug("PrincipalController.addFriend");
        Principal p = finder.find(uid);
        Principal f = finder.find(fid);
        service.addFriend(p, f);
        response.setStatus(HttpServletResponse.SC_CREATED);
        model.addAttribute(DOMAIN_DATA, "success");
        return MARSHALLER;
    }

    @RequestMapping(value = "/users/{uid}/friendrequest/{fid}", method = RequestMethod.PUT)
    public String addFriendRequest(@PathVariable("uid") final String uid,
                            @PathVariable("fid") final String fid,
                            final Model model,
                            final HttpServletResponse response) {
        log.debug("PrincipalController.addFriendRequest");

        Principal p = finder.find(uid);
        Principal f = finder.find(fid);
        service.addFriendRequest(p, f);

        response.setStatus(HttpServletResponse.SC_CREATED);
        model.addAttribute(DOMAIN_DATA, "success");
        return MARSHALLER;
    }

    @RequestMapping(value = "/users/{uid}/friends/{fid}", method = RequestMethod.DELETE)
    public String removeFriend(@PathVariable("uid") final String uid,
                               @PathVariable("fid") final String fid,
                               final Model model,
                               final HttpServletResponse response) {
        log.debug("PrincipalController.removeFriend");

        Principal p = finder.find(uid);
        Principal f = finder.find(fid);
        service.removeFriend(p, f);

        response.setStatus(HttpServletResponse.SC_RESET_CONTENT);
        model.addAttribute(DOMAIN_DATA, "deleted");
        return MARSHALLER;
    }

    @RequestMapping(value = "/users/{uid}/friendrequest/{fid}", method = RequestMethod.DELETE)
    public String declineFriendRequest(@PathVariable("uid") final String uid,
                               @PathVariable("fid") final String fid,
                               final Model model,
                               final HttpServletResponse response) {
        log.debug("PrincipalController.removeFriend");

        Principal p = finder.find(uid);
        Principal f = finder.find(fid);
        service.removeFriendRequest(p, f);

        response.setStatus(HttpServletResponse.SC_RESET_CONTENT);
        model.addAttribute(DOMAIN_DATA, "deleted");
        return MARSHALLER;
    }
}
