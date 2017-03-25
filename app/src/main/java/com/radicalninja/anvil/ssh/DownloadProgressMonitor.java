package com.radicalninja.anvil.ssh;

import com.jcraft.jsch.SftpProgressMonitor;
import org.apache.commons.lang3.StringUtils;
import org.fusesource.jansi.Ansi;

import java.util.concurrent.CountDownLatch;

class DownloadProgressMonitor implements SftpProgressMonitor {

    private final Ansi ansi = Ansi.ansi();
    private final Progress progress = new Progress();
    private final int maxBarLength;

    private char progressFull = '*';
    private char progressHalf = '~';
    private char progressEmpty = '-';

    private long maxSize;
    private float lastPrinted;

    private CountDownLatch countDownLatch = null;

    public DownloadProgressMonitor() {
        maxBarLength = 100;
    }

    public DownloadProgressMonitor(final int maxBarLength) {
        if (maxBarLength < 1) { // TODO: make this req larger and/or add math value checks.
            throw new IllegalArgumentException("maxBarLength cannot be less than 1");
        }
        this.maxBarLength = maxBarLength;
    }

    public CountDownLatch obtainNewLock() throws InterruptedException {
        // TODO: This needs better concurrency protection/support before used in any serious capacity. Controlled sequences only
        if (null != countDownLatch) {
            countDownLatch.await();
        }
        countDownLatch = new CountDownLatch(1);
        return countDownLatch;
    }

    @Override
    public void init(int op, String src, String dest, long max) {
        this.maxSize = max;
        System.out.printf("Downloading %s -> %s\n", src, dest); // TODO: IMPROVE THIS
//        ansi.saveCursorPosition();
    }

    @Override
    public boolean count(long count) {
        progress.calculate(count, maxSize);
        // TODO: Add in some tracking to eliminate unnecessary screen refresh.
            // TODO: Create a console writer utility for margins and refresh management?
        final String progressBar = createProgressBar(count);
        final String output = String.format("%d/%d (%s) %s", count, maxSize, progress.toString(), progressBar);

//        ansi.restoreCursorPosition();
        System.out.print(ansi.eraseLine().a(output));   // todo: beef up formatting
        return true;
    }

    @Override
    public void end() {
        System.out.printf("\nDownload finished!!\n");
    }

    private String createProgressBar(final float completed) {
        final String full = StringUtils.repeat(progressFull, progress.major);
        final int difference = maxBarLength - progress.major;
        final String half;
        final String empty;
        if (progress.minor >= 50) { // TODO: compact this code
            half = String.valueOf(progressHalf);
            empty = StringUtils.repeat(progressEmpty, difference - 1);
        } else {
            half = "";
            empty = StringUtils.repeat(progressEmpty, difference);
        }
        return String.format("[ %s%s%s ]", full, half, empty);
    }

    class Progress {
        private int major, minor;

        void calculate(final long completed, final long max) {
            final float progress = ((float) completed / max) * 100;
            parse(progress);
        }

        void parse(final float progress) {
            major = (int) progress;
            final float subminor = (progress - major) * 100;
            minor = (int) subminor;
        }

        @Override
        public String toString() {
            return String.format("%d.%d%%", major, minor);
        }
    }

}
