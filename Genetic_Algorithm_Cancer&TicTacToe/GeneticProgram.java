import java.util.*;

public class GeneticProgram {
    List <String[]> data = null;
    int populationSize = 0;
    int maxGeneration = 0;
    int maxDepth = 0;

    public GeneticProgram(List<String[]> data, int populationSize, int maxGenerations, int maxDepth) {
        this.data = data;
        this.populationSize = populationSize;
        this.maxGeneration = maxGenerations;
        this.maxDepth = maxDepth;
    }

    public chromosome run() {
        System.out.println("Running Genetic Program\n");
        System.out.println("Intializing population: \n");
        List <chromosome> population = new ArrayList<chromosome>();
        double bestFitness = -1;
        for(int i = 0; i < populationSize; i++){
            population.add(new chromosome(maxDepth));
            population.get(i).generateTree();
            double temp = population.get(i).getEvaluation(data);
            //System.out.println(temp);
            if(temp > bestFitness){
                bestFitness = temp;
            }
            //System.out.println(temp);
        }
        //System.out.println("Best Fitness: " + bestFitness);
        chromosome best = population.get(0);
        for(int i = 0; i < maxGeneration; i++){
            List <chromosome> newPopulation = new ArrayList<chromosome>();
            sortPopulation(population);
            //sort(population);
            for(int j = 0; j < roundEven(populationSize*0.4); j++){
                chromosome temp = new chromosome(maxDepth);
                temp.root = chromosome.cloneBinaryTree(population.get(j).root);
                //temp.getEvaluation(data);
                newPopulation.add(temp);
            }
            for(int j = 0; j < newPopulation.size()/2; j++){
                crossOver(newPopulation.get(j), population.get(j+1));
            }
            int count = 0;
            for(int j=populationSize-1;j > roundEven(populationSize*0.6); j--){
                population.remove(j);
                population.add(newPopulation.get(count));
                count++;
            }

//            for(int j = 0; j < populationSize; j++){
//                population.get(j).getEvaluation(data);
//            }
//            sortPopulation(population);
            //sort(population);
            double temp2=0;
            for(int j = (int) roundEven(populationSize*0.1); j < populationSize; j++){
                mutate(population.get(j),60);
                population.get(j).getEvaluation(data);
                population.get(j).trimTree(population.get(j).root,0);
            }

            for(int j = 0; j < populationSize; j++){
//                mutate(population.get(j),20);
                population.get(j).trimTree(population.get(j).root,0);
                population.get(j).getEvaluation(data);
                if(population.get(j).fitness > temp2){
                    temp2 = population.get(j).fitness;
                    best = population.get(j);
                }
                //System.out.println(population.get(j).fitness);
            }
            System.out.print("Generation: " + (i+1) +" ");
            System.out.println("Best Fitness: " + temp2);
            newPopulation.clear();
        }
        //best.printTree(best.root,0);
        return best;

    }

    public double roundEven(double d) {
        return Math.round(d / 2) * 2;
    }

    public double bestFitness(List<chromosome> population){
        double best = -1;
        for(int i = 0; i < population.size(); i++){
            if(population.get(i).fitness > best){
                best = population.get(i).fitness;
            }
        }
        return best;
    }

    //sorting function for population using fitness
    public void sortPopulation(List <chromosome> population){
        for(int i = 0; i < population.size(); i++){
            for(int j = 0; j < population.size(); j++){
                if(population.get(i).fitness > population.get(j).fitness){
                    chromosome temp = population.get(i);
                    population.set(i, population.get(j));
                    population.set(j, temp);
                }
            }
        }
    }

    //quicksort function for population using fitness


    public void crossOver(chromosome parent1, chromosome parent2){
        int crossOverPoint1 = (int) (Math.random() * parent1.getTotFunc()) + 1;
        int crossOverPoint2 = (int) (Math.random() * parent2.getTotFunc()) + 1;
        //System.out.println(crossOverPoint1 + " " + crossOverPoint2);
        //List<node> visited = new ArrayList<node>();
        List<node> unVisited1 = new ArrayList<node>();
            if(parent1.getDepth(parent1.root) >= 1 && parent2.getDepth(parent2.root) >= 1) {
                node currentNode1 = parent1.root;
                for (int i = 0; i < crossOverPoint1 - 1; i++) {
                    if (currentNode1.left != null) {
                        unVisited1.add(currentNode1.left);
                    } else if (currentNode1.right != null) {
                        unVisited1.add(currentNode1.right);
                    }
                    if (unVisited1.get(0).data.equals("+") || unVisited1.get(0).data.equals("-") || unVisited1.get(0).data.equals("*") || unVisited1.get(0).data.equals("/")) {
                        currentNode1 = unVisited1.get(0);
                        unVisited1.remove(0);
                    }

                }

                List<node> unVisited2 = new ArrayList<node>();
                node currentNode2 = parent2.root;
                for (int i = 0; i < crossOverPoint2 - 1; i++) {
                    if (currentNode2.left != null) {
                        unVisited2.add(currentNode2.left);
                    } else if (currentNode2.right != null) {
                        unVisited2.add(currentNode2.right);
                    }
                    if (unVisited2.get(0).data.equals("+") || unVisited2.get(0).data.equals("-") || unVisited2.get(0).data.equals("*") || unVisited2.get(0).data.equals("/")) {
                        currentNode2 = unVisited2.get(0);
                        unVisited2.remove(0);
                    }
                }

                boolean side1 = true; //true = left, false = right
                boolean side2 = true; //true = left, false = right
                if (currentNode1.left == null || !(currentNode1.left.data.equals("+") || currentNode1.left.data.equals("-") || currentNode1.left.data.equals("*") || currentNode1.left.data.equals("/"))) {
                    side1 = false;
                }
                if (currentNode2.left != null && !(currentNode2.left.data.equals("+") || currentNode2.left.data.equals("-") || currentNode2.left.data.equals("*") || currentNode2.left.data.equals("/"))) {
                    side2 = false;
                }
                if (side1) {
                    node temp = currentNode1.left;
                    if(side2) {
                        currentNode1.left = currentNode2.left;
                        currentNode2.left = temp;
                    }
                    else {
                        currentNode1.left = currentNode2.right;
                        currentNode2.right = temp;
                    }
                }
                else {
                    node temp = currentNode1.right;
                    if(side2) {
                        currentNode1.right = currentNode2.left;
                        currentNode2.left = temp;
                    }
                    else {
                        currentNode1.right = currentNode2.right;
                        currentNode2.right = temp;
                    }
            }

        }
    }

