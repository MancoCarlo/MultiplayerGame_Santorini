package it.polimi.ingsw.PSP29.virtualView;

import java.util.TimerTask;

public class UserTimerTask extends TimerTask {
    /**
     * second passed
     */
    private int seconds=0;

    /**
     * time for the interaction
     */
    private int turnSeconds = 100;

    /**
     * client that must make an interaction
     */
    private ClientHandler ch;

    /**
     * the server
     */
    private Server server;

    public UserTimerTask(Server server, ClientHandler ch){
        this.server = server;
        this.ch = ch;
    }

    /**
     * run the timer
     */
    public void run(){
        if(seconds<=turnSeconds){
            System.out.println(turnSeconds - seconds+ " seconds remaining");
            seconds++;
        }else{
            server.setTimeout(true);
            ch.resetConnected();
            ch.closeConnection();
            this.cancel();
        }
    }
}
