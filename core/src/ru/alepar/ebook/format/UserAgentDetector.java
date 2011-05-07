package ru.alepar.ebook.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAgentDetector {

    private static final Logger log = LoggerFactory.getLogger(UserAgentDetector.class);

    public EbookType detect(String userAgent) {
        if (userAgent.contains("Kindle/2")) {
            if (userAgent.contains("screen 824x1200")) {
                return EbookType.KINDLE_DX;
            }
            if (userAgent.contains("screen 600x800")) {
                return EbookType.KINDLE;
            }
            return EbookType.KINDLE;
        }

        if (userAgent.contains("Kindle/3")) {
            return EbookType.KINDLE;
        }

        if (userAgent.contains("nook browser")) {
            return EbookType.NOOK;
        }

        log.info("could not resolve ebookType, userAgent = " + userAgent);
        return EbookType.DONT_CONVERT;
    }

}
