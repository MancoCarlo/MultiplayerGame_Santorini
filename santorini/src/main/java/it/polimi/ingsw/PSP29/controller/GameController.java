package it.polimi.ingsw.PSP29.controller;

import it.polimi.ingsw.PSP29.model.*;

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

    public GameController(){
        match = new Match();
        end=false;
        athenaOn=false;
        godOn=false;
    }

    public void gameExe() throws NotValidInputException, FileNotFoundException {
        firstTurn();
        match.printBoard(match.getBoard());
        while(!end){
            for(Player p : match.getPlayers()){
                if(match.getPlayers().size()==1){
                    //è rimasto uno solo
                    break;
                }
                if(playerCanMove(p)){
                    end=newTurn(p);
                    match.printBoard(match.getBoard());
                    if(end){
                        //vittoria
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
                c=askCoordinate("posizionarti");
                while(!match.getBoard()[c.getX()][c.getY()].isEmpty()){
                    c=askCoordinate("posizionarti");
                }
                player.putWorker(i, match.getBoard(), c);
                match.printBoard(match.getBoard());
            }
        }
    }

    public void godSelection(){
        Player p = match.getPlayers().get(0);

        ArrayList<God> godlist = new ArrayList<God>();
        System.out.println("Player " + p.getNickname() + " inserisci " + match.getPlayers().size() + " divinità");
        for(int i=0; i<match.getGods().size(); i++){
            System.out.println(i + ") " + match.getGod(i).getName() + " - " + match.getGod(i).getDescription());
        }
        createGodList(godlist, match.getGods(), match.getPlayers().size());
        for(Player player : match.getPlayers()){
            System.out.println("Player " + player.getNickname() + " pesca la tua divinità tra le rimanenti :");
            for(int i=0; i<godlist.size(); i++){
                System.out.println(i + ") " + godlist.get(i).getName() + " - " + godlist.get(i).getDescription());
            }
            p.selectGod(godlist);
        }
    }

    public void createGodList(ArrayList<God> gods, ArrayList<God> matchGods, int dim){
        Scanner scanner = new Scanner(System.in);
        int i;
        if(dim>0){
            try{
                i=Integer.parseInt(scanner.nextLine());
                if(i<0 || i>matchGods.size()-1){
                    throw new NotValidInputException(0, matchGods.size()-1);
                }
                else{
                    gods.add(matchGods.get(i));
                    matchGods.remove(i);
                    dim--;
                    if(dim>1){
                        for(int j=0; j<match.getGods().size(); j++){
                            System.out.println(j + ") " + match.getGod(j).getName() + " - " + match.getGod(j).getDescription());
                        }
                    }
                    createGodList(gods, matchGods, dim);
                }
            }
            catch (NotValidInputException e){
                createGodList(gods, matchGods, dim);
            }
        }
    }

    public boolean newTurn(Player p) {
        if(athenaOn && p.getCard().getName().equals("Athena")){
            athenaOn=false;
        }
        godOn=askGod();
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

    public boolean turnExe(Player p, Turn turn){
        match.printBoard(match.getBoard());
        id=askWorker(p);
        c=askCoordinate("muoverti");
        if(athenaOn){
            while(!turn.limited_move(match, p.getWorker(id), c)){
                System.out.println("Il potere di Athena è attivo, non puoi salire di livello");
                System.out.println("Coordinate inserite non valide");
                id=askWorker(p);
                c=askCoordinate("muoverti");
            }
        }
        else{
            while(!turn.move(match, p.getWorker(id), c)){
                System.out.println("Coordinate inserite non valide");
                id=askWorker(p);
                c=askCoordinate("muoverti");
            }
        }
        match.printBoard(match.getBoard());
        c=askCoordinate("costruire");
        while(!turn.build(match, p.getWorker(id), c)){
            System.out.println("Coordinate inserite non valide");
            c=askCoordinate("costruire");
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
        return turn.winCondition(match, p);
    }

    public Coordinate askCoordinate(String str){
        int x=0, y=0;
        System.out.println(" inserisci la X dove vuoi " + str + ": ");
        x=ask_x_y();
        System.out.println(" inserisci la Y dove voui " + str + ": ");
        y=ask_x_y();
        return new Coordinate(x, y);
    }

    public int ask_x_y(){
        Scanner scanner = new Scanner(System.in);
        String s;
        int i;
        try{
            s=scanner.nextLine();
            i=Integer.parseInt(s);
            if(i<0 || i>4){
                throw (new NotValidInputException(0, 4));
            }
            else{
                return i;
            }
        }
        catch(NotValidInputException e){
            return ask_x_y();
        }
    }

    public int askWorker(Player p){
        Scanner scanner = new Scanner(System.in);
        String s;
        int i;
        try{
            System.out.println(p.getNickname() + " con che worker vuoi muoverti e costruire: ");
            System.out.println("0) Worker in posizione " + p.getWorker(0).getPosition().getX() + "," + p.getWorker(0).getPosition().getY() );
            System.out.println("1) Worker in posizione " + p.getWorker(1).getPosition().getX() + "," + p.getWorker(1).getPosition().getY() );
            s=scanner.nextLine();
            i=Integer.parseInt(s);
            if(i!=0 && i!=1){
                throw (new NotValidInputException(0, 1));
            }
            else{
                return i;
            }
        }
        catch (NotValidInputException e){
            return askWorker(p);
        }
    }

    public boolean askGod(){
        Scanner scanner = new Scanner(System.in);
        String s;
        int i;
        try{
            System.out.println("Vuoi usare la tua divinità: ");
            System.out.println("1) Sì\n2) No");
            s=scanner.nextLine();
            i=Integer.parseInt(s);
            if(i!=1 && i!=2){
                throw (new NotValidInputException(1, 2));
            }
            else{
                return(i == 1);
            }
        }
        catch(NotValidInputException e){
            return askGod();
        }
    }
}
