/**
 * 
 */
package neuralNetworkTrainer;

import java.util.ArrayList;

/**
 * @author Elias
 *
 */
class Backprop extends TrainingAlgorithm {

	/**
	 * The learning rate used for backprop
	 */
	private final Double learningRate;
	
	/**
	 * The configuration fo the network
	 */
	private ArrayList<Integer> configuration;
	
//	needs to be a delta value for each node
//	/**
//	 * the deltavalues for each layer in the network
//	 */
//	private ArrayList<Double> deltaValues;
	
	/**
	 * Constructs a new backprop algorithm
	 * @param configuration the number of nodes in each layer, first number is the number of input nodes
	 * @param learningRate the learning rate used for backprop
	 */
	Backprop(ArrayList<Integer> configuration, Double learningRate) {
		this.learningRate = learningRate;
		this.configuration = configuration;
	}
	
	/**
	 * Intializes a network with weights
	 * @return the intialized network
	 */
	private Network initializeNetwork(){
		
		// construct network
		Network network = new Network(this.configuration);

		// set input weights to 1.0, hidden and output weights between -0.5 and +0.5
		ArrayList<ArrayList<Double>> weights = new ArrayList<ArrayList<Double>>();
		for(int inputIter = 0; inputIter < this.configuration.get(0); inputIter++){
			ArrayList<Double> weightVector = new ArrayList<Double>();
			weightVector.add(0, 1.0);
			weights.add(weightVector);
		}
		for(int hiddenLayerIter = 1; hiddenLayerIter < this.configuration.size() - 1; hiddenLayerIter++){
			for(int hiddenNodeIter = 0; hiddenNodeIter < this.configuration.get(hiddenLayerIter); hiddenNodeIter++){
				ArrayList<Double> weightVector = new ArrayList<Double>();
				for(int weightIter = 0; weightIter < this.configuration.get(hiddenLayerIter - 1); weightIter++){
					weightVector.add(weightIter, Math.pow(-1, (int)(Math.random() * 2)) * Math.random() * 0.5);
				}
				weights.add(weightVector);
			}
		}
		for(int outputIter = 0; outputIter < this.configuration.get(this.configuration.size() - 1); outputIter++){
			ArrayList<Double> weightVector = new ArrayList<Double>();
			for(int weightIter = 0; weightIter < this.configuration.get(this.configuration.size() - 2); weightIter++){
				weightVector.add(weightIter, Math.pow(-1, (int)(Math.random() * 2)) * Math.random() * 0.5);
			}
			weights.add(weightVector);
		}
		network.setWeights(weights);
		return network;
	}

	/**
	 * trains a network using backprop
	 */
	@Override
	Network train() {
		
		// initialize network
		Network network = this.initializeNetwork();
		
		// do until convergence
		boolean converged = false;
		while(!converged){
			
			// holds the last computed output
			ArrayList<Double> computedOutput = new ArrayList<Double>();
			
			// holds the last expected output
			ArrayList<Double> expectedOutput = new ArrayList<Double>();
			
			// all the serialzed networks (weights) of a single run
			ArrayList<ArrayList<ArrayList<Double>>> allWeights = new ArrayList<ArrayList<ArrayList<Double>>>();
			
			// get sample dataset
			int numInputs = network.getInputLayer().getNodes().size();
			int sampleSize = (int)(Math.pow(1.8, numInputs) * 10000);
			ArrayList<ArrayList<Double>> dataset = Rosenbrock.getRosenbrockSample(sampleSize, numInputs);
			
			// iterate over each sample datapoint
			int samplePointIter = 0;
			for(ArrayList<Double> samplePoint : dataset){
				
				// set inputs and expected output
				for(int inputIter = 0; inputIter < samplePoint.size() - 1; inputIter++){
					if(network.getInputLayer().getNodes().get(inputIter).getInputs().size() != 0){
						network.getInputLayer().getNodes().get(inputIter).getInputs().clear();
					}
					network.getInputLayer().getNodes().get(inputIter).getInputs().add(samplePoint.get(inputIter));
				}
				expectedOutput = new ArrayList<Double>();
				expectedOutput.add(samplePoint.get(samplePoint.size() - 1));
				
				// execute the nodes in the network and save computed output
				computedOutput = this.executeNodes(network);
				
//				// print squared error every 10000 samples
//				if(samplePointIter % 10000 == 0){
//					ArrayList<Double> squaredError = this.getSquaredError(expectedOutput, computedOutput);
//					for(int i = 0; i < squaredError.size(); i++){
//						System.out.println("Expected: " + expectedOutput.get(0) + "\nComputed: " + computedOutput.get(0));
//						System.out.println("Squared error for output node " + i + ": " + squaredError.get(i) + "\n");
//					}
//				}
				
				// Save original weights
				ArrayList<ArrayList<Double>> originalWeights = (ArrayList<ArrayList<Double>>)Network.serializeNetwork(network).clone();
				
				// set delta values then update weights
				this.setOutputDeltas(network, expectedOutput);
				this.setHiddenDeltas(network);
				this.updateFinalNodeWeights(network, expectedOutput);
				this.updateHiddenNodeWeights(network);
				
				// Save updated weights
				allWeights.add(Network.serializeNetwork(network));
				
				// Reset to original weights
				network.setWeights(originalWeights);
				
				samplePointIter++;
			}
			
			// find average of all weights
			ArrayList<ArrayList<Double>> averagedWeights = new ArrayList<ArrayList<Double>>();
			int numWeights = 0;
			for(ArrayList<ArrayList<Double>> weights : allWeights){
				numWeights++;
				int nodeIter = 0;
				for(ArrayList<Double> weightVector : weights){
					if(numWeights == 1){
						averagedWeights.add(nodeIter, weightVector);
					}
					else{
						for(int weightIter = 0; weightIter < weightVector.size(); weightIter++){
							Double current = averagedWeights.get(nodeIter).get(weightIter);
							current += weightVector.get(weightIter);
						}
					}
					nodeIter++;
				}
			}
			for(ArrayList<Double> weightVectors : averagedWeights){
				for(Double weight : weightVectors){
					weight = weight / numWeights;
				}
			}
			
			// check convergence
			if(!this.hasConverged(network)){
				network.setWeights(averagedWeights);
				
				// print error
				ArrayList<Double> squaredError = this.getSquaredError(expectedOutput, computedOutput);
				for(int i = 0; i < squaredError.size(); i++){
					System.out.println("Expected: " + expectedOutput.get(0) + "\nComputed: " + computedOutput.get(0));
					System.out.println("Squared error for output node " + i + ": " + squaredError.get(i) + "\n");
				}
			}
			else{
				break;
			}
		}
		
		return network;
	}
	
