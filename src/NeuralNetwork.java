import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

public class NeuralNetwork {
    private static double[][] weights;
    private static double[][] neurals;
    private static int firstLayer;
    private static int layersLength;
    private static int outputLength;
    private static final double E = Math.E;
    private int[] layers;
    private double learningRate;

    public NeuralNetwork(int[] layers, double learningRate) {
        this.layers = layers;
        this.learningRate = learningRate;
        firstLayer = layers[0];
        layersLength = layers.length;
        weights = new double[layersLength - 1][firstLayer * layers[1]];
        for (int i = 0; i < layersLength - 1; i++) {
            for (int j = 0; j < layers[i] * layers[i + 1]; j++) {
                weights[i][j] = RandomGenerator.getDefault().nextDouble(-1, 1);
            }
        }
    }

    // Прямое распространение связей
    public double[] forwardPropagation(double[] input) {

        // Функция активации взвешенной суммы
        UnaryOperator<Double> sigmoid = x -> 1 / (1 + Math.pow(E, -x));

        neurals = new double[layersLength][];
        for (int k = 0; k < layersLength; k++) {
            neurals[k] = new double[layers[k]]; // Размер слоя задается как количество нейронов в нем
        }
        System.arraycopy(input, 0, neurals[0], 0, input.length);

        for (int k = 1; k < layersLength; k++) {
            int layerInput = layers[k - 1];
            int layerOutput = layers[k];
            for (int i = 0; i < layerOutput; i++) {
                for (int j = 0; j < layerInput; j++) {
                    neurals[k][i] += weights[k - 1][i + j] * neurals[k - 1][j];
                }
                System.out.println("Ответ до функции активации нейрона " + (i + 1) + ": " + neurals[k][i]);
                neurals[k][i] = sigmoid.apply(neurals[k][i] + 0.01);
                System.out.println("Ответ после функции активации нейрона " + (i + 1) + ": " + neurals[k][i]);
            }
        }
        return neurals[layersLength - 1];
    }

    public void backPropagation(double[] output) {
        outputLength = output.length;

        // Вычисление градиентов по выходам
        double[][] outputGradients = new double[layersLength][];
        for (int i = 0; i < layersLength; i++) {
            outputGradients[i] = new double[layers[i]];
        }
        for (int i = 0; i < 3; i++) {
            outputGradients[2][i] = (neurals[2][i] - output[i]) * neurals[2][i] * (1 - neurals[2][i]);
        }

        // Вычисление градиентов по весам между выходным и скрытым слоями
        double[][] weightsGradients = new double[2][784 * 16];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 16; j++) {
                weightsGradients[1][j + i * 16] =
                        outputGradients[2][i] * neurals[1][j];
            }
        }

        // Обновление весов выходного слоя
        // TODO Хардкод 160
        for (int i = 0; i < 160; i++) {
            weights[1][i] = weights[1][i] - learningRate * weightsGradients[1][i];
        }

        // Вычисление градиентов для скрытых слоев
        for (int i = 1; i > 0; i--) {
            for (int j = 0; j < 16; j++) {
                double sum = 0.0;
                for (int k = 0; k < layers[i + 1]; k++) {
                    sum += outputGradients[i + 1][k] * weights[i][k * layers[i] + j];
                }
                outputGradients[i][j] = sum * neurals[i][j] * (1 - neurals[i][j]);

            }
        }

        // Вычисление градиентов по весам скрытого слоя
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 784; j++) {
                weightsGradients[0][j + i * 784] = outputGradients[1][i] * neurals[0][j];
            }
        }

        // Обновление весов скрытого слоя
        // TODO Хардкод 784*16
        for (int i = 0; i < 784 * 16; i++) {
            weights[0][i] = weights[0][i] - learningRate * weightsGradients[0][i];
        }
    }
}
