package it.polimi.ingsw.PSP29.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoardGUI extends JPanel {
    private ArrayList<JButton> buttons = new ArrayList<>();

    public BoardGUI(){
        this.setLayout(new GridLayout(5,5));
    }

    /**
     * set the board for the GUI
     * @param command the string that represent the board
     */
    public void setBoard(String command) {
        command = command.substring(5);
        for(int i=0; i<command.length();i=i+2){
            JButton button = new JButton(command.substring(i,i+2));
            switch(command.charAt(i)){
                case '0':
                    setFloor(button, '0', command.charAt(i+1));
                    break;
                case '1':
                    setFloor(button, '1', command.charAt(i+1));
                    break;
                case '2':
                    setFloor(button, '2', command.charAt(i+1));
                    break;
                case '3':
                    setFloor(button, '3', command.charAt(i+1));
                    break;
                case '4':
                    setFloor(button, '3', command.charAt(i+1));
                    break;
                default:
                    break;
            }
            buttons.add(button);
            this.add(button);
        }
    }

    /**
     * @param b the button
     * @return the position of the button in the ArrayList
     */
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

    /**
     * set the image of the button
     * @param button the button
     * @param player char that represent the player
     * @param floor char that represent the level of the box
     */
    public void setFloor(JButton button, char player, char floor){
        ImageIcon img;
        Image img1;
        Image newimg;
        switch (player){
            case '0':
                if(floor=='0') img = new ImageIcon(getClass().getResource("/Board/floor0.png"));
                else if(floor=='1') img = new ImageIcon(getClass().getResource("/Board/floor1.png"));
                else if(floor=='2') img = new ImageIcon(getClass().getResource("/Board/floor2.png"));
                else if(floor=='3') img = new ImageIcon(getClass().getResource("/Board/floor3.png"));
                else img = new ImageIcon(getClass().getResource("/Board/floor4.png"));
                break;

            case '1':
                if(floor=='0') img = new ImageIcon(getClass().getResource("/Board/floor0red.png"));
                else if(floor=='1') img = new ImageIcon(getClass().getResource("/Board/floor1red.png"));
                else if(floor=='2') img = new ImageIcon(getClass().getResource("/Board/floor2red.png"));
                else img = new ImageIcon(getClass().getResource("/Board/floor3red.png"));
                break;

            case '2':
                if(floor=='0') img = new ImageIcon(getClass().getResource("/Board/floor0blue.png"));
                else if(floor=='1') img = new ImageIcon(getClass().getResource("/Board/floor1blue.png"));
                else if(floor=='2') img = new ImageIcon(getClass().getResource("/Board/floor2blue.png"));
                else img = new ImageIcon(getClass().getResource("/Board/floor3blue.png"));
                break;

            case '3':
                if(floor=='0') img = new ImageIcon(getClass().getResource("/Board/floor0yellow.png"));
                else if(floor=='1') img = new ImageIcon(getClass().getResource("/Board/floor1yellow.png"));
                else if(floor=='2') img = new ImageIcon(getClass().getResource("/Board/floor2yellow.png"));
                else img = new ImageIcon(getClass().getResource("/Board/floor3yellow.png"));
                break;

            default:
                if(floor=='0') img = new ImageIcon(getClass().getResource("/Board/floor0.png"));
                else if(floor=='1') img = new ImageIcon(getClass().getResource("/Board/floor1.png"));
                else if(floor=='2') img = new ImageIcon(getClass().getResource("/Board/floor2.png"));
                else if(floor=='3') img = new ImageIcon(getClass().getResource("/Board/floor3.png"));
                else img = new ImageIcon(getClass().getResource("/Board/floor4.png"));
                break;
        }

        img1 = img.getImage() ;
        newimg = img1.getScaledInstance( 80, 80,  java.awt.Image.SCALE_SMOOTH ) ;
        img = new ImageIcon( newimg );

        button.setName(""+player+floor);
        button.setIcon(img);
        button.setOpaque(false);
        button.setBorder(null);
        button.setContentAreaFilled(false);
    }
}
