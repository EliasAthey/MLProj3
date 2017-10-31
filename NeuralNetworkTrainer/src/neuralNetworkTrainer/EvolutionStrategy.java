package neuralNetworkTrainer;

import java.util.ArrayList;
import java.util.Collections;

public class EvolutionStrategy extends TrainingAlgorithm {

	private int populationSize;
	private int numberOfOffspring;


	public ArrayList<Network> generatePopulation(){
		
		//if this is the same for GA ES and DE maybe we should move this functionality to TrainingAlgorithm
		
		ArrayList<Network> population = null;
				
		//create populationSize number of individuals and add them to population
		for(int popIter = 0;  popIter < populationSize; popIter++) {
			
			//adding a global config in driver might be worth doing
			//or passing config through train() to all these methods
			Network individual = new Network(Driver.configuration);
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

	public ArrayList<Network> evaluateFitness(ArrayList<Network> population){
		//TODO
		//evaluate fitness for all memebers of the population
		//calc fitness
		
		//sorts population based on fitness
		Collections.sort(population);
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
