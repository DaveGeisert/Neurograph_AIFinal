import org.math.plot.Plot2DPanel;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 11/23/16.
 */
public class PlotParser {
    public void plot(double[] desiredOutput, double[] output, double[] error, int predictionLine){

        double[] x = new double[desiredOutput.length];
        for(int i = 0; i < x.length; i++){
            x[i] = i+1;
        }

        Plot2DPanel plot = new Plot2DPanel();
        int desiredOutputLine = plot.addLinePlot("Desired Output", x, desiredOutput);
        plot.getPlot(desiredOutputLine).setColor(Color.BLUE);

        int outputLine = plot.addLinePlot("Output", x, output);
        plot.getPlot(outputLine).setColor(Color.RED);

        JFrame frame = new JFrame("a plot panel");
        frame.setContentPane(plot);
        frame.setSize(1000, 1000);
        frame.setVisible(true);
    }
}
