/**
 * 
 */
package neuralNetworkTrainer;

/**
 * @author Elias
 *
 */
class SigmoidalFunction implements INodeFunction{

	/*
	 * (non-Javadoc)
	 * @see neuralNetworkTrainer.INodeFunction#execute(neuralNetworkTrainer.Node)
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
