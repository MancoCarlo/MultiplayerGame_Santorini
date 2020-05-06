package it.polimi.ingsw.PSP29.view;

import it.polimi.ingsw.PSP29.model.Player;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

public class GUI extends JFrame{


    private String message;
    private boolean sentMessage = false;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel mainPanel;
    private String lastViewCenter;

    public GUI(){
        mainPanel = new JPanel();
        topPanel = new JPanel();
        bottomPanel = new JPanel();
        rightPanel = new JPanel();
        leftPanel = new JPanel();
        centerPanel = new JPanel();

        JLabel text = new JLabel("Welcome to Santorini");
        centerPanel.setLayout(new GridLayout());
        centerPanel.add(text);

        mainPanel.add(centerPanel);

        this.add(mainPanel);
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(400, 300));
        this.pack();
        this.setVisible(true);

    }
    /*
         private Commands nextCommand;
        private String command;

        private enum Commands {
            LOGN,
            LOGE,
            STOP
        }

        public synchronized void loginN(String cmd)
        {
            nextCommand = Commands.LOGN;
            command = cmd;
            notifyAll();
        }

        public synchronized void processGUI(){
            while (true) {
                nextCommand = null;

                try {
                    wait();
                } catch (InterruptedException e) { }

                if (nextCommand == null)
                    continue;

                switch (nextCommand) {
                    case LOGN:
                        doLoginN();
                        break;

                    case LOGE:
                        break;

                    case STOP:
                        return;
                }
            }
        }
    */
    public synchronized void doLogin(String command) {
        lastViewCenter="login";
        mainPanel.setVisible(false);
        centerPanel.setVisible(false);
        mainPanel.remove(centerPanel);
        centerPanel.removeAll();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(command);
        final JTextField mex = new JTextField();
        mex.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        JButton button = new JButton("SEND");
        centerPanel.add(label);
        centerPanel.add(mex);
        centerPanel.add(button);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        mex.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(centerPanel);
        this.add(mainPanel);
        //this.pack();
        centerPanel.setVisible(true);
        mainPanel.setVisible(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!mex.getText().equals("")) {
                    message = mex.getText();
                    sentMessage = true;
                }
            }
        });
    }

    public synchronized void doLobby(String command) {
        lastViewCenter="lobby";
        mainPanel.setVisible(false);
        centerPanel.setVisible(false);
        mainPanel.remove(centerPanel);
        centerPanel.removeAll();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(command);
        final JRadioButton but1 = new JRadioButton("2");
        final JRadioButton but2 = new JRadioButton("3");
        ButtonGroup buttonG = new ButtonGroup();
        but1.setSelected(true);
        buttonG.add(but1);
        buttonG.add(but2);
        JPanel buttons = new JPanel(new GridLayout(1, 2));
        buttons.add(but1);
        buttons.add(but2);
        buttons.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
        JButton button = new JButton("SEND");
        centerPanel.add(label);
        centerPanel.add(buttons);
        centerPanel.add(button);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(centerPanel);
        this.add(mainPanel);
        this.pack();
        centerPanel.setVisible(true);
        mainPanel.setVisible(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(but1.isSelected()){
                    message = "2";
                }else{
                    message = "3";
                }
                sentMessage = true;
            }
        });
    }

    public synchronized void viewMessage(String command){
        mainPanel.setVisible(false);
        topPanel.setVisible(false);
        mainPanel.remove(topPanel);
        if(!lastViewCenter.equals("board")){
            centerPanel.setVisible(false);
            mainPanel.remove(centerPanel);
            centerPanel.removeAll();
        }
        topPanel.removeAll();
        topPanel.setLayout(new FlowLayout());
        JLabel label = new JLabel(command);
        topPanel.add(label);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        this.add(mainPanel);
        this.pack();
        topPanel.setVisible(true);
        mainPanel.setVisible(true);
        sentMessage=true;
    }

    public synchronized void viewBoard(String command){
        if(!lastViewCenter.equals("board")){
            createGameGui();
        }
        lastViewCenter="board";
        mainPanel.setVisible(false);
        topPanel.setVisible(false);
        centerPanel.setVisible(false);

        mainPanel.remove(topPanel);
        topPanel.removeAll();
        JLabel text = new JLabel("BOARD");
        topPanel.add(text);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.remove(centerPanel);
        centerPanel.removeAll();
        centerPanel.setLayout(new BorderLayout(20, 20));
        JPanel Y = new JPanel(new GridLayout(1, 6, 20, 0));
        JPanel X = new JPanel(new GridLayout(5, 1));
        JPanel B = new JPanel(new GridLayout(5, 5));
        //centerPanel.setLayout(new GridLayout(6, 6));
        final JButton b00 = new JButton("0,0");
        final JButton b01 = new JButton("0,1");
        final JButton b02 = new JButton("0,2");
        final JButton b03 = new JButton("0,3");
        final JButton b04 = new JButton("0,4");
        final JButton b10 = new JButton("1,0");
        final JButton b11 = new JButton("1,1");
        final JButton b12 = new JButton("1,2");
        final JButton b13 = new JButton("1,3");
        final JButton b14 = new JButton("1,4");
        final JButton b20 = new JButton("2,0");
        final JButton b21 = new JButton("2,1");
        final JButton b22 = new JButton("2,2");
        final JButton b23 = new JButton("2,3");
        final JButton b24 = new JButton("2,4");
        final JButton b30 = new JButton("3,0");
        final JButton b31 = new JButton("3,1");
        final JButton b32 = new JButton("3,2");
        final JButton b33 = new JButton("3,3");
        final JButton b34 = new JButton("3,4");
        final JButton b40 = new JButton("4,0");
        final JButton b41 = new JButton("4,1");
        final JButton b42 = new JButton("4,2");
        final JButton b43 = new JButton("4,3");
        final JButton b44 = new JButton("4,4");

        B.add(b00);
        B.add(b01);
        B.add(b02);
        B.add(b03);
        B.add(b04);
        B.add(b10);
        B.add(b11);
        B.add(b12);
        B.add(b13);
        B.add(b14);
        B.add(b20);
        B.add(b21);
        B.add(b22);
        B.add(b23);
        B.add(b24);
        B.add(b30);
        B.add(b31);
        B.add(b32);
        B.add(b33);
        B.add(b34);
        B.add(b40);
        B.add(b41);
        B.add(b42);
        B.add(b43);
        B.add(b44);

        /*
        b00.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b00.getText());
            }
        });
        b01.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b01.getText());
            }
        });
        b02.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b02.getText());
            }
        });
        b03.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b03.getText());
            }
        });
        b04.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b04.getText());
            }
        });
        b10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b10.getText());
            }
        });
        b11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b11.getText());
            }
        });
        b12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b12.getText());
            }
        });
        b13.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b13.getText());
            }
        });
        b14.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b14.getText());
            }
        });
        b20.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b20.getText());
            }
        });
        b21.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b21.getText());
            }
        });
        b22.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b22.getText());
            }
        });
        b23.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b23.getText());
            }
        });
        b24.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b24.getText());
            }
        });
        b30.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b30.getText());
            }
        });
        b31.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b31.getText());
            }
        });
        b32.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b32.getText());
            }
        });
        b33.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b33.getText());
            }
        });
        b34.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b34.getText());
            }
        });
        b40.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b40.getText());
            }
        });
        b41.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b41.getText());
            }
        });
        b42.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b42.getText());
            }
        });
        b43.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b43.getText());
            }
        });
        b44.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(b44.getText());
            }
        });
        */

        JLabel y[] = new JLabel[6];
        y[0] = new JLabel("X/Y\t");
        Y.add(y[0]);
        for(int i=1; i<6; i++){
            y[i]= new JLabel(i + "\t");
            Y.add(y[i]);
        }

        JLabel x[] = new JLabel[5];
        for(int i=0; i<5; i++){
            x[i]= new JLabel(i + "\t");
            X.add(x[i]);
        }

        centerPanel.add(Y, BorderLayout.NORTH);
        centerPanel.add(X, BorderLayout.WEST);
        centerPanel.add(B, BorderLayout.CENTER);


        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        this.add(mainPanel);
        this.setMinimumSize(new Dimension(500, 400));
        pack();
        topPanel.setVisible(true);
        centerPanel.setVisible(true);
        mainPanel.setVisible(true);
        this.setVisible(true);
        sentMessage=true;
    }

    public synchronized  void createGameGui(){
        mainPanel.setVisible(false);
        mainPanel.removeAll();
        topPanel.setVisible(false);
        centerPanel.setVisible(false);
        topPanel.removeAll();
        centerPanel.removeAll();

        JButton top = new JButton("top");
        JButton bottom = new JButton("bottom");
        JButton right = new JButton("dx");
        JButton left = new JButton("sx");
        JButton center = new JButton("center");

        topPanel.add(top);
        bottomPanel.add(bottom);
        rightPanel.add(right);
        leftPanel.add(left);
        centerPanel.add(center);

        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        pack();

        topPanel.setVisible(true);
        centerPanel.setVisible(true);

        mainPanel.setVisible(true);
    }

    public void viewListPlayer(String list){
        char c;
        String name="";
        String age="";
        ArrayList<Player> players = new ArrayList<>();
        int j=5;
        while (j<list.length()){
            c=list.charAt(j);
            System.out.println(c);
            if(c!='-'){
                j++;
            }
            else{
                j=j+2;
                c=list.charAt(j);
                while(c!=','){
                    name=name + c;
                    j++;
                    c=list.charAt(j);
                }
                j=j+2;
                c=list.charAt(j);
                while(c!=' '){
                    age=age + c;
                    j++;
                    c=list.charAt(j);
                }
                System.out.println(name);
                System.out.println(age);
                players.add(new Player(name, Integer.parseInt(age)));
                name="";
                age="";
            }
        }

        mainPanel.setVisible(false);
        mainPanel.remove(leftPanel);
        leftPanel.setVisible(false);
        leftPanel.removeAll();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Players:");
        leftPanel.add(title);
        JLabel text[] = new JLabel[players.size()];
        for(int i=0; i<players.size(); i++){
            text[i] = new JLabel(players.get(i).getNickname() + ", " + players.get(i).getAge());
            leftPanel.add(text[i]);
        }
        mainPanel.add(leftPanel);
        pack();
        leftPanel.setVisible(true);
        mainPanel.setVisible(true);
        sentMessage=true;
    }


    public synchronized String getMessage(){
        return message;
    }

    public synchronized boolean didSentMessage(){
        return sentMessage;
    }

    public synchronized void resetSentMessage(){
        sentMessage = false;
    }
}