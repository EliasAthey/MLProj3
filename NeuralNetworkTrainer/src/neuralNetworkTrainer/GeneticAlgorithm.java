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
			Network individual = new Network(Driver.configuration); //do we need to randomize the weights????
			population.add(individual);
		}
		return population;
	}
	
	public ArrayList<Network> selectParents(ArrayList<Network> population){
		//TODO
		//using rank
		//make parent pairs
		//
		return null;
	}
	
	public ArrayList<ArrayList<ArrayList<Double>>> crossoverOffspring(ArrayList<ArrayList<Double>> parent1, ArrayList<ArrayList<Double>> parent2){
		//TODO
		//make random arraylist of boolean arraylists that is the same size of the parents
		ArrayList<ArrayList<Boolean>> randomizer = new ArrayList<ArrayList<Boolean>>();
		
		for(int outerIter = 0; outerIter < parent1.size(); outerIter++) {
			
			ArrayList<Boolean> randChrom = new ArrayList<Boolean>();
			
			for(int innerIter = 0; innerIter < parent1.get(0).size(); innerIter++) {
				randChrom.add(Driver.randNum.nextBoolean());
			}
			
			randomizer.add(randChrom);
		}
		
		//make offspring 
		ArrayList<ArrayList<Double>> offspring1 = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> offspring2 = new ArrayList<ArrayList<Double>>();
		
		//randomizer selects which offspring gets which gene
		for(int outerIter = 0; outerIter < parent1.size(); outerIter++) {
			offspring1.add(new ArrayList<Double>());
			offspring2.add(new ArrayList<Double>());
			
			for(int innerIter = 0; innerIter < parent1.get(0).size(); innerIter++) {
				
				if (randomizer.get(outerIter).get(innerIter)) {
					offspring1.get(outerIter).add(parent1.get(outerIter).get(innerIter));
					offspring2.get(outerIter).add(parent2.get(outerIter).get(innerIter));
				}
				
				else {//flip which offspring get which gene
					offspring2.get(outerIter).add(parent1.get(outerIter).get(innerIter));
					offspring2.get(outerIter).add(parent2.get(outerIter).get(innerIter));
				}
			}
		}
		ArrayList<ArrayList<ArrayList<Double>>> offspringPair = new ArrayList<ArrayList<ArrayList<Double>>>();
		offspringPair.add(offspring2);
		offspringPair.add(offspring1);
		return offspringPair;
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

	//classification
	//TODO:
	//need to be able to run a single data point through the network in Network.evaluate(datapoint) method
	//need a set of evaluation data
	public ArrayList<Network> evalClasificationFitness(ArrayList<Network> population){
		//evalutaion set of data == eval
		ArrayList<ArrayList<Double>> eval = new ArrayList<ArrayList<Double>>();
		
//		for(Network individual: population) {
//			double fitness;
//			for(Object datapoint: eval) {
//				if(individual.evaluate(eval)) { //returns true or false for classification
//					fitness++;
//				}
//			}
//
//			fitness = fitness/eval.size();
//		}
		
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
