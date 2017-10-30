package neuralNetworkTrainer;

import java.util.ArrayList;

public class EvolutionStrategy extends TrainingAlgorithm {

	private int populationSize;
	private int numberOfOffspring;


	public ArrayList<Network> generatePopulation(ArrayList<Integer> configuration){
		
		//if this is the same for GA ES and DE maybe we should move this functionality to TrainingAlgorithm
		
		ArrayList<Network> population = null;
				
		//create populationSize number of individuals and add them to population
		for(int popIter = 0;  popIter < populationSize; popIter++) {
			Network individual = new Network(configuration);
			population.add(individual);
		}
		return population;
	}
	
	public ArrayList<Network> selectOffspring(ArrayList<Network> population){
		//TODO
		return null;
	}

	
	public ArrayList<Network> crossoverOffspring(ArrayList<Network> offspring){
		//TODO
		return null;
	}
	
	public ArrayList<Network> mutateOffspring(ArrayList<Network> offspring){
		//TODO
		return null;
	}

	public Double evaluateFitness(Network network){
		//TODO
		return null;
	}
	
	public Boolean hasConverged (ArrayList<Network> currentPopulation, ArrayList<Network> prevPopulation ){
		//TODO
		return null;
	}
	
	@Override
	Network train() {
		// TODO Auto-generated method stub
		return null;
	}

}
