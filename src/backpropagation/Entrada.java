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
public class Entrada {
    private ArrayList<Double> inputs;
    private ArrayList<Double> outputs;
    
    public Entrada(ArrayList<Double> i, ArrayList<Double> o){
        inputs = i;
        outputs = o;
    }
    
    public double getInput(int index){
        return inputs.get(index);
    }
    
    public double getOutput(int index){
        return outputs.get(index);
    }
    
    public int sizeInput(){
        return inputs.size();
    }
    
    public int sizeOutput(){
        return outputs.size();
    }
}
