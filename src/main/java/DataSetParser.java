import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 11/24/16.
 */
public class DataSetParser {
    private double trainPercentage;
    List<DataSetRow> dataSets = new ArrayList<DataSetRow>();

    public void parse(List<double[]> input, double trainPercentage){
        this.trainPercentage = trainPercentage;

        for(double[] row : input){
            if(input.indexOf(row) < input.size() - 1){
                DataSetRow dsr = new DataSetRow();
                double[] output = new double[1];
                output[0] = input.get(input.indexOf(row) + 1)[0];
                dsr.setInput(row);
                dsr.setDesiredOutput(output);
                dataSets.add(dsr);
            }
        }
    }

    public List<DataSetRow> getTrainingData(){
        return dataSets.subList(0, (int)Math.ceil(dataSets.size() * trainPercentage));
    }

    public List<DataSetRow> getTestingData(){
        return dataSets.subList((int)Math.ceil(dataSets.size() * trainPercentage) + 1, dataSets.size() - 1);
    }
}
