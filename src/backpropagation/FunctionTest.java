/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backpropagation;

import java.util.ArrayList;

/**
 *
 * @author lucacrot
 */
public class FunctionTest {
    public static void main(String[] args){
        int[] layers = new int[]{ 1, 3,4,1 };
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

            double input1, output0, error;
            input1 = Math.random()*4;
            output0 = 1+Math.sin(2*input1-Math.PI);

            inputs.add(input1);
            output.add(output0);
            //System.out.println("sin(2"+input1+"-Pi)+1 = "+output0);
            error = net.backPropagate(new Entrada(inputs,output));
            System.out.println("Error at step "+i+" is "+error);

        }
        System.out.println("Learning completed!");
        //System.out.println(net);

        /* Test */
        ArrayList<Double> inputTest = new ArrayList<>();
        inputTest.add(Math.random()*4);
        ArrayList<Double> outputTest = net.execute(new Entrada(inputTest,null));
        System.out.println("sin("+inputTest.get(0)+") = "+outputTest.get(0)+" (real is "+(1+Math.sin(2*inputTest.get(0)-Math.PI))+")");
    }
}
