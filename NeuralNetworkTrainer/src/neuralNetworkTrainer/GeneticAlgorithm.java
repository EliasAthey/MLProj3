package neuralNetworkTrainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneticAlgorithm extends TrainingAlgorithm {

	private int populationSize;

	public ArrayList<Network> generatePopulation(){
		//if this is the same for GA ES and DE  maybe we should move this functionality to TrainingAlgorithm
		
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
	
	public ArrayList<ArrayList<ArrayList<Double>>> crossoverOffspring(ArrayList<ArrayList<ArrayList<Double>>> parents){
		//TODO
		return null;
	}

	public ArrayList<ArrayList<ArrayList<Double>>> mutateOffspring(ArrayList<ArrayList<ArrayList<Double>>> offspring){
		double pm = .05; //mutation rate. probly store this in driver or something

		for (ArrayList<ArrayList<Double>> individual : offspring) {

			for (ArrayList<Double> chromosome : individual) {
				for(Double gene : chromosome) {
					if (Math.random() <= pm) { //gives a pm% chance of a mutation happening on any given gene, lower for higher fitness individuals maybe???? 
						//change gene with a random number from a gaussian distribution 
						//centered at 0 with a standard deviation of 1
						gene += Driver.randNum.nextGaussian();
					}
				}
			}
		}
		//TODO
		return offspring;
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
		// TODO 
		//initialize population
		//evaluateFitness(population)
		//while(hasConverged()){
		//	serialize population
		//	selectOffspring()
		//	crossoverOffspring() -- uniform crossover
		//	mutateOffspring() -----	done 
		//	unserialize population
		//	evaluateFitness(offspring)
		//	replacePop(offspring, offspring.fitness)
		//}
		//return population;
		return null;
	}

}
