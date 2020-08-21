package backpropagation;

import java.util.ArrayList;

/**
 *
 * @author lucacrot
 */
public class AndTest {
    
    public static void main(String[] args){
        int[] layers = new int[]{ 2, 2,1 };
        MultiLayerPerceptron net = new MultiLayerPerceptron(layers, 0.6);
        for(Layer capa: net.getCapas()){
            for(int i=0; i < capa.layerSize(); i++){
                capa.getNeuron(i).setFuncionTransferencia(new Sigmoidal());
            }
        }
        /* Learning */
        for(int i = 0; i < 10000; i++){
            ArrayList<Double> inputs = new ArrayList<>();
            ArrayList<Double> output = new ArrayList<>();

            double input1, input2, output0, error;
            input1 = Math.round(Math.random());
            input2 = Math.round(Math.random());
            if((input1 == input2) && (input1 == 1))
                output0 = 1.0;
            else output0 = 0.0;

            inputs.add(input1);
            inputs.add(input2);
            output.add(output0);
            //System.out.println(input1+" and "+input2+" = "+output0);
            error = net.backPropagate(new Entrada(inputs,output));
            System.out.println("Error at step "+i+" is "+error);

        }
        System.out.println("Learning completed!");
        //System.out.println(net);

        /* Test */
        ArrayList<Double> inputTest = new ArrayList<>();
        inputTest.add(1.0); inputTest.add(0.0);
        ArrayList<Double> outputTest = net.execute(new Entrada(inputTest,null));

        System.out.println(inputTest.get(0)+" and "+inputTest.get(1)+" = "+Math.round(outputTest.get(0))+" ("+outputTest.get(0)+")");

        inputTest = new ArrayList<>();
        inputTest.add(0.0); inputTest.add(0.0);
        outputTest = net.execute(new Entrada(inputTest,null));

        System.out.println(inputTest.get(0)+" and "+inputTest.get(1)+" = "+Math.round(outputTest.get(0))+" ("+outputTest.get(0)+")");

        inputTest = new ArrayList<>();
        inputTest.add(0.0); inputTest.add(1.0);
        outputTest = net.execute(new Entrada(inputTest,null));

        System.out.println(inputTest.get(0)+" and "+inputTest.get(1)+" = "+Math.round(outputTest.get(0))+" ("+outputTest.get(0)+")");

        inputTest = new ArrayList<>();
        inputTest.add(1.0); inputTest.add(1.0);
        outputTest = net.execute(new Entrada(inputTest,null));

        System.out.println(inputTest.get(0)+" and "+inputTest.get(1)+" = "+Math.round(outputTest.get(0))+" ("+outputTest.get(0)+")");
    }        
}
