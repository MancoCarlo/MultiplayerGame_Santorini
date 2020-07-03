package it.polimi.ingsw.PSP29.virtualView;

import it.polimi.ingsw.PSP29.view.Client;
import it.polimi.ingsw.PSP29.virtualView.Server;

import java.util.Scanner;

/**
 * @author Luca Martiri
 */
public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String response = "";

        System.out.println("What you want to run:\n1) Server\n2) Client");
        response=scanner.nextLine();
        while(!response.equals("1") && !response.equals("2")){
            System.out.print("Not valid input, try again: ");
            response = scanner.nextLine();
        }

        if(response.equals("2")){
            Client client = new Client();
            client.run();
        }
        else{
            Server server = new Server();
            server.launch();
        }

    }
}
