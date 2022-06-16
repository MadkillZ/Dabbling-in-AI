import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static long seed = 2;
    public static Random rn = new Random(Main.seed);
    public static void main(String[] args) throws Exception{
        boolean stop = false;
        int populationSize = 1000;
        int maxGenerations = 100;
        int depth = 1;
        while(!stop){
            System.out.println("Input population size: ");
            Scanner ss = new Scanner(System.in);
            populationSize = ss.nextInt();
            System.out.println("Input max number of generations: ");
            maxGenerations = ss.nextInt();
            System.out.println("Input depth of tree: ");
            depth = ss.nextInt();
            System.out.println("Input seed: ");
            seed = ss.nextLong();
            rn = new Random(Main.seed);
            System.out.println("tictactoe or breastcancer: ");
            String game = ss.next();
            if(game.equals("tictactoe")){
                List <String[]> data = new ArrayList<String[]>();
                Scanner sc = new Scanner(new File("tictactoe_Train.csv"));
                while (sc.hasNext())
                {
                    data.add(sc.next().split(","));
                }
                sc.close();  //closes the scanner
                //System.out.println(data);
                List <String[]> data2 = new ArrayList<String[]>();
                Scanner sc2 = new Scanner(new File("tictactoe_Test.csv"));
                while (sc2.hasNext())
                {
                    data2.add(sc2.next().split(","));
                }
                sc2.close();  //closes the scanner

                GeneticProgram gp = new GeneticProgram(data, populationSize, maxGenerations, depth);
                chromosome test = gp.run();
                System.out.println("\nBest Result from training: " + (test.fitness*100) + "%");
//        System.out.println("Evaluating the test data...");
                System.out.println("Test Result: " + test.getEvaluation(data2)*100 + "%");
                //test.trimTree(test.root,0);
                System.out.println("Do you want to print the tree? (y/n)");
                String print = ss.next();
                if(print.equals("y")){
                    test.printTree(test.root,0);
                }
            }
            else {
                List <String[]> data = new ArrayList<String[]>();
                Scanner sc = new Scanner(new File("breast_cancer_Train.csv"));
                while (sc.hasNext())
                {
                    data.add(sc.next().split(","));
                }
                sc.close();  //closes the scanner
                //System.out.println(data);
                List <String[]> data2 = new ArrayList<String[]>();
                Scanner sc2 = new Scanner(new File("breast_cancer_Test.csv"));
                while (sc2.hasNext())
                {
                    data2.add(sc2.next().split(","));
                }
                sc2.close();  //closes the scanner

                GeneticProgram gp = new GeneticProgram(data, populationSize, maxGenerations, depth);
                chromosome test = gp.run();
                System.out.println("\nBest Result from training: " + (test.fitness*100) + "%");
//        System.out.println("Evaluating the test data...");
                System.out.println("Test Result: " + test.getEvaluation(data2)*100 + "%");
                //test.trimTree(test.root,0);
                System.out.println("Do you want to print the tree? (y/n)");
                String print = ss.next();
                if(print.equals("y")){
                    test.printTree(test.root,0);
                }
            }
            System.out.println("Done!");
            System.out.println("Do you want to run again? (y/n)");
            String again = ss.next();
            if(again.equals("n")){
                stop = true;
            }
        }

    }
}
