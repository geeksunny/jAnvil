package com.radicalninja.anvil.config;

public class RemoteConfig implements Configuration.Config {

    private String username;
    private String server;
    private int port;
    private String privateKey;
    private String destinationPath;

    public String getUsername() {
        return username;
    }

    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public static final class Builder {
        private String username;
        private String server;
        private int port;
        private String publicKey;
        private String destinationPath;

        private Builder() {
        }

        public static Builder aRemoteConfig() {
            return new Builder();
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withServer(String server) {
            this.server = server;
            return this;
        }

        public Builder withPort(int port) {
            this.port = port;
            return this;
        }

        public Builder withPublicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public Builder withDestinationPath(String destinationPath) {
            this.destinationPath = destinationPath;
            return this;
        }

        public RemoteConfig build() {
            RemoteConfig remoteConfig = new RemoteConfig();
            remoteConfig.username = this.username;
            remoteConfig.destinationPath = this.destinationPath;
            remoteConfig.port = this.port;
            remoteConfig.privateKey = this.publicKey;
            remoteConfig.server = this.server;
            return remoteConfig;
        }
    }
}
