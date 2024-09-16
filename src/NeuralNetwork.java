import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

public class NeuralNetwork {
    private static double[][] weights;
    private static double[][] neurals;
    private static int layersLength;
    private int[] layers;
    private double learningRate;

    public NeuralNetwork(int[] layers, double learningRate) {
        this.layers = layers;
        this.learningRate = learningRate;
        layersLength = layers.length;
        // Инициализация весов для всех слоев
        weights = new double[layersLength - 1][];
        for (int i = 0; i < layersLength - 1; i++) {
            weights[i] = new double[layers[i] * layers[i + 1]];
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = RandomGenerator.getDefault().nextDouble(-1, 1);
            }
        }
    }

    // Прямое распространение связей
    public double[] forwardPropagation(double[] input) {

        // Функция активации взвешенной суммы
        UnaryOperator<Double> sigmoid = x -> 1 / (1 + Math.exp(-x));

        neurals = new double[layersLength][];
        for (int k = 0; k < layersLength; k++) {
            neurals[k] = new double[layers[k]]; // Размер слоя задается как количество нейронов в нем
        }
        System.arraycopy(input, 0, neurals[0], 0, input.length);

        for (int k = 1; k < layersLength; k++) {
            int prevLayerNeurons  = layers[k - 1];
            int currentLayerNeurons  = layers[k];
            for (int i = 0; i < currentLayerNeurons ; i++) {
                for (int j = 0; j < prevLayerNeurons ; j++) {
                    neurals[k][i] += weights[k - 1][i * prevLayerNeurons  + j] * neurals[k - 1][j];
                }
                System.out.println("Ответ до функции активации нейрона " + (i + 1) + ": " + neurals[k][i]);
                neurals[k][i] = sigmoid.apply(neurals[k][i] + 0.01);
                System.out.println("Ответ после функции активации нейрона " + (i + 1) + ": " + neurals[k][i]);
            }
        }
        return neurals[layersLength - 1];
    }

    public void backPropagation(double[] output) {

        // Вычисление градиентов по выходам
        double[][] outputGradients = new double[layersLength][];
        for (int i = 0; i < layersLength; i++) {
            outputGradients[i] = new double[layers[i]];
        }
        // Вычисление градиентов для выходного слоя
        for (int i = 0; i < output.length; i++) {
            outputGradients[layersLength - 1][i] = (neurals[layersLength - 1][i] - output[i]) *
                    neurals[layersLength - 1][i] * (1 - neurals[layersLength - 1][i]);
        }

        // Вычисление градиентов для весов между выходным и скрытыми слоями
        double[][] weightsGradients = new double[layersLength - 1][];
        for (int i = 0; i < layersLength - 1; i++) {
            weightsGradients[i] = new double[weights[i].length];
        }

        for (int i = 0; i < layers[layersLength - 1]; i++) {
            for (int j = 0; j < layers[layersLength - 2]; j++) {
                weightsGradients[layersLength - 2][i * layers[layersLength - 2] + j] =
                        outputGradients[layersLength - 1][i] * neurals[layersLength - 2][j];
            }
        }

        // Обновление весов выходного слоя
        for (int i = 0; i < weights[layersLength - 2].length; i++) {
            weights[layersLength - 2][i] -= learningRate * weightsGradients[layersLength - 2][i];
        }

        // Вычисление градиентов для скрытых слоев
        for (int l = layersLength - 2; l > 0; l--) {
            for (int i = 0; i < layers[l]; i++) {
                double sum = 0;
                for (int j = 0; j < layers[l + 1]; j++) {
                    sum += outputGradients[l + 1][j] * weights[l][j * layers[l] + i];
                }
                outputGradients[l][i] = sum * neurals[l][i] * (1 - neurals[l][i]);
            }
        }

        // Обновление весов скрытого слоя
        for (int l = 0; l < layersLength - 2; l++) {
            for (int i = 0; i < layers[l + 1]; i++) {
                for (int j = 0; j < layers[l]; j++) {
                    weightsGradients[l][i * layers[l] + j] =
                            outputGradients[l + 1][i] * neurals[l][j];
                }
            }
        }

        for (int l = 0; l < layersLength - 2; l++) {
            for (int i = 0; i < weights[l].length; i++) {
                weights[l][i] -= learningRate * weightsGradients[l][i];
            }
        }
    }
}
