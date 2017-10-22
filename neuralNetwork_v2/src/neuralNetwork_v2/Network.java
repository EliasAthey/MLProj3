/**
 * 
 */
package neuralNetwork_v2;

import java.util.ArrayList;

/**
 * The Network class is a container for a neural network.
 * 
 * @author Elias
 *
 */
class Network {

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
		
		// create hidden layers in reverse, starting at the second to last index of configuartion
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
	 * Creates a weighted adjacency matrix representing this network in its current state
	 * @param network the network to serialize
	 * @return an adjacency matrix representing this network and its weight
	 */
	public static Double[][] serializeToMatrix(Network network){
		
		/**
		 * TODO
		 * 
		 * # columns, # rows = # nodes in network
		 * matrix will be upper triangular and all zero on diagonal so it has no cycles
		 * 
		 */
		return null;
	}
	
	/**
	 * Creates a Network given a representative weighted adjacency matrix
	 * @param matrix the weighted adjacency matrix representing a network
	 * @return the network represented by the weighted adjacency matrix
	 */
	public static Network deserializeToNetwork(Double[][] matrix){
		
		/**
		 * TODO
		 * 
		 * create network
		 */
		return null;
	}
}

