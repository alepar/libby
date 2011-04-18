package ru.alepar.ebook.convert;

import java.io.InputStream;

public class RuntimeExec implements Exec {
    @Override
    public int exec(String[] cmd) {
        try {
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
                //TODO throw in parent thread
            }
        }
    }
}