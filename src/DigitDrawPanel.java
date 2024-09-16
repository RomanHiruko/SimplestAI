import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DigitDrawPanel extends JPanel {
    private int[][] pixels = new int[28][28];  // Матрица пикселей для рисования

    public DigitDrawPanel() {
        this.setPreferredSize(new Dimension(280, 280));  // Увеличенный размер для удобства рисования

        // Отслеживание мыши
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                drawPixel(e.getX(), e.getY());
            }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                drawPixel(e.getX(), e.getY());
            }
        });
    }

    // Рисование пикселя
    private void drawPixel(int x, int y) {
        int px = x / 10;
        int py = y / 10;
        if (px >= 0 && px < 28 && py >= 0 && py < 28) {
            pixels[px][py] = 1;
            repaint();
        }
    }

    // Перерисовка компонента
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                if (pixels[i][j] == 1) {
                    g.fillRect(i * 10, j * 10, 10, 10);
                }
            }
        }
    }

    // Сброс рисунка
    public void clear() {
        pixels = new int[28][28];
        repaint();
    }

    // Получение матрицы пикселей для нейросети
    public double[] getPixels() {
        double[] inputs = new double[28 * 28];
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                inputs[i * 28 + j] = pixels[i][j];
            }
        }
        return inputs;
    }
}
