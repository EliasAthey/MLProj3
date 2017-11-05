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
     * Gets the data points for this set of data
     * @return the data points for this set of data
     */
    public ArrayList<ArrayList<Object>> getDataPoints() {
        return dataPoints;
    }
}
