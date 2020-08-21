package backpropagation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * @thanks Davide Gessa
 * @author lucacrot
 */
public class MultiLayerPerceptron {
    private ArrayList<Layer> capas;
    private double tasaAprendizaje;
    
    public MultiLayerPerceptron(int[] capa, double tasa){
        capas = new ArrayList<>();
        tasaAprendizaje = tasa;
        for(int i=0;i<capa.length;i++){
            Layer cp = new Layer();
            for(int j=0;j<capa[i];j++){
                int prev = i == 0 ? 0 : capa[i-1];
                Neuron neu = new Neuron(prev);
                cp.addNeuron(neu);
            }
            capas.add(cp);
        }
    }
    
    public ArrayList<Double> execute(Entrada entradas) {
        double new_value;	
        ArrayList<Double> output = new ArrayList<>();
        	
        // Put input
        for(int i=0; i<capas.get(0).layerSize(); i++){
            capas.get(0).getNeuron(i).setValue(entradas.getInput(i));
        }
		
        // Execute - hiddens + output
        for(int k=1; k<capas.size(); k++){
            for(int i=0; i<capas.get(k).layerSize(); i++){
                new_value = 0.0;
                for(int j=0; j<capas.get(k-1).layerSize(); j++)
                    new_value += capas.get(k).getNeuron(i).getWeight(j) * capas.get(k-1).getNeuron(j).getValue();
                new_value += capas.get(k).getNeuron(i).getThreshold();
                double evaluated = capas.get(k).getNeuron(i).getFuncionTransferencia().evaluate(new_value);
		capas.get(k).getNeuron(i).setValue(evaluated);
            }
        }

        // Get output
        for(int i=0; i<capas.get(capas.size()-1).layerSize(); i++){
            output.add(capas.get(capas.size()-1).getNeuron(i).getValue());
        }
		
        return output;
    }
	
    public double backPropagate(Entrada entradas){
        ArrayList<Double> newOutput = execute(entradas);
        double error;
        double evaluated;
        int ultima = capas.size()-1;

        // Calculamos el error con respecto a la salida
        for(int i=0; i<capas.get(ultima).layerSize(); i++){
            error = entradas.getOutput(i) - newOutput.get(i);
            evaluated = capas.get(ultima).getNeuron(i).getFuncionTransferencia().evaluateDerivate(newOutput.get(i));
            capas.get(ultima).getNeuron(i).setDelta(error * evaluated);
        }
        //BackPropagation
        for(int k=capas.size()-2; k>=0; k--){
            // Se calcula el error de la capa actual y se recalcula los deltas
            for(int i=0; i < capas.get(k).layerSize(); i++){
                error = 0.0;
                for(int j=0; j < capas.get(k+1).layerSize(); j++)
                    error += capas.get(k+1).getNeuron(j).getDelta() * capas.get(k+1).getNeuron(j).getWeight(i);
                evaluated = capas.get(k).getNeuron(i).getFuncionTransferencia().evaluateDerivate(capas.get(k).getNeuron(i).getValue());
                capas.get(k).getNeuron(i).setDelta(error * evaluated);
            }
            // Actualizo los pesos de la siguiente capa
            for(int i=0; i < capas.get(k+1).layerSize(); i++){
                for(int j=0; j < capas.get(k).layerSize(); j++){
                    double varWeight = capas.get(k+1).getNeuron(i).getWeight(j);
                    capas.get(k+1).getNeuron(i).setWeight(j,varWeight + (tasaAprendizaje * capas.get(k+1).getNeuron(i).getDelta() * capas.get(k).getNeuron(j).getValue()));
                }
                double varThreshold = capas.get(k+1).getNeuron(i).getThreshold();
                capas.get(k+1).getNeuron(i).setThreshold(varThreshold + (tasaAprendizaje * capas.get(k+1).getNeuron(i).getDelta()));
            }

        }	
        
        // Calculamos el error
        error = 0.0;
        for(int i=0; i < entradas.sizeOutput(); i++){
            error += Math.abs(newOutput.get(i) - entradas.getOutput(i));
        }
        error = error / entradas.sizeOutput();
        return error;
    }
	
    public boolean save(String path){
        try{
            FileOutputStream fout = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(this);
            oos.close();
        }
        catch (Exception e){ 
            return false;
        }
	return true;
    }
	
	
    public static MultiLayerPerceptron load(String path){
        try{
            MultiLayerPerceptron net;
			
            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream oos = new ObjectInputStream(fin);
            net = (MultiLayerPerceptron) oos.readObject();
            oos.close();

            return net;
        }
        catch (Exception e) {
            return null;
        }
    }
	
    public double getTasaAprendizaje(){
        return tasaAprendizaje;
    }
	
    public void	setTasaAprendizaje(double rate){
        tasaAprendizaje = rate;
    }
	
    public int getInputLayerSize(){
        return capas.get(0).layerSize();
    }
	
    public int getOutputLayerSize(){
        return capas.get(capas.size()-1).layerSize();
    }
    
    public ArrayList<Layer> getCapas(){
        return capas;
    }
    
    @Override
    public String toString(){
        String output = "";
        for(int i=1; i<capas.size();i++){
            for(int j=0;j<capas.get(i).layerSize();j++){
                output += "Capa "+i+" - Neurona "+j+": [";
                for(int k=0; k<capas.get(i).getNeuron(j).getWeightsSize(); k++)
                    output += capas.get(i).getNeuron(j).getWeight(k)+",";
                output += "]\n";
            }
        }
        return output;
    }
}

class Layer{
    private ArrayList<Neuron> neuronas;
    
    public Layer(){
        neuronas = new ArrayList<>();
    }
    
    public void addNeuron(Neuron neurona){
        neuronas.add(neurona);
    }
    
    public Neuron getNeuron(int index){
        return neuronas.get(index);
    }
    
    public int layerSize(){
        return neuronas.size();
    }
}

class Neuron{
    private double[] weights;
    private double threshold;
    private double delta;
    private double value;
    private TransferFunction function;
    
    public Neuron(int inputs){
        if(inputs!=0) weights = new double[inputs];
        threshold = Math.random();
        delta = Math.random();
        value = Math.random();
        if(inputs!=0) {for(int i=0;i<weights.length;i++){
            weights[i] = Math.random();
        }}
        function = new Hyperbolic();
    }
    
    public TransferFunction getFuncionTransferencia(){
        return function;
    }
    
    public void setFuncionTransferencia(TransferFunction fun){
        function = fun;
    }
    
    public double getValue(){
        return value;
    }
    
    public void setValue(double valor){
        value = valor;
    }
    
    public double getWeight(int index){
        return weights[index];
    }
    
    public void setWeight(int index, double weight){
        weights[index] = weight;
    }
    
    public double getThreshold(){
        return threshold;
    }
    
    public void setThreshold(double bias){
        threshold = bias;
    }
    
    public double getDelta(){
        return delta;
    }
    
    public void setDelta(double delt){
        delta = delt;
    }
    
    public int getWeightsSize(){
        if(weights!=null){
            return weights.length;
        }
        else return 0;
    }
}