package com.radicalninja.anvil.ssh;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;

public class SftpRemoteSession extends RemoteSession<ChannelSftp> {

    private final DownloadProgressMonitor progressMonitor = new DownloadProgressMonitor();

    private ChannelSftp channel;

    public SftpRemoteSession(String server, int port, String username, String password) throws JSchException {
        super(server, port, username, password);
    }

    public SftpRemoteSession(String server, int port, String username, Path keyPath) throws JSchException {
        super(server, port, username, keyPath);
    }

    public SftpRemoteSession(String server, int port, String username, Path keyPath, String passphrase) throws JSchException {
        super(server, port, username, keyPath, passphrase);
    }

    @Override
    protected ChannelSftp openChannel() throws JSchException {
        return (ChannelSftp) getSession().openChannel("sftp");
    }

    public void cd(final String dst) throws SftpException {
        if (null != channel) {
            channel.cd(dst);
        }
    }

    public void lcd(final String dst) throws SftpException {
        if (null != channel) {
            channel.lcd(dst);
        }
    }

    public File get(final String remoteSrc, final String localDst)
            throws SftpException, InterruptedException, FileNotFoundException {
        final CountDownLatch latch = progressMonitor.obtainNewLock();
        channel.get(remoteSrc, localDst, progressMonitor);
        latch.await();
        final File result = new File(localDst);
        if (!result.exists()) {
            throw new FileNotFoundException("Local copy not found. Something has gone wrong.");
        }
        return result;
    }

    public File put(final String localSrc, final String remoteDst) {
        return null;
    }

}
