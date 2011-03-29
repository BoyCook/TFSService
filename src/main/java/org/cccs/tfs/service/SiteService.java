package org.cccs.tfs.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * User: Craig Cook
 * Date: May 12, 2010
 * Time: 1:23:50 PM
 */
public final class SiteService {

    public static boolean IS_ACTIVE = true;
    private static String loggedInUser;

    public static String getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            if (SiteService.loggedInUser == null) {
                return "anonymous";
            } else {
                return SiteService.loggedInUser;
            }
        } else {
            return auth.getName();
        }
    }

    public static void setLoggedInUser(String loggedInUser) {
        SiteService.loggedInUser = loggedInUser;
    }
}
