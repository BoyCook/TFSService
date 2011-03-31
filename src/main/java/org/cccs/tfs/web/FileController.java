package org.cccs.tfs.web;

import org.cccs.tfs.domain.File;
import org.cccs.tfs.finder.FileFinder;
import org.cccs.tfs.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * User: boycook
 * Date: 30/03/2011
 * Time: 13:55
 */
@Controller
@Scope("request")
public class FileController extends BaseController<File> {

    @Autowired
    private FileFinder finder;

    @Autowired
    private FileService service;

    @SuppressWarnings({"unchecked"})
    @RequestMapping(value = "/examples/file", method = RequestMethod.GET)
    public String getExampleUser(final Model model) {
        log.debug("FileController.getExampleFile");
        File example = new File("exampleGroupID", "exampleArtefactId", "exampleVersion", "ext", "exampleURL");
        model.addAttribute(DOMAIN_DATA, example);
        return MARSHALLER;
    }

    @SuppressWarnings({"unchecked"})
    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public String searchFiles(final Model model,
                           final HttpServletRequest request) {
        log.debug("FileController.searchFiles");

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

    @SuppressWarnings({"unchecked"})
    @RequestMapping(value = "/files/{groupId}", method = RequestMethod.GET)
    public String getFilesByGroupId(@PathVariable("groupId") final String groupId, final Model model) {
        log.debug("FileController.getFilesByArtefactId");
        log.debug(format("FileController.getFilesByGroupId: %s", groupId));

        Map<String, String[]> parameters = new HashMap<String, String[]>();
        parameters.put("groupId", new String[] {groupId});

        model.addAttribute(DOMAIN_DATA, finder.search(parameters));
        return MARSHALLER;
    }

    @SuppressWarnings({"unchecked"})
    @RequestMapping(value = "/files/{groupId}/{artefactId}", method = RequestMethod.GET)
    public String getFilesByArtefactId(@PathVariable("groupId") final String groupId,
                                       @PathVariable("artefactId") final String artefactId,
                                       final Model model) {
        log.debug(format("FileController.getFilesByArtefactId: %s/%s", groupId, artefactId));

        Map<String, String[]> parameters = new HashMap<String, String[]>();
        parameters.put("groupId", new String[] {groupId});
        parameters.put("artefactId", new String[] {artefactId});

        model.addAttribute(DOMAIN_DATA, finder.search(parameters));
        return MARSHALLER;
    }

    @SuppressWarnings({"unchecked"})
    @RequestMapping(value = "/files/{groupId}/{artefactId}/{version}", method = RequestMethod.GET)
    public String getFile(@PathVariable("groupId") final String groupId,
                          @PathVariable("artefactId") final String artefactId,
                          @PathVariable("version") final String version,
                           final Model model) {
        log.debug(format("FileController.getFile: %s/%s/%s", groupId, artefactId, version));

        Map<String, String[]> parameters = new HashMap<String, String[]>();
        parameters.put("groupId", new String[] {groupId});
        parameters.put("artefactId", new String[] {artefactId});
        parameters.put("version", new String[] {version});

        List<File> files = finder.search(parameters);
        File file = files.get(0);
        if (files.size() > 1) {
            throw new RuntimeException("There should only be one version of a file");
        }

        log.debug("Found file: " + file.toString());

        model.addAttribute(DOMAIN_DATA, file);
        return MARSHALLER;
    }

    @RequestMapping(value = "/files/{groupId}/{artefactId}/{version}", method = RequestMethod.PUT)
    public String addFile(@PathVariable("groupId") final String groupId,
                          @PathVariable("artefactId") final String artefactId,
                          @PathVariable("version") final String version,
                          @RequestBody final File file,
                          final Model model, final HttpServletResponse response) {
        log.debug(format("FileController.addFile: %s/%s/%s", groupId, artefactId, version));
        response.setStatus(HttpServletResponse.SC_CREATED);
        model.addAttribute(DOMAIN_DATA, service.create(file));
        return MARSHALLER;
    }

    @RequestMapping(value = "/files/{groupId}/{artefactId}/{version}", method = RequestMethod.POST)
    public String updateUser(@PathVariable("groupId") final String groupId,
                          @PathVariable("artefactId") final String artefactId,
                          @PathVariable("version") final String version,
                          @RequestBody final File file,
                             final Model model, final HttpServletResponse response) {
        log.debug(format("FileController.updateUser: %s/%s/%s", groupId, artefactId, version));
        response.setStatus(HttpServletResponse.SC_RESET_CONTENT);
        model.addAttribute(DOMAIN_DATA, service.update(file));
        return MARSHALLER;
    }
}
