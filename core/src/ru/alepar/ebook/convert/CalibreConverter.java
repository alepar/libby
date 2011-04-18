package ru.alepar.ebook.convert;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import ru.alepar.ebook.format.EbookType;
import ru.alepar.ebook.format.FormatProvider;
import ru.alepar.ebook.format.StaticFormatProvider;
import ru.alepar.io.IOUtils;
import ru.alepar.lib.stuff.Oops;

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
    public File convertFor(EbookType type, File file) {
        if (type == EbookType.UNKNOWN) {
            return file; // ebook not recognized, conversion will not be performed
        }
        try {
            if (file.getName().endsWith(".zip")) {
                file = uncompress(file);
            }
            String inputPath = file.getCanonicalPath();
            File outputFile = File.createTempFile("libby", '.' + provider.extension(type));
            String outputFileName = outputFile.getCanonicalPath();
            if (!outputFile.delete()) { // don't need the file itself, only it's name
                throw new Oops("cudnt trash " + outputFileName);
            }
            int retCode = exec.exec(new String[]{
                    binary,
                    inputPath,
                    outputFileName,
                    "--output-profile",
                    provider.outputProfile(type)
            });
            if (retCode != 0) {
                throw new RuntimeException("exec retcode = " + retCode);
            }
            return outputFile;
        } catch (Exception e) {
            throw new RuntimeException("conversion failed", e);
        }
    }

    private File uncompress(File inFile) throws IOException {
        String fileName = inFile.getName();
        String fileNameWithoutZipExtension = fileName.substring(0, fileName.length() - 4);
        ZipFile zipFile = new ZipFile(inFile);
        ZipArchiveEntry entry = getFirstEntry(zipFile);
        InputStream is = zipFile.getInputStream(entry);
        try {
            File outFile = File.createTempFile("libby", fileNameWithoutZipExtension);
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

    private ZipArchiveEntry getFirstEntry(ZipFile zipFile) {
        return (ZipArchiveEntry) zipFile.getEntriesInPhysicalOrder().nextElement();
    }

    public static void main(String[] args) throws Exception {
        String binary = "c:\\Program Files (x86)\\Calibre2\\ebook-convert.exe";
        CalibreConverter converter = new CalibreConverter(binary, new RuntimeExec(), new StaticFormatProvider());

        File out = converter.convertFor(EbookType.KINDLE_DX, new File("c:\\temp\\Лукьяненко 1 Геном.fb2.zip"));
        System.out.println(out.getCanonicalPath());
    }

}
