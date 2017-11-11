package neuralNetworkTrainer;

import sun.nio.ch.Net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class EvolutionStrategy extends TrainingAlgorithm {

	private double mutOperator = .82;
	private RouletteWheel rouletteWheel; //used to randomly select parents weighted by their rank
	Random randNum = new Random();


	public ArrayList<IndividualES> generatePopulation(){

		ArrayList<IndividualES> population = new ArrayList<>();

		//create populationSize number of individuals and add them to population
		for(int popIter = 0;  popIter < Driver.populationSize; popIter++) {

			IndividualES individual = new IndividualES(true);
			population.add(individual);
		}
		this.rouletteWheel = new RouletteWheel();
		Collections.sort(population);
		return population;
	}

	// converts matrixes into networks for fitness evaluation
	public ArrayList<Network> deserializePopulation(ArrayList<ArrayList<ArrayList<Double>>> population) {
		ArrayList<Network> deserializedPopulation = new ArrayList<Network>();

		for (ArrayList<ArrayList<Double>> individual : population) {
			deserializedPopulation.add(Network.deserializeToNetwork(individual));
		}

		return deserializedPopulation;
	}

	// converts networks into matrixes for reproduction
	public ArrayList<ArrayList<ArrayList<Double>>> serializePopulation(ArrayList<Network> population) {
		ArrayList<ArrayList<ArrayList<Double>>> serializedPopulation = new ArrayList<ArrayList<ArrayList<Double>>>();

		for (Network individual : population) {
			serializedPopulation.add(Network.serializeNetwork(individual, false));
		}

		return serializedPopulation;
	}

	// creates a new generation using rank based selection of parents, crossover and
	// mutation, then returns the new generation
	// This is specific to having 2 offspring from 2 parents but is generalizable to
	// any number of parents and offspring with a little refactoring
	public ArrayList<ArrayList<ArrayList<Double>>> newGeneration(ArrayList<ArrayList<ArrayList<Double>>> population) {
		//TODO
		return null;
	}

	public ArrayList<ArrayList<ArrayList<Double>>> selectParents(ArrayList<ArrayList<ArrayList<Double>>> population) {
		// TODO
		return null;
	}

	// creates a List of boolean Lists that mirrors the dimensions and structure of
	// an individual and uses the list to randomly assign genes from parents to offspring
	public ArrayList<IndividualES> crossoverOffspring(IndividualES parent1, IndividualES parent2) {

		//List of boolean Lists to decide if offspring get their genes from parent1 or parent2
		ArrayList<ArrayList<Boolean>> randomizer = new ArrayList<ArrayList<Boolean>>();

		for (ArrayList<Double> chromosome: parent1.getGenome()) {

			//a chromosome of random booleans that is the same length of the chromosomes of the parents and offspring
			ArrayList<Boolean> randChrom = new ArrayList<Boolean>();

			for (Double gene: chromosome) {
				//fill every randChrom with random booleans
				randChrom.add(this.randNum.nextBoolean());
			}
			randomizer.add(randChrom);
		}

		//make offspring
		IndividualES offspring1 = new IndividualES(false);
		IndividualES offspring2 = new IndividualES(false);


		ArrayList<ArrayList<Double>> genomeOffspring1 = new ArrayList<>();
		ArrayList<ArrayList<Double>> genomeOffspring2 = new ArrayList<>();
		ArrayList<ArrayList<Double>> stratParamsOffspring1 = new ArrayList<>();
		ArrayList<ArrayList<Double>> stratParamsOffspring2 = new ArrayList<>();

		//randomizer selects which offspring gets which gene
		for(int chromIter = 0; chromIter < parent1.getGenome().size(); chromIter++) {
			genomeOffspring1.add(new ArrayList<>());
			genomeOffspring2.add(new ArrayList<>());
			stratParamsOffspring1.add(new ArrayList<>());
			stratParamsOffspring2.add(new ArrayList<>());

			for(int geneIter = 0; geneIter < parent1.getGenome().get(0).size(); geneIter++) {

				if (randomizer.get(chromIter).get(geneIter)) {//give the double from parent1 to offspring1 and p2 to offspring2
					genomeOffspring1.get(chromIter).add(parent1.getGenome().get(chromIter).get(geneIter));
					genomeOffspring2.get(chromIter).add(parent2.getGenome().get(chromIter).get(geneIter));

					//combine strategy parameters from both parents such that one parent s weighted more than the other
					stratParamsOffspring1.get(chromIter).add((parent1.getStrategyParams().get(chromIter).get(geneIter)));
					stratParamsOffspring2.get(chromIter).add((parent2.getStrategyParams().get(chromIter).get(geneIter)));
				}

				else {//flip which offspring get which gene
					genomeOffspring1.get(chromIter).add(parent2.getGenome().get(chromIter).get(geneIter));
					genomeOffspring2.get(chromIter).add(parent1.getGenome().get(chromIter).get(geneIter));

					stratParamsOffspring1.get(chromIter).add((parent2.getStrategyParams().get(chromIter).get(geneIter)));
					stratParamsOffspring2.get(chromIter).add((parent1.getStrategyParams().get(chromIter).get(geneIter)));
				}
			}
		}
		//add the genomes and the stratParams to the offspring
		offspring1.setGenome(genomeOffspring1);
		offspring2.setGenome(genomeOffspring2);
		offspring1.setStrategyParams(stratParamsOffspring1);
		offspring2.setStrategyParams(stratParamsOffspring2);

		//create the offspring pair that will be returned
		ArrayList<IndividualES> offspringPair = new ArrayList<>();
		offspringPair.add(offspring2);
		offspringPair.add(offspring1);
		return offspringPair;
	}

	// for any number of offspring:
	// look at every Double and with a Driver.mutationRate chance
	// change that value by getting a random gaussian distributed number centered at
	// 0 with a standard deviation of 1
	public ArrayList<IndividualES> mutateOffspring(ArrayList<IndividualES> offspring) {

		// TODO
		for (IndividualES individual: offspring) {
			//1/5th rule, best individuals mutate less
			if (offspring.indexOf(individual) >= ( this.mutOperator * offspring.size())){
				for (ArrayList<Double> chromosome: individual.getGenome()) {
					for (Double gene: chromosome){
						if( randNum.nextDouble() <= Driver.mutationRate){
							gene += individual.getStrategyParams()							//gets strategy parameter associated with the current gene
									.get(individual.getGenome().indexOf(chromosome))		//and uses it as a standard deviation for a random gaussian number
									.get(chromosome.indexOf(gene)) * randNum.nextGaussian();//that is used to mutate the gene
						}

					}
				}
			}
			else{
				//worse individuals mutate more
				if (offspring.indexOf(individual) >= ( this.mutOperator * offspring.size())){
					for (ArrayList<Double> chromosome: individual.getGenome()) {
						for (Double gene: chromosome){
							if( randNum.nextDouble() <= (Driver.mutationRate * 2) ){			//2 times as likely to mutate with 1.5 * greater deviation
								gene += individual.getStrategyParams()							//gets strategy parameter associated with the current gene
										.get(individual.getGenome().indexOf(chromosome))		//and uses it as a standard deviation for a random gaussian number
										.get(chromosome.indexOf(gene)) * randNum.nextGaussian() *1.5;//that is used to mutate the gene
							}

						}
					}
				}
			}
		}
		return null;
	}

	// evaluated the fitness of the population
	private ArrayList<IndividualES> evalFitness(ArrayList<IndividualES> population) {

		ArrayList<ArrayList<Object>> evalSet = Driver.dataset.getEvalDataSet(0);

		for(IndividualES individual: population) {
			double fitness = 0;

			for(ArrayList<Object> datapoint: evalSet) {
				if(individual.getNetwork().evaluate(datapoint)) { //returns true or false for classification
					fitness++;
				}
			}

			fitness = fitness/evalSet.size();
			individual.getNetwork().setFitness(fitness);
		}

		//sorts population based on fitness
		Collections.sort(population);
		return population;
	}


	public Boolean hasConverged(ArrayList<IndividualES> currentPopulation, ArrayList<IndividualES> prevPopulation) {
		// TODO
		return null;
	}

	@Override
	Network train() {
		//eval fitness of offspring before mutation--------------

		ArrayList<IndividualES> population = generatePopulation();
		population = evalFitness(population);
		///serializedPopulation = serializePopulation(population);

		//while(!hasConverged(population, prevPopulation)){
			//serializedOffspring = newGeneration(serializedPopulation);
			//offspring = deserializePopulation(serializedOffspring);
			//offspring = evalFitness(offspring);
			//prevPopulation = population;
			//population = replacePop(offspring, population);

		//}
		//returns highest fit individual after convergence
		return population.get(population.size() - 1).getNetwork();
	}

}