	/**
	 * execute the nodes in the network
	 * @param network the Network to execute
	 * @return the computed output of the network
	 */
	private ArrayList<Double> executeNodes(Network network){
		
		for(Node inputNode : network.getInputLayer().getNodes()){
			inputNode.activateNode();
		}
		for(Layer hiddenLayer : network.getHiddenLayers()){
			for(Node hiddenNode : hiddenLayer.getNodes()){
				hiddenNode.activateNode();
			}
		}
		ArrayList<Double> networkOutput = new ArrayList<Double>();
		for(Node outputNode : network.getOutputLayer().getNodes()){
			outputNode.activateNode();
			networkOutput.add(outputNode.getComputedOutput());
		}
		return networkOutput;
	}
	
	/**
	 * Determines if the given network's weights have converged
	 * @param network the network to check
	 * @return true if the network has converged
	 */
	private boolean hasConverged(Network network){
		/**
		 * TODO
		 */
		return false;
	}
	
	/**
	 * Computes the squared error between the network's computed output and the sample expected output
	 * @param expectedOutput the output defined by the sample
	 * @return the squared error between the network's computed output and the sample expected output
	 */
	private ArrayList<Double> getSquaredError(ArrayList<Double> expectedOutput, ArrayList<Double> computedOutput){
		
		ArrayList<Double> squaredError = new ArrayList<Double>();
		for(int outputIter = 0; outputIter < expectedOutput.size(); outputIter++){
			Double error =  Math.pow(expectedOutput.get(outputIter) - computedOutput.get(outputIter), 2);
			squaredError.add(outputIter, error);
		}
		return squaredError;
	}
	
	/**
	 * Sets the delta values for all output nodes. The derivative used is 1 because the output function is just a weighted sum.
	 * @param network the Network to reference
	 * @param expectedOutput the expected output of the network
	 */
	private void setOutputDeltas(Network network, ArrayList<Double> expectedOutput){
		
		for(Node outputNode : network.getOutputLayer().getNodes()){
			Double expected = expectedOutput.get(outputNode.getIndexInLayer());
			Double computed = outputNode.getComputedOutput();
			Double delta = (expected - computed) * outputNode.getDerivative();
			outputNode.setBackpropDelta(delta);
		}
	}
	
	/**
	 * Sets the delta values for all hidden nodes. The derivative used is(output * (1 - output)) because the hidden nodes function is sigmoidal.
	 * @param network the Network to reference
	 */
	private void setHiddenDeltas(Network network){
		
		for(int layerIter = network.getHiddenLayers().size() - 1; layerIter >= 0; layerIter--){
			for(Node hiddenNode : network.getHiddenLayers().get(layerIter).getNodes()){
				Double computed = hiddenNode.getComputedOutput();
				Double downstreamSum = 0.0;
				for(Node downstreamNode : hiddenNode.getDownstreamNodes()){
					downstreamSum += (downstreamNode.getBackpropDelta() * downstreamNode.getWeights().get(hiddenNode.getIndexInLayer()));
				}
				hiddenNode.setBackpropDelta(downstreamSum * hiddenNode.getDerivative());
			}
		}
	}
	
	/**
	 * update the weights for every hidden node
	 * @param network the Network to update weights on
	 */
	private void updateHiddenNodeWeights(Network network){
		
		for(int layerIter = 0; layerIter < network.getHiddenLayers().size(); layerIter++){
			for(Node hiddenNode : network.getHiddenLayers().get(layerIter).getNodes()){
				for(int inputIter = 0; inputIter < hiddenNode.getInputs().size(); inputIter++){
					Double newWeight = hiddenNode.getWeights().get(inputIter);
					newWeight += (this.learningRate * hiddenNode.getBackpropDelta() * hiddenNode.getInputs().get(inputIter));
					hiddenNode.getWeights().remove(inputIter);
					hiddenNode.getWeights().add(inputIter, newWeight);
				}
			}
		}
	}
	
	/**
	 * update the weights to the final nodes
	 * @param network the Network to update weights on
	 * @param expectedOutput the expected output defined by the sample datapoint
	 */
	private void updateFinalNodeWeights(Network network, ArrayList<Double> expectedOutput){
		
		for(Node outputNode : network.getOutputLayer().getNodes()){
			for(int inputIter = 0; inputIter < outputNode.getInputs().size(); inputIter++){
				Double newWeight = outputNode.getWeights().get(inputIter);
				newWeight += (this.learningRate * outputNode.getBackpropDelta() * outputNode.getInputs().get(inputIter));
				outputNode.getWeights().remove(inputIter);
				outputNode.getWeights().add(inputIter, newWeight);
			}
		}
	}
}
