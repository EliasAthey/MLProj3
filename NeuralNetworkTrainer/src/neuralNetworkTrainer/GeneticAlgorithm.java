package neuralNetworkTrainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Random;

public class GeneticAlgorithm extends TrainingAlgorithm {

    RouletteWheel rouletteWheel; //used to randomly select parents weighted by their rank
    ArrayList<ArrayList<Double>> geneStandardDev; //holds standardDev of all genes
    Random randNum = new Random();

    public ArrayList<Network> generatePopulation() {
        //if this is the same for GA ES and DE  maybe we should move this functionality to TrainingAlgorithm

        ArrayList<Network> population = new ArrayList<Network>();

        //create populationSize number of individuals and add them to population
        for (int popIter = 0; popIter < Driver.populationSize; popIter++) {

            Network individual = new Network(true);
            population.add(individual);
        }
        this.rouletteWheel = new RouletteWheel();
        return population;
    }

    //calculates the standard deviation for every gene in the population across all individuals
    public void setGeneStDev(ArrayList<ArrayList<ArrayList<Double>>> population) {
        this.geneStandardDev = new ArrayList<>();

        for (int chromIter = 0; chromIter < population.get(0).size(); chromIter++) {
            ArrayList<Double> chromosome = new ArrayList<>();
            this.geneStandardDev.add(chromosome);

            for (int geneIter = 0; geneIter < population.get(0).get(0).size(); geneIter++) {
                //find mean of a gene
                double geneMean = 0;
                ArrayList<Double> geneValues = new ArrayList<Double>();
                for (int individualIter = 0; individualIter < population.size(); individualIter++) {
                    geneMean += population.get(individualIter).get(chromIter).get(geneIter);
                    //record the value of each gene
                    geneValues.add(population.get(individualIter).get(chromIter).get(geneIter));
                }
                //calc mean
                geneMean = geneMean / (double) population.size();
                double squareDistSum = 0;
                //sum squared distance from mean to geneValues
                for (Double geneVal : geneValues) {
                    squareDistSum += ((geneMean - geneVal) * (geneMean - geneVal));
                }
                //divide by number of data points -1
                double meanSquareDist = squareDistSum / (double) population.size();
                double stdev = Math.sqrt(meanSquareDist);

                this.geneStandardDev.get(chromIter).add(stdev);

            }

        }
    }

    //converts matrixes into networks for fitness evaluation
    public ArrayList<Network> deserializePopulation(ArrayList<ArrayList<ArrayList<Double>>> population) {
        ArrayList<Network> deserializedPopulation = new ArrayList<Network>();

        for (ArrayList<ArrayList<Double>> individual : population) {
            deserializedPopulation.add(Network.deserializeToNetwork(individual));
        }

        return deserializedPopulation;
    }

    //converts networks into matrixes for reproduction
    public ArrayList<ArrayList<ArrayList<Double>>> serializePopulation(ArrayList<Network> population) {
        ArrayList<ArrayList<ArrayList<Double>>> serializedPopulation = new ArrayList<ArrayList<ArrayList<Double>>>();

        for (Network individual : population) {
            serializedPopulation.add(Network.serializeNetwork(individual, false));
        }

        return serializedPopulation;
    }

    //creates a new generation using rank based selection of parents, crossover and mutation, then returns the new generation
    //This is specific to having 2 offspring from 2 parents but is generalizable to any number of parents and offspring with a little refactoring
    public ArrayList<ArrayList<ArrayList<Double>>> newGeneration(ArrayList<ArrayList<ArrayList<Double>>> population) {

        ArrayList<ArrayList<ArrayList<Double>>> offspringPool = new ArrayList<ArrayList<ArrayList<Double>>>();    //holds the new offspring
        ArrayList<ArrayList<ArrayList<Double>>> parentPair;                                                    //holds 2 parents for crossover / reproduction
        ArrayList<ArrayList<ArrayList<Double>>> offspringPair;                                                    //holds the 2 offspring created by crossover

        while (offspringPool.size() < Driver.numberOffspring) {                                                    //continue until the new generation is the required offspring size
            parentPair = selectParents(population);                                            //selects 2 parents with rank based selection
            offspringPair = crossoverOffspring(parentPair.get(0), parentPair.get(1));        //creates 2 new offspring via crossover
            offspringPool.add(offspringPair.get(0));            //adds new offspring to the new generation
            offspringPool.add(offspringPair.get(1));            //adds new offspring to the new generation
        }

        offspringPool = mutateOffspring(offspringPool);            //mutates all offspring

        return offspringPool;
    }

    //uses the roulette wheel to select 2 parents at random
    //while giving higher ranked individuals a higher chance of being picked
    public ArrayList<ArrayList<ArrayList<Double>>> selectParents(ArrayList<ArrayList<ArrayList<Double>>> population) {

        ArrayList<ArrayList<ArrayList<Double>>> parentPair = new ArrayList<ArrayList<ArrayList<Double>>>();

        //select 2 parents randomly based on rank using the roulette wheel
        parentPair.add(population.get(this.rouletteWheel.get(this.randNum.nextInt(this.rouletteWheel.size))));
        parentPair.add(population.get(this.rouletteWheel.get(this.randNum.nextInt(this.rouletteWheel.size))));

        return parentPair;
    }

