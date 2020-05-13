package it.polimi.ingsw.PSP29.view;



public interface ServerObserver
{
    void didReceiveMessage(String newStr1, String newStr2);

    void didInvoke(boolean response);
}