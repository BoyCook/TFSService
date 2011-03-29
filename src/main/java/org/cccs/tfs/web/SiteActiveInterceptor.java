package org.cccs.tfs.web;

import org.cccs.tfs.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Craig Cook
 * Date: Apr 3, 2010
 * Time: 12:58:45 PM
 */
public class SiteActiveInterceptor extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public boolean preHandle(HttpServletRequest request,
                        HttpServletResponse response, Object handler) throws Exception {
        log.info("SiteActiveInterceptor.preHandle");

        if (!SiteService.IS_ACTIVE) {
            response.sendRedirect("http://siteisdown");
        }

        return SiteService.IS_ACTIVE;
    }
}
