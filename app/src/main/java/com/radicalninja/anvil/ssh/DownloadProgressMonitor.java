package com.radicalninja.anvil.ssh;

import com.jcraft.jsch.SftpProgressMonitor;

import java.util.concurrent.CountDownLatch;

class DownloadProgressMonitor implements SftpProgressMonitor {

    private final Progress progress = new Progress();

    private long downloaded, maxSize;
    private int printed;

    private CountDownLatch countDownLatch = null;

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
        this.downloaded = 0;
        this.printed = 0;
        this.maxSize = max;
        System.out.printf("Downloading %s -> %s\n", src, dest); // TODO: IMPROVE THIS
    }

    @Override
    public boolean count(long count) {
        downloaded += count;
        progress.calculate(downloaded, maxSize);

        displayProgress();

        return true;
    }

    protected void displayProgress() {
        // TODO: Should this be synchronized in case of multi-thread access? Investigate how the count() method gets called...
        if (progress.major > printed) {
            final StringBuilder sb = new StringBuilder();
            for (int i = printed + 1; i <= progress.major; i++) {
                final String insert;
                if ((i % 5) == 0) {
                    insert = String.format("%d%%", i);
                } else {
                    insert = ".";
                }
                final String append = String.format("%s ", insert);
                sb.append(append);
            }
            System.out.print(sb.toString());
            printed = progress.major;
        }
    }

    @Override
    public void end() {
        System.out.printf("\nDownload finished!!\n");
        countDownLatch.countDown();
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
            return String.format("%d.%02d%%", major, minor);
        }
    }

}
