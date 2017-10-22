/**
 * 
 */
package neuralNetwork_v2;

/**
 * Defines how a Node computes an output
 * 
 * @author Elias
 *
 */
interface INodeFunction {

	/**
	 * Computes the output for the given node
	 * @param node the node to compute the output for
	 * @return the computed output of the given node
	 */
	public Double execute(Node node);
}
