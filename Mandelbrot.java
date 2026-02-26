import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Mandelbrot extends JFrame {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private BufferedImage image;
    private int[] pixels;

    public native void calcularMandelbrot(int[] pixels, int width, int height, 
                                        double xMin, double xMax, double yMin, double yMax, int maxIter);

    static {
        System.loadLibrary("mandelbrot");
    }

    public Mandelbrot() {
        setTitle("Fractal de Mandelbrot - Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        
        JPanel renderPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                calcularMandelbrot(pixels, WIDTH, HEIGHT, -2.0, 1.0, -1.2, 1.2, 30);
                g.drawImage(image, 0, 0, null);
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