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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sportak
 */
public class Cliente {

    final static String HOST = "localhost";
    
    static Scanner teclado = new Scanner(System.in);
    static String mensaje = " ";
    //Puerto del servidor
    final static int PUERTO = 5000;
    static DataInputStream in;
    static DataOutputStream out;
    static String mensajeEnviado = "";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            Socket sc = new Socket(HOST, PUERTO);
            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());
            while (!mensajeEnviado.equalsIgnoreCase("fin")) {
                System.out.println("[CLIENTE] He recibido como respuesta " + in.readUTF());
                mensajeEnviado = teclado.nextLine();
                System.out.println("[CLIENTE] Voy a enviar " + mensajeEnviado);
                out.writeUTF(mensajeEnviado);

            }

            sc.close();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
