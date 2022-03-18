package main.utils;

import javafx.scene.image.Image;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import jakarta.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by schwab on 23/12/2016.
 */
@Slf4j
public class Utils {

    public static final String FILESEPARATOR = System.getProperties().getProperty("file.separator");

    private static final String tempFolder = "temp";

    public static Image[] getImages(String folder) {

        return getImages(folder, -1);
    }

    public static Image[] getImages(final String folder, final int nbMax) {

        File directory = new File(folder);

        ArrayList<Image> images = new ArrayList<>(directory.list().length);

        for (String imagePath : directory.list()) {
            final String fileUrl = "file:" + directory.getAbsoluteFile() + FILESEPARATOR + imagePath;
            boolean added;
            if (!imagePath.startsWith(".") && isImage(fileUrl)) { // Problems with files starting with a point on
                // Windows
                images.add(new Image(fileUrl));
                added = true;
            } else {
                added = false;
            }
            log.debug("{} : added = {}", fileUrl, added);
        }

        Image[] Timages = obj2im(images.toArray());

        if (nbMax <= 0) {
            return Timages;
        }

        Image[] rimages = new Image[nbMax];

        for (int i = 0; i < nbMax; i++) {
            rimages[i] = Timages[(int) (Math.random() * Timages.length)];
        }

        return rimages;
    }

    private static Image[] obj2im(Object[] objects) {

        Image[] images = new Image[objects.length];

        for (int i = 0; i < objects.length; i++) {

            images[i] = (Image) objects[i];
        }

        return images;
    }

    public static boolean isImage(String file) {
        String mimetype = new MimetypesFileTypeMap().getContentType(file);
        log.debug("{} : mimetype = {}", file, mimetype);
        return mimetype.startsWith("image");
    }

    private static String loadLicenseFileAsString(ClassLoader classLoader) {
        try {
            try (InputStream is = classLoader.getResourceAsStream("data/common/licence.txt")) {
                return IOUtils.toString(is, Charset.forName("UTF-8"));
            }
        } catch (IOException e) {
            return "Failed to load the license file";
        }
    }

    private static Image[] defaultImage() {

        Image[] defaultImages = new Image[10];
        defaultImages[0] = new Image(
                ClassLoader.getSystemResourceAsStream("data/common/default/images/animal-807308_1920.png"));
        defaultImages[1] = new Image(
                ClassLoader.getSystemResourceAsStream("data/common/default/images/bulldog-1047518_1920.jpg"));
        defaultImages[2] = new Image(
                ClassLoader.getSystemResourceAsStream("data/common/default/images/businessman-607786_1920.png"));
        defaultImages[3] = new Image(
                ClassLoader.getSystemResourceAsStream("data/common/default/images/businessman-607834_1920.png"));
        defaultImages[4] = new Image(
                ClassLoader.getSystemResourceAsStream("data/common/default/images/crocodile-614386_1920.png"));
        defaultImages[5] = new Image(
                ClassLoader.getSystemResourceAsStream("data/common/default/images/goldfish-30837_1280.png"));
        defaultImages[6] = new Image(
                ClassLoader.getSystemResourceAsStream("data/common/default/images/graphic_missbone17.gif"));
        defaultImages[7] = new Image(
                ClassLoader.getSystemResourceAsStream("data/common/default/images/nurse-37322_1280.png"));
        defaultImages[8] = new Image(
                ClassLoader.getSystemResourceAsStream("data/common/default/images/owl-161583_1280.png"));
        defaultImages[9] = new Image(ClassLoader.getSystemResourceAsStream(
                "data/common/default/images/pez-payaso-animales-el-mar-pintado-por-teoalmeyra-9844979.jpg"));
        return defaultImages;
    }

    /**
     * @return Default directory for Interface : in user's home directory, in a folder called Interface
     */
    public static String getInterfaceFolder() {

        return System.getProperties().getProperty("user.home") + FILESEPARATOR + "Interface" + FILESEPARATOR;
    }

    /**
     * @return Temp directory for Interface : in the default directory of Interface, a folder called Temp
     */
    public static String getTempFolder() {

        return getInterfaceFolder() + tempFolder + FILESEPARATOR;
    }

    /**
     * @return current time with respect to the format HH:MM:ss
     */
    public static String time() {

        DateFormat dateFormat = new SimpleDateFormat("HH:MM:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * @return current time with respect to the format yyyy-MM-dd-HH-MM-ss
     */
    public static String now() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        return dateFormat.format(date);

    }

}
