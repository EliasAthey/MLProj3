package neuralNetworkTrainer;

import sun.nio.ch.Net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class EvolutionStrategy extends TrainingAlgorithm {


	private RouletteWheel rouletteWheel; //used to randomly select parents weighted by their rank
	Random randNum = new Random();


	public ArrayList<IndividualES> generatePopulation(){

		ArrayList<IndividualES> population = new ArrayList<>();

		//create populationSize number of individuals and add them to population
		for(int popIter = 0;  popIter < Driver.populationSize; popIter++) {

			IndividualES individual = new IndividualES();
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
	// an individual
	// uses the list to randomly assign genes from parents to offspring
	public ArrayList<ArrayList<ArrayList<Double>>> crossoverOffspring(ArrayList<ArrayList<Double>> parent1,
			ArrayList<ArrayList<Double>> parent2) {
		// TODO
		return null;
	}

	// for any number of offspring:
	// look at every Double and with a Driver.mutationRate chance
	// change that value by getting a random gaussian distributed number centered at
	// 0 with a standard deviation of 1
	public ArrayList<ArrayList<ArrayList<Double>>> mutateOffspring(ArrayList<ArrayList<ArrayList<Double>>> offspring) {

		// TODO
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
