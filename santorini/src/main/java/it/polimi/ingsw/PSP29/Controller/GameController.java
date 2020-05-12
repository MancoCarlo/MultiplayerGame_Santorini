package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.virtualView.ClientHandler;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GameController {

    private Match match;
    private Server server;
    private boolean endGame;
    private boolean godOn;
    private ArrayList<Integer> godIndex = new ArrayList<>();
    private Coordinate c = null;
    private boolean athenaOn;
    private int myturn = 0;
    private int numPlayers;

    public GameController(Server s){
        match = new Match();
        endGame=false;
        godOn=false;
        server = s;
    }

    public ArrayList<Integer> getGodIndex(){
        return godIndex;
    }

    public Match getMatch() {
        return match;
    }

    public boolean getAthenaOn() { return athenaOn; }

    public void setAthenaOn(boolean b) { athenaOn = b; }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getTurn() {
        return myturn;
    }

    public boolean controlMovement(Player p, int id, Coordinate c){
        if(c.getX()>4 || c.getY()>4 || c.getX()<0 || c.getY()<0){
            return false;
        }
        else{
            if(!match.getBoard()[c.getX()][c.getY()].isEmpty()){
                return false;
            }
            else{
                match.updateMovement(p, id, c);
                return true;
            }
        }
    }


    /**
     * let the player choose their gods
     */
    public void godSelection(){
        ArrayList<God> gods = new ArrayList<>();
        for(Integer i : godIndex){
            gods.add(match.getGods().get(i));
        }

        match.getGods().clear();
        for(God god : gods){
            match.getGods().add(god);
        }
    }

    /**
     * find the index of the next player
     */
    public void next(){
        myturn++;
        if(myturn == numPlayers){
            myturn=0;
        }
        while(!match.getPlayers().get(myturn).getInGame()){
            myturn++;
            if(myturn == numPlayers){
                myturn=0;
            }
        }
        for(int i=0;i<server.getClientHandlers().size();i++){
            if(i != myturn)
                server.write(server.getClientHandlers().get(i), "serviceMessage", "MSGE-Is the turn of "+server.getClientHandlers().get(myturn).getName()+", wait your turn!!!\n");
            if(i == myturn)
                server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Is your turn!!!\n");
        }
    }

    /**
     * assign one God to each player
     */
    public boolean godsAssignement(){
        match.getGods().clear();
        match.loadGods();
        godIndex.clear();
        int id=0;
        server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Choose " + match.playersInGame() + " gods from this list");
        server.write(server.getClientHandlers().get(myturn), "serviceMessage", "LIST-"+match.printGodlist());
        String num = Integer.toString(match.getGods().size());
        server.write(server.getClientHandlers().get(myturn), "interactionServer", "INDX-Insert n°1 god's index: ");
        while(true){
            try {
                String str = server.read(server.getClientHandlers().get(myturn));
                if(str==null){
                    match.updatePlayers(server.getClientHandlers());
                    if(match.playersInGame()!=1){
                        next();
                        server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Choose " + match.playersInGame() + " gods from this list");
                        server.write(server.getClientHandlers().get(myturn), "serviceMessage", "LIST-"+match.printGodlist());
                        num = Integer.toString(match.getGods().size());
                        server.write(server.getClientHandlers().get(myturn), "interactionServer", "INDX-Insert n°2 god's index: ");
                        continue;
                    }
                    else{
                        return false;
                    }
                }
                id=Integer.parseInt(str);
                if(id<1 || id>match.getGods().size()){
                    server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Invalid input\n");
                    num = Integer.toString(match.getGods().size());
                    server.write(server.getClientHandlers().get(myturn), "interactionServer", "INDX-Insert n°1 god's index: ");
                    continue;
                }
                break;
            } catch (NumberFormatException e){
                server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Invalid input\n");
                num = Integer.toString(match.getGods().size());
                server.write(server.getClientHandlers().get(myturn), "interactionServer", "INDX-Insert n°1 god's index: ");
            }
        }
        godIndex.add(id - 1);
        boolean find;
        for(int i=1; i<numPlayers; i++){
            num = Integer.toString(match.getGods().size());
            server.write(server.getClientHandlers().get(myturn), "interactionServer", "INDX-Insert n°" + (i+1) + " god's index: ");
            while(true){
                find=false;
                try {
                    String str = server.read(server.getClientHandlers().get(myturn));
                    if(str==null){
                        match.updatePlayers(server.getClientHandlers());
                        if(match.playersInGame()!=1){
                            next();
                            server.write(server.getClientHandlers().get(myturn), "serviceMessage", "LIST-"+match.printGodlist());
                            num = Integer.toString(match.getGods().size());
                            server.write(server.getClientHandlers().get(myturn), "interactionServer", "INDX-Insert n°" + (i+1) + " god's index: ");
                            continue;
                        }
                        else{
                            return false;
                        }
                    }
                    id=Integer.parseInt(str);
                    if(id<1 || id>match.getGods().size()){
                        server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Invalid input\n");
                        num = Integer.toString(match.getGods().size());
                        server.write(server.getClientHandlers().get(myturn), "interactionServer", "INDX-Insert n°" + (i+1) + " god's index: ");
                        continue;
                    }
                    for(int j : godIndex){
                        if(id-1==j){
                            server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-God already selected\n");
                            num = Integer.toString(match.getGods().size());
                            server.write(server.getClientHandlers().get(myturn), "interactionServer", "INDX-Insert n°" + (i+1) + " god's index: ");
                            find=true;
                        }
                    }
                    if(find){
                        continue;
                    }
                    break;
                } catch (NumberFormatException e){
                    server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Invalid input\n");
                    num = Integer.toString(match.getGods().size());
                    server.write(server.getClientHandlers().get(myturn), "interactionServer", "INDX-Insert n°" + (i+1) + " god's index: ");
                }
            }
            godIndex.add(id - 1);
        }
        godSelection();
        System.out.println(match.printGodlist());
        int i=0;
        int count = match.playersInGame();
        System.out.println(count);
        while (i<count){
            next();
            server.write(server.getClientHandlers().get(myturn), "serviceMessage", "LIST-"+match.printGodlist());
            num = Integer.toString(match.getGods().size());
            server.write(server.getClientHandlers().get(myturn), "interactionServer", "INDX-Choose one god from this list: ");
            String str = server.read(server.getClientHandlers().get(myturn));
            if(str==null){
                match.updatePlayers(server.getClientHandlers());
                if(match.playersInGame()!=1){
                    i++;
                    continue;
                }
                else{
                    return false;
                }
            }
            id=Integer.parseInt(str)-1;
            System.out.println(id);
            while(id >= match.getGods().size() || id < 0){
                server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Index not valid\n");
                num = Integer.toString(match.getGods().size());
                server.write(server.getClientHandlers().get(myturn), "interactionServer", "INDX-Choose one god from this list: ");;
                str = server.read(server.getClientHandlers().get(myturn));
                if(str==null){
                    match.updatePlayers(server.getClientHandlers());
                    if(match.playersInGame()!=1){
                        i++;
                        continue;
                    }
                    else{
                        return false;
                    }
                }
                id = Integer.parseInt(str) - 1;
            }
            match.getPlayers().get(myturn).setCard(match.getGods(), id);
            match.getGods().remove(id);
            i++;
        }
        return true;
    }

    /**
     * ask to the client where he want to put his players
     */
    public boolean putWorkers(){
        int i=0;
        int count = match.playersInGame();
        while (i<count){
            next();
            server.write(server.getClientHandlers().get(myturn), "serviceMessage",  "BORD-"+match.printBoard());
            server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Insert Worker n°1\n");
            Coordinate c = getCoordinate();
            if(c==null){
                match.updatePlayers(server.getClientHandlers());
                if(match.playersInGame()==1){
                    return false;
                }
                else{
                    i++;
                    continue;
                }
            }
            while (!controlMovement(match.getPlayers().get(myturn), 0, c)){
                server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Not valid box\n");
                server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Insert Worker n°1\n");
                c = getCoordinate();
                if(c==null){
                    match.updatePlayers(server.getClientHandlers());
                    if(match.playersInGame()==1){
                        return false;
                    }
                    else{
                        i++;
                        continue;
                    }
                }
            }

            server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Insert Worker n°2\n");
            c = getCoordinate();
            if(c==null){
                match.updatePlayers(server.getClientHandlers());
                if(match.playersInGame()==1){
                    return false;
                }
                else{
                    i++;
                    continue;
                }
            }
            while (!controlMovement(match.getPlayers().get(myturn), 1, c)){
                server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Not valid box\n");
                server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Insert Worker n°2\n");
                c = getCoordinate();
                if(c==null){
                    match.updatePlayers(server.getClientHandlers());
                    if(match.playersInGame()==1){
                        return false;
                    }
                    else{
                        i++;
                        continue;
                    }
                }
            }
            i++;
        }
        next();
        return true;
    }

    /**
     * ask a coordinate to the client
     * @return the coordinate
     */
    public Coordinate getCoordinate(){
        Coordinate c;
        String str;
        server.write(server.getClientHandlers().get(myturn), "interactionServer", "COOR-Insert coordinate: ");
        str = server.read(server.getClientHandlers().get(myturn));
        if(str==null){
            return null;
        }
        int x=Integer.parseInt(str.substring(0,1));
        int y=Integer.parseInt(str.substring(1));

        c = new Coordinate(x, y);
        return c;
    }

    /**
     *
     * used for the execution of the game
     *
     */
    public void gameExe(){
        while(!endGame){
            match.updatePlayers(server.getClientHandlers());
            if(match.getPlayers().get(myturn).getInGame()) {
                if (match.playersInGame() == 1) {
                    endGame = true;
                    for (int i = 0; i < server.getClientHandlers().size(); i++) {
                        if (i == myturn)
                            server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Congratulations you win!\n");
                        else
                            server.write(server.getClientHandlers().get(i), "serviceMessage", "MSGE-You lose, the winner is " + server.getClientHandlers().get(myturn).getName() + "!\n");
                    }
                    break;
                }
                endGame = newTurn(server.getClientHandlers().get(myturn));
                match.resetBoard();
                for (ClientHandler ch : server.getClientHandlers()) {
                    server.write(ch, "serviceMessage", "BORD-"+match.printBoard());
                }
                if (endGame) {
                    for (int i = 0; i < server.getClientHandlers().size(); i++) {
                        if (i == myturn)
                            server.write(server.getClientHandlers().get(myturn), "serviceMessage", "MSGE-Congratulations you win!\n");
                        else
                            server.write(server.getClientHandlers().get(i), "serviceMessage", "MSGE-You lose, the winner is " + server.getClientHandlers().get(myturn).getName() + "!\n");
                    }
                }
            }
            next();
        }
    }




    /**
     *
     * used to create a new turn
     *
     * @param ch the player that plays the turn
     * @return true if player win
     */
    public boolean newTurn(ClientHandler ch) {
        Player p = match.getPlayer(ch.getName());
        if(athenaOn && p.getCard().getName().equals("Athena")){
            athenaOn=false;
        }
        server.write(ch, "serviceMessage", "Would you use your god in this turn?\n");
        server.write(ch, "serviceMessage", "LIST-1) YES\n2) NO\n");
        server.write(ch, "interactionServer", "INDX-Use god power: ");
        String response = server.read(ch);
        if(response==null){
            match.updatePlayers(server.getClientHandlers());
            return false;
        }
        boolean godOn;
        if(response.equals("1")) godOn = true;
        else godOn = false;
        if(!godOn){
            BaseTurn turn = new BaseTurn();
            return turnExe(ch, turn);
        }
        else{
            switch (p.getCard().getID()){
                case 0 :
                    ApolloTurn turn0 = new ApolloTurn(new GodTurn(new BaseTurn()));
                    return turnExe(ch, turn0);
                case 1 :
                    ArtemisTurn turn1 = new ArtemisTurn(new GodTurn(new BaseTurn()));
                    return turnExe(ch, turn1);
                case 2 :
                    AthenaTurn turn2 = new AthenaTurn(new GodTurn(new BaseTurn()));
                    return turnExe(ch, turn2);
                case 3 :
                    AtlasTurn turn3 = new AtlasTurn(new GodTurn(new BaseTurn()));
                    return turnExe(ch, turn3);
                case 4 :
                    DemeterTurn turn4 = new DemeterTurn(new GodTurn(new BaseTurn()));
                    return turnExe(ch, turn4);
                case 5 :
                    HephaestusTurn turn5 = new HephaestusTurn(new GodTurn(new BaseTurn()));
                    return turnExe(ch, turn5);
                case 6 :
                    MinotaurTurn turn6 = new MinotaurTurn(new GodTurn(new BaseTurn()));
                    return turnExe(ch, turn6);
                case 7 :
                    PanTurn turn7 = new PanTurn(new GodTurn(new BaseTurn()));
                    return turnExe(ch, turn7);
                case 8 :
                    PrometheusTurn turn8 = new PrometheusTurn(new GodTurn(new BaseTurn()));
                    return turnExe(ch, turn8);
                case 9 :
                    PoseidonTurn turn9 = new PoseidonTurn(new GodTurn(new BaseTurn()));
                    return turnExe(ch, turn9);
                case 10 :
                    TritonTurn turn10 = new TritonTurn(new GodTurn(new BaseTurn()));
                    return turnExe(ch, turn10);
                case 11 :
                    HestiaTurn turn11 = new HestiaTurn(new GodTurn(new BaseTurn()));
                    return turnExe(ch, turn11);
                case 12 :
                    CharonTurn turn12 = new CharonTurn(new GodTurn(new BaseTurn()));
                    return turnExe(ch, turn12);
            }
        }
        return false;
    }


    /**
     *
     * used for the execution of the turn
     *
     * @param ch the player that plays the turn
     * @param turn the turn, can be BaseTurn or one of the gods' turn
     * @return the result of winCondition
     */
    public boolean turnExe(ClientHandler ch, Turn turn){
        for(ClientHandler clientHandler : server.getClientHandlers()){
            server.write(clientHandler, "serviceMessage", "BORD-"+match.printBoard());
        }

        if(!turn.move(match, ch, server, athenaOn)){
            losePlayer(ch);
            return false;
        }

        if(match.getPlayer(ch.getName()).getCard().getName().equals("Athena"))
            athenaCondition(ch);

        for(ClientHandler clientHandler : server.getClientHandlers()){
            server.write(clientHandler, "serviceMessage", "BORD-"+match.printBoard());
        }

        if(!turn.build(match,ch,server)){
            losePlayer(ch);
            return false;
        }

        if(!turn.winCondition(match, match.getPlayer(ch.getName()))){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Exclude the player from the game and remove his workers from the board
     * @param ch loser player
     */
    public void losePlayer(ClientHandler ch){
        server.write(ch,"serviceMessage", "MSGE-You Lose!");
        match.getPlayer(ch.getName()).setInGame(false);
        match.removeWorkers(match.getPlayer(ch.getName()));
    }

    public void athenaCondition(ClientHandler ch){
        Coordinate cprev;
        Coordinate c;
        for(Worker w : match.getPlayer(ch.getName()).getWorkers()){
            if(w.getMoved()){
                cprev = w.getPrev_position();
                c = w.getPosition();
                if(match.getBoard()[cprev.getX()][cprev.getY()].getlevelledUp()){
                    if(match.getBoard()[c.getX()][c.getY()].getLevel() - match.getBoard()[cprev.getX()][cprev.getY()].getLevel() >= 0 )
                        athenaOn = true;
                    else
                        athenaOn = false;
                }else{
                    if(match.getBoard()[c.getX()][c.getY()].getLevel() - match.getBoard()[cprev.getX()][cprev.getY()].getLevel() > 0 )
                        athenaOn = true;
                    else
                        athenaOn = false;
                }
            }
        }
    }
}
