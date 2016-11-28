import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.learning.LMS;

import java.util.List;

import static java.lang.Math.abs;

/**
 * Created by david on 11/23/16.
 */
public class Main {
    public static void main(String[] args){
        int maxIterations = 100000;

        NeuralNetwork neuralNet = new MultiLayerPerceptron(5, 11, 1);

        neuralNet.getLayerAt(0).addNeuron(new BiasNeuron());
        neuralNet.getLayerAt(1).addNeuron(new BiasNeuron());

        ((LMS) neuralNet.getLearningRule()).setMaxError(0.001);
        ((LMS) neuralNet.getLearningRule()).setLearningRate(0.2);


        DataSet trainingSet = new DataSet(5, 1);
        DataSet testingSet = new DataSet(5, 1);


        CSVNormalizer normalizer = new CSVNormalizer("HistoricalQuotes.csv");
        List<double[]> input = normalizer.normalize();
        DataSetParser parser = new DataSetParser();
        parser.parse(input, .6);
        List<DataSetRow> trainingData = parser.getTrainingData();
        List<DataSetRow> testingData = parser.getTestingData();


        for (DataSetRow row:trainingData) {
            trainingSet.addRow(row);
        }
        neuralNet.learn(trainingSet);

        double[] output = new double[trainingData.size() + testingData.size()];
        double[] desiredOutput = new double[trainingData.size() + testingData.size()];
        double[] percentError = new double[trainingData.size() + testingData.size()];

        double totalError = 0;
        int counter = 0;
        for (DataSetRow row : trainingData){
            neuralNet.setInput(row.getInput());
            neuralNet.calculate();
            double out = neuralNet.getOutput()[0];
            double exp = row.getDesiredOutput()[0];
            double error = abs((exp - out )/((exp + out)/2));
            output[counter] = out;
            desiredOutput[counter] = exp;
            percentError[counter] = error;
            counter++;
        }
        int predictionLine = counter;
        for (DataSetRow row : testingData){
            neuralNet.setInput(row.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();

            System.out.print("Input: ");
            for (double in : row.getInput()){
                System.out.print(in + "; ");
            }
            double out = networkOutput[0];
            double exp = row.getDesiredOutput()[0];
            double error = abs((exp - out )/((exp + out)/2));
            totalError += error;

            System.out.print(" Output: " + out + ";");
            System.out.print(" Expected Output: ");
            System.out.print(exp + "; ");
            System.out.print("Error: " + error);
            System.out.println();
            output[counter] = out;
            desiredOutput[counter] = exp;
            percentError[counter] = error * 100;
            counter++;
        }
        System.out.println("Mean Error: " + totalError/testingData.size());
        output = normalizer.reverseNorm(output, 0);
        desiredOutput = normalizer.reverseNorm(desiredOutput, 0);
        PlotParser plot = new PlotParser();
        plot.plot(desiredOutput, output, percentError, predictionLine);
    }
}
