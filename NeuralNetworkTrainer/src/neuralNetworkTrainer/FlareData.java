package neuralNetworkTrainer;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class FlareData extends Data{

    /**
     * Constructs FlareData
     */
    FlareData(){
        numInputs = 10;
        numOutputs = 3;
        dataPoints = new ArrayList<>();
        this.setDataPoints();
    }

    /**
     * Sets the data points by scanning the file
     */
    private void setDataPoints(){
        Scanner fileScanner = new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream("flare2.data"));
        do{
            try{
                String line = fileScanner.nextLine();
                String[] entries = line.split(",");
                ArrayList<Object> values = new ArrayList<>();
                for(int entryIter = 0; entryIter < entries.length; entryIter++){
                    if(entryIter == 0){
                        char letter = entries[entryIter].charAt(0);
                        switch(letter){
                            case 'A': values.add(1); break;
                            case 'B': values.add(2); break;
                            case 'C': values.add(3); break;
                            case 'D': values.add(4); break;
                            case 'E': values.add(5); break;
                            case 'F': values.add(6); break;
                            case 'H': values.add(7); break;
                        }
                    }
                    else if(entryIter == 1){
                        char letter = entries[entryIter].charAt(0);
                        switch(letter){
                            case 'X': values.add(1); break;
                            case 'R': values.add(2); break;
                            case 'S': values.add(3); break;
                            case 'A': values.add(4); break;
                            case 'H': values.add(5); break;
                            case 'K': values.add(6); break;
                        }
                    }
                    else if(entryIter == 2){
                        char letter = entries[entryIter].charAt(0);
                        switch(letter){
                            case 'X': values.add(1); break;
                            case 'O': values.add(2); break;
                            case 'I': values.add(3); break;
                            case 'C': values.add(4); break;
                        }
                    }
                    else{
                        values.add(Integer.parseInt(entries[entryIter]));
                    }
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
