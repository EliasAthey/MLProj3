package neuralNetworkTrainer;

import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm extends TrainingAlgorithm {

	private int populationSize;

	public ArrayList<Network> generatePopulation(ArrayList<Integer> configuration){
		//if this is the same for GA ES and DE  maybe we should move this functionality to TrainingAlgorithm
		
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
		double pm = .05; //mutation rate. probly store this in driver or something
		Random randNum = new Random(); //this could be global in the driver. no reason to make one in every method that needs it

		for (Network individual : offspring) {
			ArrayList<ArrayList<Double>> matrixIndividual = Network.serializeNetwork(individual);
			for (ArrayList<Double> chromosome : matrixIndividual) {
				for(Double gene : chromosome) {
					if (Math.random() <= pm) { //gives a pm% chance of a mutation happening on any given gene
						//change gene with a random number from a gaussian distribution 
						//centered at 0 with a standard deviation of 1
						gene += randNum.nextGaussian();
					}
				}
			}
			offspring.remove(individual);
			offspring.add(Network.deserializeToNetwork(Driver.configuration, matrixIndividual));
			
		}
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
		// TODO 
		//initialize population
		//evaluateFitness(population)
		//while(hasConverged()){
		//	createOffspring()
		//	crossoverOffspring() -- uniform crossover
		//	mutateOffspring() -----	done 
		//	evaluateFitness(offspring)
		//	replacePop(offspring, offspring.fitness)
		//}
		//return population;
		return null;
	}

}
