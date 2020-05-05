package it.polimi.ingsw.PSP29.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame{


    private String message;
    private boolean sentMessage = false;
    private JPanel mainPanel = new JPanel();

    public GUI(){
        this.add(mainPanel);
        this.setDefaultLookAndFeelDecorated(true);
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
        mainPanel.setVisible(false);
        mainPanel.removeAll();
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JLabel label = new JLabel(command);
        final JTextField mex = new JTextField();
        mex.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JButton button = new JButton("SEND");
        panel.add(label);
        panel.add(mex);
        panel.add(button);
        mainPanel.add(panel);
        this.add(mainPanel);
        this.setMinimumSize(new Dimension(500, 400));
        pack();
        mainPanel.setVisible(true);
        this.setVisible(true);
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
        mainPanel.setVisible(false);
        mainPanel.removeAll();
        JPanel panel = new JPanel(new GridLayout(4, 1));
        JLabel label = new JLabel(command);
        final JRadioButton but1 = new JRadioButton("2");
        final JRadioButton but2 = new JRadioButton("3");
        ButtonGroup buttonG = new ButtonGroup();
        but1.setSelected(true);
        buttonG.add(but1);
        buttonG.add(but2);
        JButton button = new JButton("SEND");
        panel.add(label);
        panel.add(but1);
        panel.add(but2);
        panel.add(button);
        mainPanel.add(panel);
        this.add(mainPanel);
        this.setMinimumSize(new Dimension(500, 400));
        pack();
        mainPanel.setVisible(true);
        this.setVisible(true);
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
        mainPanel.removeAll();
        JPanel panel = new JPanel(new GridLayout(4, 1));
        JLabel label = new JLabel(command);
        panel.add(label);
        mainPanel.add(panel);
        this.add(mainPanel);
        this.setMinimumSize(new Dimension(500, 400));
        pack();
        mainPanel.setVisible(true);
        this.setVisible(true);
    }

    public synchronized void viewBoard(String command){
        mainPanel.setVisible(false);
        mainPanel.removeAll();
        JPanel panel = new JPanel(new GridLayout(4, 1));
        JLabel label = new JLabel(command);
        panel.add(label);
        mainPanel.add(panel);
        this.add(mainPanel);
        this.setMinimumSize(new Dimension(500, 400));
        pack();
        mainPanel.setVisible(true);
        this.setVisible(true);
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
