package neuralNetworkTrainer;

import java.util.ArrayList;
import java.util.Random;

public class IndividualES implements Comparable {
    private ArrayList<ArrayList<Double>> genome;
    private ArrayList<ArrayList<Double>> strategyParams;
    private Network network;



    public IndividualES(boolean  initialPopulation){
        Random randNum = new Random();
        strategyParams = new ArrayList<>();
        genome = new ArrayList<>();
        if(initialPopulation){
            this.network = new Network(true);
            genome = Network.serializeNetwork(this.network, false);

            for (ArrayList<Double> chromosome: genome){
                ArrayList<Double> stratChrom = new ArrayList<>();
                for (Double gene: chromosome ) {
                    stratChrom.add(randNum.nextGaussian() * 15);
                }
                this.strategyParams.add(stratChrom);
            }
        }
    }

    public ArrayList<ArrayList<Double>> getGenome() {
        return genome;
    }

    public void setGenome(ArrayList<ArrayList<Double>> genome) {
        this.genome = genome;
    }

    public void setStrategyParams(ArrayList<ArrayList<Double>> strategyParams) {
        this.strategyParams = strategyParams;
    }

    public ArrayList<ArrayList<Double>> getStrategyParams() {
        return strategyParams;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    @Override
    public int compareTo(Object o) {
        return (int)(this.network.getFitness() - ((IndividualES) o).getNetwork().getFitness());
    }
}
