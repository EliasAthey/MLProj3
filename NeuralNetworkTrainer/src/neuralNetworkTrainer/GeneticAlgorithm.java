package neuralNetworkTrainer;

import java.util.ArrayList;
import java.util.Collections;

public class GeneticAlgorithm extends TrainingAlgorithm {
	
	ArrayList<Integer> rouletteWheel;

	public ArrayList<Network> generatePopulation(){
		//if this is the same for GA ES and DE  maybe we should move this functionality to TrainingAlgorithm
		
		ArrayList<Network> population = null;
		
		//create populationSize number of individuals and add them to population
		for(int popIter = 0;  popIter < Driver.populationSize; popIter++) {
			
			//adding a global config in driver might be worth doing
			//or passing config through train() to all these methods
			Network individual = new Network(Driver.configuration); //do we need to randomize the weights????
			population.add(individual);
		}
		this.rouletteWheel = genWheel();
		
		return population;
	}
	
	public ArrayList<Network>  deserializePopulation (ArrayList<ArrayList<ArrayList<Double>>> population) {
		ArrayList<Network> deserializedPopulation = new ArrayList<Network>();
		
		for (ArrayList<ArrayList<Double>> individual : population) {
			deserializedPopulation.add(Network.deserializeToNetwork(Driver.configuration, individual));
		}
		
		return deserializedPopulation;
	}
	
	public ArrayList<ArrayList<ArrayList<Double>>> serializePopulation( ArrayList<Network> population){
		ArrayList<ArrayList<ArrayList<Double>>> serializedPopulation = new ArrayList<ArrayList<ArrayList<Double>>>();
		
		for (Network individual : population) {
			serializedPopulation.add(Network.serializeNetwork(individual));
		}
		
		return serializedPopulation;
	}
	
	//This is specific to having 2 offspring from 2 parents
	//I could modify it to create more or less offspring or use more parents
	//I think this is good for now though
	public  ArrayList<ArrayList<ArrayList<Double>>> newGeneration( ArrayList<ArrayList<ArrayList<Double>>> population){
		
		ArrayList<ArrayList<ArrayList<Double>>> offspringPool = new ArrayList<ArrayList<ArrayList<Double>>>();
		ArrayList<ArrayList<ArrayList<Double>>> parentPair;
		ArrayList<ArrayList<ArrayList<Double>>> offspringPair;
		
		while(offspringPool.size() < Driver.numberOffspring) {
			parentPair = selectParents(population);
			offspringPair = crossoverOffspring(parentPair.get(0), parentPair.get(1));
			offspringPool.add(offspringPair.get(0));
			offspringPool.add(offspringPair.get(1));
		}
		offspringPool = mutateOffspring(offspringPool);
		return offspringPool;
	}
	
	public ArrayList<ArrayList<ArrayList<Double>>> selectParents(ArrayList<ArrayList<ArrayList<Double>>> population){

		ArrayList<ArrayList<ArrayList<Double>>> parentPair = new ArrayList<ArrayList<ArrayList<Double>>>();
		
		//select 2 parents randomly based on rank using the roulette wheel
		parentPair.add(population.get(this.rouletteWheel.get(Driver.randNum.nextInt(this.rouletteWheel.size()))));
		parentPair.add(population.get(this.rouletteWheel.get(Driver.randNum.nextInt(this.rouletteWheel.size()))));
		
		return parentPair;
	}
	
	
	public ArrayList<ArrayList<ArrayList<Double>>> crossoverOffspring(ArrayList<ArrayList<Double>> parent1, ArrayList<ArrayList<Double>> parent2){

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
		for (ArrayList<ArrayList<Double>> individual : offspring) {

			for (ArrayList<Double> chromosome : individual) {
				for(Double gene : chromosome) {
					if (Math.random() <= Driver.mutationRate) { //gives a mustationRate % chance of a mutation happening on any given gene, lower for higher fitness individuals maybe????
						//change gene with a random number from a gaussian distribution 
						//centered at 0 with a standard deviation of 1
						gene += Driver.randNum.nextGaussian();
					}
				}
			}
		}
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
	
	//maybe we can pass this a boolean to differentiate between classification and function approx
	//we would only need to check the boolean for eval fitness, maybe hasConverged
	@Override
	Network train() {
		// TODO 
		ArrayList<Network> prevPopulation = null;
		ArrayList<Network> offspring =null;
		ArrayList<ArrayList<ArrayList<Double>>> serializedPopulation = new ArrayList<ArrayList<ArrayList<Double>>>();
		ArrayList<ArrayList<ArrayList<Double>>> serializedOffspring = new ArrayList<ArrayList<ArrayList<Double>>>();

		ArrayList<Network> population = generatePopulation();
		population = evalClasificationFitness(population);
		while(hasConverged(population, prevPopulation)){
			serializedPopulation = serializePopulation(population);
			prevPopulation = population;
			serializedOffspring = newGeneration(serializedPopulation);
			offspring = deserializePopulation(serializedOffspring);
			offspring = evalClasificationFitness(offspring);
		//	replacePop(offspring, offspring.fitness)
		//}
		//return population;
		return new Network(null);
		}
	}
	

	public ArrayList<Integer> genWheel(){
		ArrayList<Integer> wheel = null;
		
		for (int rank = 0; rank < Driver.populationSize; rank++) {
			for (int rankIter = 0; rankIter <= (rank/2); rankIter++) {
				wheel.add(rank);
			}
		}
		
		return wheel;
	}
}
