package NeuralNetwork;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;

/**
 * Name: Ante Zovko
 * CWID: 103-55-122
 * Date: Oct 28, 2021
 * Assignment 2
 * 
 * Neural Network Implementation that can recognize handwritten digits using the MNIST dataset
 * 
 */
public class Driver {
    public static void main(String[] args) throws FileNotFoundException {

        setup();
        boolean running = true;
        while(running) {

            String first_response = JOptionPane.showInputDialog(null, "Please select one of the following options\n1.Train a new network\n2.Load weights and biases\n3.Quit");

            try{
                int response = Integer.parseInt(first_response);
                switch (response) {
                    case 1:
                        train_new_network();
                        break;
                
                    case 2:
                    String train_or_test = JOptionPane.showInputDialog(null, "Please select one of the following options\n1.Display accuracy on TRAINING data\n2.Display accuracy on TESTING data");
                    String draw_str = JOptionPane.showInputDialog(null, "Would you like to display the incorrectly guessed images? [Yes/No]");

                    if(draw_str.equalsIgnoreCase("yes"))
                        load_network(Integer.parseInt(train_or_test), true);
                    else if(draw_str.equalsIgnoreCase("no")) {
                        load_network(Integer.parseInt(train_or_test), false);
                    }
                    else {
                        throw new NumberFormatException();
                    }
                    case 3:
                        running = false;
                        break;
                    default:
                        throw new NumberFormatException();
                }
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Please select a valid option");
                continue;
            }

        }

    } 

    /**
     * Sets up all variables, layers, parameters
     * 
     */
    private static void setup() {

        NeuralNetwork.setInstance(4, (double)3, 10, 60);

        // Layers
        InputLayer inputLayer = new InputLayer(784, "sigmoid", 10);
        HiddenLayer hiddenLayer_1 = new HiddenLayer(64, "sigmoid", 1);
        HiddenLayer hiddenLayer_2 = new HiddenLayer(32, "sigmoid", 2);
        OutputLayer outputLayer = new OutputLayer(10, "sigmoid", 3);


        NeuralNetwork.getInstance().set_layers(inputLayer, hiddenLayer_1, hiddenLayer_2, outputLayer);


        // Initialize layers
        inputLayer.initialize_variables();
        outputLayer.initialize_variables();
        hiddenLayer_2.initialize_variables();
        hiddenLayer_1.initialize_variables();
        

        // Set initial variables for each layer
        outputLayer.set_initial_variables();
        hiddenLayer_2.set_initial_variables();
        hiddenLayer_1.set_initial_variables();
        inputLayer.set_initial_variables();

    }
    
    /**
     * 
     * Trains the neural network
     * 
     */
    public static void train_new_network() {

        
        NeuralNetwork.getInstance().process_input("NeuralNetwork/mnist_train.csv");
        NeuralNetwork.getInstance().start_feed_forward();
        
    }
    
    /**
     * Loads weights and biases from a csv file
     * 
     * @param response user response
     */
    public static void load_network(int response, boolean draw) {
        
        switch (response) {
            case 1:
            NeuralNetwork.getInstance().feed_forward_for_testing("NeuralNetwork/mnist_train.csv", draw);
                 break;
        
            case 2:
                NeuralNetwork.getInstance().feed_forward_for_testing("NeuralNetwork/mnist_test.csv", draw);
                break;

            case 3:
                NeuralNetwork.getInstance().output_to_csv("weights.csv", "biases.csv");
            default:
                throw new NumberFormatException();
        }

    }

}