package neuralNetworkTrainer;

import java.util.ArrayList;
import java.util.Collections;

public class EvolutionStrategy extends TrainingAlgorithm {

	public ArrayList<Network> generatePopulation() {

		// if this is the same for GA ES and DE maybe we should move this functionality
		// to TrainingAlgorithm

		ArrayList<Network> population = null;

		// create populationSize number of individuals and add them to population
		for (int popIter = 0; popIter < Driver.populationSize; popIter++) {

			// adding a global config in driver might be worth doing
			// or passing config through train() to all these methods
			Network individual = new Network(Driver.configuration);
			population.add(individual);
		}
		return population;
	}

	// converts matrixes into networks for fitness evaluation
	public ArrayList<Network> deserializePopulation(ArrayList<ArrayList<ArrayList<Double>>> population) {
		ArrayList<Network> deserializedPopulation = new ArrayList<Network>();

		for (ArrayList<ArrayList<Double>> individual : population) {
			deserializedPopulation.add(Network.deserializeToNetwork(Driver.configuration, individual));
		}

		return deserializedPopulation;
	}

	// converts networks into matrixes for reproduction
	public ArrayList<ArrayList<ArrayList<Double>>> serializePopulation(ArrayList<Network> population) {
		ArrayList<ArrayList<ArrayList<Double>>> serializedPopulation = new ArrayList<ArrayList<ArrayList<Double>>>();

		for (Network individual : population) {
			serializedPopulation.add(Network.serializeNetwork(individual));
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

	// calls the correct fitness evaluation measure
	private ArrayList<Network> evalFitness(ArrayList<Network> population) {
		ArrayList<Network> fitPop = null;
		if (Driver.isClassificationNetwork) {
			fitPop = evalClasificationFitness(population);
		} else {
			fitPop = evalFunApproxFitness(population);
		}
		return fitPop;
	}

	private ArrayList<Network> evalFunApproxFitness(ArrayList<Network> population) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<Network> evalClasificationFitness(ArrayList<Network> population) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean hasConverged(ArrayList<Network> currentPopulation, ArrayList<Network> prevPopulation) {
		// TODO
		return null;
	}

	@Override
	Network train() {
		// TODO Auto-generated method stub
		return null;
	}

}
