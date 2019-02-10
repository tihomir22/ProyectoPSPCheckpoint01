/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectocine;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sportak
 */
public class FilServidorCompraEntrades extends Thread {

    Socket sc;
    DataInputStream input;
    DataOutputStream output;
    String mensaje = "";

    public FilServidorCompraEntrades(Socket sc, String nombre) {
        try {
            this.sc = sc;
            this.setName(nombre);
            input = new DataInputStream(sc.getInputStream());
            output = new DataOutputStream(sc.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(FilServidorCompraEntrades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public FilServidorCompraEntrades() {

    }

    @Override
    public void run() {
        System.out.println("[HILO SERVIDOR  " + this.getName() + "] Se ha iniciado");
        ArrayList<Pelicula>lista=Pelicules.recuperarPeliculasArray();
        try {
            while (!mensaje.equalsIgnoreCase("fin")) {
                 System.out.println("[HILO SERVIDOR  " + this.getName() + "] Voy a renviarle informacion al cliente");
                output.writeUTF(lista.toString());
                mensaje = input.readUTF();
                System.out.println("[HILO SERVIDOR  " + this.getName() + "] He recibido " + mensaje);
               
            }
            output.close();
            input.close();
        } catch (IOException ex) {
            Logger.getLogger(FilServidorCompraEntrades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
