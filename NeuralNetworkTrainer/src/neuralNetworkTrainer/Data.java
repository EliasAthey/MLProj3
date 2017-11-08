package neuralNetworkTrainer;

import java.util.ArrayList;

/**
 * A parent class for all data classes
 */
public abstract class Data {

    /**
     * The data points
     */
    protected ArrayList<ArrayList<Object>> dataPoints;

    /**
     * Number of inputs/attributes of the data
     */
    protected int numInputs;

    /**
     * Number of outputs/classes of the data
     */
    protected int numOutputs;

    /**
     * Gets the data points for this data set
     * @return the data points for this set of data
     */
    public ArrayList<ArrayList<Object>> getDataPoints() {
        return dataPoints;
    }

    /**
     * Gets the number of inputs for this data set
     * @return the number of inputs for this data set
     */
    public int getNumInputs() {
        return numInputs;
    }

    /**
     * Gets the number of outputs for this data set
     * @return the number of outputs for this data set
     */
    public int getNumOutputs() {
        return numOutputs;
    }
}
