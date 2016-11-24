import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 11/23/16.
 */
public class CSVNormalizer {
    private double[] min = null;
    private double[] max = null;
    private boolean minMaxInit = false;
    private List<double[]> data = null;
    private String file;
    private BufferedReader br;
    private String line;

    public CSVNormalizer(String file){
        this.file = file;
    }

    public List<double[]> normalize(){
        try{
            br = new BufferedReader(new FileReader(file));
            br.readLine();
            data = new ArrayList<double[]>();
            while((line = br.readLine()) != null) {
                String[] items = line.split(",");
                String[] values = new String[items.length - 1];
                System.arraycopy(items, 1, values, 0, values.length);

                if (min == null) {
                    min = new double[values.length];
                }
                if (max == null) {
                    max = new double[values.length];
                }
                double[] dRow = new double[values.length];
                for (int i = 0; i < values.length; i++) {
                    double current = Double.parseDouble(values[i]);
                    if(!minMaxInit){
                        min[i] = current;
                        max[i] = current;
                    }
                    if (current < min[i]) {
                        min[i] = current;
                    } else if (current > max[i]) {
                        max[i] = current;
                    }
                    dRow[i] = current;
                }
                data.add(dRow);
                minMaxInit = true;
            }
            br.close();
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        for(double[] row : data){
            for(int i = 0; i < row.length; i++) {
                row[i] = (row[i] - min[i]) / (max[i] - min[i]);
            }
        }
        return data;
    }

    public List<double[]> reverseNorm(List<double[]> input){
        for(double[] row : input){
            for (int i = 0; i < row.length; i++){
                row[i] = (row[i] * (max[i] - min[i])) + min[i];
            }
        }
        return data;
    }

    public double[] reverseNorm(double[] input, int column){
        for (int i = 0; i < input.length; i++){
            input[i] = (input[i] * (max[column] - min[column])) + min[column];
        }
        return input;
    }

    public double[] getMin() {
        return min;
    }

    public double[] getMax() {
        return max;
    }

    public String getFile() {
        return file;
    }

    public List<double[]> getData() {
        return this.data;
    }
}
