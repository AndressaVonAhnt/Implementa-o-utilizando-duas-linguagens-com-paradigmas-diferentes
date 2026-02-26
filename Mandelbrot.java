// Interface Gráfica 

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Mandelbrot extends JFrame {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    public Mandelbrot() {
        setTitle("Fractal de Mandelbrot - Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel renderPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLUE); 
                g.fillRect(50, 50, 700, 500);
                g.setColor(Color.WHITE);
                g.drawString("Aguardando Integração JNI...", 350, 300);
            }
        };
        renderPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        add(renderPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Mandelbrot::new);
    }
}