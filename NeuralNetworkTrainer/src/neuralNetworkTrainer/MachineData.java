package neuralNetworkTrainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class MachineData extends Data{

    /**
     * The path to the machine datafile
     */
    private final String filepath = "src/datasets/machine.data";

    /**
     * The machine datafile
     */
    private final File dataFile;

    /**
     * The data points
     */
    private ArrayList<ArrayList<Integer>> dataPoints = new ArrayList<>();

    /**
     * Construct MachineData
     */
    MachineData(){
        this.dataFile = new File(this.filepath);
        if(!dataFile.exists()){
            System.out.println("Error: " + this.filepath + " does not exist.\n");
            System.exit(0);
        }
        this.setDataPoints();
    }

    private void setDataPoints(){
        try{
            Scanner fileScanner = new Scanner(this.dataFile);
            do{
                try{
                    String line = fileScanner.nextLine();
                    System.out.println(line);
                }
                catch(NoSuchElementException e){
                    break;
                }
            }
            while(true);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
