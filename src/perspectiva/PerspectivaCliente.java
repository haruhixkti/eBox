/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perspectiva;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import codeebox.vistas.PestanasPrincipal;
/**
 *
 * @author Katherine
 */
public class PerspectivaCliente extends Thread implements Serializable {
    
    private ObjectInputStream entrada;
    private PrintWriter salida;
    private boolean running = true;
    private String instruccion = "";
    private Socket cliente;
    private final int port = 3000;
    private List<String> dispositivos = new ArrayList<>();
    public static boolean listaDispositivos = false;
    public static boolean listaFrames = false;
    public static boolean video = false;
    private String path;
    public static boolean con = false;
    private String nombreVideo = "";
    private String nombreProyecto = "";
    private String experimentos = "";
    
     public String getExperimentos() {
        return experimentos;
    }

    public void setExperimentos(String experimentos) {
        this.experimentos = experimentos;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getNombreVideo() {
        return nombreVideo;
    }

    public void setNombreVideo(String nombreVideo) {
        video = true;
        this.nombreVideo = nombreVideo;
    }

    public List<String> getDispositivos() {
        return dispositivos;
    }

    public void setDispositivos(List<String> dispositivos) {
        listaDispositivos = true;
        this.dispositivos = dispositivos;
    }

    public PerspectivaCliente(String servidor) {
        listaDispositivos = false;
        listaFrames = false;
        video = false;
        try {
            cliente = new Socket(InetAddress.getByName(servidor), port);

            if (cliente != null) {
                salida = new PrintWriter(cliente.getOutputStream());
                entrada = new ObjectInputStream(cliente.getInputStream());
                con = true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
        public void enviarInstruccion(String cmd) {
        try {
            if (!"DISPOSITIVOS".equalsIgnoreCase(cmd)) {
                System.out.println("Enviando instruccion a servidor: " + cmd);
               // log.info("Enviando instruccion a servidor: " + cmd);
            }
            instruccion = cmd;
            salida.println(cmd);
            salida.flush();
        } catch (Exception e) {
            System.out.println("error");
            //log.error(e);
        }
    }
            public Object recibirObj() {
        Object o = null;
        if (entrada != null) {
            try {
                o = entrada.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        return o;
    }
             public void recibirArchivo() {
        byte[] content = (byte[]) recibirObj();
        try {
            Files.write(new File(path + getNombreProyecto() + "_" + getNombreVideo() + "_" + getExperimentos() + ".avi").toPath(), content);
        } catch (IOException ex) {
             System.out.println("error");   
            //log.error(ex);
        }
    }
    @Override
    public synchronized void run() {
        while (running) {
            if (!"CERRAR".equalsIgnoreCase(instruccion)) {
                Object o = recibirObj();

                if (o != null) {
                    if (o instanceof List) {
                        if ("DISPOSITIVOS".equalsIgnoreCase(instruccion)) {
                            setDispositivos((List<String>) o);
                        }
                    }
                    if (o instanceof String) {

                        if (o.toString().contains("nombreVideo:")) {
                            String[] arr = o.toString().split(":");
                            setNombreVideo(arr[1]);

                        } else {
                            System.out.println("Recibiendo respuesta del servidor: " + o);
                            //log.info("Recibiendo respuesta del servidor: " + o);
                        }
//                        if (instruccion.contains("FRAMES")) {
//                            log.info("Canal externo: Inicio de grabacion");
//                        }
                        if (o.toString().equalsIgnoreCase("Canal externo: Transfiriendo video")) {
                            recibirArchivo();
                        }

                        if (o.toString().equalsIgnoreCase("Canal externo: Inicio de grabación")) {
                            PestanasPrincipal.ScreenGo = true;
                        }
                        
                        if (o.toString().equalsIgnoreCase("Canal externo: Fin de grabación")) {
                            PestanasPrincipal.ScreenStop = true;
                        }
                        
                        
//                        if (instruccion.contains("DETENER")) {
//                            log.info("Canal externo: Fin de grabacion");
//                        }
                    }
                }
            } else {
                con = false;
                running = false;
            }
        }
    }
     public String getPath() {
        return path;
    }
      public void setPath(String path) {
        this.path = path;
    }
}
