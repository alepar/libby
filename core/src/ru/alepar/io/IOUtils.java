package ru.alepar.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {

    public static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[10240];
        int read;
        while ((read = is.read(buf)) != -1) {
            os.write(buf, 0, read);
        }
    }

}
