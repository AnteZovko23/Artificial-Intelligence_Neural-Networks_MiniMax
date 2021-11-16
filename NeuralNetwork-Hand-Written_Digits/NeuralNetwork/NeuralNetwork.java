package NeuralNetwork;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Ante Zovko
 * 
 * Oct 28, 2021
 * 
 * Implementation of a Neural Network that performs feed forward and back propagation algorithms
 * Implemented as a singleton
 * 
 */
public class NeuralNetwork {

    private static double learning_rate;
    private static int batch_size;
    private static int batch;
    private static int epochs;
    private static String[][] batch_dataset;
    public static String[] dataset;
    private static Layer[] layers;
    private static int[] expected_output;
    private static int expected_output_length;

    private static String[] weights_read;
    private static String[] biases_read;

    public int expectedOutputInteger;
    
    public int total_progress = 0;

    private static int sum0 = 0;
    private static int sum1 = 0;
    private static int sum2 = 0;
    private static int sum3 = 0;
    private static int sum4 = 0;
    private static int sum5 = 0;
    private static int sum6 = 0;
    private static int sum7 = 0;
    private static int sum8 = 0;
    private static int sum9 = 0;

    private static int guess0 = 0;
    private static int guess1 = 0;
    private static int guess2 = 0;
    private static int guess3 = 0;
    private static int guess4 = 0;
    private static int guess5 = 0;
    private static int guess6 = 0;
    private static int guess7 = 0;
    private static int guess8 = 0;
    private static int guess9 = 0;
    

    private static NeuralNetwork neural_network_instance = null;
    private static int totalAccuracy = 0;


    /**
     * Constructor
     * 
     * @param number_of_layers number of layers
     * @param learning_rate the learning rate
     * @param batch_size the batch size
     * @param epochs the number of epochs
     */
    private NeuralNetwork(int number_of_layers, double learning_rate, int batch_size, int epochs) {

        NeuralNetwork.learning_rate = learning_rate;
        NeuralNetwork.batch_size = batch_size;
        NeuralNetwork.epochs = epochs;
        NeuralNetwork.batch = 0;
        

    }
    
    

