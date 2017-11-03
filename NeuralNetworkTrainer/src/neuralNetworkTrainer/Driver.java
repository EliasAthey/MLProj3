/**
 * 
 */
package neuralNetworkTrainer;

import java.util.ArrayList;

/**
 * @author Elias
 *
 */
class Driver {

	/**
	 * The training algorithm to use
	 */
	private static TrainingAlgorithm trainingAlgorithm;

	/**
	 * The configuration of the network
	 */
	public static ArrayList<Integer> configuration;

	/**
	 * True if the current problem is a classification problem; false if it is a linear regression problem
	 */
	public static boolean isClassificationProblem;

	/**
	 * Backprop parameters
	 */
	public static double learningRate;
	public static double momentum;

	/**
	 * Evolutionary algorithm parameters
	 */
	public static int populationSize;  // "mu"
	public static int numberOffspring; // "lambda"
	public static double mutationRate; // used by GA and ES
	public static double beta;         // used by DE

	
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
		
		for(String x : args){
			System.out.println(x);
		}
		
		// test backprop
		Driver.configuration = new ArrayList<>();
		Driver.configuration.add(0, 2);// inputs
		Driver.configuration.add(1, 20);// first hidden layer
		Driver.configuration.add(2, 1);// output
		Driver.learningRate = 0.04;
		Driver.momentum = 0.4;
		Driver.trainingAlgorithm = new Backprop();
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
