/**
 * 
 */
package neuralNetworkTrainer;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Elias
 *
 */
class Driver {

	/**
	 * The training algorithm to use
	 */
	private static TrainingAlgorithm trainingAlgorithm;
	public static ArrayList<Integer> configuration;
	public static Random randNum = new Random();
	
//	these values will be passed to the training algorithm, not sure if we need to keep them here
//	/**
//	 * Backprop parameters
//	 */
//	private static Double learningRate;
//	
//	/**
//	 * Gentic Algorithm parameters
//	 */
//	
//	/**
//	 * Evolution Strategy parameters
//	 */
//	
//	/**
//	 * Differential Evolution parameters
//	 */
	
	/**
	 * The entry point of the application
	 * @param args The network training algorithm and specified parameters
	 */
	public static void main(String[] args) {
		
		/**
		 * TODO
		 * 
		 * parse by dash notation to set specified parameters
		 * (-a argA -b argB -c -d argD)
		 */
		
		
		
		// test backprop
		ArrayList<Integer> config = new ArrayList<Integer>();
		config.add(0, 2);// inputs
		config.add(1, 20);// first hidden layer
		config.add(2, 1);// output
		Double learningRate = 0.04;
		Double momentum = 0.5;
		Driver.trainingAlgorithm = new Backprop(config, learningRate, momentum);
		Driver.train();
	}
	
	/**
	 * Trains a network using the current Training Algorithm
	 * @return a trained network
	 */
	private static Network train(){
		
		return Driver.trainingAlgorithm.train();
	}

}
