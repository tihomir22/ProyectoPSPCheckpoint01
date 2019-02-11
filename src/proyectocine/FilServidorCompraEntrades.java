/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectocine;

import com.sun.xml.internal.ws.util.StringUtils;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
    String mensaje = "", respuesta;
    Pelicula peli;
    Sessio ses;
    int numEntradas;
    int opcion = 0;

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
        try {
            while (!mensaje.equalsIgnoreCase("fin")) {
                System.out.println("[HILO SERVIDOR  " + this.getName() + "] Voy a renviarle informacion al cliente");
                output.writeUTF(procesarEscritura(this.opcion));
                mensaje = input.readUTF();
                respuesta = this.procesarRespuesta(mensaje);

                System.out.println("[HILO SERVIDOR  " + this.getName() + "] He recibido " + mensaje);
            }
            output.close();
            input.close();
        } catch (IOException ex) {
            Logger.getLogger(FilServidorCompraEntrades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String procesarEscritura(int opcion) {
        switch (opcion) {
            case 0:
                return "\nElija una pelicula" + Pelicules.respuestaPeliculasTCP();
            case 1:
                return respuesta + "\nElija una sesion" + Sessions.respuestaSessionsTCP();
            case 2:
                return respuesta + "\nIntroduzca numero de entradas";
            case 3: {
                try {
                    this.comprarEntradaPelicula();
                    return this.ses.mapaSessionTCP() + "\n";
                } catch (InterruptedException ex) {
                    Logger.getLogger(FilServidorCompraEntrades.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return "Iniciando compra de entradas ...";

        }
        return "Error de solicitud";
    }

    public String procesarRespuesta(String mensaje) {
        boolean isNumeric;
        switch (opcion) {
            case 0:
                System.out.println("He recibido del cliente " + mensaje);
                isNumeric = mensaje.trim().chars().allMatch(Character::isDigit);
                if (isNumeric) {
                    Integer index = Integer.parseInt(mensaje.trim());
                    if (index < Pelicules.quantitatPelicules()) {
                        opcion = 1;
                        this.peli = Pelicules.retornaPelicula(index + 1);
                        return Pelicules.retornaPelicula(index + 1).toString();
                    }

                } else {
                    return "Has introducido un dato incorrecto!";
                }
                break;
            case 1:
                System.out.println("He recibido del cliente " + mensaje);
                isNumeric = mensaje.trim().chars().allMatch(Character::isDigit);
                if (isNumeric) {
                    Integer index = Integer.parseInt(mensaje.trim());
                    if (index < Sessions.quantitatSessions()) {
                        opcion = 2;
                        this.ses = Sessions.retornaSessio(index + 1);
                        return Sessions.retornaSessio(index + 1).toString();
                    }

                } else {
                    return "Has introducido un dato incorrecto!";
                }
            case 2:
                System.out.println("He recibido del cliente " + mensaje);
                isNumeric = mensaje.trim().chars().allMatch(Character::isDigit);
                if (isNumeric) {
                    Integer index = Integer.parseInt(mensaje.trim());
                    this.numEntradas = index;
                    System.out.println("He recibido " + index + " entradas");
                    opcion = 3;
                } else {
                    return "Has introducido un dato incorrecto!";
                }
        }
        return "Error de respuesta";
    }

    public void comprarEntradaPelicula() throws InterruptedException {
        Sala sa = null;
        System.out.println(this.getName() + " Pelicula elegida " + this.peli);
        System.out.println();
        System.out.println();
        Thread.sleep(1000);
        //Selecció de la SESSIO
        System.out.println(this.getName() + " Sesion elegida" + this.ses);
        Thread.sleep(1000);
        sa = this.ses.getSala();
        this.ses.mapaSessio();
        Thread.sleep(1000);
        Seient[][] seients = this.ses.getSeients();
        ArrayList<Seient> listaAsientosTemporal = new ArrayList<>();

        int i = 0;
        while (i < this.numEntradas) {

            int fila = (int) (Math.random() * ((sa.getFiles() - 1) + 1)) + 1;
            int seient = (int) (Math.random() * ((sa.getTamanyFila() - 1) + 1)) + 1;

            System.out.println(this.getName() + " Va a intentar reservar asiento fila: " + fila + " seient " + seient);

            if (seients[fila - 1][seient - 1].verificaSeient()) { //Si SEIENT lliure -> reserva SEIENT
                seients[fila - 1][seient - 1].reservantSeient();
                listaAsientosTemporal.add(seients[fila - 1][seient - 1]);
                System.out.println(this.getName() + " Seient reservat" + listaAsientosTemporal.get(listaAsientosTemporal.size() - 1).toString());
                Thread.sleep(1000);
                //se.imprimirTicket(seients[fila - 1][seient - 1], se, sa, p);
            } else { //NO Reserva
                System.out.println(this.getName() + " \t ERROR Cine:compraEntradaPelicula: No sha pogut fer reserva Seient fila" + fila + " seient " + seient);
                aliberar_a_los_perros(listaAsientosTemporal, seients);
                return;
            };
            Thread.sleep(1000);
            this.ses.mapaSessio();
            i++;
        }
        if (pagamentEntrada(new BigDecimal(listaAsientosTemporal.size()).multiply(this.ses.getPreu()))) {
            for (int j = 0; j < listaAsientosTemporal.size(); j++) { // Una vez pagada la entrada, se disponen a ocupar los asientos en si y a imprimir los tickets
                seients[listaAsientosTemporal.get(j).getFilaSeient()][listaAsientosTemporal.get(j).getNumeroSeient()].ocupaSeient();
                this.ses.imprimirTicket(seients[listaAsientosTemporal.get(j).getFilaSeient()][listaAsientosTemporal.get(j).getNumeroSeient()], this.ses, sa, this.peli);
            }

        } else {
            aliberar_a_los_perros(listaAsientosTemporal, seients);
        }

    }

    public void aliberar_a_los_perros(ArrayList<Seient> listaAsientosOcupados, Seient[][] seients) {
        for (int i = 0; i < listaAsientosOcupados.size(); i++) {
            seients[listaAsientosOcupados.get(i).getFilaSeient()][listaAsientosOcupados.get(i).getNumeroSeient()].alliberaSeient();
        }
        System.out.print(this.getName() + " Aliberados con exito " + listaAsientosOcupados.size() + " asientos");

    }

    //PAGAMENT D'UNA ENTRADA
    boolean pagamentEntrada(BigDecimal preu) {
        System.out.println(this.getName() + " Import a pagar: " + preu);
        System.out.println(this.getName() + " \nPagant...(2seg)");
        //pagant
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;

    }
}