    /**
     * Starts training of the given excel file weights and biases
     * 
     * @param path path to Initial Activations
     */
    public void excel_file_training(String path) {

        // Loads weights and biases
        NeuralNetwork.getInstance().get_weights_from_csv("NeuralNetwork/Excel_Weights/weights.csv", "NeuralNetwork/Excel_Weights/biases.csv");
        NeuralNetwork.getInstance().process_input(path);
        NeuralNetwork.create_batches();
        
         // Reshape dataset into batches
         for (int i = 0; i < NeuralNetwork.epochs; i++) {
            System.out.println("*********************** Epoch: " + (i+1) + " ****************************");
            double cost = -1;
        
            
        for (int rows = 0; rows < batch_dataset.length; rows++) {
            System.out.println("*********************** MINI BATCH: " + (rows+1) + " ****************************");


            NeuralNetwork.getInstance().setBatch(0);
            
            for (int columns = 0; columns < batch_dataset[0].length; columns++) {
                System.out.println("*********************** ITEM: " + (columns+1) + " ****************************");
                
                int current_batch = NeuralNetwork.getInstance().getBatch();
                
                // Read current input
                ((InputLayer) layers[0]).read_input(NeuralNetwork.batch_dataset[rows][columns]);
                
                // Update activations 
                for(int layer = 0; layer < layers.length - 1; layer++) {
                    System.out.println("*********************** Layer: " + layer + " ****************************");
                    

                    double[][] activations = UsefulLibrary.vector_multiplication(layers[layer].getWeights(), layers[layer].getNeurons(), layers[layer+1].getBiases());

                    
                    layers[layer+1].activation_function(activations);
                    
                    
                    System.out.println("Activations");
                    for(int z = 0; z < layers[layer].getNumberOfNeurons(); z++){
                        System.out.println(layers[layer].getNeurons()[z][0].getActivation());

                    }
                    System.out.println();
                    System.out.println("Weights");

                    for(int z = 0; z < layers[layer].getWeights().length; z++){

                        for (int zz = 0; zz < layers[layer].getWeights()[0].length; zz++) {
                            
                            System.out.print(layers[layer].getWeights()[z][zz] + ", ");
                            
                        }
                        System.out.println();

                    }

                    System.out.println();

                    if(layers[layer].level != 0) {

                        System.out.println();

                        System.out.println("Biases: ");
                        for(int z = 0; z < layers[layer].getBiases().length; z++){
                            System.out.println(layers[layer].getBiases()[z][0]);
                            
    
                        }
    

                    }


                    if(layers[layer+1].isFinalLayer){
                        System.out.println("*********************** Layer: " + (layer+1) + " ****************************");

                        System.out.println("Activations");
                        for(int z = 0; z < layers[layer+1].getNumberOfNeurons(); z++){
                            System.out.println(layers[layer+1].getNeurons()[z][0].getActivation());

    
                        }

                        System.out.println();

                        System.out.println("Biases: ");
                        for(int z = 0; z < layers[layer+1].getBiases().length; z++){
                            System.out.println(layers[layer+1].getBiases()[z][0]);
                            
    
                        }
                        
                    }
                    System.out.println();
                    // Statistics
                    if(layers[layer+1].isFinalLayer)
                    cost = UsefulLibrary.calculate_cost(layers[layer+1].getNeurons(), NeuralNetwork.getInstance().getExpected_output(), false);
                    
                    System.out.println("Cost: " + cost);

                    System.out.println("*******************************************************");
                    System.out.println();
                }
                
                
                // Start of the back propagation algorithm
                NeuralNetwork.back_propagation();

                NeuralNetwork.getInstance().setBatch(++current_batch);
                for (int layer = 0; layer < layers.length; layer++)  {
                
                    System.out.println("*********************** Layer: " + layer + " ****************************");
                    
                    if(!(layers[layer].level == 0)) {

                    System.out.println("Bias Gradeints");
                    for(int z = 0; z < layers[layer].getNumberOfNeurons(); z++){
                        System.out.println(layers[layer].getNeurons()[z][0].getBiasGradient());

                    }
                }

                    System.out.println();

                    if(!layers[layer].isFinalLayer){
                    System.out.println("Weight Gradients");
                    for(int z = 0; z < layers[layer].get_weight_gradients().length; z++){

                        for (int zz = 0; zz < layers[layer].get_weight_gradients()[0].length; zz++) {
                            
                            System.out.print(layers[layer].get_weight_gradients()[z][zz] + ", ");
                            
                        }
                        System.out.println();

                    }
                }
                    System.out.println();
       
                
            }
        }
            
            System.out.println("*******************************************************");
            System.out.println();
            // !For Excel Sheet
            // display_stats();
            NeuralNetwork.update_parameters();
        }
        // !For Excel Sheet
        // display_stats();
        // After epoch save parameters
        // output_to_csv("weights.csv", "biases.csv");
        reset_stats();
        System.out.println("*******************************************************");
        System.out.println();        
    }
    
                  


    }

    
    
