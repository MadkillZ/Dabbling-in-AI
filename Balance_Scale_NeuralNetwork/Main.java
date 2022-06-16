import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        boolean isRunning = true;
        System.out.println("Which file is your training data? (training.data | training2.data)");
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        List <String[]> data = new ArrayList<String[]>();
        Scanner sc = new Scanner(new File(fileName));
        while (sc.hasNext())
        {
            data.add(sc.next().split(","));
        }
        System.out.println("Size of training data: " + data.size() + "\n");
        sc.close();  //closes the scanner

        //Reading in test data
        System.out.println("Which file is your test data? (test.data)");
        scanner = new Scanner(System.in);
        fileName = scanner.nextLine();
        List <String[]> testData = new ArrayList<String[]>();
        Scanner sc2 = new Scanner(new File(fileName));
        while (sc2.hasNext()) {
            testData.add(sc2.next().split(","));
        }
        System.out.println("Size of test data: " + testData.size() + "\n");
        sc2.close();
        while(isRunning){
            scanner = new Scanner(System.in);
            System.out.println("Enter the amount of hidden layers");
            int nrOfHiddenLayers = scanner.nextInt();
            int [] hiddenLayers = new int[nrOfHiddenLayers+2];
            for(int k=0;k<hiddenLayers.length;k++){
                if(k!=0 && k!=hiddenLayers.length-1) {
                    System.out.println("Enter the amount of neurons in layer " + (k+1));
                    int nrOfNeurons = scanner.nextInt();
                    hiddenLayers[k] = nrOfNeurons;
                }
                else if(k==0){
                    hiddenLayers[k] = 4;
                }
                else if(k==hiddenLayers.length-1){
                    hiddenLayers[k] = 2;
                }
            }

            System.out.println("Enter the max amount of iterations");
            int nrOfItterations = scanner.nextInt();

            //Creating Neural Network
            Network nn = new Network(hiddenLayers); //6,3 got 100% accuracy
            for(int k = 0; k < nrOfItterations; k++) {
                for (int i = 0; i < data.size(); i++) {
                    double[] input = new double[4];
                    for (int j = 1; j < 5; j++) {
                        input[j - 1] = Double.parseDouble(data.get(i)[j]);
                    }
                    double[] output = new double[2];
                    switch (data.get(i)[0]) {
                        case "B" -> {
                            output[0] = 1;
                            output[1] = 1;
                        }
                        case "L" -> {
                            output[0] = 1.0;
                            output[1] = 0.0;
                        }
                        case "R" -> {
                            output[0] = 0.0;
                            output[1] = 1.0;
                        }
                    }

                    nn.train(input, output, 0.2);
                }

                int correct = 0;
                for(int i = 0; i < testData.size(); i++) {
                    double[] input = new double[4];
                    for (int j = 1; j < 5; j++) {
                        input[j - 1] = Double.parseDouble(testData.get(i)[j]);
                    }
                    char type = 'N';
                    switch (testData.get(i)[0]) {
                        case "B" -> {
                            type  = 'B';
                        }
                        case "L" -> {
                            type = 'L';
                        }
                        case "R" -> {
                            type = 'R';
                        }
                    }
                    double[] test = nn.calculate(input);
                    if(test[0] < 0.5 && test[1] > 0.5 && type == 'R'){
                        correct++;
                    }
                    else if(test[0] > 0.5 && test[1] < 0.5 && type == 'L'){
                        correct++;
                    }
                    else if(test[0] > 0.5 && test[1] > 0.5 && type == 'B'){
                        correct++;
                    }
                    else{
                        //System.out.println("Incorrect");
//                        System.out.println(type + " " + Arrays.toString(input));
//                        System.out.println(Arrays.toString(test));
                    }
                }
                if (correct == testData.size()){
                    System.out.println("Iterations run: " + k);
                    break;
                }

            }

            //testing accuracy
            int correct = 0;
            for(int i = 0; i < testData.size(); i++) {
                double[] input = new double[4];
                for (int j = 1; j < 5; j++) {
                    input[j - 1] = Double.parseDouble(testData.get(i)[j]);
                }
                char type = 'N';
                switch (testData.get(i)[0]) {
                    case "B" -> {
                        type  = 'B';
                    }
                    case "L" -> {
                        type = 'L';
                    }
                    case "R" -> {
                        type = 'R';
                    }
                }
                double[] test = nn.calculate(input);
                if(test[0] < 0.5 && test[1] > 0.5 && type == 'R'){
                    correct++;
                }
                else if(test[0] > 0.5 && test[1] < 0.5 && type == 'L'){
                    correct++;
                }
                else if(test[0] > 0.5 && test[1] > 0.5 && type == 'B'){
                    correct++;
                }
                else{
                    //System.out.println("Incorrect");
                    System.out.println(type + " " + Arrays.toString(input));
                    System.out.println(Arrays.toString(test));
                }
            }
            System.out.println("\n" + correct + " out of " + testData.size());
            System.out.println("Accuracy: " + (double)correct/testData.size()* 100 + "%");


//            double [] inputs = {1,1,1,1};
//            double [] output = nn.calculate(inputs);
//            System.out.println("\n" + Arrays.toString(output));
//            if(output[0] < 0.5 && output[1] > 0.5){
//                System.out.println("Right");
//            }
//            else if(output[0] > 0.5 && output[1] < 0.5){
//                System.out.println("Left");
//            }
//            else{
//                System.out.println("Balanced");
//            }


            System.out.println("Do you want to see the weight matrix? (y/n)");
            String answer = scanner.next();
            if(answer.equals("y")){
                System.out.println("\n" + nn.getWeightsAsString());
            }

            //run through training data
            int accuracy = 0;
            for (int i = 0; i < data.size(); i++) {
                double[] input = new double[4];
                for (int j = 1; j < 5; j++) {
                    input[j - 1] = Double.parseDouble(data.get(i)[j]);
                }
                double[] output = nn.calculate(input);
                if(output[0] < 0.5 && output[1] > 0.5 && data.get(i)[0].equals("R")){
                    accuracy++;
                }
                else if(output[0] > 0.5 && output[1] < 0.5 && data.get(i)[0].equals("L")){
                    accuracy++;
                }
                else if(output[0] > 0.5 && output[1] > 0.5 && data.get(i)[0].equals("B")){
                    accuracy++;
                }
                else{
//                    System.out.println("Incorrect");
//                    System.out.println(data.get(i)[0] + " " + Arrays.toString(input));
//                    System.out.println(Arrays.toString(output));
                }

            }


            //print weights to text file
            try {
                PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
                writer.println("Weights:");
                writer.println(nn.getWeightsAsString() + "\n");
                writer.println("Biases:");
                writer.println(nn.getBiasAsString() + "\n");
                writer.println("Accuracy for test data: " + (double)correct/testData.size()* 100 + "%");
                writer.println("Accuracy for training data: " + (double)accuracy/data.size()* 100 + "%");
                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            System.out.println("Enter 1 to continue, 0 to exit");
            int input = scanner.nextInt();
            if(input == 0){
                isRunning = false;
            }
        }
        System.out.println("Goodbye");
    }
}
