/**
 * 
 */
package neuralNetworkTrainer;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @author Elias
 *
 */
class Driver {

	/**
	 * The file for the data set
	 */
	private static File dataFile;

	/**
	 * The training algorithm to use
	 */
	private static TrainingAlgorithm trainingAlgorithm;

	/**
	 * The configuration of the network
	 */
	public static ArrayList<Integer> configuration = new ArrayList<>();

	/**
	 * True if the current problem is a classification problem; false if it is a linear regression problem
	 */
	public static boolean isClassificationNetwork;

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
	 * @param args args[0] is the datafile
	 *             args[1] is -bp/-ga/-es/-de specifying the training algorithm
	 *             args[2] is the configuration of the network(ie 1-2-3)
	 */
	public static void main(String[] args) {
		
		/**
		 * TODO
		 * 
		 * parse by dash notation to set specified parameters
		 * (-a argA -b argB -c -d argD)
		 */

		
		if(args.length < 3){
			Driver.displayHelpText();
			return;
		}
		else{
			// set required variables, return if they are not correct
			if(!Driver.setDataFile(args[0])
				|| !Driver.setTrainingAlgorithm(args[1])
				|| !configureNetwork(args[2]))
			{
				Driver.displayHelpText();
				return;
			}

			// check for any additional parameters, set accordingly
			Pattern dashPattern = Pattern.compile("\\A-\\w");
			for(int argIter = 3; argIter < args.length; argIter++){
				if(dashPattern.matcher(args[argIter]).matches()){
					switch(args[argIter].substring(1)){
						// is classification network?
						case "c":
							Driver.isClassificationNetwork = true;
							break;
						// learning rate
						case "lr":
							if(argIter + 1 < args.length && !dashPattern.matcher(args[argIter + 1]).matches()){
								//System.out.println("-a has the argument: " + args[++argIter]);
							}
							else{
								//System.out.println("-a must be followed by a valid argument");
							}
							break;
						// momentum
						case "m":
							if(argIter + 1 < args.length && !dashPattern.matcher(args[argIter + 1]).matches()){
								//System.out.println("-b has the argument: " + args[++argIter]);
							}
							else{
								//System.out.println("-b must be followed by a valid argument");
							}
							break;
						// size of population
						case "p":
							if(argIter + 1 < args.length && !dashPattern.matcher(args[argIter + 1]).matches()){
								//System.out.println("-b has the argument: " + args[++argIter]);
							}
							else{
								//System.out.println("-b must be followed by a valid argument");
							}
							break;
						// number of offspring generated each iteration
						case "o":
							if(argIter + 1 < args.length && !dashPattern.matcher(args[argIter + 1]).matches()){
								//System.out.println("-b has the argument: " + args[++argIter]);
							}
							else{
								//System.out.println("-b must be followed by a valid argument");
							}
							break;
						// mutation rate
						case "mr":
							if(argIter + 1 < args.length && !dashPattern.matcher(args[argIter + 1]).matches()){
								//System.out.println("-b has the argument: " + args[++argIter]);
							}
							else{
								//System.out.println("-b must be followed by a valid argument");
							}
							break;
						// DE beta parameter
						case "b":
							if(argIter + 1 < args.length && !dashPattern.matcher(args[argIter + 1]).matches()){
								//System.out.println("-b has the argument: " + args[++argIter]);
							}
							else{
								//System.out.println("-b must be followed by a valid argument");
							}
							break;
						// set default values
						default:
							Driver.isClassificationNetwork = false;
							Driver.learningRate = 0.01;
							Driver.momentum = 0.5;
							Driver.populationSize = 32;
							Driver.numberOffspring = 50;
							Driver.mutationRate = 0.01;
							Driver.beta = 0.1;
					}
				}
			}
		}
		
		// test backprop
//		Driver.configuration = new ArrayList<>();
//		Driver.configuration.add(0, 2);// inputs
//		Driver.configuration.add(1, 20);// first hidden layer
//		Driver.configuration.add(2, 1);// output
//		Driver.learningRate = 0.04;
//		Driver.momentum = 0.4;
//		Driver.trainingAlgorithm = new Backprop();
//		Driver.train();
	}
	
	/**
	 * Trains a network using the current Training Algorithm
	 * @return a trained network
	 */
	private static Network train(){
		
		return Driver.trainingAlgorithm.train();
	}

	/**
	 * Sets the data file to learn from
	 * @param input the file path
	 * @return true if the operation is successful, false otherwise
	 */
	private static boolean setDataFile(String input){
		Driver.dataFile = new File(input);
		if(!dataFile.exists()){
			System.out.println("The file does not exist, try again.\n");
			return false;
		}
		return true;
	}

	/**
	 * Sets the training algorithm based on a string
	 * @param input the training algorithm
	 * @return true if successful, false otherwise
	 */
	private static boolean setTrainingAlgorithm(String input){
		boolean flag = true;
		switch(input){
			case "-bp":
				Driver.trainingAlgorithm = new Backprop();
				break;
			case "-ga":
				//Driver.trainingAlgorithm = new GA();
				break;
			case "-es":
				//Driver.trainingAlgorithm = new ES();
				break;
			case "-de":
				//Driver.trainingAlgorithm = new DE();
				break;
			default:
				System.out.println("The training algorithm is wrong, try again.\n");
				flag = false;
		}
		return flag;
	}

	/**
	 * Splits the input string into an ArrayList of Integers and passes it to the Network constructor
	 * @param input the network configuration
	 * @return true if successful, false otherwise
	 */
	private static boolean configureNetwork(String input){
		if(Pattern.matches("\\d+-\\d+(-\\d+)*", input)){
			String[] layers = input.split("-");
			for(String layerSize : layers){
				Driver.configuration.add(Integer.parseInt(layerSize));
			}
			return true;
		}
		else{
			System.out.println("The network configuration is wrong, try again.\n");
			return false;
		}
	}

	/**
	 * Displays the help text for the program, specifically how to input parameters
	 */
	private static void displayHelpText(){
		System.out.println("HELP!\n");
	}
}
