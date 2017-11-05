package neuralNetworkTrainer;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class MachineData extends Data{

    /**
     * Construct MachineData
     */
    MachineData(){
        numInputs = 6;
        numOutputs = 1;
        dataPoints = new ArrayList<>();
        this.setDataPoints();
    }

    /**
     * Sets the data points by scanning the file
     */
    private void setDataPoints(){
        Scanner fileScanner = new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream("machine.data"));
        do{
            try{
                String line = fileScanner.nextLine();
                String[] entries = line.split(",");
                ArrayList<Object> values = new ArrayList<>();
                for(String entry : entries){
                    values.add(Integer.parseInt(entry));
                }
                dataPoints.add(values);
            }
            catch(NoSuchElementException e){
                break;
            }
        }
        while(true);
    }
}
