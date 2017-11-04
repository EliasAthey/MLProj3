package neuralNetworkTrainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneticAlgorithm extends TrainingAlgorithm {
	
	ArrayList<Integer> rouletteWheel; //used to randomly select parents weighted by their rank
	ArrayList<ArrayList<Double>> geneStandardDev = new ArrayList<ArrayList<Double>>();

	public ArrayList<Network> generatePopulation(){
		//if this is the same for GA ES and DE  maybe we should move this functionality to TrainingAlgorithm
		
		ArrayList<Network> population = new ArrayList<Network>();
		
		//create populationSize number of individuals and add them to population
		for(int popIter = 0;  popIter < Driver.populationSize; popIter++) {
			
			Network individual = new Network(Driver.configuration);
			population.add(individual);
		}
		this.rouletteWheel = genWheel();
		return population;
	}
	
	//calculates the standard deviation for every gene in the population across all individuals
	public void setGeneStDev(ArrayList<ArrayList<ArrayList<Double>>> population) {
		for (int chromIter = 0; chromIter < population.get(0).size(); chromIter++ ) {
			for (int geneIter = 0; geneIter < population.get(0).get(0).size(); geneIter++ ) {
				//find mean of a gene
				double geneMean = 0;
				ArrayList<Double> geneValues = new ArrayList<Double>();
				for (int individualIter = 0; individualIter < population.size(); individualIter++ ) {
					geneMean += population.get(individualIter).get(chromIter).get(geneIter);
					//record the value of each gene
					geneValues.add(population.get(individualIter).get(chromIter).get(geneIter));
				}
				//calc mean
				geneMean = geneMean/ (double) population.size();
				double squareDistSum = 0;
				//sum squared distance from mean to geneValues
				for (Double geneVal: geneValues) {
					squareDistSum += ((geneMean - geneVal) * (geneMean - geneVal));
				}
				//divide by number of data points -1 
				double meanSquareDist = squareDistSum/ (double) population.size();
				double stdev = Math.sqrt(meanSquareDist);

				geneStandardDev.get(chromIter).add(stdev);
				
			}
			
		}
	}
	
	//converts matrixes into networks for fitness evaluation
	public ArrayList<Network>  deserializePopulation (ArrayList<ArrayList<ArrayList<Double>>> population) {
		ArrayList<Network> deserializedPopulation = new ArrayList<Network>();
		
		for (ArrayList<ArrayList<Double>> individual : population) {
			deserializedPopulation.add(Network.deserializeToNetwork(Driver.configuration, individual));
		}
		
		return deserializedPopulation;
	}
	
	//converts networks into matrixes for reproduction 
	public ArrayList<ArrayList<ArrayList<Double>>> serializePopulation( ArrayList<Network> population){
		ArrayList<ArrayList<ArrayList<Double>>> serializedPopulation = new ArrayList<ArrayList<ArrayList<Double>>>();
		
		for (Network individual : population) {
			serializedPopulation.add(Network.serializeNetwork(individual));
		}
		
		return serializedPopulation;
	}
	
	//creates a new generation using rank based selection of parents, crossover and mutation, then returns the new generation
	//This is specific to having 2 offspring from 2 parents but is generalizable to any number of parents and offspring with a little refactoring
	public  ArrayList<ArrayList<ArrayList<Double>>> newGeneration( ArrayList<ArrayList<ArrayList<Double>>> population){
		
		ArrayList<ArrayList<ArrayList<Double>>> offspringPool = new ArrayList<ArrayList<ArrayList<Double>>>(); 	//holds the new offspring
		ArrayList<ArrayList<ArrayList<Double>>> parentPair; 													//holds 2 parents for crossover / reproduction
		ArrayList<ArrayList<ArrayList<Double>>> offspringPair;													//holds the 2 offspring created by crossover
		
		while(offspringPool.size() < Driver.numberOffspring) {													//continue until the new generation is the required offspring size
			parentPair = selectParents(population);											//selects 2 parents with rank based selection
			offspringPair = crossoverOffspring(parentPair.get(0), parentPair.get(1));		//creates 2 new offspring via crossover
			offspringPool.add(offspringPair.get(0));			//adds new offspring to the new generation
			offspringPool.add(offspringPair.get(1));			//adds new offspring to the new generation
		}

		offspringPool = mutateOffspring(offspringPool);			//mutates all offspring
		
		return offspringPool;
	}
	
	//uses the roulette wheel to select 2 parents at random 
	//while giving higher ranked individuals a higher chance of being picked
	public ArrayList<ArrayList<ArrayList<Double>>> selectParents(ArrayList<ArrayList<ArrayList<Double>>> population){

		ArrayList<ArrayList<ArrayList<Double>>> parentPair = new ArrayList<ArrayList<ArrayList<Double>>>();
		
		//select 2 parents randomly based on rank using the roulette wheel
		parentPair.add(population.get(this.rouletteWheel.get(Driver.randNum.nextInt(this.rouletteWheel.size()))));
		parentPair.add(population.get(this.rouletteWheel.get(Driver.randNum.nextInt(this.rouletteWheel.size()))));
		
		return parentPair;
	}
	
	//creates a List of boolean Lists that mirrors the dimensions and structure of an individual
	//uses the list to randomly assign genes from parents to offspring
	public ArrayList<ArrayList<ArrayList<Double>>> crossoverOffspring(ArrayList<ArrayList<Double>> parent1, ArrayList<ArrayList<Double>> parent2){
		
		//List of boolean Lists to decide if offspring get their genes from parent1 or parent2 
		ArrayList<ArrayList<Boolean>> randomizer = new ArrayList<ArrayList<Boolean>>();
		
		for(int chromIter = 0; chromIter < parent1.size(); chromIter++) {
			
			//a chromosome of random booleans that is the same length of the chromosomes of the parents and offspring
			ArrayList<Boolean> randChrom = new ArrayList<Boolean>();
			for(int geneIter = 0; geneIter < parent1.get(0).size(); geneIter++) {
				//fill every randChrom with random booleans
				randChrom.add(Driver.randNum.nextBoolean());
			}
			
			randomizer.add(randChrom);
		}
		
		//make offspring 
		ArrayList<ArrayList<Double>> offspring1 = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> offspring2 = new ArrayList<ArrayList<Double>>();
		
		//randomizer selects which offspring gets which gene
		for(int chromIter = 0; chromIter < parent1.size(); chromIter++) {
			offspring1.add(new ArrayList<Double>());
			offspring2.add(new ArrayList<Double>());
			
			for(int geneIter = 0; geneIter < parent1.get(0).size(); geneIter++) {
				
				if (randomizer.get(chromIter).get(geneIter)) {//give the double from parent1 to offspring1 and p2 to offsp2
					offspring1.get(chromIter).add(parent1.get(chromIter).get(geneIter));
					offspring2.get(chromIter).add(parent2.get(chromIter).get(geneIter));
				}
				
				else {//flip which offspring get which gene
					offspring2.get(chromIter).add(parent1.get(chromIter).get(geneIter));
					offspring2.get(chromIter).add(parent2.get(chromIter).get(geneIter));
				}
			}
		}
		//create the offspring pair that will be returned
		ArrayList<ArrayList<ArrayList<Double>>> offspringPair = new ArrayList<ArrayList<ArrayList<Double>>>();
		offspringPair.add(offspring2);
		offspringPair.add(offspring1);
		return offspringPair;
	}
	
	//for any number of offspring:
	//look at every Double and with a Driver.mutationRate chance
	//change that value by getting a random gaussian distributed number centered at 0 with a standard deviation of 1
	public ArrayList<ArrayList<ArrayList<Double>>> mutateOffspring(ArrayList<ArrayList<ArrayList<Double>>> offspring){
		for (ArrayList<ArrayList<Double>> individual : offspring) {

			for (ArrayList<Double> chromosome : individual) {
				for(Double gene : chromosome) {
					if (Math.random() <= Driver.mutationRate) { //gives a mustationRate % chance of a mutation happening on any given gene, lower for higher fitness individuals maybe????
						//change gene with a random number from a gaussian distribution 
						//centered at 0
						//standard deviation is the standard deviation for that particular gene
						gene += (geneStandardDev.get(individual.indexOf(chromosome)).get(chromosome.indexOf(gene)) * Driver.randNum.nextGaussian());
					}
				}
			}
		}
		return offspring;
	}

	//calls the correct fitness evaluation measure
	private ArrayList<Network> evalFitness(ArrayList<Network> population) {
		ArrayList<Network> fitPop = null;
		if (Driver.isClassificationNetwork) {
			fitPop = evalClasificationFitness(population);
		}
		else {
			fitPop = evalFunApproxFitness(population);
		}
		return fitPop;
	}
	

	private ArrayList<Network> evalFunApproxFitness(ArrayList<Network> population) {
		// TODO Auto-generated method stub
		return null;
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
		population = evalFitness(population);
		while(hasConverged(population, prevPopulation)){
			serializedPopulation = serializePopulation(population);
			setGeneStDev(serializedPopulation);
			prevPopulation = population;
			serializedOffspring = newGeneration(serializedPopulation);
			offspring = deserializePopulation(serializedOffspring);
			offspring = evalFitness(offspring);
		//	replacePop(offspring, offspring.fitness)
		//}
		//return population;
		}
		//returns highest fit individual after convergence
		return population.get(population.size() - 1);
	}

	//creates the roulette wheel for rank based selection
	//every value in the wheel corresponds to a rank
	//ranks with higher fiteness are more represented in the wheel
	//sampling randomly from the wheel will give higher fitness ranks 
	//a greater chance of being selected 
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
