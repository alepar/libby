package ru.alepar.ebook.convert;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import ru.alepar.ebook.format.EbookType;
import ru.alepar.ebook.format.FormatProvider;
import ru.alepar.io.IOUtils;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalibreConverter implements Converter {

    private static final Pattern extensionPattern = Pattern.compile("^.*\\.(.*)\\.zip$");

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
        final Matcher matcher = extensionPattern.matcher(inFile.getName());
        if(!matcher.find()) {
            throw new RuntimeException("can not detect extension of compressed file: " + inFile);
        }

        final ZipFile zipFile = new ZipFile(inFile);
        final ZipArchiveEntry entry = getFirstEntry(zipFile);
        final InputStream is = zipFile.getInputStream(entry);
        try {
            final File outFile = File.createTempFile("libby-", ".unzip." + matcher.group(1));
            final OutputStream fos = new FileOutputStream(outFile);
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