    /**
     * Starts the feed forward algorithm
     */
    public void start_feed_forward() {
        
        // Reshape dataset into batches
        for (int i = 0; i < NeuralNetwork.epochs; i++) {
            NeuralNetwork.create_batches();
        
            double cost = -1;
        
            
        for (int rows = 0; rows < batch_dataset.length; rows++) {


            NeuralNetwork.getInstance().setBatch(0);
            
            for (int columns = 0; columns < batch_dataset[0].length; columns++) {
                
                int current_batch = NeuralNetwork.getInstance().getBatch();
                
                // Read current input
                ((InputLayer) layers[0]).read_input(NeuralNetwork.batch_dataset[rows][columns]);
                
                // Update activations 
                for(int layer = 0; layer < layers.length - 1; layer++) {
                    double[][] activations = UsefulLibrary.vector_multiplication(layers[layer].getWeights(), layers[layer].getNeurons(), layers[layer+1].getBiases());
                    layers[layer+1].activation_function(activations);
                    
                    // Statistics
                    if(layers[layer+1].isFinalLayer)
                    cost = UsefulLibrary.calculate_cost(layers[layer+1].getNeurons(), NeuralNetwork.getInstance().getExpected_output(), false);
                    
                    
                }
                
                
                // Start of the back propagation algorithm
                NeuralNetwork.back_propagation();

                NeuralNetwork.getInstance().setBatch(++current_batch);
                
                
                
            }
            
            display_stats();
            NeuralNetwork.update_parameters();
        }
        // display_stats();
        // System.out.println("Cost: " + cost);
        // After epoch save parameters
        output_to_csv("weights.csv", "biases.csv");
        reset_stats();
        
        
    }
    
    
}

/**
 * Feed Forward Algorithm without training
 * 
 * @param path path of input
 */
public void feed_forward_for_testing(String path, boolean draw) {
    
        // Loads weights and biases
        NeuralNetwork.getInstance().get_weights_from_csv("weights.csv", "biases.csv");
        String[] dataset = NeuralNetwork.getInstance().process_input(path);
        
        for (int rows = 0; rows < dataset.length; rows++) {
                
                ((InputLayer) layers[0]).read_input(dataset[rows]);
                
                // Update activations
                for(int layer = 0; layer < layers.length - 1; layer++) {
                    double[][] activations = UsefulLibrary.vector_multiplication(layers[layer].getWeights(), layers[layer].getNeurons(), layers[layer+1].getBiases());
                    layers[layer+1].activation_function(activations);
                    
                    
                    if(layers[layer+1].isFinalLayer)
                    UsefulLibrary.calculate_cost(layers[layer+1].getNeurons(), NeuralNetwork.getInstance().getExpected_output(), draw);
                    
                    
                }
                display_stats();
            }
            
            
            
            
        }
        

        /**
         * The back propagation algorithm
         * 
         */
        public static void back_propagation() {
            
            // Start from final layer
            for(int i = layers.length - 1; i >= 0; i--) {
                Neuron[][] neurons = layers[i].getNeurons();
        
                for(int rows = 0; rows < layers[i].getNumberOfNeurons(); rows++) {
                    
                    // Get bias gradient
                    neurons[rows][0].setBiasGradient(neurons[rows][0].calculate_bias_gradient(neurons[rows][0].getPosition()));
                        layers[i].getTotal_bias_gradients()[rows][0][NeuralNetwork.getInstance().getBatch()] = neurons[rows][0].getBiasGradient();
                        
                        
                    }
                    
                    // Get weight gradients
                    if(!layers[i].isFinalLayer)
                    layers[i].calculate_weight_gradients();
                    
                }
                
                
                
            }
            

            /**
             * Updates the weights and biases
             * 
             */
            public static void update_parameters() {
                
                for(int i = layers.length - 1; i >= 0; i--) {
                    
                    // Final layer does not hold weights
                    if(!layers[i].isFinalLayer){
                        
                        
                        for(int rows = 0; rows < layers[i].getWeights().length;rows++) {
                            
                            for(int columns = 0; columns < layers[i].getWeights()[0].length; columns++){
                                
                                double sum = 0;
                                for(int j = 0; j < NeuralNetwork.getInstance().get_batch_size(); j++) {
                                    
                                    sum += layers[i].getTotal_weight_gradients()[rows][columns][j];
                                }
        
                                double weight_old = layers[i].getWeights()[rows][columns];
                                // Update weights
                                layers[i].getWeights()[rows][columns] = weight_old - (NeuralNetwork.getInstance().get_learning_rate()/NeuralNetwork.getInstance().get_batch_size()) * sum;
                            
                        }
                        
                    }
                }
        
                // First layer does not hold biases
                if(!(layers[i].level == 0)) {
                    
                    for(int rows = 0; rows < layers[i].getBiases().length; rows++) {
                        
                        double sum = 0;
                        for(int j = 0; j < NeuralNetwork.getInstance().get_batch_size(); j++) {
        
                            sum += layers[i].getTotal_bias_gradients()[rows][0][j];
        
                        }
                        
                        double bias_old = layers[i].getBiases()[rows][0];
                        // Update biases
                        layers[i].getBiases()[rows][0] = bias_old - (NeuralNetwork.getInstance().get_learning_rate()/NeuralNetwork.getInstance().get_batch_size()) * sum;
        
                    }
                }
                
            }
            
        }

