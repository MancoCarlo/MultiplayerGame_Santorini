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
        optionPanel.setOpaque(false);
        optionPanel.setLayout(new GridLayout(3,3, 10, 10));
    }

    public synchronized void setOptions() {
        if(!list.get(0).substring(1,2).equals(")")){
            list.remove(0);
        }

        if(isGodList(list)){
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
                if(g.getName().contains("Apollo")) {
                    ImageIcon img = new ImageIcon(getClass().getResource("/god/01.png"));
                    Image img1 = img.getImage() ;
                    Image newimg = img1.getScaledInstance( 100, 175,  java.awt.Image.SCALE_SMOOTH ) ;
                    img = new ImageIcon( newimg );
                    JButton buttonA = new JButton("");
                    buttonA.setIcon(img);
                    buttonA.setOpaque(false);
                    buttonA.setBorder(null);
                    buttonA.setContentAreaFilled(false);
                    buttonA.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            index = g.getID();
                            indexObtained = true;
                        }
                    });
                    optionPanel.add(buttonA);
                }
                if(g.getName().contains("Arthemis")) {
                    ImageIcon img = new ImageIcon(getClass().getResource("/god/02.png"));
                    Image img1 = img.getImage() ;
                    Image newimg = img1.getScaledInstance( 100, 175,  java.awt.Image.SCALE_SMOOTH ) ;
                    img = new ImageIcon( newimg );
                    JButton buttonA = new JButton("");
                    buttonA.setIcon(img);
                    buttonA.setOpaque(false);
                    buttonA.setBorder(null);
                    buttonA.setContentAreaFilled(false);
                    buttonA.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            index = g.getID();
                            indexObtained = true;
                        }
                    });
                    optionPanel.add(buttonA);
                }
                if(g.getName().contains("Athena")) {
                    ImageIcon img = new ImageIcon(getClass().getResource("/god/03.png"));
                    Image img1 = img.getImage() ;
                    Image newimg = img1.getScaledInstance( 100, 175,  java.awt.Image.SCALE_SMOOTH ) ;
                    img = new ImageIcon( newimg );
                    JButton buttonA = new JButton("");
                    buttonA.setIcon(img);
                    buttonA.setOpaque(false);
                    buttonA.setBorder(null);
                    buttonA.setContentAreaFilled(false);
                    buttonA.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            index = g.getID();
                            indexObtained = true;
                        }
                    });
                    optionPanel.add(buttonA);
                }
                if(g.getName().contains("Atlas")) {
                    ImageIcon img = new ImageIcon(getClass().getResource("/god/04.png"));
                    Image img1 = img.getImage() ;
                    Image newimg = img1.getScaledInstance( 100, 175,  java.awt.Image.SCALE_SMOOTH ) ;
                    img = new ImageIcon( newimg );
                    JButton buttonA = new JButton("");
                    buttonA.setIcon(img);
                    buttonA.setOpaque(false);
                    buttonA.setBorder(null);
                    buttonA.setContentAreaFilled(false);
                    buttonA.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            index = g.getID();
                            indexObtained = true;
                        }
                    });
                    optionPanel.add(buttonA);
                }
                if(g.getName().contains("Demeter")) {
                    ImageIcon img = new ImageIcon(getClass().getResource("/god/05.png"));
                    Image img1 = img.getImage() ;
                    Image newimg = img1.getScaledInstance( 100, 175,  java.awt.Image.SCALE_SMOOTH ) ;
                    img = new ImageIcon( newimg );
                    JButton buttonA = new JButton("");
                    buttonA.setIcon(img);
                    buttonA.setOpaque(false);
                    buttonA.setBorder(null);
                    buttonA.setContentAreaFilled(false);
                    buttonA.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            index = g.getID();
                            indexObtained = true;
                        }
                    });
                    optionPanel.add(buttonA);
                }
                if(g.getName().contains("Hephaestus")) {
                    ImageIcon img = new ImageIcon(getClass().getResource("/god/06.png"));
                    Image img1 = img.getImage() ;
                    Image newimg = img1.getScaledInstance( 100, 175,  java.awt.Image.SCALE_SMOOTH ) ;
                    img = new ImageIcon( newimg );
                    JButton buttonA = new JButton("");
                    buttonA.setIcon(img);
                    buttonA.setOpaque(false);
                    buttonA.setBorder(null);
                    buttonA.setContentAreaFilled(false);
                    buttonA.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            index = g.getID();
                            indexObtained = true;
                        }
                    });
                    optionPanel.add(buttonA);
                }
                if(g.getName().contains("Minotaur")) {
                    ImageIcon img = new ImageIcon(getClass().getResource("/god/08.png"));
                    Image img1 = img.getImage() ;
                    Image newimg = img1.getScaledInstance( 100, 175,  java.awt.Image.SCALE_SMOOTH ) ;
                    img = new ImageIcon( newimg );
                    JButton buttonA = new JButton("");
                    buttonA.setIcon(img);
                    buttonA.setOpaque(false);
                    buttonA.setBorder(null);
                    buttonA.setContentAreaFilled(false);
                    buttonA.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            index = g.getID();
                            indexObtained = true;
                        }
                    });
                    optionPanel.add(buttonA);
                }
                if(g.getName().contains("Pan")) {
                    ImageIcon img = new ImageIcon(getClass().getResource("/god/09.png"));
                    Image img1 = img.getImage() ;
                    Image newimg = img1.getScaledInstance( 100, 175,  java.awt.Image.SCALE_SMOOTH ) ;
                    img = new ImageIcon( newimg );
                    JButton buttonA = new JButton("");
                    buttonA.setIcon(img);
                    buttonA.setOpaque(false);
                    buttonA.setBorder(null);
                    buttonA.setContentAreaFilled(false);
                    buttonA.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            index = g.getID();
                            indexObtained = true;
                        }
                    });
                    optionPanel.add(buttonA);
                }
                if(g.getName().contains("Prometheus")) {
                    ImageIcon img = new ImageIcon(getClass().getResource("/god/10.png"));
                    Image img1 = img.getImage() ;
                    Image newimg = img1.getScaledInstance( 100, 175,  java.awt.Image.SCALE_SMOOTH ) ;
                    img = new ImageIcon( newimg );
                    JButton buttonA = new JButton("");
                    buttonA.setIcon(img);
                    buttonA.setOpaque(false);
                    buttonA.setBorder(null);
                    buttonA.setContentAreaFilled(false);
                    buttonA.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            index = g.getID();
                            indexObtained = true;
                        }
                    });
                    optionPanel.add(buttonA);
                }
                if(g.getName().contains("Hestia")) {
                    ImageIcon img = new ImageIcon(getClass().getResource("/god/21.png"));
                    Image img1 = img.getImage() ;
                    Image newimg = img1.getScaledInstance( 100, 175,  java.awt.Image.SCALE_SMOOTH ) ;
                    img = new ImageIcon( newimg );
                    JButton buttonA = new JButton("");
                    buttonA.setIcon(img);
                    buttonA.setOpaque(false);
                    buttonA.setBorder(null);
                    buttonA.setContentAreaFilled(false);
                    buttonA.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            index = g.getID();
                            indexObtained = true;
                        }
                    });
                    optionPanel.add(buttonA);
                }
                if(g.getName().contains("Poseidon")) {
                    ImageIcon img = new ImageIcon(getClass().getResource("/god/27.png"));
                    Image img1 = img.getImage() ;
                    Image newimg = img1.getScaledInstance( 100, 175,  java.awt.Image.SCALE_SMOOTH ) ;
                    img = new ImageIcon( newimg );
                    JButton buttonA = new JButton("");
                    buttonA.setIcon(img);
                    buttonA.setOpaque(false);
                    buttonA.setBorder(null);
                    buttonA.setContentAreaFilled(false);
                    buttonA.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            index = g.getID();
                            indexObtained = true;
                        }
                    });
                    optionPanel.add(buttonA);
                }
                if(g.getName().contains("Triton")) {
                    ImageIcon img = new ImageIcon(getClass().getResource("/god/29.png"));
                    Image img1 = img.getImage() ;
                    Image newimg = img1.getScaledInstance( 100, 175,  java.awt.Image.SCALE_SMOOTH ) ;
                    img = new ImageIcon( newimg );
                    JButton buttonA = new JButton("");
                    buttonA.setIcon(img);
                    buttonA.setOpaque(false);
                    buttonA.setBorder(null);
                    buttonA.setContentAreaFilled(false);
                    buttonA.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            index = g.getID();
                            indexObtained = true;
                        }
                    });
                    optionPanel.add(buttonA);
                }
                if(g.getName().contains("Charon")) {
                    ImageIcon img = new ImageIcon(getClass().getResource("/god/15.png"));
                    Image img1 = img.getImage() ;
                    Image newimg = img1.getScaledInstance( 100, 175,  java.awt.Image.SCALE_SMOOTH ) ;
                    img = new ImageIcon( newimg );
                    JButton buttonA = new JButton("");
                    buttonA.setIcon(img);
                    buttonA.setOpaque(false);
                    buttonA.setBorder(null);
                    buttonA.setContentAreaFilled(false);
                    buttonA.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            index = g.getID();
                            indexObtained = true;
                        }
                    });
                    optionPanel.add(buttonA);
                }
                if(g.getName().contains("Zeus")) {
                    ImageIcon img = new ImageIcon(getClass().getResource("/god/30.png"));
                    Image img1 = img.getImage() ;
                    Image newimg = img1.getScaledInstance( 100, 175,  java.awt.Image.SCALE_SMOOTH ) ;
                    img = new ImageIcon( newimg );
                    JButton buttonA = new JButton("");
                    buttonA.setIcon(img);
                    buttonA.setOpaque(false);
                    buttonA.setBorder(null);
                    buttonA.setContentAreaFilled(false);
                    buttonA.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            index = g.getID();
                            indexObtained = true;
                        }
                    });
                    optionPanel.add(buttonA);
                }
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
        optionPanel.setOpaque(false);
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

    public boolean isGodList(ArrayList<String> list){
        if(list.get(0).contains("Apollo")) return true;
        if(list.get(0).contains("Arthemis")) return true;
        if(list.get(0).contains("Athena")) return true;
        if(list.get(0).contains("Atlas")) return true;
        if(list.get(0).contains("Demeter")) return true;
        if(list.get(0).contains("Hephaestus")) return true;
        if(list.get(0).contains("Minotaur")) return true;
        if(list.get(0).contains("Pan")) return true;
        if(list.get(0).contains("Prometheus")) return true;
        if(list.get(0).contains("Hestia")) return true;
        if(list.get(0).contains("Poseidon")) return true;
        if(list.get(0).contains("Triton")) return true;
        if(list.get(0).contains("Charon")) return true;
        if(list.get(0).contains("Zeus")) return true;
        return false;
    }

}
