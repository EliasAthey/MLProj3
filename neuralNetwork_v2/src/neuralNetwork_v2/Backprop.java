/**
 * 
 */
package neuralNetwork_v2;

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

		// set input weights to 1.0
		for(Node inputNode : network.getInputLayer().getNodes()){
			inputNode.getWeights().add(1.0);
		}
		
		// set random weights for hidden and output nodes, weights set between -0.5 and +0.5
		int numUpstreamNodes = network.getInputLayer().getNodes().size();
		for(Layer hiddenLayer : network.getHiddenLayers()){
			for(Node hiddenNode : hiddenLayer.getNodes()){
				for(int weightIter = 0; weightIter < numUpstreamNodes; weightIter++){
					hiddenNode.getWeights().add(Math.pow(-1, (int)(Math.random() * 2)) * Math.random() * 0.5);
				}
			}
			numUpstreamNodes = hiddenLayer.getNodes().size();
		}
		for(Node outputNode : network.getOutputLayer().getNodes()){
			for(int weightIter = 0; weightIter < numUpstreamNodes; weightIter++){
				outputNode.getWeights().add(Math.pow(-1, (int)(Math.random() * 2)) * Math.random() * 0.5);
			}
		}
		return network;
	}

	/**
	 * trains a network using backprop
	 */
	@Override
	Network train() {
		
		/**
		 * TODO
		 * 
		 * train network
		 * return resulting network
		 */
		
		// initialize network
		Network network = this.initializeNetwork();
		
		// get sample dataset
		int numInputs = network.getInputLayer().getNodes().size();
		int sampleSize = (int)(Math.pow(1.8, numInputs) * 100000);
		ArrayList<ArrayList<Double>> dataset = Rosenbrock.getRosenbrockSample(sampleSize, numInputs);
		
		// iterate over each sample datapoint
		int samplePointIter = 0;
		for(ArrayList<Double> samplePoint : dataset){
			
			// set inputs and expected output
			for(int inputIter = 0; inputIter < samplePoint.size() - 1; inputIter++){
				if(samplePointIter > 0){
					network.getInputLayer().getNodes().get(inputIter).getInputs().clear();
				}
				network.getInputLayer().getNodes().get(inputIter).getInputs().add(samplePoint.get(inputIter));
			}
			ArrayList<Double> expectedOutput = new ArrayList<Double>();
			expectedOutput.add(samplePoint.get(samplePoint.size() - 1));
			
			// execute the nodes in the network and save computed output
			ArrayList<Double> computedOutput = this.executeNodes(network);
			
			// print squared error
			ArrayList<Double> squaredError = this.getSquaredError(network, expectedOutput);
			for(int i = 0; i < squaredError.size(); i++){
				System.out.println("Squared error for output node " + i + ": " + squaredError.get(i));
			}
			
			// set delta values then update weights
			this.setOutputDeltas(network, expectedOutput);
			this.setHiddenDeltas(network);
			this.updateHiddenNodeWeights(network);
			this.updateFinalNodeWeights(network, expectedOutput);
			
			samplePointIter++;
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
	 * Computes the squared error between the network's computed output and the sample expected output
	 * @param expectedOutput the output defined by the sample
	 * @return the squared error between the network's computed output and the sample expected output
	 */
	private ArrayList<Double> getSquaredError(Network network, ArrayList<Double> expectedOutput){
		
		ArrayList<Double> squaredError = new ArrayList<Double>();
		for(int outputIter = 0; outputIter < expectedOutput.size(); outputIter++){
			Double error =  Math.pow(expectedOutput.get(outputIter) - network.getOutputLayer().getNodes().get(outputIter).getComputedOutput(), 2);
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
			Double delta = expected - computed;
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
				hiddenNode.setBackpropDelta(computed * (1.0 - computed) * downstreamSum);
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
