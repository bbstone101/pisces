package com.bbstone.pisces.server;

public class ServerStarter {
    public static void main(String[] args) {
        FileServer server = new FileServer();
        server.startup();
    }
}
