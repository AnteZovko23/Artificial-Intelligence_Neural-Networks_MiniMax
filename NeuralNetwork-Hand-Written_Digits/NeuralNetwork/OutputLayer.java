package NeuralNetwork;

/**
 * Ante Zovko
 * Oct 28, 2021
 * 
 * Output Layer implementation as a child from Layer
 */
public class OutputLayer extends Layer{

    /**
     * Constructor
     * 
     * @param number_of_neurons number of neurons
     * @param activation_function activation function to be applied
     * @param level the level in the neural network
     */
    public OutputLayer(int number_of_neurons, String activation_function, int level) {
        super(number_of_neurons, activation_function);
    
        this.level = level;
        this.isFinalLayer = true;
    }


        /**
         * Initializes the variables to be random numbers between -1 and 1
         */
        public void set_initial_variables() {

            for(int rows = 0; rows < this.getNumberOfNeurons(); rows++) {

                this.getBiases()[rows][0] = this.rand.nextDouble() * (rand.nextBoolean() ? -1 : 1);
            
           
    
                this.getNeurons()[rows][0] = new Neuron(0, level, rows);
                this.getNeurons()[rows][0].setFinalLayer(true);
    
            }

        }
    
    
}
