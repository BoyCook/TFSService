package org.cccs.tfs.web;

import org.cccs.tfs.integration.oxm.Marshal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: boycook
 * Date: 15/02/2011
 * Time: 10:49
 */
public class ExceptionResolver implements HandlerExceptionResolver {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Map<Class<? extends Throwable>, Integer> statusCodeMappings = new HashMap<Class<? extends Throwable>, Integer>();
    private Marshal marshal;
    private XStreamMarshaller marshaller;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.debug(String.format("resolving exception: %s[%s]", ex.getClass().getSimpleName(), ex.getMessage()));
        ex.printStackTrace();
        final int responseCode = applyExceptionToHttpStatusCodeMappings(response, ex);
        log.info("Resolved exception " + ex.getClass().getSimpleName() + " to http status code (" + responseCode + ")");
        renderResponse(request, response, ex, responseCode);
        return null;
    }

    private boolean renderResponse(HttpServletRequest request, HttpServletResponse response, Throwable ex, int responseCode) {
        try {
            ExceptionResponse e = new ExceptionResponse(responseCode, ex.getMessage());
            marshaller.marshal(e, new StreamResult(response.getOutputStream()));
        } catch (IOException e) {
            log.error("Unable to marshal back exception");
            return true;
        }
        return false;
    }

    private int applyExceptionToHttpStatusCodeMappings(final HttpServletResponse response, final Throwable ex) {
        for (Class<? extends Throwable> exClazz : statusCodeMappings.keySet()) {
            if (exClazz.isAssignableFrom(ex.getClass())) {
                final Integer responseCode = statusCodeMappings.get(exClazz);
                response.setStatus(responseCode);
                return responseCode;
            }
        }
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }

    public void setStatusCodeMappings(Map<Class<? extends Throwable>, Integer> statusCodeMappings) {
        this.statusCodeMappings = statusCodeMappings;
    }

    public void setMarshaller(XStreamMarshaller marshaller) {
        this.marshaller = marshaller;
    }

    public Marshal getMarshal() {
        if (marshal == null) {
            marshal = new Marshal(marshaller);
        }
        return marshal;
    }
}
