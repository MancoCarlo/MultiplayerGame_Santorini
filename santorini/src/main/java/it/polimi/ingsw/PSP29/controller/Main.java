package it.polimi.ingsw.PSP29.controller;

import it.polimi.ingsw.PSP29.virtualView.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server();
        server.launch();
    }
}
