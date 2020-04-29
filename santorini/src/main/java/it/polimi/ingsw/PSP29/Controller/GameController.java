package it.polimi.ingsw.PSP29.Controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.InputControl.*;
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
     *
     * used for the execution of the turn
     *
     * @param ch the player that plays the turn
     * @param turn the turn, can be BaseTurn or one of the gods' turn
     * @return the result of winCondition
     */
    public boolean turnExe(ClientHandler ch, Turn turn){
        Player p = match.getPlayer(ch.getName());
        int wID=2;
        ArrayList<Coordinate> coordinates0 = whereCanMove(0);
        ArrayList<Coordinate> coordinates1 = whereCanMove(1);
        if(coordinates0.size()!=0 && coordinates1.size()!=0){
            server.write(ch, "serviceMessage", "It's your turn\n");
            server.write(ch, "interactionServer", match.getPlayer(ch.getName()).printWorkers());
            server.write(ch, "serviceMessage", "Choose the worker to use in this turn: \n");
            while(true){
                try{
                    wID = Integer.parseInt(server.read(ch));
                    if(wID<0 || wID>1){
                        server.write(ch, "serviceMessage", "Invalid input\n");
                        server.write(ch, "interactionServer", "Try another index: ");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e){
                    server.write(ch, "serviceMessage", "Invalid input\n");
                    server.write(ch, "interactionServer", "Try another index: ");
                }
            }
        }
        else if(coordinates0.size()!=0 && coordinates1.size()==0){
            server.write(ch, "serviceMessage", "You can only move one of your worker in these positions: \n");
            wID = 0;
        }
        else if(coordinates0.size()==0 && coordinates1.size()!=0){
            server.write(ch, "serviceMessage", "You can only move one of your worker in these positions: \n");
            wID = 1;
        }
        Coordinate c = null;
        if(wID==0){
            server.write(ch, "serviceMessage", printCoordinates(coordinates0));
            server.write(ch, "interactionServer", "Where you want to move?\n");
            int id;
            while(true){
                try{
                    id = Integer.parseInt(server.read(ch));
                    if(id<0 || id>=coordinates0.size()){
                        server.write(ch, "serviceMessage", "Invalid input\n");
                        server.write(ch, "interactionServer", "Try another index: ");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e){
                    server.write(ch, "serviceMessage", "Invalid input\n");
                    server.write(ch, "interactionServer", "Try another index: ");
                }
            }
            c = coordinates0.get(id);
        }
        else if(wID==1){
            server.write(ch, "serviceMessage", printCoordinates(coordinates1));
            server.write(ch, "interactionServer", "Where you want to move?\n");
            int id;
            while(true){
                try{
                    id = Integer.parseInt(server.read(ch));
                    if(id<0 || id>=coordinates1.size()){
                        server.write(ch, "serviceMessage", "Invalid input\n");
                        server.write(ch, "interactionServer", "Try another index: ");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e){
                    server.write(ch, "serviceMessage", "Invalid input\n");
                    server.write(ch, "interactionServer", "Try another index: ");
                }
            }
            c = coordinates1.get(id);
        }
        turn.move(match, p.getWorker(wID), c);
        /*
        if(p.getWorker(wID).canBuild(match)){
            //Costruzione
            return turn.winCondition(match, p);
        }
        else{
            System.out.println("Non puoi costruire, hai perso");
            match.removePlayer(p);
            return false;
        }
        */
        return false;
    }

    /**
     * !!è una prova!!
     * create an arrayList with all the coordinates in wich the worker can move
     * @param id the worker id
     * @return the list
     */
    public ArrayList<Coordinate> whereCanMove(int id) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        Player player = match.getPlayer(server.getClientHandlers().get(myturn).getName());
        for (int i = 0; i < match.getRows(); i++) {
            for (int j = 0; j < match.getColumns(); j++) {
                Coordinate c = new Coordinate(i, j);
                if (player.getWorker(id).canMoveTo(c, player.getCard().getName(), match, athenaOn)) {
                    coordinates.add(new Coordinate(i, j));
                }
            }
        }
        return coordinates;
    }

    /**
     * !!è una prova!!
     * print the list of valids coordinate
     * @param coordinates
     * @return the string that print the list
     */
    public String printCoordinates(ArrayList<Coordinate> coordinates){
        String c = "Valid coordinates:\n";
        for(int i=0; i<coordinates.size(); i++){
            c = c + i + ") " + coordinates.get(i).toString() + "\n";
        }
        return c;
    }

    /**
     * find the index of the next player
     */
    public void next(){
        myturn++;
        if(myturn == numPlayers){
            myturn=0;
        }
        for(int i=0;i<server.getClientHandlers().size();i++){
            if(i != myturn)
                server.write(server.getClientHandlers().get(i), "serviceMessage", "Is the turn of "+server.getClientHandlers().get(myturn).getName()+", wait your turn!!!\n");
            if(i == myturn)
                server.write(server.getClientHandlers().get(myturn), "serviceMessage", "Is your turn!!!\n");
        }
    }

    /**
     * assign one God to each player
     */
    public void godsAssignement(){
        int id=0;
        server.write(server.getClientHandlers().get(myturn), "serviceMessage", "Choose " + numPlayers + " gods from this list");
        server.write(server.getClientHandlers().get(myturn), "serviceMessage", match.printGodlist());
        server.write(server.getClientHandlers().get(myturn), "interactionServer", "Insert n°1 index: ");
        while(true){
            try {
                id=Integer.parseInt(server.read(server.getClientHandlers().get(myturn)));
                if(id<1 || id>match.getGods().size()){
                    server.write(server.getClientHandlers().get(myturn), "serviceMessage", "Invalid input\n");
                    server.write(server.getClientHandlers().get(myturn), "interactionServer", "Insert n°1 index: ");
                    continue;
                }
                break;
            } catch (NumberFormatException e){
                server.write(server.getClientHandlers().get(myturn), "serviceMessage", "Invalid input\n");
                server.write(server.getClientHandlers().get(myturn), "interactionServer", "Insert n°1 index: ");
            }
        }
        godIndex.add(id - 1);
        boolean find;
        for(int i=1; i<numPlayers; i++){
            server.write(server.getClientHandlers().get(myturn), "interactionServer", "Insert n°" + (i+1) + " index: ");
            while(true){
                find=false;
                try {
                    id=Integer.parseInt(server.read(server.getClientHandlers().get(myturn)));
                    if(id<1 || id>match.getGods().size()){
                        server.write(server.getClientHandlers().get(myturn), "serviceMessage", "Invalid input\n");
                        server.write(server.getClientHandlers().get(myturn), "interactionServer", "Insert n°" + (i+1) + " index: ");
                        continue;
                    }
                    for(int j : godIndex){
                        if(id-1==j){
                            server.write(server.getClientHandlers().get(myturn), "serviceMessage", "God already selected\n");
                            server.write(server.getClientHandlers().get(myturn), "interactionServer", "Insert n°" + (i+1) + " index: ");
                            find=true;
                        }
                    }
                    if(find){
                        continue;
                    }
                    break;
                } catch (NumberFormatException e){
                    server.write(server.getClientHandlers().get(myturn), "serviceMessage", "Invalid input\n");
                    server.write(server.getClientHandlers().get(myturn), "interactionServer", "Insert n°" + (i+1) + " index: ");
                }
            }
            godIndex.add(id - 1);
        }
        godSelection();
        int i=0;
        while (i<server.getClientHandlers().size()){
            next();
            server.write(server.getClientHandlers().get(myturn), "serviceMessage", match.printGodlist());
            server.write(server.getClientHandlers().get(myturn), "interactionServer", "Choose one god from this list: ");
            id = Integer.parseInt(server.read(server.getClientHandlers().get(myturn))) - 1;
            while(id >= match.getGods().size()|| id < 0){
                server.write(server.getClientHandlers().get(myturn), "serviceMessage", "Index not valid\n");
                server.write(server.getClientHandlers().get(myturn), "interactionServer", "Insert another index: ");
                id = Integer.parseInt(server.read(server.getClientHandlers().get(myturn))) - 1;
            }
            match.getPlayers().get(myturn).setCard(match.getGods(), id);
            match.getGods().remove(id);
            i++;
        }
    }

    /**
     * ask to the client where he want to put his players
     */
    public void putWorkers(){
        int i=0;
        while (i<numPlayers){
            next();
            server.write(server.getClientHandlers().get(myturn), "serviceMessage",  match.printBoard());
            server.write(server.getClientHandlers().get(myturn), "serviceMessage", "Insert Worker n°1\n");
            Coordinate c = getCoordinate();
            while (!controlMovement(match.getPlayers().get(myturn), 0, c)){
                server.write(server.getClientHandlers().get(myturn), "serviceMessage", "Not valid box\n");
                server.write(server.getClientHandlers().get(myturn), "serviceMessage", "Insert Worker n°1\n");
                c = getCoordinate();
            }

            server.write(server.getClientHandlers().get(myturn), "serviceMessage", "Insert Worker n°2\n");
            c = getCoordinate();
            while (!controlMovement(match.getPlayers().get(myturn), 1, c)){
                server.write(server.getClientHandlers().get(myturn), "serviceMessage", "Not valid box\n");
                server.write(server.getClientHandlers().get(myturn), "serviceMessage", "Insert Worker n°2\n");
                c = getCoordinate();
            }
            i++;
        }
        next();
    }

    /**
     * ask a coordinate to the client
     * @return the coordinate
     */
    public Coordinate getCoordinate(){
        Coordinate c;
        server.write(server.getClientHandlers().get(myturn), "interactionServer", "X: ");
        int x=Integer.parseInt(server.read(server.getClientHandlers().get(myturn)));
        server.write(server.getClientHandlers().get(myturn), "interactionServer", "Y: ");
        int y=Integer.parseInt(server.read(server.getClientHandlers().get(myturn)));
        c = new Coordinate(x, y);
        return c;
    }

    public boolean playerCanMove(Player p){
        BaseTurn turn = new BaseTurn();
        switch (p.getCard().getID()){
            case 0 :
                ApolloTurn turn0 = new ApolloTurn(new GodTurn(new BaseTurn()));
                return (!turn.cantMove(match, p.getWorker(0), athenaOn) || !turn.cantMove(match, p.getWorker(1), athenaOn) || !turn0.cantMove(match, p.getWorker(0), athenaOn) || !turn0.cantMove(match, p.getWorker(1), athenaOn));
            case 1 :
                ArtemisTurn turn1 = new ArtemisTurn(new GodTurn(new BaseTurn()));
                return (!turn.cantMove(match, p.getWorker(0), athenaOn) || !turn.cantMove(match, p.getWorker(1), athenaOn) || !turn1.cantMove(match, p.getWorker(0), athenaOn) || !turn1.cantMove(match, p.getWorker(1), athenaOn));
            case 2 :
                AthenaTurn turn2 = new AthenaTurn(new GodTurn(new BaseTurn()));
                return (!turn.cantMove(match, p.getWorker(0), athenaOn) || !turn.cantMove(match, p.getWorker(1), athenaOn) || !turn2.cantMove(match, p.getWorker(0), athenaOn) || !turn2.cantMove(match, p.getWorker(1), athenaOn));
            case 3 :
                AtlasTurn turn3 = new AtlasTurn(new GodTurn(new BaseTurn()));
                return (!turn.cantMove(match, p.getWorker(0), athenaOn) || !turn.cantMove(match, p.getWorker(1), athenaOn) || !turn3.cantMove(match, p.getWorker(0), athenaOn) || !turn3.cantMove(match, p.getWorker(1), athenaOn));
            case 4 :
                DemeterTurn turn4 = new DemeterTurn(new GodTurn(new BaseTurn()));
                return (!turn.cantMove(match, p.getWorker(0), athenaOn) || !turn.cantMove(match, p.getWorker(1), athenaOn) || !turn4.cantMove(match, p.getWorker(0), athenaOn) || !turn4.cantMove(match, p.getWorker(1), athenaOn));
            case 5 :
                HephaestusTurn turn5 = new HephaestusTurn(new GodTurn(new BaseTurn()));
                return (!turn.cantMove(match, p.getWorker(0), athenaOn) || !turn.cantMove(match, p.getWorker(1), athenaOn) || !turn5.cantMove(match, p.getWorker(0), athenaOn) || !turn5.cantMove(match, p.getWorker(1), athenaOn));
            case 6 :
                MinotaurTurn turn6 = new MinotaurTurn(new GodTurn(new BaseTurn()));
                return (!turn.cantMove(match, p.getWorker(0), athenaOn) || !turn.cantMove(match, p.getWorker(1), athenaOn) || !turn6.cantMove(match, p.getWorker(0), athenaOn) || !turn6.cantMove(match, p.getWorker(1), athenaOn));
            case 7 :
                PanTurn turn7 = new PanTurn(new GodTurn(new BaseTurn()));
                return (!turn.cantMove(match, p.getWorker(0), athenaOn) || !turn.cantMove(match, p.getWorker(1), athenaOn) || !turn7.cantMove(match, p.getWorker(0), athenaOn) || !turn7.cantMove(match, p.getWorker(1), athenaOn));
            case 8 :
                PrometheusTurn turn8 = new PrometheusTurn(new GodTurn(new BaseTurn()));
                return (!turn.cantMove(match, p.getWorker(0), athenaOn) || !turn.cantMove(match, p.getWorker(1), athenaOn) || !turn8.cantMove(match, p.getWorker(0), athenaOn) || !turn8.cantMove(match, p.getWorker(1), athenaOn));
            default:
                return (!turn.cantMove(match, p.getWorker(0), athenaOn) || !turn.cantMove(match, p.getWorker(1), athenaOn) || !turn.cantMove(match, p.getWorker(0), athenaOn) || !turn.cantMove(match, p.getWorker(1), athenaOn));
        }
    }

    /**
     *
     * used for the execution of the game
     *
     * @throws NotValidInputException
    */
    public void gameExe(){

        while(!endGame){
            if(match.getPlayers().size()==1){
                endGame=true;
                for(int i=0; i<server.getClientHandlers().size();i++){
                    if(i == myturn) server.write(server.getClientHandlers().get(myturn), "serviceMessage", "Congratulations you win!\n");
                    else server.write(server.getClientHandlers().get(i), "serviceMessage", "You lose, the winner is "+server.getClientHandlers().get(myturn).getName()+"!\n");
                }
                break;
            }
            if(playerCanMove(match.getPlayers().get(myturn))){
                endGame=newTurn(server.getClientHandlers().get(myturn));
                match.resetBoard();
                for(ClientHandler ch : server.getClientHandlers()){
                    server.write(ch, "serviceMessage", match.printBoard());
                }
                if(endGame){
                    for(int i=0; i<server.getClientHandlers().size();i++){
                        if(i == myturn) server.write(server.getClientHandlers().get(myturn), "serviceMessage", "Congratulations you win!\n");
                        else server.write(server.getClientHandlers().get(i), "serviceMessage", "You lose, the winner is "+server.getClientHandlers().get(myturn).getName()+"!\n");
                    }
                }
            }
            else{
                server.write(server.getClientHandlers().get(myturn), "serviceMessage", "You lose!!!\n\n");
                match.removePlayer(match.getPlayer(server.getClientHandlers().get(myturn).getName()));
                //Gestire rimozione clientHandler
                numPlayers--;
                endGame=false;
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
        server.write(ch, "interactionServer", "Would you use your god in this turn?\n1) YES\n2) NO\n");
        String response = server.read(ch);
        boolean godOn;
        if(response.equals("1")) godOn = true;
        else godOn = false;
        if(!godOn){
            BaseTurn turn = new BaseTurn();
            if(!turn.cantMove(match, p.getWorker(0), athenaOn) || !turn.cantMove(match, p.getWorker(1), athenaOn)){
                return turnExe(ch, turn);
            }
            else{
                server.write(ch, "serviceMessage", "You lose!!!\n\n");
                match.removePlayer(p);
                //Bisogna gestire lo spostamento del clientHandler in coda
                numPlayers--;
                return false;
            }
        }
        else{
            switch (p.getCard().getID()){
                case 0 :
                    ApolloTurn turn0 = new ApolloTurn(new GodTurn(new BaseTurn()));
                    if(!turn0.cantMove(match, p.getWorker(0), athenaOn) || !turn0.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(ch, turn0);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 1 :
                    ArtemisTurn turn1 = new ArtemisTurn(new GodTurn(new BaseTurn()));
                    if(!turn1.cantMove(match, p.getWorker(0), athenaOn) || !turn1.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(ch, turn1);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 2 :
                    AthenaTurn turn2 = new AthenaTurn(new GodTurn(new BaseTurn()));
                    if(!turn2.cantMove(match, p.getWorker(0), athenaOn) || !turn2.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(ch, turn2);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 3 :
                    AtlasTurn turn3 = new AtlasTurn(new GodTurn(new BaseTurn()));
                    if(!turn3.cantMove(match, p.getWorker(0), athenaOn) || !turn3.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(ch, turn3);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 4 :
                    DemeterTurn turn4 = new DemeterTurn(new GodTurn(new BaseTurn()));
                    if(!turn4.cantMove(match, p.getWorker(0), athenaOn) || !turn4.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(ch, turn4);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 5 :
                    HephaestusTurn turn5 = new HephaestusTurn(new GodTurn(new BaseTurn()));
                    if(!turn5.cantMove(match, p.getWorker(0), athenaOn) || !turn5.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(ch, turn5);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 6 :
                    MinotaurTurn turn6 = new MinotaurTurn(new GodTurn(new BaseTurn()));
                    if(!turn6.cantMove(match, p.getWorker(0), athenaOn) || !turn6.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(ch, turn6);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 7 :
                    PanTurn turn7 = new PanTurn(new GodTurn(new BaseTurn()));
                    if(!turn7.cantMove(match, p.getWorker(0), athenaOn) || !turn7.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(ch, turn7);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 8 :
                    PrometheusTurn turn8 = new PrometheusTurn(new GodTurn(new BaseTurn()));
                    if(!turn8.cantMove(match, p.getWorker(0), athenaOn) || !turn8.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(ch, turn8);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
            }
        }
        return false;
    }
}