    public static void mutate(chromosome parent, int mutationRate) {
//        Random r = new Random(Main.seed);
//        Random rn = new Random(Main.seed);
        int mutation = (int) (Math.random() * 100) + 1;

        if (mutation < mutationRate) {
            if(parent.getDepth(parent.root) > 2) {
                int coinToss = (int) chromosome.coinFlip();
                if (coinToss == 1 && parent.getDepth(parent.root) < (parent.maxDepth-1)) {
                    grow(parent);
                }
                else {
                    shrink(parent);
                }
            }
            else{
                grow(parent);
            }
        }
    }

    public static void grow(chromosome parent) {
        //System.out.println("Growing");
        int mutationPoint = (int) (Math.random() * parent.getTotTerm()) + 1;
        List<node> unVisited = new ArrayList<node>();
        node currentNode = parent.root;
        chromosome newChromosome = new chromosome(4);
        newChromosome.generateTree();
        newChromosome.trimTree(newChromosome.root,0);
        if(mutationPoint > 1) {
            for (int i = 0; i < mutationPoint; i++) {
                if (currentNode.left != null) {
                    unVisited.add(currentNode.left);
                }
                if (currentNode.right != null) {
                    unVisited.add(currentNode.right);
                }
                currentNode = unVisited.get(0);
                unVisited.remove(0);
            }
        }
        if (currentNode.data.contains("f")) {
            currentNode.data = newChromosome.root.data;
            currentNode.left = newChromosome.root.left;
            currentNode.right = newChromosome.root.right;
        }
        else{
            currentNode.data = chromosome.randomOperator();
        }
    }

    public static void shrink(chromosome parent) {
        int mutationPoint = (int) (Math.random() * parent.getTotFunc()) + 1;
        //System.out.println("Mutation point: " + mutationPoint);
        List<node> unVisited = new ArrayList<node>();
        node currentNode = parent.root;
        if (mutationPoint > 1) {
            for (int i = 0; i < mutationPoint; i++) {
                if (currentNode.left != null) {
                    unVisited.add(currentNode.left);
                }
                if (currentNode.right != null) {
                    unVisited.add(currentNode.right);
                }
                currentNode = unVisited.get(0);
                unVisited.remove(0);
            }
        }
        if(currentNode.data.contains("f")){
            int randomNum = (int) (Math.random() * 8) + 1;
            currentNode.data = "f" + randomNum;
            currentNode.left = null;
            currentNode.right = null;
        }
        else{
            currentNode.data = chromosome.randomOperator();
        }
    }

    //tournament selection function for the genetic algorithm
    public chromosome tournamentSelection(List<chromosome> population) {
        Random rand = new Random(Main.seed);
        int [] tournamentRandom = new int[(int)roundEven(population.size() * 0.2)];
        for (int i = 0; i < tournamentRandom.length; i++) {
            tournamentRandom[i] = rand.nextInt(population.size());
            //System.out.println(tournamentRandom[i]);
        }
        List<chromosome> testCase = new ArrayList<chromosome>();
        for (int i = 0; i < tournamentRandom.length; i++) {
                testCase.add(population.get(tournamentRandom[i]));
        }
        sortPopulation(testCase);
        return testCase.get(0);

    }

        public void sort(List <chromosome> arr)
        {
            int n = arr.size();

            // Build heap (rearrange array)
            for (int i = n / 2 - 1; i >= 0; i--)
                heapify(arr, n, i);

            // One by one extract an element from heap
            for (int i = n - 1; i > 0; i--) {
                // Move current root to end
                chromosome temp = arr.get(0);
                arr.set(0, arr.get(i));
                arr.set(i, temp);

                // call max heapify on the reduced heap
                heapify(arr, i, 0);
            }
        }

        // To heapify a subtree rooted with node i which is
        // an index in arr[]. n is size of heap
        void heapify(List <chromosome> arr, int n, int i)
        {
            int largest = i; // Initialize largest as root
            int l = 2 * i + 1; // left = 2*i + 1
            int r = 2 * i + 2; // right = 2*i + 2

            // If left child is larger than root
            if (l < n && arr.get(l).fitness > arr.get(largest).fitness)
                largest = l;

            // If right child is larger than largest so far
            if (r < n && arr.get(r).fitness > arr.get(largest).fitness)
                largest = r;

            // If largest is not root
            if (largest != i) {
                Collections.swap(arr, i, largest);

                // Recursively heapify the affected sub-tree
                heapify(arr, n, largest);
            }
        }

}
