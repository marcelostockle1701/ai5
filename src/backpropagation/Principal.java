package backpropagation;

import java.io.IOException;
import java.util.ArrayList;

public class Principal {
    public static void main(String[] args) {
        String trainfile = "data\\train.csv";
        String testfile = "data\\test.csv";
        System.out.println("Training file: " + trainfile);
        System.out.println("Testing file: " + testfile);
        int filas = 60000, columnas = 784, categorias = 10;
        int[][] arr;
        try {
            arr = LeerCSV.leerArchivo(trainfile, filas, columnas + 1);
        } catch (IOException e) {
            arr = null;
            e.printStackTrace();
            System.exit(1);
        }
        int[] layers = new int[]{ 5, 6,categorias };
        double tasaAprendizaje = 0.7;
        int iteraciones = 60000;
        
        MultiLayerPerceptron net = new MultiLayerPerceptron(layers, tasaAprendizaje);
        for (Layer capa: net.getCapas()){
            for(int i=0; i < capa.layerSize(); i++){
                capa.getNeuron(i).setFuncionTransferencia(new Hyperbolic());
            }
        }
        ArrayList<Double> inputs = new ArrayList<>();
        ArrayList<Double> outputs = new ArrayList<>();
        double error;
        for(int i = 0; i < iteraciones && i < filas; i++){
            inputs.clear();
            for(int j = 0; j < columnas; j++)
                inputs.add((double) arr[i][j]/ 256.0);
            outputs.clear();
            for(int j = 0; j < categorias; j++)
                outputs.add((arr[i][columnas] == j) ? 1.0 : -1.0 );
            error = net.backPropagate(new Entrada(inputs,outputs));
            System.out.println("Error at step "+i+" is "+error);
        }
        System.out.println("Learning completed!");
        // System.out.println(net);
        // System.out.println("Press ENTER to proceed with testing...");
        
        filas = 10000;
        try { 
            // System.in.read();
            System.out.println("Reading test file");
            arr = LeerCSV.leerArchivo(testfile, filas, columnas + 1);
        }
        catch (IOException e) {
            arr = null;
            e.printStackTrace();
            System.exit(1);
        }
        
        int prediccion;
        double confianza;
        for(int i = 0; i < filas; i++){
            inputs.clear();
            for(int j = 0; j < columnas; j++)
                inputs.add((double) arr[i][j]/ 256.0);
            outputs.clear();
            outputs = net.execute(new Entrada(inputs,null));
            
            prediccion = 0;
            confianza = outputs.get(0);
            for(int j = 1; j < categorias; j++) {
                if (outputs.get(j) > confianza) {
                    confianza = outputs.get(j);
                    prediccion = j;
                }
            }
            System.out.println(i + ". Valor esperado: " + 
                    arr[i][columnas] + ", Prediccion: " +
                    prediccion + ", Confianza: " +
                    confianza);
        }
    }
}
