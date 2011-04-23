package ru.alepar.ebook.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Arrays;

public class JavaRuntimeExec implements Exec {

    private final Logger log = LoggerFactory.getLogger(JavaRuntimeExec.class);

    @Override
    public int exec(String[] cmd) {
        try {
            log.debug("Runtime.exec {}", Arrays.toString(cmd));
            Process proc = Runtime.getRuntime().exec(cmd);
            proc.getOutputStream().close();
            StreamSink stdinSink = new StreamSink(proc.getInputStream());
            StreamSink stderrSink = new StreamSink(proc.getErrorStream());
            stdinSink.start();
            stderrSink.start();
            stdinSink.join();
            stderrSink.join();
            return proc.waitFor();
        } catch (Exception e) {
            throw new RuntimeException("failed to exec: " + cmd.toString(), e);
        }
    }

    private class StreamSink extends Thread {
        private final InputStream is;

        public StreamSink(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            try {
                while (is.read() != -1) ;
            } catch (Exception e) {
                //shuda rethrow in parent thread
            }
        }
    }
}
