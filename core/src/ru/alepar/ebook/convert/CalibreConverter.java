package ru.alepar.ebook.convert;

import ru.alepar.ebook.format.EbookType;
import ru.alepar.ebook.format.FormatProvider;
import ru.alepar.lib.stuff.Oops;

import java.io.File;

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
        try {
            String inputPath = file.getCanonicalPath();
            File outputFile = File.createTempFile("libby", '.' + provider.extension(type));
            String outputFileName = outputFile.getCanonicalPath();
            if (!outputFile.delete()) {
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

}