        /**
         * Resets statistics after epoch
         * 
         */
        public void reset_stats() {
            
            NeuralNetwork.getInstance().total_progress = 0;
            
            NeuralNetwork.sum0 = 0;
            NeuralNetwork.sum1 = 0;
            NeuralNetwork.sum2 = 0;
            NeuralNetwork.sum3 = 0;
            NeuralNetwork.sum4 = 0;
            NeuralNetwork.sum5 = 0;
            NeuralNetwork.sum6 = 0;
            NeuralNetwork.sum7 = 0;
            NeuralNetwork.sum8 = 0;
            NeuralNetwork.sum9 = 0;
            
            NeuralNetwork.guess0 = 0;
            NeuralNetwork.guess1 = 0;
            NeuralNetwork.guess2 = 0;
            NeuralNetwork.guess3 = 0;
            NeuralNetwork.guess4 = 0;
            NeuralNetwork.guess5 = 0;
            NeuralNetwork.guess6 = 0;
            NeuralNetwork.guess7 = 0;
            NeuralNetwork.guess8 = 0;
            NeuralNetwork.guess9 = 0;
            
            NeuralNetwork.totalAccuracy = 0;
        
         
    }
    
    
    /**
     * Displays live statistics
     */
    public void display_stats() {

        if(NeuralNetwork.dataset.length == 4) {

            System.out.println("0 => " + NeuralNetwork.guess0 + "/" + NeuralNetwork.sum0);
            System.out.println("1 => " + NeuralNetwork.guess1 + "/" + NeuralNetwork.sum1);
        }
        else{
            System.out.println("0 => " + NeuralNetwork.guess0 + "/" + NeuralNetwork.sum0);
            System.out.println("1 => " + NeuralNetwork.guess1 + "/" + NeuralNetwork.sum1);
            System.out.println("2 => " + NeuralNetwork.guess2 + "/" + NeuralNetwork.sum2);
            System.out.println("3 => " + NeuralNetwork.guess3 + "/" + NeuralNetwork.sum3);
            System.out.println("4 => " + NeuralNetwork.guess4 + "/" + NeuralNetwork.sum4);
            System.out.println("5 => " + NeuralNetwork.guess5 + "/" + NeuralNetwork.sum5);
            System.out.println("6 => " + NeuralNetwork.guess6 + "/" + NeuralNetwork.sum6);
            System.out.println("7 => " + NeuralNetwork.guess7 + "/" + NeuralNetwork.sum7);
            System.out.println("8 => " + NeuralNetwork.guess8 + "/" + NeuralNetwork.sum8);
            System.out.println("9 => " + NeuralNetwork.guess9 + "/" + NeuralNetwork.sum9);
        }
       
        System.out.println("Total Accuracy: " + NeuralNetwork.totalAccuracy + "/" + (NeuralNetwork.getInstance().total_progress) + " or " + ((double)NeuralNetwork.totalAccuracy /  (NeuralNetwork.getInstance().total_progress)*100 + "%"));
        
    }
    

