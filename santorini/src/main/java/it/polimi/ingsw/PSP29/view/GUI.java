package it.polimi.ingsw.PSP29.view;

import it.polimi.ingsw.PSP29.model.Player;

import javax.swing.*;
import javax.swing.border.Border;
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
        this.setTitle("SANTORINI the GAME");
        this.setPreferredSize(new Dimension(635, 635));
        this.pack();
        this.setVisible(true);
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
                    try{
                        viewIndex();
                    }catch (InterruptedException e){}
                    break;

                case COORDINATE:
                    doCoordinate();
                    break;

                case TURN:
                    doTurn();
                    break;

                case STOP:
                    return;
            }
        }
    }

    public synchronized void doLogin() {
        lastViewCenter="login";
        mainPanel.setVisible(false);
        centerPanel.setVisible(false);
        mainPanel.remove(centerPanel);
        centerPanel.removeAll();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(command.substring(5));
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

    public synchronized void doLobby() {
        lastViewCenter="lobby";
        mainPanel.setVisible(false);
        centerPanel.setVisible(false);
        mainPanel.remove(centerPanel);
        centerPanel.removeAll();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(command.substring(5));
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

    public synchronized void viewMessage(){
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
        JLabel label = new JLabel(command.substring(5));
        topPanel.add(label);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        this.add(mainPanel);
        this.pack();
        topPanel.setVisible(true);
        mainPanel.setVisible(true);
        sentMessage=true;
    }

    public synchronized void viewIndex() throws InterruptedException {
        if(list==null){
            list.add("1) Yes");
            list.add("2) No");
        }
        command = command.substring(4);
        String size = command.substring(0,1);
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
        topPanel.setLayout(new FlowLayout());
        JLabel label = new JLabel(command);
        topPanel.add(label);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
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
                B.getButtons().get(i).setBackground(Color.GREEN);
        }
        for (final JButton b : B.getButtons()) {
            for(Integer i : indexes){
                if(i == B.getCoordinate(b)){
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int id = B.getCoordinate(b);
                            message = getIndex(indexes, id);
                            System.out.println(message);
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
        topPanel.setLayout(new FlowLayout());
        JLabel label = new JLabel(command);
        topPanel.add(label);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        this.setMinimumSize(new Dimension(600, 500));
        pack();
        mainPanel.remove(rightPanel);
        rightPanel.setVisible(false);
        rightPanel.removeAll();
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

    public void viewListPlayer(){
        ArrayList<Player> players = getPlayersFromList(command);

        mainPanel.setVisible(false);
        mainPanel.remove(leftPanel);
        leftPanel.setVisible(false);
        leftPanel.removeAll();

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Players:");
        leftPanel.add(title);
        JLabel[] text = new JLabel[players.size()*2];
        int j=0;
        for(int i=0; i<players.size(); i++){
            text[j] = new JLabel(players.get(i).getId()+") "+players.get(i).getNickname());
            leftPanel.add(text[j]);
            j++;
            text[j] = new JLabel(players.get(i).getAge() + " years");
            leftPanel.add(text[j]);
            j++;
        }

        mainPanel.add(leftPanel, BorderLayout.WEST);
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