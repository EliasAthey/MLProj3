/**
 * 
 */
package neuralNetworkTrainer;

import java.util.ArrayList;

/**
 * All things to do with the Rosenbrock function
 * @author Elias
 *
 */
class Rosenbrock {

	/**
	 * The range in which to generate sample data
	 */
	private final static Double sampleRange = 10.0;
	
	/**
	 * Computes the value of the Rosenbrock function on the given inputs
	 * @param inputs the n-dimensional vector of inputs
	 * @return the output of the Rosenbrock function
	 * @throws Exception if there are fewer then 2 inputs
	 */
	static Double computeRosenbrock(ArrayList<Double> inputs) throws Exception{
		
		if(inputs.size() < 2){
			throw new Exception("Rosenbrock function input must have at least two elements.");
		}
		double output = 0f;
		for(int i = 0; i < inputs.size() - 1; i++){
			output += Math.pow(1 - inputs.get(i), 2) + (100 * Math.pow(inputs.get(i + 1) - Math.pow(inputs.get(i), 2), 2));
		}
		return output;
	}
	
	/**
	 * Gets a sample dataset of the rosenbrock function
	 * @param sampleSize the size of the sample
	 * @return the sample dataset, each element is a list of doubles with last entry the output of rosenbrock
	 */
	static ArrayList<ArrayList<Double>> getRosenbrockSample(int sampleSize, int  numInputs){
		
		// generate sample datapoints
		ArrayList<ArrayList<Double>> rosenbrockSample = new ArrayList<ArrayList<Double>>();
		for(int sampleIter = 0; sampleIter < sampleSize; sampleIter++) {
			
			// generate random positive and negative input values
			ArrayList<Double> inputs = new ArrayList<Double>();
			for(int inputIter = 0; inputIter < numInputs; inputIter++ ) {
				inputs.add(inputIter, Math.random() * Math.pow(-1, (int)(Math.random() * 2)) * Rosenbrock.sampleRange);
			}
			rosenbrockSample.add(sampleIter, inputs);
			
			// set the rosenbrock output
			try{
				double output = Rosenbrock.computeRosenbrock(rosenbrockSample.get(sampleIter));
				rosenbrockSample.get(sampleIter).add(output);
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		return rosenbrockSample;
	}
}
