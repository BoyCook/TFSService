package org.cccs.tfs.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: Craig Cook
 * Date: Apr 1, 2010
 * Time: 7:12:59 PM
 */
public abstract class BaseController<T> {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    protected String MARSHALLER = "marshallingView";
    protected String FILE = "fileView";

    public static final String DOMAIN_DATA = "domain-data";
    public static final String ERROR_DATA = "error-data";
    public static final String BIN_OPT = "__bin_opt";

    private static final Set<String> searchStringReservedWords = new HashSet<String>();

    static {
        searchStringReservedWords.add(BIN_OPT);
    }

    protected boolean isOr(Map<String, String[]> params) {
        String opt = params.get(BIN_OPT)[0];
        return opt.equalsIgnoreCase("OR");
    }

    static Map<String, String[]> strip(Map<String, String[]> paramMap) {
        Map<String, String[]> rtn = new HashMap<String, String[]>();
        for (final String key : paramMap.keySet()) {
            if (!BaseController.searchStringReservedWords.contains(key)) {
                rtn.put(key, paramMap.get(key));
            }
        }
        return rtn;
    }

}
