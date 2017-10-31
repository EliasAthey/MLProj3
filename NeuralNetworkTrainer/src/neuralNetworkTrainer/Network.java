/**
 * 
 */
package neuralNetworkTrainer;

import java.util.ArrayList;

/**
 * The Network class is a container for a neural network.
 * 
 * @author Elias
 *
 */
class  Network implements Comparable {

	/**
	 * The input layer
	 */
	private Layer inputLayer;
	
	/**
	 * The hidden layers
	 */
	private ArrayList<Layer> hiddenLayers;
	
	/**
	 * The output layer
	 */
	private Layer outputLayer;
	
	/**
	 * The fitness of this network
	 */
	private double fitness;

	/**
	 * Constructs a network given a configuration
	 * 
	 * @param configuration each number represents the number of nodes in the layer, first index is associated to the input layer, last index is the ouptut layer.
	 */
	Network(ArrayList<Integer> configuration){
		
		this.inputLayer = new Layer();
		this.hiddenLayers = new ArrayList<Layer>();
		this.outputLayer = new Layer();
		
		// create output layer, these nodes use linear function and have no downstream nodes
		for(int nodeIter = 0; nodeIter < configuration.get(configuration.size() - 1); nodeIter++){
			this.outputLayer.getNodes().add(nodeIter, new Node(new LinearFunction(), new ArrayList<Node>(), nodeIter));
		}
		
		// create hidden layers in reverse, starting at the second to last index of configuration
		ArrayList<Node> downstreamNodes = this.outputLayer.getNodes();
		for(int layerIter = configuration.size() - 2; layerIter > 0; layerIter--){
			this.hiddenLayers.add(new Layer());
		}
		for(int layerIter = configuration.size() - 2; layerIter > 0; layerIter--){
			
			// create hidden nodes for this layer, these nodes use sigmoidal function
			for(int nodeIter = 0; nodeIter < configuration.get(layerIter); nodeIter++){
				this.hiddenLayers.get(layerIter - 1).getNodes().add(nodeIter, new Node(new SigmoidalFunction(), downstreamNodes, nodeIter));
			}
			downstreamNodes = this.hiddenLayers.get(layerIter - 1).getNodes();
		}
		
		// create input layer, these node use sigmoidal function
		for(int nodeIter = 0; nodeIter < configuration.get(0); nodeIter++){
			this.inputLayer.getNodes().add(new Node(new SigmoidalFunction(), downstreamNodes, nodeIter));
		}
	}
	
	/**
	 * Gets the input layer of this network
	 * @return the input layer of this network
	 */
	Layer getInputLayer(){
		return this.inputLayer;
	}
	
	/**
	 * Gets the hidden layers of this network
	 * @return the hidden layers of this network
	 */
	ArrayList<Layer> getHiddenLayers(){
		return this.hiddenLayers;
	}
	
	/**
	 * Gets the output layer of this network
	 * @return the output layer of this network
	 */
	Layer getOutputLayer(){
		return this.outputLayer;
	}
	
	/**
	 * Creates a weighted adjacency "matrix" (really a list of lists) representing this network in its current state.
	 * @param network the network to serialize
	 * @return an adjacency "matrix" representing this network and its weights.
	 * 		   The top-level list contains an entry for each node; index 0 is the first input node; the final index is the final output node.
	 * 		   The sub-list contains weights for the top-level index Node (ie list.get(A) is the weights of node A).
	 */
	static ArrayList<ArrayList<Double>> serializeNetwork(Network network){
		
		ArrayList<ArrayList<Double>> weights = new ArrayList<ArrayList<Double>>();
		for(Node node : network.inputLayer.getNodes()){
			weights.add((ArrayList<Double>)node.getWeights().clone());
		}
		for(Layer hiddenLayer : network.getHiddenLayers()){
			for(Node node : hiddenLayer.getNodes()){
				weights.add((ArrayList<Double>)node.getWeights().clone());
			}
		}
		for(Node node : network.outputLayer.getNodes()){
			weights.add((ArrayList<Double>)node.getWeights().clone());
		}
		return weights;
	}
	
	/**
	 * Creates a Network given a representative weighted adjacency matrix
	 * @param configuration the configuration fo the network
	 * @param weights matrix the weighted adjacency matrix representing a network
	 * @return the network represented by the weighted adjacency matrix
	 */
	static Network deserializeToNetwork(ArrayList<Integer> configuration, ArrayList<ArrayList<Double>> weights){
		
		Network network = new Network(configuration);
		network.setWeights(weights, false);
		return network;
	}
	
	/**
	 * Sets the weights in this network
	 * @param weights the weight "matrix" (list of lists) representing the weights
	 * 		  The top-level list should contain an entry for each node; index 0 is the first input node; the final index is the final output node.
	 * 		  The sub-list should contain the weights for the top-level index Node (ie list.get(A) is the weights of node A).
	 * @param isWeightChange if true, sets the prevWeightChange of the nodes instead
	 */
	void setWeights(ArrayList<ArrayList<Double>> weights, Boolean isWeightChange){

		int weightsIter = 0;
		for(Node node : this.inputLayer.getNodes()){
			if(isWeightChange){
				node.getPrevWeightChange().clear();
			}
			else{
				node.getWeights().clear();
			}
			for(Double weight : weights.get(weightsIter)){
				if(isWeightChange){
					node.getPrevWeightChange().add(weight);
				}
				else{
					node.getWeights().add(weight);
				}
			}
			weightsIter++;
		}
		for(Layer hiddenLayer : this.getHiddenLayers()){
			for(Node node : hiddenLayer.getNodes()){
				if(isWeightChange){
					node.getPrevWeightChange().clear();
				}
				else{
					node.getWeights().clear();
				}
				for(Double weight : weights.get(weightsIter)){
					if(isWeightChange){
						node.getPrevWeightChange().add(weight);
					}
					else{
						node.getWeights().add(weight);
					}
				}
				weightsIter++;
			}
		}
		for(Node node : this.outputLayer.getNodes()){
			if(isWeightChange){
				node.getPrevWeightChange().clear();
			}
			else{
				node.getWeights().clear();
			}
			for(Double weight : weights.get(weightsIter)){
				if(isWeightChange){
					node.getPrevWeightChange().add(weight);
				}
				else{
					node.getWeights().add(weight);
				}
			}
			weightsIter++;
		}
	}
	
	public double getFitness() {
		return this.fitness;
	}	
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	@Override
	public int compareTo(Object otherNetwork) {
		return (int)(this.fitness - ((Network) otherNetwork).getFitness());
	}
}

