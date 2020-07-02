package it.polimi.ingsw.PSP29.view;



public interface ServerObserver
{
    /**
     * return the message received
     * @param newStr1 string that contains the name of the method
     * @param newStr2 string that contains a message
     */
    void didReceiveMessage(String newStr1, String newStr2);

    /**
     * return true if the method invoke is executed
     * @param response true if the methode is executed
     */
    void didInvoke(boolean response);
}