    /**
     * Updates statistics
     * 
     * @param expected_val the expected value
     * @param guess the value the neural network predicted
     * @param correct if it was correct
     */
    public void update_stats(int expected_val, int guess, boolean correct, boolean draw) {
        
        
        
        switch (expected_val) {
            case 0:
            
            NeuralNetwork.sum0++;
            if(correct)
                NeuralNetwork.guess0++;
                break;
            
                case 1:
            
                NeuralNetwork.sum1++;
            if(correct)
            NeuralNetwork.guess1++;
            break;
            case 2:
            
            NeuralNetwork.sum2++;
            if(correct)
            NeuralNetwork.guess2++;
            break;
            case 3:
            
            NeuralNetwork.sum3++;
            if(correct)
            NeuralNetwork.guess3++;
            break;
            case 4:
            
            NeuralNetwork.sum4++;
            if(correct)
            NeuralNetwork.guess4++;
            break;
            case 5:
            
            NeuralNetwork.sum5++;
            if(correct)
            NeuralNetwork.guess5++;
            break;
            case 6:
            
            NeuralNetwork.sum6++;
            if(correct)
            NeuralNetwork.guess6++;
            break;
            case 7:
                
            NeuralNetwork.sum7++;
            if(correct)
            NeuralNetwork.guess7++;
            break;
            case 8:
            
            NeuralNetwork.sum8++;
            if(correct)
            NeuralNetwork.guess8++;
            break;
            
            case 9:
            
            NeuralNetwork.sum9++;
            if(correct)
            NeuralNetwork.guess9++;
            break;
            
            default:
            break;
        }
        
        if(correct)
            NeuralNetwork.totalAccuracy++;
        else
            if(draw){
                UsefulLibrary.draw(expected_val, guess);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        
        
        
    }
    
    /**
     * Input processing to create dataset
     * 
     * @param path path to input
     * @return dataset
     */
    public String[] process_input(String path) {
        
        File inputs = new File(path);
        Path path_object = Paths.get(path);
        String[] batches = null;
        long lines;
        
        int counter = 0;
        try {
            lines = Files.lines(path_object).count();
            
            batches = new String[(int)lines];
            Scanner sc = new Scanner(inputs);
            
            while(sc.hasNextLine()) {
                
                batches[counter] = sc.nextLine();

                counter++;
                

            }
            
            sc.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        NeuralNetwork.dataset = batches;
        return batches;
        
    }
    
    
    
    /**
     * Creates mini batches
     * 
     */
    private static void create_batches() {
        
        if(NeuralNetwork.dataset.length != 4)
        UsefulLibrary.shuffleArray(NeuralNetwork.dataset);

        NeuralNetwork.batch_dataset = new String[NeuralNetwork.dataset.length/NeuralNetwork.getInstance().get_batch_size()][NeuralNetwork.getInstance().get_batch_size()];
        
        int counter = 0;
        for(int i = 0; i < NeuralNetwork.batch_dataset.length; i++){
            
            for (int j = 0; j < NeuralNetwork.batch_size; j++) {
                
                NeuralNetwork.batch_dataset[i][j] = NeuralNetwork.dataset[counter];
                counter++;
                
            }
            
        }
        
        
    }
    
    /**
     * Sends current weights and biases to csv files
     * 
     * @param weight_path the path to the weights csv file
     * @param bias_path the path to the bias csv file
     */
    public void output_to_csv(String weight_path, String bias_path) {
        
        File weights_csv = new File(weight_path);
        File bias_csv = new File(bias_path);
        
        FileWriter fileWriterWeights = null;
        
        
            save_weights_biases_to_file(fileWriterWeights, weights_csv, bias_csv);
            

            
            

        }
        
        /**
         * Extracts weights and biases and sends them to a csv file
         * 
         * @param fw the file writer
         * @param weights_csv the weights file
         * @param bias_csv the biases file
         */
        private void save_weights_biases_to_file(FileWriter fw, File weights_csv, File bias_csv) {

            StringBuilder weights_line = new StringBuilder();
            StringBuilder bias_line = new StringBuilder();
            for(int i = 0; i < layers.length; i++) {
                
            if(!(layers[i].isFinalLayer)){
                
            for(int rows = 0; rows < layers[i].getWeights().length;rows++){
                
                for(int columns = 0; columns < layers[i].getWeights()[0].length; columns++){
                    
                    weights_line.append(layers[i].getWeights()[rows][columns]);
                    // Adds commas until the last element
                    if(columns != layers[i].getWeights()[0].length - 1)
                        weights_line.append(',');
                        
                }
                
                
                weights_line.append("\n");
                
            }
        }
        
        if(layers[i].level != 0) {
            for(int rows = 0; rows < layers[i].getBiases().length; rows++) {
                
                bias_line.append(layers[i].getBiases()[rows][0]);
                
                if(rows != layers[i].getBiases().length - 1)
                bias_line.append(',');
                
            }
            
            bias_line.append("\n");
            
        }
        
        
        // Write to files
        try {
            fw = new FileWriter(weights_csv);

            
            fw.write(weights_line.toString());
            fw.close();
            fw = new FileWriter(bias_csv);
            fw.write(bias_line.toString());
            fw.close();
            


        } catch (IOException e) {
            e.printStackTrace();
        }

        
        
        
    }
    
}

/**
 * Gets weights and biases from csv files
 * 
 * @param weight_path the path to the weights csv file
 * @param bias_path the path to the bias csv file
 */
private void get_weights_from_csv(String weight_path, String bias_path) {
    
        File weights = new File(weight_path);
        File biases = new File(bias_path);
        
        int sum_of_layers = 0;
        int num_of_layers = 0;
        for(int i = 0; i < NeuralNetwork.getInstance().getLayers().length; i++) {
            
            if(NeuralNetwork.getInstance().getLayers()[i].level != 0)
            sum_of_layers += NeuralNetwork.getInstance().getLayers()[i].getNumberOfNeurons();
            
            if(!NeuralNetwork.getInstance().getLayers()[i].isFinalLayer)
            num_of_layers++;
            
        }
        
        String[] weights_read = new String[sum_of_layers];
        String[] biases_read = new String[num_of_layers];
        
        int counter = 0;
        try {
            
            Scanner sc = new Scanner(weights);
            
            while(sc.hasNextLine()) {
                
                weights_read[counter] = sc.nextLine();
                
                counter++;
                
                
            }
            
            sc.close();
            
            sc = new Scanner(biases);
            counter = 0;
            while(sc.hasNextLine()) {
                
                biases_read[counter] = sc.nextLine();
                
                counter++;
                
                
            }
            
           
            sc.close();
            
            

        } catch (IOException e) {
            // TODO
        e.printStackTrace();
    }

    NeuralNetwork.weights_read = weights_read;
    NeuralNetwork.biases_read = biases_read;
    

    for(int i = 0; i < layers.length; i++) {
        
        
        
        if(!layers[i].isFinalLayer){
            
            // Set weights
            layers[i].setWeights(convert_weights(layers[i].number_of_neurons, layers[i].get_neuron_count_next_layer()));
            
            
            
        }
        
        
        if(!(layers[i].level == 0)) {
            
            // Set biases
            layers[i].setBiases(convert_biases(layers[i].getNumberOfNeurons()));
            
        }
        
    }
    
    
    
}

/**
 * Converts read weights from csv file to numeric values 
 * 
 * @param number_of_neurons the number of neurons from current layer
 * @param number_of_neurons_layer_up the number of neurons from next layer
 * @return double[][] the numeric weights
 */
private double[][] convert_weights(int number_of_neurons, int number_of_neurons_layer_up) {

        double[][] numeric_weights = new double[number_of_neurons_layer_up][number_of_neurons];

        int rows = 0;
        int columns = 0;  
        

        for(int i = 0; i < number_of_neurons_layer_up; i++){
            Scanner sc = new Scanner(NeuralNetwork.weights_read[i]);
            sc.useDelimiter(",");   //sets the delimiter pattern
            while(sc.hasNext()){
                
                numeric_weights[rows][columns] = Double.parseDouble(sc.next());
                columns++;
                
            }

            columns = 0;
            rows++;
        }
        
        for (int i = 0; i < rows; i++) {
            NeuralNetwork.weights_read = UsefulLibrary.removeElement(NeuralNetwork.weights_read, 0);
            
        }
        
        return numeric_weights;
        
    }
    
    /**
     * Converts read biases from csv file to numeric values 
     * 
     * @param number_of_neurons the number of neurons from current layer
     * @return double[][] the numeric biases
     */
    private double[][] convert_biases(int number_of_neurons) {

        double[][] numeric_biases = new double[number_of_neurons][1];
        
        int rows = 0;
        
        // System.out.println(number_of_neurons);
        // for(int i = 0; i < number_of_neurons; i++){
            Scanner sc = new Scanner(NeuralNetwork.biases_read[0]);
            sc.useDelimiter(",");   //sets the delimiter pattern
            while(sc.hasNext()){
                
                numeric_biases[rows][0] = Double.parseDouble(sc.next());
                rows++;
            }
            
        // }

       
        NeuralNetwork.biases_read = UsefulLibrary.removeElement(NeuralNetwork.biases_read, 0);
        
        return numeric_biases;
        
    }
    
    /****************** GETTERS AND SETTERS **************************** */
    /**
     * Sets the Neural Network instance
     * 
     * @param number_of_layers number of layers
     * @param learning_rate the learning rate
     * @param batch_size the batch size
     * @param epochs the num of epochs
     */
    public static void setInstance(int number_of_layers, double learning_rate, int batch_size, int epochs) {
        
        
        if(neural_network_instance == null) {
            
            neural_network_instance = new NeuralNetwork(number_of_layers, learning_rate, batch_size, epochs);
            
        } 
        
        
        
    }
    
    /**
     * Gets the instance
     * @return The Neural Network instance
     */
    public static NeuralNetwork getInstance() {

        return neural_network_instance;
        
    }
    
    
    /**
     * Sets the expected output as a 1 hot vector
     * @param one_hot_output
     */
    public void setExpectedOutput(int one_hot_output[]) {

        NeuralNetwork.getInstance().setExpected_output(one_hot_output);
        
    }
    
    /**
     * @return the layers
     */
    public Layer[] getLayers() {
        return NeuralNetwork.getInstance().layers;
    }
    
    /**
     * @return the batch
     */
    public int getBatch() {
        return NeuralNetwork.batch;
    }
    
    
    /**
     * @param batch the batch to set
     */
    public void setBatch(int batch) {
        NeuralNetwork.batch = batch;
    }
    
    /**
     * @param layers the layers to set
     */
    public void setLayers(Layer[] layers) {
        NeuralNetwork.getInstance().layers = layers;
    }
    
    
    /**
     * @return batch_size the batch_size 
     */
    public int get_batch_size() {
        return NeuralNetwork.batch_size;
    }
    
    /**
     * @param batch_size the batch_size to set
     */
    public void set_batch_size(int batch_size) {
        NeuralNetwork.batch_size = batch_size;
    }
    
    /**
     * @return the expected_output_length
     */
    public int getExpected_output_length() {
        return expected_output_length;
    }
    
    
    /**
     * @param expected_output_length the expected_output_length to set
     */
    public void setExpected_output_length(int expected_output_length) {
        NeuralNetwork.expected_output_length = expected_output_length;
    }
    
    
    /**
     * Sets layers
     * @param layers 
     */
    public void set_layers(Layer... layers) {
        NeuralNetwork.layers = layers;
    }
    
    /**
     * Gets learning rate
     * @return learning rate
     */
    public double get_learning_rate() {
    
        return NeuralNetwork.learning_rate;
    
    }
    
    /**
     * sets learning rate
     * @param lr learning rate
     */
    public void set_learning_rate(double lr) {
    
        NeuralNetwork.learning_rate = lr;
    
    }
    /**
     * @return the expected_output
     */
    public int[] getExpected_output() {
        return expected_output;
    }


    /**
     * @param expected_output the expected_output to set
     */
    public void setExpected_output(int[] expected_output) {
        NeuralNetwork.expected_output = expected_output;
    }


   


}
