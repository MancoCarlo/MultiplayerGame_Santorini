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
        optionPanel=new ImagePanel("/back.png", getWidth(), getHeight());
        options = new ArrayList<>();
        optionPanel.setLayout(new GridLayout(3,3));
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
                if(g.getName().contains("Apollo")) {
                    JPanel ap = new JPanel();
                    ap.setOpaque(false);
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
                    ap.add(buttonA);
                    optionPanel.add(ap);
                }
                if(g.getName().contains("Arthemis")) {
                    JPanel ap = new JPanel();
                    ap.setOpaque(false);
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
                    ap.add(buttonA);
                    optionPanel.add(ap);
                }
                if(g.getName().contains("Athena")) {
                    JPanel ap = new JPanel();
                    ap.setOpaque(false);
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
                    ap.add(buttonA);
                    optionPanel.add(ap);
                }
                if(g.getName().contains("Atlas")) {
                    JPanel ap = new JPanel();
                    ap.setOpaque(false);
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
                    ap.add(buttonA);
                    optionPanel.add(ap);
                }
                if(g.getName().contains("Demeter")) {
                    JPanel ap = new JPanel();
                    ap.setOpaque(false);
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
                    ap.add(buttonA);
                    optionPanel.add(ap);
                }
                if(g.getName().contains("Hephaestus")) {
                    JPanel ap = new JPanel();
                    ap.setOpaque(false);
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
                    ap.add(buttonA);
                    optionPanel.add(ap);
                }
                if(g.getName().contains("Minotaur")) {
                    JPanel ap = new JPanel();
                    ap.setOpaque(false);
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
                    ap.add(buttonA);
                    optionPanel.add(ap);
                }
                if(g.getName().contains("Pan")) {
                    JPanel ap = new JPanel();
                    ap.setOpaque(false);
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
                    ap.add(buttonA);
                    optionPanel.add(ap);
                }
                if(g.getName().contains("Prometheus")) {
                    JPanel ap = new JPanel();
                    ap.setOpaque(false);
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
                    ap.add(buttonA);
                    optionPanel.add(ap);
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

}
