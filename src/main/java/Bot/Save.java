package Bot;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;


public class Save {


    public String toFileName(String name) {
        return "./data/" + name + ".txt";
    }


    public void saveHours(double hours, String file) {
        try {
            FileWriter fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);
            pw.print(hours);
            pw.close();
        } catch (Exception e) {
            System.out.println("Unable to save.");
        }
    }


    public double loadHours(String file) {
        double load = 0;
        try {
            FileReader fr = new FileReader(file);
            Scanner scan = new Scanner(fr);
            load = scan.nextDouble();
            scan.close();
        } catch (Exception e) {
            System.out.println("Could not find file.");
        }
        return load;
    }
}
