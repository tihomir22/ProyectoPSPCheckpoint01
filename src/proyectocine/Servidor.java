/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectocine;

import proyectocine.FilServidorCompraEntrades;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sportak
 */
public class Servidor extends Thread {

    ServerSocket socket_s = null;
    Socket sc = null;
    DataInputStream input;
    DataOutputStream output;
    String mensaje = "";
    final int puertoServidor = 5000;

    @Override
    public void run() {
        try {
            socket_s = new ServerSocket(puertoServidor);
            System.out.println("[SERVIDOR] Servidor iniciado");
            while (true) {
                sc = socket_s.accept();
                System.out.println("[SERVIDOR] Se ha recibido petición del cliente , se lanza FilServidorCompraEntrades");
                FilServidorCompraEntrades fil = new FilServidorCompraEntrades(sc, "HILO"+Math.round(Math.random())*100);
                fil.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
