package it.polimi.ingsw.PSP29.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoardGUI extends JPanel {
    private ArrayList<JButton> buttons = new ArrayList<>();

    public BoardGUI(){
        this.setLayout(new GridLayout(5,5));
    }

    public void setBoard(String command) {
        command = command.substring(5);
        for(int i=0; i<command.length();i=i+2){
            JButton button = new JButton(command.substring(i,i+2));
            switch(command.charAt(i)){
                case '1':
                    button.setBackground(Color.RED);
                    break;
                case '2':
                    button.setBackground(Color.BLUE);
                    break;
                case '3':
                    button.setBackground(Color.YELLOW);
                    break;
                default:
                    break;
            }
            buttons.add(button);
            this.add(button);
        }
    }

    public int getCoordinate(JButton b){
        int i;
        for(i = 0; i < buttons.size(); i++){
            if(b.equals(buttons.get(i)))
                break;
        }
        return i;
    }

    public ArrayList<JButton> getButtons() {
        return buttons;
    }
}
