public class Network {

    private double[][] output;
    private double[][][] weights;
    private double[][] bias;

    private double[][] error_signal;
    private double[][] output_derivative;

    public final int[] NETWORK_LAYER_SIZES;
    public final int INPUT_SIZES;
    public final int OUTPUT_SIZES;
    public final int NETWORK_SIZES;

    public Network(int... networkSizes) {
        this.NETWORK_LAYER_SIZES = networkSizes;
        this.INPUT_SIZES = networkSizes[0];
        this.NETWORK_SIZES = networkSizes.length;
        this.OUTPUT_SIZES = networkSizes[networkSizes.length - 1];

        this.output = new double[NETWORK_SIZES][];
        this.weights = new double[NETWORK_SIZES][][];
        this.bias = new double[NETWORK_SIZES][];
        this.error_signal = new double[NETWORK_SIZES][];
        this.output_derivative = new double[NETWORK_SIZES][];

        for (int i = 0; i < NETWORK_SIZES; i++) {
            this.output[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.error_signal[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.output_derivative[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.bias[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i],0.3,0.7);

            if(i>0){
                this.weights[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i],NETWORK_LAYER_SIZES[i-1],-0.3,0.5);
            }
        }
    }

    public double[] calculate(double... input) {
        if (input.length != INPUT_SIZES) {
            throw new IllegalArgumentException("Input size does not match network input size");
        }
        this.output[0] = input;
        for (int layer = 1; layer < NETWORK_SIZES; layer++) {
            for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                double sum = 0;
                for (int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer - 1]; prevNeuron++) {
                    sum += output[layer - 1][prevNeuron] * weights[layer][neuron][prevNeuron];
                }
                sum += bias[layer][neuron];
                output[layer][neuron] = sigmoid(sum);
                output_derivative[layer][neuron] = (output[layer][neuron] * (1 - output[layer][neuron]));
            }
        }
        return output[NETWORK_SIZES - 1];
    }

    private double sigmoid(double x) {
        return 1d / (1 + Math.exp(-x));
    }

    public void train(double [] input, double[] target, double learningRate) {
        if(input.length != INPUT_SIZES || target.length != OUTPUT_SIZES){
            throw new IllegalArgumentException("Input size does not match network input size");
        }
        calculate(input);
        backpropError(target);
        updateWeights(learningRate);
    }

    public void backpropError(double[] target) {
        for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[NETWORK_SIZES - 1]; neuron++){
            error_signal[NETWORK_SIZES - 1][neuron] = (output[NETWORK_SIZES - 1][neuron] - target[neuron]) * output_derivative[NETWORK_SIZES - 1][neuron];
        }
        for(int layer = NETWORK_SIZES - 2; layer > 0; layer--){
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++){
                double sum = 0;
                for(int nextNeuron = 0; nextNeuron < NETWORK_LAYER_SIZES[layer + 1]; nextNeuron++){
                    double delta = weights[layer + 1][nextNeuron][neuron] * error_signal[layer + 1][nextNeuron];
                    sum += delta;
                }
                error_signal[layer][neuron] = sum * output_derivative[layer][neuron];
            }
        }
    }

    public void updateWeights(double learningRate) {
        for(int layer = 1; layer < NETWORK_SIZES; layer++){
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++){
                for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer - 1]; prevNeuron++){
                    double delta = -learningRate * output[layer - 1][prevNeuron] * error_signal[layer][neuron];
                    weights[layer][neuron][prevNeuron] += delta;
                }
                bias[layer][neuron] += -learningRate * error_signal[layer][neuron];
            }
        }
    }

    public String getWeightsAsString() {
        StringBuilder sb = new StringBuilder();
        for(int layer = 1; layer < NETWORK_SIZES; layer++){
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++){
                for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer - 1]; prevNeuron++){
                    sb.append(weights[layer][neuron][prevNeuron]).append(" ");
                }
                sb.append(bias[layer][neuron]).append("\n");
            }
        }
        return sb.toString();
    }

    public String getBiasAsString() {
        StringBuilder sb = new StringBuilder();
        for(int layer = 1; layer < NETWORK_SIZES; layer++){
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++){
                sb.append(bias[layer][neuron]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
