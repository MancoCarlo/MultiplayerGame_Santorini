package it.polimi.ingsw.PSP29.view;

import it.polimi.ingsw.PSP29.model.God;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class IndexGUI extends JFrame implements Runnable{
    private JLabel text;
    private ArrayList<String> list;
    private JPanel optionPanel;
    private ArrayList<JButton> options;
    private int index;
    private boolean indexObtained;

    @Override
    public void run() {
        this.setOptions();
    }

    public IndexGUI(ArrayList<String> list, String command){
        this.setTitle("Choose Index");
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(close());
        this.list=list;
        indexObtained=false;
        this.setLayout(new BorderLayout());
        text=new JLabel(command);
        optionPanel=new JPanel();
        options = new ArrayList<>();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
    }

    public synchronized void setOptions() {
        if(!list.get(0).substring(1,2).equals(")")){
            list.remove(0);
        }
        if(list.size()>2){
            ArrayList<God> gods = new ArrayList<>();
            for(int i = 0; i<list.size(); i++){
                String name = "";
                String anal = list.get(i);
                while(anal.charAt(0) != ','){
                    name = name + anal.charAt(0);
                    anal = anal.substring(1);
                }
                anal = anal.substring(1);
                String des = anal;
                God g = new God(i+1, name, des);
                gods.add(g);
            }
            for(final God g : gods){
                final JButton button = new JButton(g.getName());
                button.setBackground(Color.LIGHT_GRAY);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        index=g.getID();
                        indexObtained=true;
                    }
                });
                options.add(button);
                optionPanel.add(button);
            }
        }else {
            for (String str : list) {
                final JButton button = new JButton(str);
                button.setBackground(Color.LIGHT_GRAY);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        index = getPosition(button);
                        System.out.println(index);
                        indexObtained = true;
                    }
                });
                options.add(button);
                optionPanel.add(button);
            }
        }
        this.add(text, BorderLayout.NORTH);
        this.add(optionPanel, BorderLayout.CENTER);
        pack();
        this.setVisible(true);
    }

    public synchronized int getPosition(JButton button){
        int i;
        for(i=0; i<options.size(); i++){
            if(button.equals(options.get(i))){
                break;
            }
        }
        return i+1;
    }

    public synchronized boolean isIndexObtained(){
        return indexObtained;
    }

    public synchronized int getIndex(){
        return index;
    }

    public synchronized int close(){
        index=1;
        indexObtained=true;
        return 0;
    }

}
