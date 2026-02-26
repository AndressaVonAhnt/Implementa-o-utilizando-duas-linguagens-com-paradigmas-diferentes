// Interface Gráfica
// Andressa Von Ahnt e Eduarda Fernades

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mandelbrot extends JFrame {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private BufferedImage image;
    private int[] pixels;

    // Configurações de iteração (faz o controle da profundidade do cálculo)
    private int maxIter = 30;
    private final int MIN_ITER = 10;
    private final int MAX_ITER = 500;

    // Coordenadas do plano complexo para controle de Zoom
    private double xMin = -2.0, xMax = 1.0;
    private double yMin = -1.2, yMax = 1.2;

    static {
        // Carregamento dinâmico da biblioteca nativa (.so ou .dll)
        System.loadLibrary("mandelbrot");
    }

    public native void calcularMandelbrot(int[] pixels, int width, int height, 
                                        double xMin, double xMax, double yMin, double yMax, int maxIter);

    public Mandelbrot() {
        setTitle("Fractal de Mandelbrot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Centraliza o fractal em modo tela cheia
        getContentPane().setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.BLACK);

        // Extrai o array de inteiros diretamente do buffer da imagem, fazendo com que o C escreva diretamente na memória de vídeo do Java
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        
        JPanel renderPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        
        renderPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // Listener no mouse para o zoom 
        renderPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                double mouseX = xMin + (e.getX() / (double) WIDTH) * (xMax - xMin);
                double mouseY = yMin + (e.getY() / (double) HEIGHT) * (yMax - yMin);
                
                // Botão Esquerdo: Zoom In (0.5x) | Botão Direito: Zoom Out (2.0x)
                double zoomFactor = (e.getButton() == MouseEvent.BUTTON1) ? 0.5 : 2.0;

                double newWidth = (xMax - xMin) * zoomFactor;
                double newHeight = (yMax - yMin) * zoomFactor;

                xMin = mouseX - newWidth / 2.0; xMax = mouseX + newWidth / 2.0;
                yMin = mouseY - newHeight / 2.0; yMax = mouseY + newHeight / 2.0;

                updateFractal(); // Recalcula via JNI
                renderPanel.repaint();
            }
        });

        // Slider para profundidade de iterações
        JSlider iterSlider = new JSlider(JSlider.HORIZONTAL, MIN_ITER, MAX_ITER, maxIter);
        iterSlider.setBackground(Color.BLACK);
        iterSlider.setPreferredSize(new Dimension(400, 30));
        iterSlider.addChangeListener(e -> {
            // Só dispara o cálculo pesado ao soltar o slider
            if (!iterSlider.getValueIsAdjusting()) {
                maxIter = iterSlider.getValue();
                updateFractal();
                renderPanel.repaint();
            }
        });

        // Legenda
        JLabel labelIter = new JLabel("Iteration Depth: " + MIN_ITER + " - " + MAX_ITER);
        labelIter.setForeground(Color.LIGHT_GRAY);
        labelIter.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel labelZoom = new JLabel("Left Click: Zoom In | Right Click: Zoom Out");
        labelZoom.setForeground(Color.GRAY);
        labelZoom.setFont(new Font("SansSerif", Font.ITALIC, 11));

        // Layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.gridy = 0; add(renderPanel, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(15, 0, 0, 0); add(iterSlider, gbc);
        gbc.gridy = 2; gbc.insets = new Insets(5, 0, 0, 0); add(labelIter, gbc);
        gbc.gridy = 3; gbc.insets = new Insets(5, 0, 15, 0); add(labelZoom, gbc);

        updateFractal();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateFractal() {
        calcularMandelbrot(pixels, WIDTH, HEIGHT, xMin, xMax, yMin, yMax, maxIter);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Mandelbrot::new);
    }
}