    //creates a List of boolean Lists that mirrors the dimensions and structure of an individual
    //uses the list to randomly assign genes from parents to offspring
    public ArrayList<ArrayList<ArrayList<Double>>> crossoverOffspring(ArrayList<ArrayList<Double>> parent1, ArrayList<ArrayList<Double>> parent2) {

        //List of boolean Lists to decide if offspring get their genes from parent1 or parent2
        ArrayList<ArrayList<Boolean>> randomizer = new ArrayList<ArrayList<Boolean>>();

        for (int chromIter = 0; chromIter < parent1.size(); chromIter++) {

            //a chromosome of random booleans that is the same length of the chromosomes of the parents and offspring
            ArrayList<Boolean> randChrom = new ArrayList<Boolean>();
            for (int geneIter = 0; geneIter < parent1.get(0).size(); geneIter++) {
                //fill every randChrom with random booleans
                randChrom.add(this.randNum.nextBoolean());
            }

            randomizer.add(randChrom);
        }

        //make offspring
        ArrayList<ArrayList<Double>> offspring1 = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Double>> offspring2 = new ArrayList<ArrayList<Double>>();

        //randomizer selects which offspring gets which gene
        for (int chromIter = 0; chromIter < parent1.size(); chromIter++) {
            offspring1.add(new ArrayList<Double>());
            offspring2.add(new ArrayList<Double>());

            for (int geneIter = 0; geneIter < parent1.get(0).size(); geneIter++) {

                if (randomizer.get(chromIter).get(geneIter)) {//give the double from parent1 to offspring1 and p2 to offsp2
                    offspring1.get(chromIter).add(parent1.get(chromIter).get(geneIter));
                    offspring2.get(chromIter).add(parent2.get(chromIter).get(geneIter));
                } else {//flip which offspring get which gene
                    offspring1.get(chromIter).add(parent2.get(chromIter).get(geneIter));
                    offspring2.get(chromIter).add(parent1.get(chromIter).get(geneIter));
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
    public ArrayList<ArrayList<ArrayList<Double>>> mutateOffspring(ArrayList<ArrayList<ArrayList<Double>>> offspring) {

        for (final ListIterator<ArrayList<ArrayList<Double>>> individualIter = offspring.listIterator(); individualIter.hasNext(); ) {
            final ArrayList<ArrayList<Double>> individual = individualIter.next();


            for (final ListIterator<ArrayList<Double>> chromIter = individual.listIterator(); chromIter.hasNext(); ) {
                final ArrayList<Double> chromosome = chromIter.next();

                for (final ListIterator<Double> geneIter = chromosome.listIterator(); geneIter.hasNext(); ) {
                    final Double gene = geneIter.next();

                    if (Math.random() <= Driver.mutationRate) { //gives a mustationRate % chance of a mutation happening on any given gene
                        //change gene with a random number from a gaussian distribution
                        //centered at 0
                        //standard deviation is the standard deviation for that particular gene
                        geneIter.set(gene + geneStandardDev.get(individual.indexOf(chromosome)).get(chromosome.indexOf(gene)) * this.randNum.nextGaussian());
                    }
                }
            }
        }
        return offspring;
    }


    /**
     * calculates the fitness of the population using a random subset of data and sorts according to that fitness
     *
     * @param population individuals to clac fitness for
     * @return population sorted according to their fitness
     */
    public ArrayList<Network> evalFitness(ArrayList<Network> population) {

        ArrayList<ArrayList<Object>> evalSet = Driver.dataset.getEvalDataSet(0);

        for (Network individual : population) {
            double fitness = 0;

            for (ArrayList<Object> datapoint : evalSet) {
                if (individual.evaluate(datapoint)) { //returns true or false for classification
                    fitness++;
                }
            }

            fitness = fitness / evalSet.size();
            individual.setFitness(fitness);
        }

        //sorts population based on fitness
        Collections.sort(population);
        return population;
    }

    public Boolean hasConverged(ArrayList<Network> currentPopulation, ArrayList<Network> prevPopulation) {
        return false;
    }


    @Override
    Network train() {
        // TODO
        ArrayList<Network> prevPopulation = new ArrayList<>();
        ArrayList<Network> offspring;
        ArrayList<ArrayList<ArrayList<Double>>> serializedPopulation;
        ArrayList<ArrayList<ArrayList<Double>>> serializedOffspring;

        ArrayList<Network> population = generatePopulation();
        population = evalFitness(population);

        while (!hasConverged(population, prevPopulation)) {
            serializedPopulation = serializePopulation(population);
            setGeneStDev(serializedPopulation);
            serializedOffspring = newGeneration(serializedPopulation);
            offspring = deserializePopulation(serializedOffspring);
            offspring = evalFitness(offspring);
            prevPopulation = population;
            population = replacePop(offspring, population);

        }
        //returns highest fit individual after convergence
        return population.get(population.size() - 1);
    }

    private ArrayList<Network> replacePop(ArrayList<Network> offspring, ArrayList<Network> prevGeneration) {
        ArrayList<Network> nextGeneration = new ArrayList<>();
        while (nextGeneration.size() < Driver.populationSize) {

            //if the fittest offspring is fitter than the fittest individual from prevGeneration,
            //add offspring to nextGen and remove from offspring
            if (offspring.get(offspring.size() - 1).getFitness() >= prevGeneration.get(prevGeneration.size() - 1).getFitness()) {
                nextGeneration.add(offspring.get(offspring.size() - 1));
                offspring.remove(offspring.size() - 1);
            } else {
                nextGeneration.add(prevGeneration.get(prevGeneration.size() - 1));
                prevGeneration.remove(prevGeneration.size() - 1);
            }
        }
        return nextGeneration;
    }
}
