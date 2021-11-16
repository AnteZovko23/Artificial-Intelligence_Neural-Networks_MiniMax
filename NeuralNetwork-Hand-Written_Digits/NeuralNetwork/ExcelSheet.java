package NeuralNetwork;
/**
 * Name: Ante Zovko
 * CWID: 103-55-122
 * Date: Oct 28, 2021
 * Assignment 2
 * 
 * Neural Network Implementation using the excel spreadsheet
 * 
 */
public class ExcelSheet {

    public static void main(String[] args) {
        
        NeuralNetwork.setInstance(3, (double)10, 2, 6);

        // Layers
        InputLayer inputLayer = new InputLayer(4, "sigmoid", 2);
        HiddenLayer hiddenLayer_1 = new HiddenLayer(3, "sigmoid", 1);
        OutputLayer outputLayer = new OutputLayer(2, "sigmoid", 2);


        NeuralNetwork.getInstance().set_layers(inputLayer, hiddenLayer_1, outputLayer);


        // Initialize layers
        inputLayer.initialize_variables();
        outputLayer.initialize_variables();
        hiddenLayer_1.initialize_variables();
        

        // Set initial variables for each layer
        outputLayer.set_initial_variables();
        hiddenLayer_1.set_initial_variables();
        inputLayer.set_initial_variables();

          
        // NeuralNetwork.getInstance().process_input("NeuralNetwork/ExcelSheetInputs.csv");
        NeuralNetwork.getInstance().excel_file_training("NeuralNetwork/ExcelSheetInputs.csv");
    }
}
