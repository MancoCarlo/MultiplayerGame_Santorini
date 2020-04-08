package it.polimi.ingsw.PSP29.controller;

import it.polimi.ingsw.PSP29.model.*;
import it.polimi.ingsw.PSP29.InputControl.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GameController {

    Match match;
    boolean end;
    boolean athenaOn;
    boolean godOn;
    int id;
    Coordinate c = null;
    Input input;

    public GameController(){
        match = new Match();
        end=false;
        athenaOn=false;
        godOn=false;
        input=new Input();
    }

    /**
     *
     * used for the execution of the game
     *
     * @throws NotValidInputException
     */
    public void gameExe() throws NotValidInputException, FileNotFoundException {
        firstTurn();
        match.printBoard(match.getBoard());
        while(!end){
            for(Player p : match.getPlayers()){
                if(match.getPlayers().size()==1){
                    //è rimasto uno solo
                    System.out.println(match.getPlayers().get(0).getNickname() + " hai vinto");
                    break;
                }
                if(playerCanMove(p)){
                    end=newTurn(p);
                    match.printBoard(match.getBoard());
                    if(end){
                        System.out.println(p.getNickname() + " hai vinto");
                        break;
                    }
                }
                else{
                    System.out.println("Hai perso");
                    match.removePlayer(p);
                    match.printBoard(match.getBoard());
                    end=false;
                }
            }
        }
    }

    /**
     * control if the player can move in this turn
     * @param p the player
     * @return true if player can move
     */
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
        }
        return false;
    }

    /**
     * execution of the first turn of the game
     * @throws FileNotFoundException
     */
    public void firstTurn() throws FileNotFoundException {
        match.addPlayers();
        match.printPlayers();
        match.sortPlayers();
        match.printPlayers();
        match.inizializeBoard();
        match.printBoard(match.getBoard());
        match.loadGods();
        match.printGodlist();
        godSelection();
        for(Player player : match.getPlayers()){
            for(int i=0; i<2; i++){
                System.out.print(player.getNickname());
                c=input.askCoordinate("posizionarti");
                while(!match.getBoard()[c.getX()][c.getY()].isEmpty()){
                    c=input.askCoordinate("posizionarti");
                }
                player.putWorker(i, match.getBoard(), c);
                match.printBoard(match.getBoard());
            }
        }
    }

    /**
     * let the player choose their gods
     */
    public void godSelection(){
        Player p = match.getPlayers().get(0);

        ArrayList<God> godlist = new ArrayList<God>();
        System.out.println("Player " + p.getNickname() + " inserisci " + match.getPlayers().size() + " divinità");
        createGodList(godlist, match.getGods(), match.getPlayers().size());
        for(int i=1; i<match.getPlayers().size(); i++){
            System.out.println("Player " + match.getPlayers().get(i).getNickname() + " pesca la tua divinità tra le rimanenti :");
            for(int j=0; j<godlist.size(); j++){
                System.out.println(j + ") " + godlist.get(j).getName() + " - " + godlist.get(j).getDescription());
            }
            match.getPlayers().get(i).selectGod(godlist);
        }
        System.out.println("Player " + match.getPlayers().get(0).getNickname() + " pesca la tua divinità tra le rimanenti :");
        for(int i=0; i<godlist.size(); i++){
            System.out.println(i + ") " + godlist.get(i).getName() + " - " + godlist.get(i).getDescription());
        }
        p.selectGod(godlist);
    }

    /**
     * creates the list of god that will be used during the game
     * @param gods the list of gods used in the game
     * @param matchGods the list of all gods
     * @param dim the number of player
     */
    public void createGodList(ArrayList<God> gods, ArrayList<God> matchGods, int dim){
        Scanner scanner = new Scanner(System.in);
        int i;
        if(dim>0){
            for(int j=0; j<match.getGods().size(); j++){
                System.out.println(j + ") " + match.getGod(j).getName() + " - " + match.getGod(j).getDescription());
            }
            try{
                i=Integer.parseInt(scanner.nextLine());
                if(i<0 || i>matchGods.size()-1){
                    throw new NotValidInputException(0, matchGods.size()-1);
                }
                else{
                    gods.add(matchGods.get(i));
                    matchGods.remove(i);
                    dim--;
                    createGodList(gods, matchGods, dim);
                }
            }
            catch (NotValidInputException e){
                createGodList(gods, matchGods, dim);
            }
        }
    }

    /**
     *
     * used to create a new turn
     *
     * @param p the player that plays the turn
     * @return true if player win
     */
    public boolean newTurn(Player p) {
        System.out.println("Turno di " + p.getNickname());
        if(athenaOn && p.getCard().getName().equals("Athena")){
            athenaOn=false;
        }
        godOn=input.askGod();
        if(!godOn){
            BaseTurn turn = new BaseTurn();
            if(!turn.cantMove(match, p.getWorker(0), athenaOn) || !turn.cantMove(match, p.getWorker(1), athenaOn)){
                return turnExe(p, turn);
            }
            else{
                System.out.println("Hai perso");
                match.removePlayer(p);
                return false;
            }
        }
        else{
            switch (p.getCard().getID()){
                case 0 :
                    ApolloTurn turn0 = new ApolloTurn(new GodTurn(new BaseTurn()));
                    if(!turn0.cantMove(match, p.getWorker(0), athenaOn) || !turn0.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(p, turn0);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 1 :
                    ArtemisTurn turn1 = new ArtemisTurn(new GodTurn(new BaseTurn()));
                    if(!turn1.cantMove(match, p.getWorker(0), athenaOn) || !turn1.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(p, turn1);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 2 :
                    AthenaTurn turn2 = new AthenaTurn(new GodTurn(new BaseTurn()));
                    if(!turn2.cantMove(match, p.getWorker(0), athenaOn) || !turn2.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(p, turn2);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 3 :
                    AtlasTurn turn3 = new AtlasTurn(new GodTurn(new BaseTurn()));
                    if(!turn3.cantMove(match, p.getWorker(0), athenaOn) || !turn3.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(p, turn3);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 4 :
                    DemeterTurn turn4 = new DemeterTurn(new GodTurn(new BaseTurn()));
                    if(!turn4.cantMove(match, p.getWorker(0), athenaOn) || !turn4.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(p, turn4);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 5 :
                    HephaestusTurn turn5 = new HephaestusTurn(new GodTurn(new BaseTurn()));
                    if(!turn5.cantMove(match, p.getWorker(0), athenaOn) || !turn5.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(p, turn5);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 6 :
                    MinotaurTurn turn6 = new MinotaurTurn(new GodTurn(new BaseTurn()));
                    if(!turn6.cantMove(match, p.getWorker(0), athenaOn) || !turn6.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(p, turn6);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 7 :
                    PanTurn turn7 = new PanTurn(new GodTurn(new BaseTurn()));
                    if(!turn7.cantMove(match, p.getWorker(0), athenaOn) || !turn7.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(p, turn7);
                    }
                    else{
                        System.out.println("Hai perso");
                        match.removePlayer(p);
                        return false;
                    }
                case 8 :
                    PrometheusTurn turn8 = new PrometheusTurn(new GodTurn(new BaseTurn()));
                    if(!turn8.cantMove(match, p.getWorker(0), athenaOn) || !turn8.cantMove(match, p.getWorker(1), athenaOn)){
                        return turnExe(p, turn8);
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

    /**
     *
     * used for the execution of the turn
     *
     * @param p the player that plays the turn
     * @param turn the turn, can be BaseTurn or one of the gods' turn
     * @return the result of winCondition
     */
    public boolean turnExe(Player p, Turn turn){
        match.printBoard(match.getBoard());
        id=input.askWorker(match, p, turn, athenaOn);
        c=input.askCoordinate("muoverti");
        if(athenaOn){
            while(!turn.limited_move(match, p.getWorker(id), c)){
                System.out.println("Il potere di Athena è attivo, non puoi salire di livello");
                System.out.println("Coordinate inserite non valide");
                id=input.askWorker(match, p, turn, athenaOn);
                c=input.askCoordinate("muoverti");
            }
        }
        else{
            while(!turn.move(match, p.getWorker(id), c)){
                System.out.println("Coordinate inserite non valide");
                id=input.askWorker(match, p, turn, athenaOn);
                c=input.askCoordinate("muoverti");
            }
        }
        match.printBoard(match.getBoard());
        if(p.getWorker(id).canBuild(match)){
            c=input.askCoordinate("costruire");
            while(!turn.build(match, p.getWorker(id), c)){
                System.out.println("Coordinate inserite non valide");
                c=input.askCoordinate("costruire");
            }
            //Condizione attivazione AthenaON
            if(p.getCard().getName().equals("Athena")){
                Coordinate oldPos = p.getWorker(id).getPrev_position();
                Coordinate newPos = p.getWorker(id).getPrev_position();
                if(match.getBoard()[oldPos.getX()][oldPos.getY()].getLevel() < match.getBoard()[newPos.getX()][newPos.getY()].getLevel()){
                    athenaOn = true;
                }
            }
            match.printBoard(match.getBoard());
            System.out.println(p.getWorker(0).toString());
            System.out.println(p.getWorker(1).toString());
            return turn.winCondition(match, p);
        }
        else{
            System.out.println("Non puoi costruire, hai perso");
            match.removePlayer(p);
            return false;
        }
    }


}
