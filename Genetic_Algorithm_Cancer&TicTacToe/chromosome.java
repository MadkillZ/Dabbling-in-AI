import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class chromosome {
    public String [] dataSet;
    public node root;
    public double fitness;
    public int depth;
    public int maxDepth;
    private int totalTerminals;
    private int totalFunctions;

    public void setDataSet(String [] dataSet) {
        this.dataSet = dataSet;
    }

    public chromosome(int maxDepth) {
        root = null;
        fitness = 0;
        depth = 0;
        this.maxDepth = maxDepth;
    }

    public void generateTree() {
        //random amount of nodes between 1 and 9
        //Random rn = new Random(Main.seed);
        int totTerm = Main.rn.nextInt(9)+1;

        //System.out.println("totTerm: " + totTerm);
        int totFunc = totTerm - 1;
        totalTerminals = totTerm;
        totalFunctions = totFunc;
        //System.out.println("totFunc: " + totFunc);
        if(totTerm == 1) {
            root = new node("f1");
        }
        else{
            root = new node(randomOperator());
            node currentNode = root;
            totFunc--;
            while(totFunc > 0) {
                if(coinFlip() == 0) {
                    currentNode.left = new node(randomOperator());
                    totFunc--;
                    currentNode = currentNode.left;
                }
                else{
                    if(totFunc >= 2 && coinFlip() == 0) {
                        currentNode.right = new node(randomOperator());
                        currentNode.left = new node(randomOperator());
                        totFunc -= 2;
                    }
                    else{
                        currentNode.right = new node(randomOperator());
                        totFunc--;
                        currentNode = currentNode.right;
                    }

                }
            }
            for(int i = 0; i < totTerm; i++) {
                insert(root, new node("f" + (i+1)));
            }
        }
        //printTree(root,0);
    }

    public void trimTree(node root, int depth) {
        //recursive function to trim the tree
        //if(getDepth(root) > maxDepth) {
            if(depth == maxDepth) {
                if(root.data.equals("+") || root.data.equals("-") || root.data.equals("*") || root.data.equals("/")) {
                    if(root.left != null && root.left.data.contains("f")) {
                        root.data = root.left.data;
                        root.left = null;
                    }
                    else if(root.right != null && root.right.data.contains("f")) {
                        root.data = root.right.data;
                        root.right = null;
                        root.left = null;
                    }
                    else{
                        Random rn = new Random(Main.seed);
                        int randomNum = rn.nextInt(8)+1;
                        root.data = "f" + randomNum;
                        root.left = null;
                        root.right = null;
                    }
                }
            }
            if(root.left != null) {
                trimTree(root.left, depth + 1);
            }
            if(root.right != null) {
                trimTree(root.right, depth + 1);
            }
        //}


//        if(getDepth(root) > maxDepth) {
//            Random rn = new Random();
//            List<node> list = new ArrayList<>();
//            list.add(root);
//            int count = 0;
//            while(!list.isEmpty()) {
//                node current = list.remove(0);
//                if(current.left != null) {
//                    list.add(current.left);
//                }
//                if(current.right != null) {
//                    list.add(current.right);
//                }
//                count++;
//                if(count == maxDepth) {
//                    current.left = null;
//                    current.right = null;
//                    int randomNum = rn.nextInt(8)+1;
//                    current.data = "f" + randomNum;
//                }
//            }
//        }
    }


    public void printTree(node start, int depth1) {
        int deep = depth1;
        if(start == null) {
            System.out.print("");
        }
        else{
            System.out.println(start.data);
            if(start.left != null) {
                System.out.print('[' + deep + ']' +  "left: ");
                printTree(start.left, deep + 1);
            }
            if(start.right != null) {
                System.out.print('[' + deep + ']' +  "right: ");
                printTree(start.right, deep + 1);
            }
        }
    }

    int getDepth(node root)
    {
        if (root == null) {
            return 0;
        }
        return 1 + Math.max(getDepth(root.left), getDepth(root.right));
    }

    public void insert(node start, node newNode) {
        if(start == null) {
            start = newNode;
        }
        else{
            if(start.left == null && (start.data.equals("+") || start.data.equals("-") || start.data.equals("*") || start.data.equals("/"))) {
                start.left = newNode;
            }
            else if(start.right == null && (start.data.equals("+") || start.data.equals("-") || start.data.equals("*") || start.data.equals("/"))){
                start.right = newNode;
            }
            else{
                if(start.left != null) {
                    insert(start.left, newNode);
                }
                if(start.right != null) {
                    insert(start.right, newNode);
                }
            }
        }
    }

    public static String randomOperator(){
        String op = "";
        //randomly choose an operator from 1 to 4
        int rand = (int) (Math.random() * 4) + 1;
        switch (rand) { //switch statement to choose the operator
            case 1 -> op = "+";
            case 2 -> op = "-";
            case 3 -> op = "*";
            case 4 -> op = "/";
        }
        return op;
    }

    public double getFitness(String [] inputs, node currentNode) {
        if(currentNode == null) {
            return 0;
        }
        switch(currentNode.data) {
            case "+":
                return getFitness(inputs, currentNode.left) + getFitness(inputs, currentNode.right);
            case "-":
                return getFitness(inputs, currentNode.left) - getFitness(inputs, currentNode.right);
            case "*":
                return getFitness(inputs, currentNode.left) * getFitness(inputs, currentNode.right);
            case "/":{
                if(getFitness(inputs, currentNode.right) == 0) {
                    return 0;
                }
                return getFitness(inputs, currentNode.left) / getFitness(inputs, currentNode.right);
            }
            default:
                int index = Integer.parseInt(currentNode.data.substring(1));
                return Double.parseDouble(inputs[index - 1]);
        }
    }

    public double getEvaluation(List<String[]> data){
        double fitness = 0;
        int total = data.size();
        int fitnessCount = 0;
        int val = 0;
        for(String[] input : data) {
            fitness = getFitness(input, root);
            if(fitness < 0.5) {
                val = 0;
            }
            else if(fitness > 0.5) {
                val = 1;
            }
            if(val == Integer.parseInt(input[input.length - 1])) {
                fitnessCount++;
            }
        }
        double sum = (double) fitnessCount/ total;
        this.fitness = sum;
        return (double) sum;
        //System.out.println("Fitness: " + fitnessCount + "/" + total);
    }

    public static node cloneBinaryTree(node root)
    {
        // base case
        if (root == null) {
            return null;
        }

        // create a new node with the same data as the root node
        node root_copy = new node(root.data);

        // clone the left and right subtree
        root_copy.left = cloneBinaryTree(root.left);
        root_copy.right = cloneBinaryTree(root.right);

        // return cloned root node
        return root_copy;
    }

    public static int coinFlip(){
        //randomly choose a 1 or 0
        return (int) (Math.random() * 2);
    }

    //determine total function nodes in the tree
    public int getTotalFunctionNodes(node currentNode) {
        if(currentNode == null) {
            return 0;
        }
        else {
            if(currentNode.data.equals("+") || currentNode.data.equals("-") || currentNode.data.equals("*") || currentNode.data.equals("/")) {
                return 1 + getTotalFunctionNodes(currentNode.left) + getTotalFunctionNodes(currentNode.right);
            }
            else {
                return getTotalFunctionNodes(currentNode.left) + getTotalFunctionNodes(currentNode.right);
            }
        }
    }

    public int getTotalTerminals(node currentNode) {
        if(currentNode == null) {
            return 0;
        }
        else {
            if(currentNode.data.contains("f")) {
                return getTotalTerminals(currentNode.left) + getTotalTerminals(currentNode.right);
            }
            else {
                return 1 + getTotalTerminals(currentNode.left) + getTotalTerminals(currentNode.right);
            }
        }
    }

    public int getTotFunc() {
        return getTotalFunctionNodes(root);
        //return totalFunctions;
    }

    public int getTotTerm() {
        return getTotalFunctionNodes(root);
    }
}

