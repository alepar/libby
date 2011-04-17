package ru.alepar.ebook;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class UserAgentDetectorTest {

    private final UserAgentDetector detector = new UserAgentDetector();

    @Test
    public void detectsKindleDx() throws Exception {
        String userAgent = "Mozilla/4.0 (compatible; Linux 2.6.22) NetFront/3.4 Kindle/2.5 (screen 824x1200; rotate)";
        assertThat(detector.detect(userAgent), equalTo(EbookType.KINDLE_DX));
    }

    @Test
    public void detectsKindle2() throws Exception {
        String userAgent = "Mozilla/4.0 (compatible; Linux 2.6.22) NetFront/3.4 Kindle/2.0 (screen 600x800)";
        assertThat(detector.detect(userAgent), equalTo(EbookType.KINDLE));
    }

    @Test
    public void detectsKindle3() throws Exception {
        String userAgent = "Mozilla/5.0 (Linux; U; en-US) AppleWebKit/528.5+ (KHTML, like Gecko, Safari/528.5+) Version/4.0 Kindle/3.0 (screen 600X800; rotate)";
        assertThat(detector.detect(userAgent), equalTo(EbookType.KINDLE));
    }

    @Test
    public void detectsNook() throws Exception {
        String userAgent = "nook browser/1.0";
        assertThat(detector.detect(userAgent), equalTo(EbookType.NOOK));
    }

}
