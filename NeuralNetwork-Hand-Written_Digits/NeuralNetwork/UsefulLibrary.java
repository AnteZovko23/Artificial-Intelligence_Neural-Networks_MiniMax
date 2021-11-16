package NeuralNetwork;

import java.util.Random;

/**
 * Ante Zovko
 * Oct 28, 2021
 * 
 * A library of useful functions
 * 
 * 
 */
public class UsefulLibrary {


    /**
     * Converts given number to a one hot vector representation
     * 
     * @param given_number the number
     * @param range the number of outputs
     * @return one hot vector representation
     */
    public static int[] convert_to_one_hot_vector(String given_number, int range) {

        
        int[] vector = new int[range];

        for(int i = 0; i < range; i++) {

            vector[i] = 0;

        }

        vector[Integer.parseInt(given_number)] = 1;
        
        return vector;

    }

    /**
     * Mathematical vector subtraction
     * 
     * @param vector1 first vector
     * @param vector2 second vector
     * @return subtraction
     */
    public static double[][] vector_subtraction(double[][] vector1, double[][] vector2) {

        double[][] result_vector = new double[vector1.length][1];

        for(int rows = 0; rows < vector1.length; rows++) {

            result_vector[rows][0] = vector1[rows][0] - vector2[rows][0];

        }

        return result_vector;

    }

    /**
     * Mathematical vector multiplication to get the activations for next layer
     * 
     * @param weights the weights
     * @param neurons the neurons
     * @param biases the biases
     * @return activations
     */
    public static double[][] vector_multiplication(double[][] weights, Neuron[][] neurons, double[][] biases) {

  
        double[][] weighted_sum_vector = new double[weights.length][1]; 
     
        double weighted_sum = 0;
        
        for(int rows = 0; rows < weights.length; rows++) {

            for(int columns = 0; columns < weights[0].length; columns++) {
                weighted_sum += weights[rows][columns] * neurons[columns][0].getActivation();

            }
            // System.out.println(weights.length);
            weighted_sum_vector[rows][0] = weighted_sum + biases[rows][0];
            weighted_sum = 0;
        }
        // System.out.println(weights.length);
        return weighted_sum_vector;

    }

    /**
     * Sigmoid function
     * 
     * @param z_values given value
     * @return sigmoid(given value)
     */
    public static double[][] sigmoid(double[][] z_values) {

        for(int rows = 0; rows < z_values.length; rows++) {

            double current_value = z_values[rows][0];
            z_values[rows][0] = (double)(1 / (1 + Math.pow(Math.E, -current_value)));

        } 

        return z_values;

    }

    /**
     * Relu function
     * 
     * @param z_values given value
     * @return relu(given value)
     */
    public static double[][] relu(double[][] z_values) {

        for(int rows = 0; rows < z_values.length; rows++) {

            double current_value = z_values[rows][0];
            z_values[rows][0] = current_value > 0 ? current_value : 0;

        } 

        return z_values;

    }


    /**
     * Calculates various metrics including cost and accuracy
     * 
     * @param neurons
     * @param expected_vals
     * @return
     */
    public static double calculate_cost(Neuron[][] neurons, int[] expected_vals, boolean draw) {

        double sum = 0;
        
        
        int guess = 0;
        for(int i = 1; i < neurons.length; i++) {

            if(neurons[i][0].getActivation() > neurons[guess][0].getActivation())
                guess = i;
            
                
            

        }


        // Cost function
        for(int rows = 0; rows < neurons.length; rows++) {

            sum += Math.pow(expected_vals[rows] - neurons[rows][0].getActivation(), 2);

        }

        NeuralNetwork.getInstance().total_progress += 1;

        // Statistics
        NeuralNetwork.getInstance().update_stats(NeuralNetwork.getInstance().expectedOutputInteger, guess, (guess == NeuralNetwork.getInstance().expectedOutputInteger), draw);

        return (double)0.5 * sum;

    }

    /**
     * Draws the image that was given to the network
     * 
     * @param expected
     * @param guess
     */
    public static void draw(int expected, int guess) {

        char[] ASCII_CHARS = {'!', '@', '#', '$', '%', '^', '&'};
        Random rand = new Random();

        System.out.println("\nExpected Value: " + expected + " " + "Guessed: " + guess);

        Neuron[][] neurons = NeuralNetwork.getInstance().getLayers()[0].getNeurons();

        int counter = 0;
        while(counter < neurons.length) {

            if(neurons[counter][0].getActivation() != 0) {
                System.out.print(ASCII_CHARS[rand.nextInt(ASCII_CHARS.length)]);
            } else {
                System.out.print(" ");
            }
            if(counter % 28 == 0) {

                System.out.println();

            }

            counter++;
        }

    }


    /**
     * Shuffles a given array
     * @param a given array
     */
    public static void shuffleArray(String[] a) {
        int n = a.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(a, i, change);
        }
    }

    private static void swap(String[] a, int i, int change) {
        String helper = a[i];
        a[i] = a[change];
        a[change] = helper;
    }
 
    
 /**
  * Removes element from given array at an index

  * @param arr given array
  * @param index index
  * @return updated array
  */
 public static String[] removeElement(String[] arr, int index)
 {

     // If the array is empty
     // or the index is not in array range
     // return the original array
     if (arr == null || index < 0
         || index >= arr.length) {

         return arr;
     }

     // Create another array of size one less
     String[] anotherArray = new String[arr.length - 1];

     // Copy the elements except the index
     // from original array to the other array
     for (int i = 0, k = 0; i < arr.length; i++) {

         // if the index is
         // the removal element index
         if (i == index) {
             continue;
         }

         // if the index is not
         // the removal element index
         anotherArray[k++] = arr[i];
     }

     // return the resultant array
     return anotherArray;
 }

}
