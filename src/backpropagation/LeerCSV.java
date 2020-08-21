package backpropagation;

import java.io.*;
// import java.util.LinkedList;
import java.util.Scanner;

public class LeerCSV {
    
    static int[][] leerArchivo(String filename, int rows, int columns) throws FileNotFoundException
    {
        int output[][] = new int[rows][columns];
        File archivo = new File(filename);
        Scanner scan = new Scanner(archivo);
        String linea;
        String aux[];
        int rowcount = 0;
        while(scan.hasNextLine()) {
            linea = scan.nextLine();
            aux = linea.split(";");
            for(int i = 0; i < columns; i++) {
                output[rowcount][i] = Integer.parseInt(aux[i]);
                
            }
            rowcount++;
        }
        return output;
    }
    /*
    LinkedList<LinkedList<Integer>> leerArchivo(String filename) throws FileNotFoundException
    {
        File archivo = new File(filename);
        LinkedList<LinkedList<Integer>> output = new LinkedList<>();
        LinkedList<Integer> tmp;
        Scanner scan = new Scanner(archivo);
        String linea;
        String aux[];
        while(scan.hasNextLine())
        {
            linea = scan.nextLine();
            aux = linea.split(";");
            tmp = new LinkedList<>();
            for(String numero : aux)
            {
                tmp.add(Integer.parseInt(numero));
            }
            output.add(tmp);
        }
        return output;
    }
    
    int[][] linkedToArray( LinkedList<LinkedList<Integer>> data)
    {
        int n, m;
        n = data.size();
        m = data.get(0).size();
        int output[][] = new int[n][m];
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<m;j++)
            {
                output[i][j] = data.get(i).get(j);
            }
        }
        return output;   
    }
    */
}
