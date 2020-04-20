package it.polimi.ingsw.PSP29.view;

import it.polimi.ingsw.PSP29.model.*;


public interface ServerObserver
{
    void didLogin(Player p1, Player p2);

    void didReceiveBoard(Box[][] board);
}