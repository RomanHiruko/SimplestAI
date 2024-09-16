import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DigitDrawPanel extends JPanel {
    private int[][] pixels = new int[28][28];  // Матрица пикселей для рисования

    public DigitDrawPanel() {
        this.setPreferredSize(new Dimension(280, 280));
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
