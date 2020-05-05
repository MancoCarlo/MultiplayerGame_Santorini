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
        JPanel panel = new JPanel(new BorderLayout(100, 100));
        JLabel label = new JLabel(command);
        final JTextField nick = new JTextField();
        nick.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JButton button = new JButton("SEND");
        JPanel login = new JPanel();
        JPanel n = new JPanel();
        JPanel s = new JPanel();
        JPanel e = new JPanel();
        JPanel o = new JPanel();
        login.setLayout(new BoxLayout(login, BoxLayout.Y_AXIS));
        panel.add(login, BorderLayout.CENTER);
        panel.add(n, BorderLayout.NORTH);
        panel.add(s, BorderLayout.SOUTH);
        panel.add(o, BorderLayout.WEST);
        panel.add(e, BorderLayout.EAST);
        login.add(label);
        login.add(nick);
        login.add(button);
        this.add(panel);
        this.setMinimumSize(new Dimension(500, 400));
        pack();
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
