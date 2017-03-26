package com.radicalninja.anvil.ssh;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.CountDownLatch;

public class SftpRemoteSession extends RemoteSession<ChannelSftp> {

    private final DownloadProgressMonitor progressMonitor = new DownloadProgressMonitor();

    private ChannelSftp channel;

    public SftpRemoteSession(String server, int port, String username, String password) throws JSchException {
        super(server, port, username, password);
    }

    public SftpRemoteSession(String server, int port, String username, File prvKey) throws JSchException {
        super(server, port, username, prvKey);
    }

    public SftpRemoteSession(String server, int port, String username, File prvKey, String passphrase) throws JSchException {
        super(server, port, username, prvKey, passphrase);
    }

    @Override
    protected ChannelSftp openChannel() throws JSchException {
        return (ChannelSftp) getSession().openChannel("sftp");
    }

    @Override
    public void connect() throws JSchException {
        super.connect();
        initChannel();
    }

    @Override
    public void connect(int timeout) throws JSchException {
        super.connect(timeout);
        initChannel();
    }

    protected void initChannel() throws JSchException {
        if (null == channel) {
            channel = openChannel();
        }
        if (!channel.isConnected()) {
            channel.connect();
        }
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
            throws SftpException, InterruptedException, FileNotFoundException, JSchException {

        if (!isConnected()) {
            // TODO: attempt connection before throwing exception?
            throw new JSchException("Session not connected.");
        }

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
