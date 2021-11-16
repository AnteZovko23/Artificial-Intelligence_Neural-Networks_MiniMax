package NeuralNetwork;


/**
 * Ante Zovko
 * Oct 28, 2021
 * 
 * Implementation of a Neuron with an activation, bias gradient, level and position
 * 
 */
public class Neuron {

    private double activation;
    private double biasGradient;
    private int level;
    private int position;
    private boolean isFinalLayer;
    
    /**
     * Constructor
     * 
     * @param activation Activation of the neuron
     * @param level Level of the neuron in the Neural Network
     * @param position Position of the neuron in the layer
     */
    public Neuron(double activation, int level, int position) {

        this.activation = activation;
        this.level = level;
        this.position = position;

    }
    
    /**
     * Calculates bias gradient
     * 
     * @return bias gradient
     */
    public double calculate_bias_gradient(int column) {

        // Check if neuron in final layer and use appropriate equation
        if(this.isFinalLayer) {

            return (this.getActivation() - this.get_correct_output(this.getPosition())) * this.getActivation() * (1 - this.getActivation());
            
        } else {
            
            // Get bias gradients from layer up
            // Get Weights from layer up
            // Weighted sum of weights and bias gradients * a * (1 - a)
            Layer current_layer = NeuralNetwork.getInstance().getLayers()[this.level];
            Layer layer_above = NeuralNetwork.getInstance().getLayers()[this.level + 1];
            double weighted_bias_gradients = 0;
            // for(int columns = 0; columns < current_layer.getWeights()[0].length; columns++) {

                for(int rows = 0; rows < current_layer.getWeights().length; rows++){
                    
                    weighted_bias_gradients += (current_layer.getWeights()[rows][column] * layer_above.getNeurons()[rows][0].getBiasGradient());
                    // System.out.println(current_layer.getWeights()[rows][column]);
                    // System.out.println(layer_above.getNeurons()[rows][0].getBiasGradient());

                }

                
            // }
        
            return this.activation * (1 - this.activation) * weighted_bias_gradients;
        }

    }

    /**
     * Gets correct output from the Neural Network
     * 
     * @param position the position in the layer
     * @return expected output
     */
    private int get_correct_output(int position) {

        return NeuralNetwork.getInstance().getExpected_output()[position];

    }

    
    /**
     * @return the isFinalLayer
     */
    public boolean isFinalLayer() {
        return isFinalLayer;
    }

    /**
     * @param isFinalLayer the isFinalLayer to set
     */
    public void setFinalLayer(boolean isFinalLayer) {
        this.isFinalLayer = isFinalLayer;
    }

    /**
     * @return the activation
     */
    public double getActivation() {
        return activation;
    }
    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }
    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }
    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }
    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }
    /**
     * @return the biasGradient
     */
    public double getBiasGradient() {
        return biasGradient;
    }
    /**
     * @param biasGradient the biasGradient to set
     */
    public void setBiasGradient(double biasGradient) {
        this.biasGradient = biasGradient;
    }
 
  
    /**
     * @param activation the activation to set
     */
    public void setActivation(double activation) {
        this.activation = activation;
    }

}