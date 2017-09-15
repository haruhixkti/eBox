/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infoProyecto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Katherine
 */
public class CapturaInformacion {
    
    boolean faceRecoder,activityRender,nuevaPerspectiva;
    boolean dispositivoSeleccionado=true;
    String direcionIp,nombreProyecto,pathProyecto,descripcionProyecto;
    public boolean getDispositivoSeleccionado(){
    return dispositivoSeleccionado;
    }
    public static boolean guardarInformacion(){
    
    return true;
    }
    public static boolean leerInformacion(String path) throws FileNotFoundException, IOException{
    File archivo = new File (path);
    FileReader fr = new FileReader(archivo);
    BufferedReader br = new BufferedReader(fr);
    String linea = br.readLine();
        System.out.println("linea:"+ linea);
        return true;
    }
        public static void main(String args[]) throws IOException {
        boolean archivoEncontrado;
            archivoEncontrado = leerInformacion("C:\\Users\\Katherine\\Desktop\\archivo.txt");
            
    }
}
