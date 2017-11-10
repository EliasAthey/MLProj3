package neuralNetworkTrainer;

import java.util.ArrayList;

public class IndividualES implements Comparable {
    private ArrayList<ArrayList<Double>> genome;
    private ArrayList<ArrayList<Double>> strategyParams;
    private Network network;

    public IndividualES(){
        this.network = new Network(true);
        genome = Network.serializeNetwork(this.network, false);
        for (ArrayList<Double> chromosome: genome){
            ArrayList<Double> stratChrom = new ArrayList<>();
            for (Double gene: chromosome ) {
                stratChrom.add(Math.random() * 50);
            }
            strategyParams.add(stratChrom);
        }
    }

    public ArrayList<ArrayList<Double>> getGenome() {
        return genome;
    }

    public ArrayList<ArrayList<Double>> getStrategyParams() {
        return strategyParams;
    }

    public Network getNetwork() {
        return network;
    }

    @Override
    public int compareTo(Object o) {
        return (int)(this.network.getFitness() - ((IndividualES) o).getNetwork().getFitness());
    }
}
