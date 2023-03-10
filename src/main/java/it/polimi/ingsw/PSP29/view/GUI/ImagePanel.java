package it.polimi.ingsw.PSP29.view.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Carlo Manco
 */
public class ImagePanel extends JPanel {

    /**
     * background of the panel
     */
    private Image img;

    /**
     * width of the panel
     */
    private int width;

    /**
     * height of the panel
     */
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
