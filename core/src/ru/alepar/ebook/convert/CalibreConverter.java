package ru.alepar.ebook.convert;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import ru.alepar.ebook.format.EbookType;
import ru.alepar.ebook.format.FormatProvider;
import ru.alepar.io.IOUtils;

import java.io.*;

public class CalibreConverter implements Converter {

    private final String binary;
    private final Exec exec;
    private final FormatProvider provider;

    public CalibreConverter(String binary, Exec exec, FormatProvider provider) {
        this.binary = binary;
        this.exec = exec;
        this.provider = provider;
    }

    @Override
    public void convertFor(EbookType type, File file, File out) {
        if (type == EbookType.DONT_CONVERT) {
            return;
        }

        try {
            if (file.getName().endsWith(".zip")) {
                file = uncompress(file);
            }
            final String inputPath = file.getCanonicalPath();
            final String outputFileName = out.getCanonicalPath();

            final int retCode = exec.exec(new String[]{
                    binary,
                    inputPath,
                    outputFileName,
                    "--output-profile",
                    provider.outputProfile(type)
            });
            if (retCode != 0) {
                throw new RuntimeException("exec retcode = " + retCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("conversion failed", e);
        }
    }

    private static File uncompress(File inFile) throws IOException {
        String fileName = inFile.getName();
        String fileNameWithoutZipExtension = fileName.substring(0, fileName.length() - 4);
        ZipFile zipFile = new ZipFile(inFile);
        ZipArchiveEntry entry = getFirstEntry(zipFile);
        InputStream is = zipFile.getInputStream(entry);
        try {
            File outFile = File.createTempFile("libby", ".unzip");
            OutputStream fos = new FileOutputStream(outFile);
            try {
                IOUtils.copy(is, fos);
                return outFile;
            } finally {
                fos.close();
            }
        } finally {
            is.close();
        }
    }

    private static ZipArchiveEntry getFirstEntry(ZipFile zipFile) {
        return zipFile.getEntriesInPhysicalOrder().nextElement();
    }

}
