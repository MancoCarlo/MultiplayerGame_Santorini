package it.polimi.ingsw.PSP29.view;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image img;
    private int width;
    private int height;

    public ImagePanel(String path, int width, int height) {
        ImageIcon image = new ImageIcon(getClass().getResource(path));
        img = image .getImage();
        this.width = width;
        this.height = height;
        this.setVisible(true);
    }

    /**
     * add an image as background of the panel
     * @param g is the Graphics's Object
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, width, height,null);
    }
}
