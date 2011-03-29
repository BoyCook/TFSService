package org.cccs.tfs.utils;

import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import static java.lang.String.format;

/**
 * User: boycook
 * Date: 24/02/2011
 * Time: 10:00
 */
public final class IOUtils {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(IOUtils.class);

    public static void writeDataFile(String file, Object obj) {
        try {
            writeDataFileToDisk(file, obj);
        } catch (IOException e) {
            log.error(format("There was an error writing [%s] to disk", file));
            System.out.println(e.getMessage());
        }
    }

    public static Object readDataFile(String file) {
        try {
            return readDataFileFromDisk(file);
        } catch (IOException e) {
            log.error(format("Sorry file [%s] does not exist", file));
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            log.error(format("Sorry file [%s] has unknown class type", file));
            System.out.println(e.getMessage());
        } catch (ClassCastException e) {
            log.error(format("Sorry file [%s] is of wrong class type", file));
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static void writeDataFileToDisk(String file, Object obj) throws IOException {
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(obj);
    }

    private static Object readDataFileFromDisk(String file) throws IOException, ClassNotFoundException {
        FileInputStream f = new FileInputStream(file);
        ObjectInputStream o = new ObjectInputStream(f);
        return o.readObject();
    }

    private static Object readResource(String file) throws IOException, ClassNotFoundException {
        ObjectInputStream o = new ObjectInputStream(IOUtils.class.getResourceAsStream(file));
        return o.readObject();
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }


//    public static File readFileFromDisk(String file) {
//        FileUtils.
//
//        Path file = ...;
//        InputStream in = null;
//        try {
//            in = file.newInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//        } catch (IOException x) {
//            System.err.println(x);
//        } finally {
//            if (in != null) in.close();
//        }
//
//    }
}
