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
