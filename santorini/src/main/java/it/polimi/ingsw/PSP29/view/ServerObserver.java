package it.polimi.ingsw.PSP29.view;

import it.polimi.ingsw.PSP29.model.*;


public interface ServerObserver
{
    void didReceiveMessage(String newStr1, String newStr2);

    void didInvoke(boolean response);
}