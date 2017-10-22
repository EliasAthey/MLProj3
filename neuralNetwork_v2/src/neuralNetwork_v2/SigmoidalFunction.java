/**
 * 
 */
package neuralNetwork_v2;

/**
 * @author Elias
 *
 */
class SigmoidalFunction implements INodeFunction{

	/*
	 * (non-Javadoc)
	 * @see neuralNetwork_v2.INodeFunction#execute(neuralNetwork_v2.Node)
	 */
	@Override
	public Double execute(Node node){
		
		Double weightedSum = 0.0;
		for(int inputIter = 0; inputIter < node.getInputs().size(); inputIter++){
			weightedSum += node.getInputs().get(inputIter) * node.getWeights().get(inputIter);
		}
		return 1 / (1 + Math.exp(-1 * weightedSum));
	}
}
