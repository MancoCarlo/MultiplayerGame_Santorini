package it.polimi.ingsw.PSP29.controller;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, NotValidInputException {
        GameController gm = new GameController();
        gm.gameExe();
    }
}
