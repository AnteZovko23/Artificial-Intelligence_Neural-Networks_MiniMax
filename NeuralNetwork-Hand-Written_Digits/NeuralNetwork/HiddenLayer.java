package NeuralNetwork;


/**
 * Ante Zovko
 * Oct 28, 2021
 * 
 * Hidden Layer Implementation as a child class of Layer
 * 
 */
public class HiddenLayer extends Layer{



    /**
     * Constructor
     * 
     * @param number_of_neurons the number of neurons for the layer
     * @param activation_function the activation function that will be used on the layer
     * @param level the level of the layer in the neural network
     */
    public HiddenLayer(int number_of_neurons, String activation_function, int level) {
        super(number_of_neurons, activation_function);
        this.level = level;
    

    }

    /**
     * Sets the initial variables of the hidden layer (weights and biases) to random values between -1 and 1
     * 
     */
    public void set_initial_variables() {

        for(int rows = 0; rows < this.get_neuron_count_next_layer(); rows++) {

            

            for(int columns = 0; columns < this.getNumberOfNeurons(); columns++) {
                // Random weights
                this.getWeights()[rows][columns] = this.rand.nextDouble()* (rand.nextBoolean() ? -1 : 1);

            }

            


        }

     

        for(int i = 0; i < this.getNeurons().length; i++) {

            this.getNeurons()[i][0] = new Neuron(0, level, i);
            this.getBiases()[i][0] = this.rand.nextDouble()* (rand.nextBoolean() ? -1 : 1);


        }

    }


    
}
