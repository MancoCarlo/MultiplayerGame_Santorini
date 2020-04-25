package it.polimi.ingsw.PSP29.view;

import it.polimi.ingsw.PSP29.model.*;


public interface ServerObserver
{
    void didReceiveMessage(String newStr1, Object obj);

    void didInvoke(boolean response);
}