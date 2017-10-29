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

	/**
	 * The value used for momentum when updating weights
	 */
	private final Double momentumValue;
	
//	needs to be a delta value for each node
//	/**
//	 * the delta values for each layer in the network
//	 */
//	private ArrayList<Double> deltaValues;
	
	/**
	 * Constructs a new backprop algorithm
	 * @param configuration the number of nodes in each layer, first number is the number of input nodes
	 * @param learningRate the learning rate used for backprop
	 */
	Backprop(ArrayList<Integer> configuration, Double learningRate, Double momentumValue) {
		this.learningRate = learningRate;
		this.configuration = configuration;
		this.momentumValue = momentumValue;
	}
	
	/**
	 * Initializes a network with weights
	 * @return the initialized network
	 */
	private Network initializeNetwork(){
		
		// construct network
		Network network = new Network(this.configuration);

		// set input weights to 1.0, hidden and output weights between -0.5 and +0.5, prevWeightChange to 0.
		ArrayList<ArrayList<Double>> weights = new ArrayList<>();
		ArrayList<ArrayList<Double>> weightChange = new ArrayList<>();
		for(int inputIter = 0; inputIter < this.configuration.get(0); inputIter++){
			ArrayList<Double> weightVector = new ArrayList<>();
			ArrayList<Double> weightChangeVector = new ArrayList<>();
			weightVector.add(0, 1.0);
			weightChangeVector.add(0, 0.0);
			weights.add(weightVector);
			weightChange.add(weightChangeVector);
		}
		for(int hiddenLayerIter = 1; hiddenLayerIter < this.configuration.size() - 1; hiddenLayerIter++){
			for(int hiddenNodeIter = 0; hiddenNodeIter < this.configuration.get(hiddenLayerIter); hiddenNodeIter++){
				ArrayList<Double> weightVector = new ArrayList<>();
				ArrayList<Double> weightChangeVector = new ArrayList<>();
				for(int weightIter = 0; weightIter < this.configuration.get(hiddenLayerIter - 1); weightIter++){
					weightVector.add(weightIter, Math.pow(-1, (int)(Math.random() * 2)) * Math.random() * 0.5);
					weightChangeVector.add(0, 0.0);
				}
				weights.add(weightVector);
				weightChange.add(weightChangeVector);
			}
		}
		for(int outputIter = 0; outputIter < this.configuration.get(this.configuration.size() - 1); outputIter++){
			ArrayList<Double> weightVector = new ArrayList<>();
			ArrayList<Double> weightChangeVector = new ArrayList<>();
			for(int weightIter = 0; weightIter < this.configuration.get(this.configuration.size() - 2); weightIter++){
				weightVector.add(weightIter, Math.pow(-1, (int)(Math.random() * 2)) * Math.random() * 0.5);
				weightChangeVector.add(0, 0.0);
			}
			weights.add(weightVector);
			weightChange.add(weightChangeVector);
		}
		network.setWeights(weights, false);
		network.setWeights(weightChange, true);
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
		while(true){
			
			// holds the last computed output
			ArrayList<Double> computedOutput = new ArrayList<>();
			
			// holds the last expected output
			ArrayList<Double> expectedOutput = new ArrayList<>();
			
			// all the serialized networks (weights) of a single run
			ArrayList<ArrayList<ArrayList<Double>>> allWeights = new ArrayList<>();

			// all the squared errors, used to find average
			ArrayList<ArrayList<Double>> squaredErrors = new ArrayList<>();
			
			// get sample data set
			int numInputs = network.getInputLayer().getNodes().size();
			int sampleSize = (int)(Math.pow(1.8, numInputs) * 10000);
			ArrayList<ArrayList<Double>> dataset = Rosenbrock.getRosenbrockSample(sampleSize, numInputs);
			
			// iterate over each sample data point
			int samplePointIter = 0;
			for(ArrayList<Double> samplePoint : dataset){

				// set inputs and expected output
				for(int inputIter = 0; inputIter < samplePoint.size() - 1; inputIter++){
					if(network.getInputLayer().getNodes().get(inputIter).getInputs().size() != 0){
						network.getInputLayer().getNodes().get(inputIter).getInputs().clear();
					}
					network.getInputLayer().getNodes().get(inputIter).getInputs().add(samplePoint.get(inputIter));
				}
				expectedOutput = new ArrayList<>();
				expectedOutput.add(samplePoint.get(samplePoint.size() - 1));
				
				// execute the nodes in the network and save computed output
				computedOutput = this.executeNodes(network);

				// add squared error to list
				squaredErrors.add(samplePointIter, this.getSquaredError(expectedOutput, computedOutput));
				
				// Save original weights
				ArrayList<ArrayList<Double>> originalWeights = (ArrayList<ArrayList<Double>>)Network.serializeNetwork(network).clone();
				
				// set delta values then update weights
				this.setOutputDeltas(network, expectedOutput);
				this.setHiddenDeltas(network);
				this.updateFinalNodeWeights(network);
				this.updateHiddenNodeWeights(network);
				
				// Save updated weights
				allWeights.add((ArrayList<ArrayList<Double>>)Network.serializeNetwork(network).clone());
				
				// Reset to original weights
				network.setWeights(originalWeights, false);
				
				samplePointIter++;
			}
			
			// find average of all weights
			ArrayList<ArrayList<Double>> averagedWeights = new ArrayList<>();
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
				ArrayList<ArrayList<Double>> originalWieghts = (ArrayList<ArrayList<Double>>)Network.serializeNetwork(network);
				network.setWeights(this.getChangeInWeights(averagedWeights, originalWieghts), true);
				network.setWeights(averagedWeights, false);

//				// print previous weights
//				System.out.println("Previous Weights");
//				for(ArrayList<Double> node : originalWieghts){
//					System.out.print("Node: ");
//					for(Double weight : node){
//						System.out.print(weight + " ");
//					}
//					System.out.print("\n");
//				}
//
//				// print new weights
//				System.out.println("\nNew Weights");
//				for(ArrayList<Double> node : averagedWeights){
//					System.out.print("Node: ");
//					for(Double weight : node){
//						System.out.print(weight + " ");
//					}
//					System.out.print("\n");
//				}
				
				// print error
				ArrayList<Double> averagedErrors = this.getAveragedSquareError(squaredErrors);
				for(int i = 0; i < averagedErrors.size(); i++){
					System.out.println("Average squared error for output node " + i + ": " + averagedErrors.get(i) + "\n");
				}
				squaredErrors.clear();
			}
			else{
				// break out of while loop if we have converged
				break;
			}
		}
		
		return network;
	}

	/**
	 * Gets the list of lists of differences in weights
	 * @param newWeights the new weight matrix
	 * @param oldWeights the previous weight matrix
	 * @return the matrix of differences
	 */
	private ArrayList<ArrayList<Double>> getChangeInWeights(ArrayList<ArrayList<Double>> newWeights, ArrayList<ArrayList<Double>> oldWeights){

		ArrayList<ArrayList<Double>> change = new ArrayList<>();
		for(int nodeIter = 0; nodeIter < newWeights.size(); nodeIter++){
			change.add(nodeIter, new ArrayList<>());
			for(int weightIter = 0; weightIter < newWeights.get(nodeIter).size(); weightIter++){
				Double difference = newWeights.get(nodeIter).get(weightIter) - oldWeights.get(nodeIter).get(weightIter);
				change.get(nodeIter).add(difference);
			}
		}
		return change;
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
		ArrayList<Double> networkOutput = new ArrayList<>();
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
		
		ArrayList<Double> squaredError = new ArrayList<>();
		for(int outputIter = 0; outputIter < expectedOutput.size(); outputIter++){
			Double error =  Math.pow(expectedOutput.get(outputIter) - computedOutput.get(outputIter), 2);
			squaredError.add(outputIter, error);
		}
		return squaredError;
	}

	/**
	 * Computes the average square error given a list of errors
	 * @param allErrors the list of all squared errors
	 * @return the average squared error
	 */
	private ArrayList<Double> getAveragedSquareError(ArrayList<ArrayList<Double>> allErrors){

		double[] sums = new double[allErrors.get(0).size()];
		for(int errorIter = 0; errorIter < allErrors.size(); errorIter++){
			for(int i = 0; i < allErrors.get(errorIter).size(); i++){
				sums[i] += allErrors.get(errorIter).get(i);
			}
		}
		ArrayList<Double> averageError = new ArrayList<>();
		for(int sumIter = 0; sumIter < sums.length; sumIter++){
			averageError.add(sums[sumIter] / allErrors.size());
		}
		return averageError;
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
					Double weightChange = 0.0;
					weightChange += ((1 - this.momentumValue) * this.learningRate * hiddenNode.getBackpropDelta() * hiddenNode.getInputs().get(inputIter));
					weightChange += (this.momentumValue * hiddenNode.getPrevWeightChange().get(inputIter));
					Double originalWeight = hiddenNode.getWeights().get(inputIter);
					hiddenNode.getWeights().remove(inputIter);
					hiddenNode.getWeights().add(inputIter, originalWeight + weightChange);
					hiddenNode.getPrevWeightChange().remove(inputIter);
					hiddenNode.getPrevWeightChange().add(inputIter, weightChange);
				}
			}
		}
	}
	
	/**
	 * update the weights to the final nodes
	 * @param network the Network to update weights on
	 */
	private void updateFinalNodeWeights(Network network){
		
		for(Node outputNode : network.getOutputLayer().getNodes()){
			for(int inputIter = 0; inputIter < outputNode.getInputs().size(); inputIter++){
				Double weightChange = 0.0;
				weightChange += ((1 - this.momentumValue) * this.learningRate * outputNode.getBackpropDelta() * outputNode.getInputs().get(inputIter));
				weightChange += (this.momentumValue * outputNode.getPrevWeightChange().get(inputIter));
				Double originalWeight = outputNode.getWeights().get(inputIter);
				outputNode.getWeights().remove(inputIter);
				outputNode.getWeights().add(inputIter, originalWeight + weightChange);
				outputNode.getPrevWeightChange().remove(inputIter);
				outputNode.getPrevWeightChange().add(inputIter, weightChange);
			}
		}
	}
}
