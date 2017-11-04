/**
 * 
 */
package neuralNetworkTrainer;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
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
	static ArrayList<Integer> configuration = new ArrayList<>();

	/**
	 * True if the current problem is a classification problem; false if it is a linear regression problem
	 */
	static boolean isClassificationNetwork;

	/**
	 * Backprop parameters
	 */
	static double learningRate;
	static double momentum;

	/**
	 * Evolutionary algorithm parameters
	 */
	static int populationSize;  // "mu"
	static int numberOffspring; // "lambda"
	static double mutationRate; // used by GA and ES
	static double beta;         // used by DE
	
	/**
	 * The entry point of the application
	 * @param args args[0] is the datafile
	 *             args[1] is -bp/-ga/-es/-de specifying the training algorithm
	 *             args[2] is the configuration of the network(ie 1-2-3)
	 */
	public static void main(String[] args) {

		// Check for three required parameters
		if(args.length < 3){
			Driver.displayHelpText();
			System.exit(0);
		}
		else{
			// set required parameters, exit if they are not correct
			if(!Driver.setDataFile(args[0])
				|| !Driver.setTrainingAlgorithm(args[1])
				|| !configureNetwork(args[2]))
			{
				Driver.displayHelpText();
				System.exit(0);
			}

			// set default values for all optional parameters
			Driver.isClassificationNetwork = false;
			Driver.learningRate = 0.01;
			Driver.momentum = 0.5;
			Driver.populationSize = 32;
			Driver.numberOffspring = 50;
			Driver.mutationRate = 0.01;
			Driver.beta = 0.1;

			// check for any additional options (parameters), set accordingly
			Pattern dashPattern = Pattern.compile("\\A-\\w+");
			for(int argIter = 3; argIter < args.length; argIter++){
				if(dashPattern.matcher(args[argIter]).matches()){
					switch(args[argIter]){
						// is classification network?
						case "-c":
							Driver.isClassificationNetwork = true;
							break;
						// learning rate
						case "-lr":
							if(argIter + 1 < args.length && Pattern.matches("\\d+\\.\\d+", args[argIter + 1])){
								Driver.learningRate = Float.parseFloat(args[++argIter]);
							}
							else{
								System.out.println("-lr must be followed by a float value for the Backprop learning rate\n");
								System.exit(0);
							}
							break;
						// momentum
						case "-m":
							if(argIter + 1 < args.length && Pattern.matches("\\d+\\.\\d+", args[argIter + 1])){
								Driver.momentum = Float.parseFloat(args[++argIter]);
							}
							else{
								System.out.println("-m must be followed by a float value for the Backprop momentum\n");
								System.exit(0);
							}
							break;
						// size of population
						case "-p":
							if(argIter + 1 < args.length && Pattern.matches("\\d+", args[argIter + 1])){
								Driver.populationSize = Integer.parseInt(args[++argIter]);
							}
							else{
								System.out.println("-p must be followed by a positive integer for the population size\n");
								System.exit(0);
							}
							break;
						// number of offspring generated each iteration
						case "-o":
							if(argIter + 1 < args.length && Pattern.matches("\\d+", args[argIter + 1])){
								Driver.numberOffspring = Integer.parseInt(args[++argIter]);
							}
							else{
								System.out.println("-o must be followed by a positive integer for the number of offspring generated each generation\n");
								System.exit(0);
							}
							break;
						// mutation rate
						case "-mr":
							if(argIter + 1 < args.length && Pattern.matches("\\d+\\.\\d+", args[argIter + 1])){
								Driver.mutationRate = Float.parseFloat(args[++argIter]);
							}
							else{
								System.out.println("-mr must be followed by a float value for the mutation rate\n");
								System.exit(0);
							}
							break;
						// DE beta parameter
						case "-b":
							if(argIter + 1 < args.length && Pattern.matches("\\d+\\.\\d+", args[argIter + 1])){
								Driver.beta = Float.parseFloat(args[++argIter]);
							}
							else{
								System.out.println("-b must be followed by a float value for the Differential Evolution beta value\n");
								System.exit(0);
							}
							break;
						// wrong option
						default:
							System.out.println(args[argIter] + " is not a valid option\n");
							System.exit(0);
					}
				}
			}
			System.out.println("Starting training...\n");
			Driver.train();
		}
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
	 * Displays the help text for the program
	 */
	private static void displayHelpText(){
		System.out.println("usage:   java -jar NeuralNetworkTrainer.jar <datafile> <training-algorithm> <network-configuration> [parameters]");
		System.out.println("\n<datafile>:              path to file containing a data set");
		System.out.println("\n<training-algorithm>:      -bp (backprop)");
		System.out.println("                           -ga (genetic algorithm)");
		System.out.println("                           -es (evolution strategy)");
		System.out.println("                           -de (differential evolution)");
		System.out.println("\n<network-configuration>:   a-b[-c]*");
		System.out.println("                           a,b,c,... are positive integers representing the number of nodes in each respective layer");
		System.out.println("                           the leftmost value is the input layer, the rightmost is the output layer, any values in between are hidden layers");
		System.out.println("                           any network must have at least 2 layers (input and output)");
		System.out.println("                           examples:   3-1 is the network with 3 input nodes and 1 output node, no hidden nodes");
		System.out.println("                                       3-20-1 is the network with 3 input nodes, 20 hidden nodes, and 1 output node");
		System.out.println("\nparameters:   [-c][-lr <learning rate>][-m <momentum>][-p <population-size>]");
		System.out.println("                [-o <offspring-size>][-mr <mutation rate>][-b <beta>]");
		System.out.println("\n-c    defines  a  CLASSIFICATION problem, assumes linear regression otherwise");
		System.out.println("-lr   defines the LEARNING RATE used by backprop");
		System.out.println("-m    defines the MOMENTUM used by backprop");
		System.out.println("-p    defines the POPULATION SIZE used by evolutionary algorithms");
		System.out.println("-o    defines the OFFSPRING SIZE used by evolutionary algorithms");
		System.out.println("-mr   defines the MUTATION RATE used by genetic algorithm and evolution strategy");
		System.out.println("-b    defines the BETA parameter used by differential evolution\n");
	}
}
