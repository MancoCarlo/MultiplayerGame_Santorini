package it.polimi.ingsw.PSP29.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame{


    private String message;
    private boolean sentMessage = false;
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
    public synchronized void doLoginN(String command) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(command);
        final JTextField nick = new JTextField("Nome");
        nick.setSize(200,100);
        JButton button = new JButton("SEND");
        button.setSize(10,10);
        panel.setLayout(new FlowLayout());
        panel.add(label);
        panel.add(nick);
        panel.add(button);
        this.add(panel);
        this.setSize(500,500);
        this.setVisible(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname;
                if (!nick.getText().equals("")) {
                    nickname = nick.getText();
                    message = nickname;
                    sentMessage = true;
                }
            }
        });
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
