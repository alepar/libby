package ru.alepar.ebook.convert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.alepar.ebook.format.EbookType;
import ru.alepar.ebook.format.FormatProvider;

import java.io.File;

import static org.hamcrest.Matchers.*;
import static ru.alepar.testsupport.MyMatchers.*;

@RunWith(JMock.class)
public class CalibreConverterTest {

    private static final String BINARY = "/bin/someBinary.sh";

    private final Mockery mockery = new JUnit4Mockery();
    private final Exec exec = mockery.mock(Exec.class);
    private final FormatProvider provider = mockery.mock(FormatProvider.class);
    private final CalibreConverter converter = new CalibreConverter(BINARY, exec, provider);
    private final File out = new File("someoutfile");

    @Before
    public void setUp() throws Exception {
        mockery.checking(new Expectations() {{
            allowing(provider).extension(with(any(EbookType.class)));
            will(returnValue("ext"));
            allowing(provider).outputProfile(with(any(EbookType.class)));
            will(returnValue("out_fmt"));
        }});
    }

    @Test
    public void executesRightBinary() throws Exception {
        mockery.checking(new Expectations() {{
            one(exec).exec(with(item(0, startsWith(BINARY))));
            will(returnValue(0));
        }});

        converter.convertFor(EbookType.KINDLE_DX, new File("somefile"), out);
    }

    @Test
    public void firstArgIsInputFile() throws Exception {
        final File input = new File("somefile");
        final String inputPath = input.getCanonicalPath();

        mockery.checking(new Expectations() {{
            one(exec).exec(with(item(1, equalTo(inputPath))));
            will(returnValue(0));
        }});

        converter.convertFor(EbookType.KINDLE_DX, input, out);
    }

    @Test
    public void secondArgIsOutputFileWithProperExtension() throws Exception {
        final File input = new File("somefile.fb2");

        mockery.checking(new Expectations() {{
            one(exec).exec(with(item(2, like(".*\\.ext"))));
            will(returnValue(0));
        }});

        converter.convertFor(EbookType.KINDLE_DX, input, out);
    }

    @Test
    public void thirdArgIsOutputProfile() throws Exception {
        final File input = new File("somefile.fb2");

        mockery.checking(new Expectations() {{
            one(exec).exec(with(item(3, equalTo("--output-profile"))));
            will(returnValue(0));
        }});

        converter.convertFor(EbookType.KINDLE_DX, input, out);
    }

    @Test
    public void fourthArgIsValueForOutputFormat() throws Exception {
        final File input = new File("somefile.fb2");

        mockery.checking(new Expectations() {{
            one(exec).exec(with(item(4, equalTo("out_fmt"))));
            will(returnValue(0));
        }});

        converter.convertFor(EbookType.KINDLE_DX, input, out);
    }

}
