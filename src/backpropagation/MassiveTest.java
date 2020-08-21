package backpropagation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author lucacrot
 */
public class MassiveTest {

    static ArrayList<Entrada> entradas;
    /*Como los datos vienen desde un archivo se 'revolverán'
    y luego e utilizará un 80% para entrenamiento y luego un 20% para probar la red*/
    public static void main(String[] args){
        double trainingError, testError;
        int[] layers = new int[]{ 16, 8,4,2,4 };
        MultiLayerPerceptron net = new MultiLayerPerceptron(layers, 0.6);
        for(Layer capa: net.getCapas()){
            for(int i=0; i < capa.layerSize(); i++){
                capa.getNeuron(i).setFuncionTransferencia(new Sigmoidal());
            }
        }
        readingData("kart1.csv");
        int cien = entradas.size();
        int ochenta = (int)(cien*0.8);
        /* Training */
        trainingError = 0;
        int repetition = 3; //Aplicaremos 3 veces el 80% de los datos entrada para mejorar el entrenamiento
        for(int i = 0; i < ochenta*repetition; i++){
            double error;
            error = net.backPropagate(entradas.get(i%ochenta));
            System.out.println("Error at step "+i+" is "+error);
            trainingError += error;

        }
        System.out.println("Learning completed!");
        //System.out.println(net);

        /* Test */
        testError = 0;
        for(int i = ochenta; i < cien; i++){
            double error = 0.0;
            ArrayList<Double> outputTest = net.execute(entradas.get(i));
            for(int j=0; j < entradas.get(i).sizeOutput(); j++){   
                error += Math.abs(outputTest.get(j) - entradas.get(i).getOutput(j));
            }
            error = error / entradas.get(i).sizeOutput();
            System.out.println("Error at test "+(i-ochenta)+" is "+error);
            testError += error;
        }
        double trainingFactor = trainingError/(ochenta*repetition);
        double testFactor = testError/(cien-ochenta);
        System.out.println("Training error: "+trainingFactor);
        System.out.println("Test error: "+testFactor);
        if(testFactor > 1.5*trainingFactor){
            //Si el error de testeo es mucho mayor al de entrenamiento
            System.out.println("OVERFITTING");
            //Es decir la red no aprendió, sino que memorizó las entradas
        }
    }
    
    private static void readingData(String file) {
        ArrayList<Entrada> orderedEntradas = new ArrayList<>();
        File f = new File(file);
        try{
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String aux = br.readLine(); //leemos la primera linea
            while(aux!=null){ //esto actua como el while(!feof)
                //System.out.println("¡"+aux+"¡");
                String[] values = aux.split(";");
                /*for(int k=0; k < values.length;k++)
                    System.out.println("¡"+values[k]+"¡");*/
                if(values.length==20){
                    ArrayList<Double> inputs = new ArrayList<>();
                    for(int i=0;i<16;i++){
                        double x = Double.parseDouble(values[i]);
                        inputs.add(x);
                    }
                    ArrayList<Double> output = new ArrayList<>();
                    for(int i=16;i<20;i++){
                        double x = Double.parseDouble(values[i]);
                        output.add(x);
                    }
                    orderedEntradas.add(new Entrada(inputs,output));
                }
                aux = br.readLine(); //leemos una nueva linea
            }
            //cerramos todo
            br.close();
            fr.close();
            entradas = new ArrayList<>();
            entradas.add(orderedEntradas.get(0));
            Random rnd = new Random();
            for(int i=1;i<orderedEntradas.size();i++){
                int s = entradas.size();
                int x = rnd.nextInt(s+1);
                entradas.add(x,orderedEntradas.get(i));
            }
        }
        catch(IOException e){ 
            System.err.println("Archivo no leido: "+e.getMessage());
        }
    }
    
}
