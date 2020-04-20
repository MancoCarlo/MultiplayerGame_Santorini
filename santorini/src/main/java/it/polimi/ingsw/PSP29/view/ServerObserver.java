package it.polimi.ingsw.PSP29.view;

import it.polimi.ingsw.PSP29.model.Player;


public interface ServerObserver
{
    void didLogin(Player p1, Player p2);

    void didReceiveBoard(String str);
}