import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DigitRecognitionApp {
    private DigitDrawPanel drawPanel;
    private JLabel predictionLabel; // Метка для отображения предсказания
    private int[] layers = {784, 16, 10};
    private double learningRate = 0.1;
    private NeuralNetwork neuralNetwork = new NeuralNetwork(layers, learningRate);

    public DigitRecognitionApp() {

        // Создание окна
        JFrame frame = new JFrame("Digit Recognition");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Панель для рисования
        drawPanel = new DigitDrawPanel();
        frame.add(drawPanel, BorderLayout.CENTER);

        // Метка для отображения предсказания
        predictionLabel = new JLabel("Prediction: ");
        predictionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(predictionLabel, BorderLayout.NORTH);

        // Кнопка для предсказания
        JButton predictButton = new JButton("Predict");
        predictButton.addActionListener(e -> predictDigit());
        frame.add(predictButton, BorderLayout.EAST);

        // Правильные ответы
        JPanel jPanel = new JPanel(new GridLayout(1, 10));
        for (int i = 0; i < 10; i++) {
            JButton button = new JButton(i + "");
            button.addActionListener(e -> getRightAnswer(Integer.parseInt(button.getText())));
            jPanel.add(button);
        }
        frame.add(jPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    private void predictDigit() {
        // Получение предсказания
        double[] inputs = drawPanel.getPixels();
        double[] outputs = neuralNetwork.forwardPropagation(inputs);

        // Определяем предсказанную цифру
        int predictedDigit = getMaxIndex(outputs);
        predictionLabel.setText("Prediction: " + predictedDigit); // Обновляем метку
    }

    public void getRightAnswer(int button) {
        double[] output = new double[10];
        output[button] = 1;
        neuralNetwork.backPropagation(output);
    }

    // Метод для получения индекса максимального значения в массиве
    private int getMaxIndex(double[] outputs) {
        int maxIndex = 0;
        for (int i = 1; i < outputs.length; i++) {
            if (outputs[i] > outputs[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DigitRecognitionApp::new);
    }
}
