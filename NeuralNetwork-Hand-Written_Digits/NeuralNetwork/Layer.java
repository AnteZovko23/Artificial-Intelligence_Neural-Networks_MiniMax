package NeuralNetwork;

import java.util.Random;

/**
 * Ante Zovko
 * Oct 28, 2021
 * 
 * Abstract parent class for each layer of the Neural Network
 * 
 */
public abstract class Layer {

    protected int number_of_neurons;
    protected String activation_function;
    protected Random rand;
    protected int level;
    protected boolean isFinalLayer;
    protected int number_of_outputs;

    private double[][][] total_weight_gradients;
    private double[][][] total_bias_gradients;

    private double[][] weight_gradients;
    private double[][] weights;
    private Neuron[][] neurons;
    private double[][] biases;

    

    /**
     * Constructor
     * 
     * @param number_of_neurons the number of neurons for the layer
     * @param activation_function the activation function that will be used for the layer
     */
    public Layer(int number_of_neurons, String activation_function) {

        this.number_of_neurons = number_of_neurons;
        this.activation_function = activation_function;
        this.rand = new Random();
        


    }
    
    /**
     * Allocates space in memory for variables(Neurons, Biases, Gradients)
     * 
     */
    protected void initialize_variables() {
        
        // Checks if it is a final layer
        // Final layer does not hold weights
        if(this.isFinalLayer) {
            this.setNeurons(new Neuron[this.number_of_neurons][1]);
            this.setTotal_bias_gradients(new double[this.getNumberOfNeurons()][1][NeuralNetwork.getInstance().get_batch_size()]);
            this.setBiases(new double[this.number_of_neurons][1]);


            
        } else {
            
            this.setWeights(new double[this.get_neuron_count_next_layer()][this.number_of_neurons]);
            this.setNeurons(new Neuron[this.number_of_neurons][1]);
            this.setBiases(new double[this.number_of_neurons][1]);
            this.set_weight_gradients(new double[this.get_neuron_count_next_layer()][this.number_of_neurons]);
            this.setTotal_weight_gradients(new double[this.get_neuron_count_next_layer()][this.number_of_neurons][NeuralNetwork.getInstance().get_batch_size()]);
            this.setTotal_bias_gradients(new double[this.getNumberOfNeurons()][1][NeuralNetwork.getInstance().get_batch_size()]);
        }
        
    }
    
    
    /**
     * Applies the activation function to a vector of z-values
     * 
     * @param z_values the activations
     */
    protected void activation_function(double[][] z_values) {
        
        double[][] a_values;
        
        switch (this.activation_function) {
            case "sigmoid":
            
            a_values = UsefulLibrary.sigmoid(z_values);
            // System.out.println(a_values.length);
            for(int rows = 0; rows < a_values.length; rows++) {
                    this.getNeurons()[rows][0].setActivation(a_values[rows][0]);
                    
                }
                
            case "relu":
            
            a_values = UsefulLibrary.relu(z_values);
            for(int rows = 0; rows < a_values.length; rows++) {
                
                this.getNeurons()[rows][0].setActivation(a_values[rows][0]);
                
            }
            
            default:
                return;
            }
            
        }


        /**
         * Calculates weight gradients using the formula activation of given neuron * bias gradient of neuron above
         * 
         */
        public void calculate_weight_gradients() {

        for(int rows = 0; rows < this.getWeights().length; rows++) {
            
            for(int columns = 0; columns < this.getWeights()[0].length; columns++) {
                
                this.get_weight_gradients()[rows][columns] = this.getNeurons()[columns][0].getActivation() * NeuralNetwork.getInstance().getLayers()[this.level+1].getNeurons()[rows][0].getBiasGradient();
                this.total_weight_gradients[rows][columns][NeuralNetwork.getInstance().getBatch()] = this.get_weight_gradients()[rows][columns];
            }

        }

    }
    
    /**
     * Gets the number of neurons from a layer after the given layer
     * 
     * @return the number of neurons
     */
    public int get_neuron_count_next_layer() {
        
        return NeuralNetwork.getInstance().getLayers()[level+1].number_of_neurons;

    }
    
    /**
     * @return the biases
     */
    public double[][] getBiases() {
        return biases;
    }
    
    
    
    /**
     * @param biases the biases to set
     */
    public void setBiases(double[][] biases) {
        this.biases = biases;
    }

    /**
     * @return the total_weight_gradients
     */
    public double[][][] getTotal_weight_gradients() {
        return total_weight_gradients;
    }

    /**
     * @param total_weight_gradients the total_weight_gradients to set
     */
    public void setTotal_weight_gradients(double[][][] total_weight_gradients) {
        this.total_weight_gradients = total_weight_gradients;
    }
    


    /**
     * @return the total_bias_gradients
     */
    public double[][][] getTotal_bias_gradients() {
        return total_bias_gradients;
    }

    /**
     * @param total_bias_gradients the total_bias_gradients to set
     */
    public void setTotal_bias_gradients(double[][][] total_bias_gradients) {
        this.total_bias_gradients = total_bias_gradients;
    }
    

    /**
     * @return the weight_gradients
     */
    public double[][] get_weight_gradients() {
        return weight_gradients;
    }



    /**
     * @param weight_gradients the weight_gradients to set
     */
    public void set_weight_gradients(double[][] weight_gradients) {
        this.weight_gradients = weight_gradients;
    }




    /**
     * @return the neurons
     */
    public Neuron[][] getNeurons() {
        return neurons;
    }



    /**
     * @param neurons the neurons to set
     */
    public void setNeurons(Neuron[][] neurons) {
        this.neurons = neurons;
    }

    public int getNumberOfNeurons() {

        return this.number_of_neurons;

    }

    /**
     * @return the weights
     */
    public double[][] getWeights() {
        return weights;
    }



    /**
     * @param weights the weights to set
     */
    public void setWeights(double[][] weights) {
        this.weights = weights;
    }



 

    

}
