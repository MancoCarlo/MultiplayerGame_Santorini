package it.polimi.ingsw.PSP29.Controller;

public class NotValidInputException extends Exception{

    public NotValidInputException(int min, int max){
        System.out.println("Input must be between " + min + " and " + max);
    }
}
