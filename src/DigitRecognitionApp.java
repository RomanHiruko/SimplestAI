import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class DigitRecognitionApp {
    private DigitDrawPanel drawPanel;
    private JLabel predictionLabel; // Метка для отображения предсказания
    private int[] layers = {784, 128, 64, 10};
    private double learningRate = 0.05;
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

        // Панель для правильных ответов (кнопки от 0 до 9)
        JPanel jPanel = new JPanel(new GridLayout(1, 10));
        for (int i = 0; i < 10; i++) {
            JButton button = new JButton(i + "");
            button.addActionListener(e -> getRightAnswer(Integer.parseInt(button.getText())));
            jPanel.add(button);
        }
        frame.add(jPanel, BorderLayout.SOUTH);

        // Установка Key Bindings для клавиш 0-9
        setupKeyBindings(frame.getRootPane());

        frame.pack();
        frame.setVisible(true);
    }

    private void setupKeyBindings(JComponent component) {
        // Настройка биндингов для клавиш 0-9
        for (int i = 0; i <= 9; i++) {
            int digit = i;
            String key = "digit" + digit;

            // InputMap связывает нажатие клавиши с названием команды
            component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke(KeyEvent.VK_0 + digit, 0), key);

            // ActionMap связывает команду с действием
            component.getActionMap().put(key, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getRightAnswer(digit); // вызываем обработку правильного ответа
                }
            });
        }
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
        drawPanel.clear();
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
