package org.cccs.tfs.web;

import org.apache.commons.io.FileUtils;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * User: boycook
 * Date: 25/02/2011
 * Time: 23:36
 */
public class FileView extends AbstractView {
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = (File) model.get(BaseController.DOMAIN_DATA);

        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);

            System.out.println("Bytes: " + bytes.length);
            response.setContentLength(bytes.length);
            response.setContentType("image/jpeg");
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName() + ";");

            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println(file.getName() + " does not exist");
            response.setStatus(200);
        }
    }
}
