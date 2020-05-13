package it.polimi.ingsw.PSP29.view;

import it.polimi.ingsw.PSP29.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class GUI extends JFrame implements Runnable{


    private String message;
    private boolean sentMessage = false;
    private boolean GuiLoaded = false;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel mainPanel;
    private String lastViewCenter;

    private ArrayList<String> list;

    private Commands nextCommand;
    private String command;

    private BoardGUI B;

    public boolean getGuiLoaded() {
        return GuiLoaded;
    }

    @Override
    public void run() {

        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        topPanel = new JPanel();
        bottomPanel = new ImagePanel("/bottomPanel.png", getWidth(), getHeight());
        rightPanel = new JPanel();
        leftPanel = new JPanel();
        centerPanel = new JPanel();
        mainPanel = new JPanel();

        mainPanel.add(centerPanel);

        this.add(mainPanel);
        this.setTitle("SANTORINI the GAME");
        this.setPreferredSize(new Dimension(640, 640));
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
        initialPage();
        processGUI();
    }

    private enum Commands {
        LOG,
        LOBBY,
        BOARD,
        LISTP,
        LIST,
        MESSAGE,
        INDEX,
        COORDINATE,
        TURN,
        GOD,
        STOP
    }

    public synchronized void login(String cmd)
    {
        nextCommand = Commands.LOG;
        command = cmd;
        notifyAll();
    }

    public synchronized void lobby(String cmd)
    {
        nextCommand = Commands.LOBBY;
        command = cmd;
        notifyAll();
    }

    public synchronized void index(String cmd)
    {
        nextCommand = Commands.INDEX;
        command = cmd;
        notifyAll();
    }

    public synchronized void board(String cmd)
    {
        nextCommand = Commands.BOARD;
        command = cmd;
        notifyAll();
    }

    public synchronized void listPlayers(String cmd)
    {
        nextCommand = Commands.LISTP;
        command = cmd;
        notifyAll();
    }

    public synchronized void list(String cmd)
    {
        nextCommand = Commands.LIST;
        command = cmd;
        notifyAll();
    }

    public synchronized void message(String cmd)
    {
        nextCommand = Commands.MESSAGE;
        command = cmd;
        notifyAll();
    }

    public synchronized void turn(String cmd)
    {
        nextCommand = Commands.TURN;
        command = cmd.substring(5);
        notifyAll();
    }

    public synchronized void coordinate(String cmd)
    {
        nextCommand = Commands.COORDINATE;
        command = "Click coordinate on the board to put the worker: ";
        notifyAll();
    }

    public synchronized void viewGod(String cmd)
    {
        nextCommand = Commands.GOD;
        command = cmd;
        notifyAll();
    }

    public synchronized void processGUI(){
        GuiLoaded=true;
        while (true) {
            nextCommand = null;

            try {
                wait();
            } catch (InterruptedException e) { }

            if (nextCommand == null)
                continue;

            switch (nextCommand) {

                case LOG:
                    doLogin();
                    break;

                case LOBBY:
                    doLobby();
                    break;

                case BOARD:
                    viewBoard();
                    break;

                case LISTP:
                    viewListPlayer();
                    break;

                case LIST:
                    viewList();
                    break;

                case MESSAGE:
                    viewMessage();
                    break;

                case INDEX:
                    viewIndex();
                    break;

                case COORDINATE:
                    doCoordinate();
                    break;

                case TURN:
                    doTurn();
                    break;

                case GOD:
                    viewIconGod();
                    break;

                case STOP:
                    return;
            }
        }
    }

    public synchronized void initialPage(){
        lastViewCenter="initial";
        mainPanel.setVisible(false);
        centerPanel.setVisible(false);
        mainPanel.remove(centerPanel);
        centerPanel.removeAll();
        centerPanel = new ImagePanel("/login.png", this.getWidth()-10, this.getHeight()-35);
        centerPanel.setLayout(new GridLayout(3,3));
        JPanel fake1 = new JPanel();
        JPanel fake2 = new JPanel();
        JPanel fake3 = new JPanel();
        JPanel fake4 = new JPanel();
        JPanel fake6 = new JPanel();
        JPanel fake7 = new JPanel();
        JPanel fake8 = new JPanel();
        JPanel fake9 = new JPanel();
        ImagePanel cen = new ImagePanel("/form.png", this.getWidth()/3, this.getHeight()/3);

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        cen.setLayout(gridbag);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel label1;
        JLabel label2;
        if(command != null){
            label1 = new JLabel("");
            label2 = new JLabel(command.substring(5));
        }else {
            label1 = new JLabel("Welcome to Santorini!");
            label2 = new JLabel("Wait for a lobby");
        }

        c.gridx = 1;
        c.gridy = 0;
        gridbag.setConstraints(label1, c);
        cen.add(label1);

        c.gridx = 1;
        c.gridy = 1;
        gridbag.setConstraints(label2, c);
        cen.add(label2);

        fake1.setOpaque(false);
        fake2.setOpaque(false);
        fake3.setOpaque(false);
        fake4.setOpaque(false);
        fake6.setOpaque(false);
        fake7.setOpaque(false);
        fake8.setOpaque(false);
        fake9.setOpaque(false);

        centerPanel.add(fake1);
        centerPanel.add(fake2);
        centerPanel.add(fake3);
        centerPanel.add(fake4);
        centerPanel.add(cen);
        centerPanel.add(fake6);
        centerPanel.add(fake7);
        centerPanel.add(fake8);
        centerPanel.add(fake9);

        this.getContentPane().add(centerPanel);
        this.pack();
        centerPanel.setVisible(true);
    }


    public synchronized void doLogin() {
        lastViewCenter="login";
        mainPanel.setVisible(false);
        centerPanel.setVisible(false);
        mainPanel.remove(centerPanel);
        centerPanel.removeAll();
        centerPanel = new ImagePanel("/login.png", this.getWidth()-10, this.getHeight()-35);
        centerPanel.setLayout(new GridLayout(3,3));
        JPanel fake1 = new JPanel();
        JPanel fake2 = new JPanel();
        JPanel fake3 = new JPanel();
        JPanel fake4 = new JPanel();
        JPanel fake6 = new JPanel();
        JPanel fake7 = new JPanel();
        JPanel fake8 = new JPanel();
        JPanel fake9 = new JPanel();
        ImagePanel cen = new ImagePanel("/form.png", this.getWidth()/3, this.getHeight()/3);

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        cen.setLayout(gridbag);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel(command.substring(5));
        final JTextField mex = new JTextField();
        mex.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        JButton button = new JButton("SEND");

        fake1.setOpaque(false);
        fake2.setOpaque(false);
        fake3.setOpaque(false);
        fake4.setOpaque(false);
        fake6.setOpaque(false);
        fake7.setOpaque(false);
        fake8.setOpaque(false);
        fake9.setOpaque(false);
        label.setAlignmentX(CENTER_ALIGNMENT);
        mex.setAlignmentX(CENTER_ALIGNMENT);
        button.setAlignmentX(CENTER_ALIGNMENT);
        label.setAlignmentY(CENTER_ALIGNMENT);
        mex.setAlignmentY(CENTER_ALIGNMENT);
        button.setAlignmentY(CENTER_ALIGNMENT);

        c.gridx = 1;
        c.gridy = 0;
        gridbag.setConstraints(label, c);
        cen.add(label);

        c.gridx = 1;
        c.gridy = 1;
        gridbag.setConstraints(mex, c);
        cen.add(mex);

        c.gridx = 1;
        c.gridy = 2;
        gridbag.setConstraints(button, c);
        cen.add(button);

        centerPanel.add(fake1);
        centerPanel.add(fake2);
        centerPanel.add(fake3);
        centerPanel.add(fake4);
        centerPanel.add(cen);
        centerPanel.add(fake6);
        centerPanel.add(fake7);
        centerPanel.add(fake8);
        centerPanel.add(fake9);

        this.getContentPane().add(centerPanel);
        this.pack();
        centerPanel.setVisible(true);

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

    public synchronized void doLobby() {
        lastViewCenter="lobby";
        mainPanel.setVisible(false);
        centerPanel.setVisible(false);
        mainPanel.remove(centerPanel);
        centerPanel.removeAll();
        centerPanel = new ImagePanel("/login.png", this.getWidth()-10, this.getHeight()-35);
        centerPanel.setLayout(new GridLayout(3,3));
        JPanel fake1 = new JPanel();
        JPanel fake2 = new JPanel();
        JPanel fake3 = new JPanel();
        JPanel fake4 = new JPanel();
        JPanel fake6 = new JPanel();
        JPanel fake7 = new JPanel();
        JPanel fake8 = new JPanel();
        JPanel fake9 = new JPanel();
        ImagePanel cen = new ImagePanel("/form.png", this.getWidth()/3, this.getHeight()/3);

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        cen.setLayout(gridbag);
        c.fill = GridBagConstraints.HORIZONTAL;

        fake1.setOpaque(false);
        fake2.setOpaque(false);
        fake3.setOpaque(false);
        fake4.setOpaque(false);
        fake6.setOpaque(false);
        fake7.setOpaque(false);
        fake8.setOpaque(false);
        fake9.setOpaque(false);

        JLabel label = new JLabel(command.substring(5));
        final JRadioButton but1 = new JRadioButton("2");
        final JRadioButton but2 = new JRadioButton("3");
        ButtonGroup buttonG = new ButtonGroup();
        but1.setSelected(true);
        buttonG.add(but1);
        buttonG.add(but2);
        but1.setOpaque(false);
        but2.setOpaque(false);
        JButton button = new JButton("SEND");

        JPanel radio = new JPanel(new GridLayout(1,2));
        radio.setOpaque(false);

        radio.add(but1);
        radio.add(but2);

        c.gridx = 1;
        c.gridy = 0;
        gridbag.setConstraints(label, c);
        cen.add(label);

        c.gridx = 1;
        c.gridy = 1;
        gridbag.setConstraints(radio, c);
        cen.add(radio);

        c.gridx = 1;
        c.gridy = 2;
        gridbag.setConstraints(button, c);
        cen.add(button);

        centerPanel.add(fake1);
        centerPanel.add(fake2);
        centerPanel.add(fake3);
        centerPanel.add(fake4);
        centerPanel.add(cen);
        centerPanel.add(fake6);
        centerPanel.add(fake7);
        centerPanel.add(fake8);
        centerPanel.add(fake9);
        this.getContentPane().add(centerPanel);
        this.pack();
        centerPanel.setVisible(true);

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

    public synchronized void viewMessage(){
        mainPanel.setVisible(false);
        topPanel.setVisible(false);
        topPanel.removeAll();
        mainPanel.remove(topPanel);

        if(!lastViewCenter.equals("board")){
            centerPanel.setVisible(false);
            mainPanel.remove(centerPanel);
            centerPanel.removeAll();
            initialPage();
            sentMessage = true;
        }
        else{
            topPanel = new ImagePanel("/top.png", getWidth(), getHeight());
            topPanel.setLayout(new GridLayout(5,1));

            JPanel fake1 = new JPanel();
            JPanel fake2 = new JPanel();
            JPanel fake3 = new JPanel();
            JPanel correct = new JPanel();
            JPanel fake4 = new JPanel();

            fake1.setOpaque(false);
            fake2.setOpaque(false);
            fake3.setOpaque(false);
            fake4.setOpaque(false);
            correct.setOpaque(false);

            GridBagLayout gridbag = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();
            correct.setLayout(gridbag);
            c.fill = GridBagConstraints.HORIZONTAL;

            JLabel label = new JLabel(command.substring(5));

            c.gridx = 1;
            c.gridy = 0;
            gridbag.setConstraints(label, c);
            correct.add(label);

            topPanel.add(fake1);
            topPanel.add(fake2);
            topPanel.add(fake3);
            topPanel.add(correct);
            topPanel.add(fake4);

            mainPanel.add(topPanel, BorderLayout.NORTH);
            this.add(mainPanel);
            this.pack();
            topPanel.setVisible(true);
            mainPanel.setVisible(true);
            sentMessage=true;
        }
    }

    public synchronized void viewIndex(){
        if(list==null){
            list.add("1) Yes");
            list.add("2) No");
        }
        command = command.substring(4);
        IndexGUI indexGUI = new IndexGUI(list, command.substring(1));
        Thread indexThread = new Thread(indexGUI);
        indexThread.start();
        while (!indexGUI.isIndexObtained());
        message = "" +indexGUI.getIndex();
        indexGUI.setVisible(false);
        sentMessage=true;
    }

    public synchronized void doTurn() {
        mainPanel.setVisible(false);
        topPanel.setVisible(false);
        mainPanel.remove(topPanel);
        if (!lastViewCenter.equals("board")) {
            centerPanel.setVisible(false);
            mainPanel.remove(centerPanel);
            centerPanel.removeAll();
        }
        topPanel.removeAll();

        JPanel fake1 = new JPanel();
        JPanel fake2 = new JPanel();
        JPanel fake3 = new JPanel();
        JPanel correct = new JPanel();
        JPanel fake4 = new JPanel();

        fake1.setOpaque(false);
        fake2.setOpaque(false);
        fake3.setOpaque(false);
        fake4.setOpaque(false);
        correct.setOpaque(false);

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        correct.setLayout(gridbag);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel(command);

        c.gridx = 1;
        c.gridy = 0;
        gridbag.setConstraints(label, c);
        correct.add(label);

        topPanel.add(fake1);
        topPanel.add(fake2);
        topPanel.add(fake3);
        topPanel.add(correct);
        topPanel.add(fake4);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        this.add(mainPanel);
        this.pack();
        final ArrayList<Integer> indexes = new ArrayList<>();
        for(int i = 0; i<list.size();i++){
            String a = list.get(i).substring(3,4);
            String b = list.get(i).substring(5);
            int x = Integer.parseInt(a);
            int y = Integer.parseInt(b);
            indexes.add(reverseConvert(x,y));
        }
        centerPanel.setVisible(false);
        for(Integer i : indexes){
            setButtonBackground(B.getButtons().get(i), B.getButtons().get(i).getName().charAt(1));
        }
        for (final JButton b : B.getButtons()) {
            for(Integer i : indexes){
                if(i == B.getCoordinate(b)){
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int id = B.getCoordinate(b);
                            message = getIndex(indexes, id);
                            sentMessage = true;
                            for (final JButton b : B.getButtons()){
                                b.removeActionListener(this);
                            }
                        }
                    });
                }
            }
        }
        centerPanel.revalidate();
        centerPanel.setVisible(true);
        topPanel.setVisible(true);
        mainPanel.setVisible(true);
    }

    public void setButtonBackground(JButton button, char level){
        ImageIcon img;
        Image img1;
        Image newimg;
        switch (level){
            case '0':
                img = new ImageIcon(getClass().getResource("/Board/floor0ok.png"));
                break;

            case '1':
                img = new ImageIcon(getClass().getResource("/Board/floor1ok.png"));
                break;

            case '2':
                img = new ImageIcon(getClass().getResource("/Board/floor2ok.png"));
                break;

            case '3':
                img = new ImageIcon(getClass().getResource("/Board/floor3ok.png"));
                break;

            default:
                img = new ImageIcon(getClass().getResource("/Board/floor0ok.png"));
                break;
        }

        img1 = img.getImage() ;
        newimg = img1.getScaledInstance( 80, 80,  java.awt.Image.SCALE_SMOOTH ) ;
        img = new ImageIcon( newimg );

        button.setText("");
        button.setIcon(img);
        button.setOpaque(false);
        button.setBorder(null);
        button.setContentAreaFilled(false);
    }

    public String getIndex(ArrayList<Integer> indexes, int id){
        int result = 0;
        for(int i=0; i<indexes.size();i++){
            if(id == indexes.get(i)){
                result = i;
                break;
            }
        }
        return ""+result;
    }

    public synchronized void doCoordinate() {
        mainPanel.setVisible(false);
        topPanel.setVisible(false);
        mainPanel.remove(topPanel);
        if (!lastViewCenter.equals("board")) {
            centerPanel.setVisible(false);
            mainPanel.remove(centerPanel);
            centerPanel.removeAll();
        }
        topPanel.removeAll();

        JPanel fake1 = new JPanel();
        JPanel fake2 = new JPanel();
        JPanel fake3 = new JPanel();
        JPanel fake4 = new JPanel();
        JPanel correct = new JPanel();

        fake1.setOpaque(false);
        fake2.setOpaque(false);
        fake3.setOpaque(false);
        fake4.setOpaque(false);
        correct.setOpaque(false);

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        correct.setLayout(gridbag);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel(command);

        c.gridx = 1;
        c.gridy = 0;
        gridbag.setConstraints(label, c);
        correct.add(label);

        topPanel.add(fake1);
        topPanel.add(fake2);
        topPanel.add(fake3);
        topPanel.add(correct);
        topPanel.add(fake4);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        this.add(mainPanel);
        this.pack();
        for (final JButton b : B.getButtons()) {
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int id = B.getCoordinate(b);
                    message = convert(id);
                    sentMessage = true;
                    for (final JButton b : B.getButtons()){
                        b.removeActionListener(this);
                    }
                }
            });
        }
        topPanel.setVisible(true);
        mainPanel.setVisible(true);
    }

    public synchronized void viewBoard(){
        B = new BoardGUI();
        if(!lastViewCenter.equals("board")){
            createGameGui();
        }
        lastViewCenter="board";
        mainPanel.setVisible(false);
        topPanel.setVisible(false);
        centerPanel.setVisible(false);

        mainPanel.setBackground(new Color(209, 198, 185));
        mainPanel.setOpaque(true);

        centerPanel.setOpaque(false);

        mainPanel.remove(topPanel);
        topPanel.removeAll();

        JLabel label = new JLabel("");

        JPanel fake1 = new JPanel();
        JPanel fake2 = new JPanel();
        JPanel fake3 = new JPanel();
        JPanel correct = new JPanel();
        JPanel fake4 = new JPanel();

        fake1.setOpaque(false);
        fake2.setOpaque(false);
        fake3.setOpaque(false);
        fake4.setOpaque(false);
        correct.setOpaque(false);

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        correct.setLayout(gridbag);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 1;
        c.gridy = 0;
        gridbag.setConstraints(label, c);
        correct.add(label);

        topPanel.add(fake1);
        topPanel.add(fake2);
        topPanel.add(fake3);
        topPanel.add(correct);
        topPanel.add(fake4);

        mainPanel.remove(centerPanel);
        centerPanel.removeAll();
        centerPanel.setLayout(new BorderLayout(10,10));
        JPanel Y = new JPanel(new GridLayout(1, 6, 50, 50));
        Y.setOpaque(false);
        JPanel X = new JPanel(new GridLayout(5, 1, 50, 50));
        X.setOpaque(false);
        B.setBoard(command);

        JLabel y[] = new JLabel[6];
        y[0] = new JLabel("X/Y\t");
        Y.add(y[0]);
        for(int i=1; i<6; i++){
            y[i]= new JLabel((i-1) + "\t");
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
        this.setMinimumSize(new Dimension(1000, 500));
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
        centerPanel = new JPanel();

        bottomPanel = new ImagePanel("/bottomPanel.png", this.getWidth()*2, this.getHeight()/6);
        JButton top = new JButton("top");
        JButton left = new JButton("sx");
        JButton center = new JButton("center");
        bottomPanel.setLayout(new GridLayout(3,1));

        JPanel fake1 = new JPanel();
        JPanel fake2 = new JPanel();
        fake1.setOpaque(false);
        fake2.setOpaque(false);

        JLabel label = new JLabel("Santorini The Game");

        bottomPanel.add(fake1);
        bottomPanel.add(label);
        bottomPanel.add(fake2);

        topPanel.add(top);
        leftPanel.add(left);
        centerPanel.add(center);

        mainPanel.setLayout(new BorderLayout(15,0));
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

    public void viewListPlayer(){
        ArrayList<Player> players = getPlayersFromList(command);

        mainPanel.setVisible(false);
        mainPanel.remove(leftPanel);
        leftPanel.setVisible(false);
        leftPanel.removeAll();

        JLabel title = new JLabel("  Players:             ");

        leftPanel = new ImagePanel("/form.png", 4*title.getWidth()+300, leftPanel.getHeight());
        leftPanel.setLayout(new GridLayout(4,2));

        JPanel fake = new JPanel();
        fake.setOpaque(false);

        JPanel fake2 = new JPanel();
        fake2.setOpaque(false);

        JPanel fake3 = new JPanel();
        fake3.setOpaque(false);

        JPanel fake4 = new JPanel();
        fake4.setOpaque(false);

        leftPanel.add(fake);

        JPanel listP = new JPanel();
        listP.setOpaque(false);

        if(players.size()==2)
            listP.setLayout(new GridLayout(6,1));
        else
            listP.setLayout(new GridLayout(9,1));

        leftPanel.add(fake2);
        leftPanel.add(fake3);
        leftPanel.add(title);
        leftPanel.add(fake4);
        JLabel[] text = new JLabel[players.size()*3];
        int j=0;
        for(int i=0; i<players.size(); i++){
            text[j] = new JLabel(players.get(i).getId()+") "+players.get(i).getNickname()+"\t");
            text[j].setBackground(new Color(224, 213, 200));
            text[j].setOpaque(true);
            listP.add(text[j]);
            j++;
            text[j] = new JLabel(players.get(i).getAge() + " years");
            text[j].setBackground(new Color(224, 213, 200));
            text[j].setOpaque(true);
            listP.add(text[j]);
            j++;
            text[j] = new JLabel("\n");
            listP.add(text[j]);
            j++;
        }
        leftPanel.add(listP);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        pack();
        leftPanel.setVisible(true);
        mainPanel.setVisible(true);

        sentMessage=true;
    }

    public void viewIconGod(){
        mainPanel.setVisible(false);
        mainPanel.remove(rightPanel);
        rightPanel.setVisible(false);
        rightPanel.removeAll();

        JLabel title = new JLabel("              Your god:          ");

        rightPanel = new ImagePanel("/rightPanel.png", 4*rightPanel.getWidth()+300, rightPanel.getHeight());

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        rightPanel.setLayout(gridbag);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 1;
        c.gridy = 0;
        gridbag.setConstraints(title, c);
        rightPanel.add(title);

        ImageIcon img;

        switch(command.substring(5)){
            case "Apollo":
                img = new ImageIcon(getClass().getResource("/god/01.png"));
                break;
            case "Arthemis":
                img = new ImageIcon(getClass().getResource("/god/02.png"));
                break;
            case "Athena":
                img = new ImageIcon(getClass().getResource("/god/03.png"));
                break;
            case "Atlas":
                img = new ImageIcon(getClass().getResource("/god/04.png"));
                break;
            case "Demeter":
                img = new ImageIcon(getClass().getResource("/god/05.png"));
                break;
            case "Hephaestus":
                img = new ImageIcon(getClass().getResource("/god/06.png"));
                break;
            case "Minotaur":
                img = new ImageIcon(getClass().getResource("/god/08.png"));
                break;
            case "Pan":
                img = new ImageIcon(getClass().getResource("/god/09.png"));
                break;
            case "Prometheus":
                img = new ImageIcon(getClass().getResource("/god/10.png"));
                break;
            case "Poseidon":
                img = new ImageIcon(getClass().getResource("/god/27.png"));
                break;
            case "Triton":
                img = new ImageIcon(getClass().getResource("/god/29.png"));
                break;
            case "Hestia":
                img = new ImageIcon(getClass().getResource("/god/21.png"));
                break;
            case "Charon":
                img = new ImageIcon(getClass().getResource("/god/15.png"));
                break;
            case "Zeus":
                img = new ImageIcon(getClass().getResource("/god/30.png"));
                break;
            default:
                img = new ImageIcon(getClass().getResource("/god/01.png"));
                break;
        }

        Image img1 = img.getImage() ;
        Image newimg = img1.getScaledInstance( 100, 175,  java.awt.Image.SCALE_SMOOTH ) ;
        img = new ImageIcon( newimg );

        JLabel god = new JLabel(img);

        c.gridx = 1;
        c.gridy = 1;
        gridbag.setConstraints(god, c);
        rightPanel.add(god);

        mainPanel.add(rightPanel, BorderLayout.EAST);
        pack();
        rightPanel.setVisible(true);
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

    public synchronized ArrayList<Player> getPlayersFromList(String list){
        int j=5;
        char c;
        ArrayList<Player> players = new ArrayList<>();
        String name="";
        String age="";
        String id="";
        while (j<list.length()){
            c=list.charAt(j);
            if(c!='-'){
                j++;
            }
            else{
                j=j+2;
                c=list.charAt(j);
                while(c!=','){
                    id=id + c;
                    j++;
                    c=list.charAt(j);
                }
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
                players.add(new Player(name, Integer.parseInt(age), Integer.parseInt(id)));
                name="";
                age="";
                id="";
            }
        }
        return players;
    }

    public synchronized void viewList(){
        list = getList(command);

        /*
        mainPanel.setVisible(false);
        mainPanel.remove(rightPanel);
        rightPanel.setVisible(false);
        rightPanel.removeAll();

        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        JLabel[] text = new JLabel[list.size()];
        for(int i=0; i<list.size(); i++){
            text[i] = new JLabel("- " + list.get(i));
            rightPanel.add(text[i]);
        }

        mainPanel.add(rightPanel, BorderLayout.EAST);
        pack();
        rightPanel.setVisible(true);
        mainPanel.setVisible(true);
        */
        sentMessage=true;
    }

    public synchronized ArrayList<String> getList(String l){
        ArrayList<String> list = new ArrayList<>();
        char c;
        String element="";
        int i=5;
        while (i<l.length()){
            c=l.charAt(i);
            while (c!='\n'){
                element=element+c;
                i++;
                c=l.charAt(i);
            }
            list.add(element);
            element="";
            i++;
        }
        return list;
    }

    public String convert(int id) {
        switch (id){
            case 0:
                return "00";
            case 1:
                return "01";
            case 2:
                return "02";
            case 3:
                return "03";
            case 4:
                return "04";
            case 5:
                return "10";
            case 6:
                return "11";
            case 7:
                return "12";
            case 8:
                return "13";
            case 9:
                return "14";
            case 10:
                return "20";
            case 11:
                return "21";
            case 12:
                return "22";
            case 13:
                return "23";
            case 14:
                return "24";
            case 15:
                return "30";
            case 16:
                return "31";
            case 17:
                return "32";
            case 18:
                return "33";
            case 19:
                return "34";
            case 20:
                return "40";
            case 21:
                return "41";
            case 22:
                return "42";
            case 23:
                return "43";
            case 24:
                return "44";
            default:
                return "";
        }

    }

    public Integer reverseConvert(int x, int y) {
        return x * 5 + y;
    }
}