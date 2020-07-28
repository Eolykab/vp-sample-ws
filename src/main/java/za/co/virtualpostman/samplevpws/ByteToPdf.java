package za.co.virtualpostman.samplevpws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ByteToPdf {

    public ByteToPdf() {
    }

    public static void convertPdfToByte(byte[] pdfDocContentByte, String outputLocation) {
        OutputStream o = null;
        try {
            File f = new File(outputLocation);

            if (!f.exists()) {
                f.createNewFile();
            }

            o = new FileOutputStream(f);
            o.write(pdfDocContentByte);
            o.flush();
        } catch (IOException e) {
            throw new RuntimeException("Unable to create new file", e);
        } finally {
            try {
                if (o != null) {
                    o.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException("Error closing stream", ex);
            }
        }
    }
}
