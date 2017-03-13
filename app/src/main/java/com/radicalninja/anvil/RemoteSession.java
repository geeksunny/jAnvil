package com.radicalninja.anvil;

import com.jcraft.jsch.*;

import java.io.*;

public class RemoteSession {

    private final String server;
    private final int port;
    private final String username;

    private final Session session;

    // TODO: Make session non-final for re-use. (otherwise why store remote connection info?)
    // TODO: Add support for plaintext password and passphrase-protected key authentication methods.

    public RemoteSession(final String server, final int port, final String username, final String keyPath) throws JSchException {
        this.server = server;
        this.port = port;
        this.username = username;
        final JSch jSch = new JSch();

        final File knownHosts = new HomeFile("~/.ssh/known_hosts");
        if (knownHosts.exists() && knownHosts.isFile()) {
            jSch.setKnownHosts(knownHosts.getAbsolutePath());
        }

        jSch.addIdentity(keyPath);
        session = jSch.getSession(username, server, port);
        session.setConfig("server_host_key","ecdsa-sha2-nistp256"); // SSH's default preferred algorithm for hostnames. For compatibility with ~/.ssh/known_hosts
        // TODO: Add timeout support and error handling
        session.connect();
    }

    public boolean isConnected() {
        return session.isConnected();
    }

    public void close() {
        session.disconnect();
    }

    public int exec(final String[] cmd) throws IOException, JSchException {
        final String command = String.join(" ", cmd);
        return exec(command);
    }

    public int exec(final String cmd) throws JSchException, IOException {

        final ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(cmd);
        final OutputStream bos = new BufferedOutputStream(System.out);
        channel.setErrStream(bos);
        channel.setInputStream(null);
        final BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        channel.connect();

        String text;
        while ((text = in.readLine()) != null) {
            System.out.println(text);
        }

        final int exitStatus = channel.getExitStatus();
        channel.disconnect();
        return exitStatus;
    }

    // public boolean push(final File localFile) { }

    public boolean pull(final String remotePath) throws JSchException, IOException {

        // exec 'scp -f rfile' remotely
        final String cmd = String.format("scp -f %s", remotePath);
        final ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(cmd);

        // get I/O streams for remote scp   // todo: rewrite with buffered readers/writers.
        final OutputStream out = channel.getOutputStream();
        final InputStream in = channel.getInputStream();

        channel.connect();
        byte[] buf = new byte[1024];

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        while (true) {  // todo: bleh. rewrite this loop.
            final int c = checkAck(in);
            if (c != 'C') {
                break;
            }

            // read '0644'
            in.read(buf, 0, 5);

            long filesize = 0L;
            while (true) {
                if (in.read(buf, 0, 1) < 0) {
                    // error
                    break;
                }
                if (buf[0] == ' ') {
                    break;
                }
                filesize = filesize * 10L + (long)(buf[0]-'0');
            }

            String file;
            for (int i = 0; /**/; i++) {
                in.read(buf, i, 1);
                if (buf[i] == (byte)0x0a) {
                    file = new String(buf, 0, i);
                    break;
                }
            }

            //System.out.println("filesize="+filesize+", file="+file);

            // send '\0'    //todo: consolodate this and identical code above
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            // read a content of lfile
            final FileOutputStream fos = new FileOutputStream("./"+file);
            int foo;
            while (true) {
                if (buf.length < filesize) {
                    foo = buf.length;
                } else {
                    foo = (int)filesize;
                }
                foo = in.read(buf, 0, foo);
                if (foo < 0) {
                    // error
                    break;
                }
                fos.write(buf, 0, foo);
                filesize -= foo;
                if (filesize == 0L) {
                    break;
                }
            }
            fos.close();

            if (checkAck(in) != 0) {
                break;
            }

            // send '\0'    //todo: consolodate this and identical code above
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
        }

        channel.disconnect();

        // TODO: return file object of local file
        return false;
    }

    private int checkAck(final InputStream in) throws IOException {
        final int b = in.read();
        // 0: success | 1: error | 2: fatal error | -1: no response?

        if (b >= 1) {
            final StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char)c);
            } while (c != '\n');    // TODO: Could a buffered reader work here?
            System.out.println(sb.toString());
        }

        return b;
    }

}
