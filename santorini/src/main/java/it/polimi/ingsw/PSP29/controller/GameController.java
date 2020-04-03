package it.polimi.ingsw.PSP29.controller;

import it.polimi.ingsw.PSP29.model.*;

import java.util.Scanner;

public class GameController {

    Match match;
    boolean end;
    boolean athenaOn;
    boolean godOn;

    public GameController(){
        match = new Match();
        end=false;
        athenaOn=false;
        godOn=false;
    }

    public void gameExe() throws NotValidInputException {
        while(!end){
            for(Player p : match.getPlayers()){
                if(loseControl(p)){
                    match.removePlayer(p);
                    if(match.getPlayers().size()==1){
                        //vince perchè è rimasto solo
                        break;
                    }
                }
                else{
                    end=!newTurn(p);
                    if(end){
                        //vittoria
                        break;
                    }
                }
            }
        }
    }

    public boolean loseControl(Player player){
        if(player.getWorker(0).cantMove(match) && player.getWorker(1).cantMove(match){
            return true;
        }
        return false;
    }

    public boolean newTurn(Player p) throws NotValidInputException {
        if(athenaOn && p.getCard().getName().equals("Athena")){
            athenaOn=false;
        }
        askGod();
        if(!godOn){
            BaseTurn turn = new BaseTurn();
            return turnExe(p, turn);
        }
        else{
            switch (p.getCard().getID()){
                case 0 :
                    ApolloTurn turn0 = new ApolloTurn(new GodTurn(new BaseTurn()));
                    return turnExe(p, turn0);
                    break;
                case 1 :
                    ArtemisTurn turn1 = new ArtemisTurn(new GodTurn(new BaseTurn()));
                    return turnExe(p, turn1);
                    break;
                case 2 :
                    athenaOn=true;
                    AthenaTurn turn2 = new AthenaTurn(new GodTurn(new BaseTurn()));
                    return turnExe(p, turn2);
                    break;
                case 3 :
                    AtlasTurn turn3 = new AtlasTurn(new GodTurn(new BaseTurn()));
                    return turnExe(p, turn3);
                    break;
                case 4 :
                    DemeterTurn turn4 = new DemeterTurn(new GodTurn(new BaseTurn()));
                    return turnExe(p, turn4);
                    break;
                case 5 :
                    HephaestusTurn turn5 = new HephaestusTurn(new GodTurn(new BaseTurn()));
                    return turnExe(p, turn5);
                    break;
                case 6 :
                    MinotaurTurn turn6 = new MinotaurTurn(new GodTurn(new BaseTurn()));
                    return turnExe(p, turn6);
                    break;
                case 7 :
                    PanTurn turn7 = new PanTurn(new GodTurn(new BaseTurn()));
                    return turnExe(p, turn7);
                    break;
                case 8 :
                    PrometheusTurn turn8 = new PrometheusTurn(new GodTurn(new BaseTurn()));
                    return turnExe(p, turn8);
                    break;
            }
        }
        return false;
    }

    public boolean turnExe(Player p, Turn turn){
        Coordinate c = null;
        int id=0;
        askWorker(id, p);
        askCoordinate(c, "muoverti");
        if(athenaOn){
            while(!turn.limited_move(match, p.getWorker(id), c)){
                System.out.println("Il potere di Athena è attivo, non puoi salire di livello");
                System.out.println("Coordinate inserite non valide");
                askWorker(id, p);
                askCoordinate(c, "muoverti");
            }
        }
        else{
            while(!turn.move(match, p.getWorker(id), c)){
                System.out.println("Coordinate inserite non valide");
                askWorker(id, p);
                askCoordinate(c, "muoverti");
            }
        }
        askCoordinate(c, "costruire");
        while(!turn.build(match, p.getWorker(id), c)){
            System.out.println("Coordinate inserite non valide");
            askCoordinate(c, "costruire");
        }
        return turn.winCondition(match, p);
    }

    public void askCoordinate(Coordinate c, String str){
        int x=0, y=0;
        System.out.println("Inserisci la X dove vuoi " + str + ": ");
        ask_x_y(x);
        System.out.println("Inserisci la Y dove voui " + str + ": ");
        ask_x_y(y);
        c=new Coordinate(x, y);
    }

    public void ask_x_y(int c){
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
                c=i;
            }
        }
        catch(NotValidInputException e){
            ask_x_y(c);
        }
    }

    public void askWorker(int id, Player p){
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
                id=i;
            }
        }
        catch (NotValidInputException e){
            askWorker(id, p);
        }
    }

    public void askGod(){
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
                godOn=(i == 1);
            }
        }
        catch(NotValidInputException e){
            askGod();
        }
    }
}
