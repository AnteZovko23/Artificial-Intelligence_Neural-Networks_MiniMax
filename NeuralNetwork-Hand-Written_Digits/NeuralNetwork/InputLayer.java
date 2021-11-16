package NeuralNetwork;

import java.util.Scanner;

/**
 * Ante Zovko
 * Oct 28, 2021
 * 
 * Input Layer Implementation as a child class of Layer
 * 
 */
public class InputLayer extends Layer {



    /**
     * 
     * Constructor
     * 
     * @param number_of_neurons the number of neurons for this layer
     * @param activation_function the activation function that will be used in the layer
     * @param numberOfExpectedOutputs the number of expected outputs for the final layer
     */
    public InputLayer(int number_of_neurons, String activation_function, int numberOfExpectedOutputs) {
        
        super(number_of_neurons, activation_function);
        this.level = 0;
        this.number_of_outputs = numberOfExpectedOutputs;
        NeuralNetwork.getInstance().setExpected_output_length(this.number_of_outputs);
        
        
    }
    

    /**
     * Initializes variables (Weights) to a random value between -1 and 1
     * 
     */
    public void set_initial_variables() {

        for(int rows = 0; rows < this.get_neuron_count_next_layer(); rows++) {

    
        for(int columns = 0; columns < this.getNumberOfNeurons(); columns++) {
            // Random weights
            this.getWeights()[rows][columns] = this.rand.nextDouble() * (rand.nextBoolean() ? -1 : 1);

        }}

    }

    /**
     * Given a line from a csv file, reads it as input
     * 
     * @param given_line the csv file line
     */
    public void read_input(String given_line) {


            String updated_line;
            int rows = 0;
            
            // How many outputs to expect
            NeuralNetwork.getInstance().expectedOutputInteger = Integer.parseInt(String.valueOf(given_line.charAt(0)));

            
            // One hot vector of output
            int[] vector = UsefulLibrary.convert_to_one_hot_vector(String.valueOf(given_line.charAt(0)), this.number_of_outputs);
            NeuralNetwork.getInstance().setExpectedOutput(vector);

            // line without the label
            updated_line = given_line.substring(1, given_line.length());

            // Set activations based on values
            Scanner char_scanner = new Scanner(updated_line);
            char_scanner.useDelimiter(",");   //sets the delimiter pattern  
            while(char_scanner.hasNext() && rows < this.getNumberOfNeurons()){
                // ! For excel sheet
                double activation;
                if(NeuralNetwork.dataset.length != 4){
                     activation = (Double.parseDouble(char_scanner.next())/255);
                }
                else {
                     activation = Double.parseDouble(char_scanner.next());
                }
                
                this.getNeurons()[rows][0] = new Neuron(activation, 0, rows);
                rows++;
                
            }   

          

            char_scanner.close();
                


        }

    }

