package ru.alepar.ebook;

public class UserAgentDetector {

    public EbookType detect(String userAgent) {
        if (userAgent.contains("Kindle/2")) {
            if (userAgent.contains("screen 824x1200")) {
                return EbookType.KINDLE_DX;
            }
            if (userAgent.contains("screen 600x800")) {
                return EbookType.KINDLE;
            }
        }

        if (userAgent.contains("Kindle/3")) {
            return EbookType.KINDLE;
        }

        if (userAgent.contains("nook browser")) {
            return EbookType.NOOK;
        }

        return EbookType.UNKNOWN;
    }

}
