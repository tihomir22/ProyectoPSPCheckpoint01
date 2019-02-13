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
import proyectocine.Seient.Estat;

/**
 *
 * @author sportak
 */
public class FilServidorCompraEntrades extends Thread {

    Socket sc;
    DataInputStream input;
    DataOutputStream output;
    String mensaje = "", respuesta;
    static int idCliente = (int) Math.round(Math.random() * 1000);
    Pelicula peli;
    Sessio ses;
    Seient sei;
    Seient[][] asientosSesion;
    ArrayList<Seient> listaAsientos = new ArrayList<>();
    int numEntradas;
    int contEntradasProcesadas = 0;
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
        } catch (InterruptedException ex) {
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
            case 3:
                return respuesta + "\n" + this.ses.mapaSessionTCP() + "\n Introduzca fila de asiento y numero de asiento separados por espacio";
            case 4:
                return respuesta + "\n Es correcto el asiento? S/N";
            case 5:
                return this.ses.mapaSessionTCP() + "\n" + respuesta;

        }
        return "Error de solicitud";
    }

    public String procesarRespuesta(String mensaje) throws InterruptedException {
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
                        asientosSesion = this.ses.getSeients();
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

            case 3:
                System.out.println("He recibido del cliente " + mensaje);
                String[] respuesta = mensaje.split(" ");
                if (respuesta.length >= 2) {
                    if (respuesta[0].trim().chars().allMatch(Character::isDigit) && respuesta[1].trim().chars().allMatch(Character::isDigit)) {
                        int filas = Integer.parseInt(respuesta[0]);
                        int col = Integer.parseInt(respuesta[1]);
                        sei = new Seient(filas, col);
                        opcion = 4;
                        return sei.toString();
                    } else {
                        return "Has introducido mal los datos, un ejemplo correcto es : [ 2 4 ]";
                    }
                } else {
                    return "Has introducido mal los datos, un ejemplo correcto es : [ 2 4 ]";
                }

            case 4:
                System.out.println("He recibido del cliente " + mensaje);
                if (mensaje.equalsIgnoreCase("S")) {
                    if (this.asientosSesion[sei.getFilaSeient() - 1][sei.getNumeroSeient() - 1].verificaSeient()) {
                        this.asientosSesion[sei.getFilaSeient() - 1][sei.getNumeroSeient() - 1].reservantSeient();
                        this.sei.setIdCliente(idCliente);
                        this.listaAsientos.add(this.sei);
                        this.ses.setSeients(this.asientosSesion);

                        if (this.numEntradas - 1 == this.contEntradasProcesadas) {
                            this.opcion = 5;
                            //this.contEntradasProcesadas++;
                            return "Todas las entradas fijadas , ahora procedemos a realizar el pago";
                        } else {
                            this.opcion = 3;
                            this.contEntradasProcesadas++;
                            return "Reserva realizado con exito del asiento " + this.asientosSesion[sei.getFilaSeient() - 1][sei.getNumeroSeient() - 1].toString();
                        }
                    } else {
                        this.contEntradasProcesadas = 0;
                        this.opcion = 3;
                        this.aliberar_a_los_perros(listaAsientos, this.ses.getSeients());
                        return "Esa butaca ya esta reserva / ocupada";
                    }

                } else {
                    this.opcion = 3;
                    return "Vuelve a introducir la entrada";
                }
            case 5:
                comprarEntradaPelicula();
                break;

        }
        return "** Respuesta por defecto **";
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

            int fila = this.listaAsientos.get(i).getFilaSeient();

            int seient = this.listaAsientos.get(i).getNumeroSeient();

            System.out.println(this.getName() + " Va a intentar reservar asiento fila: " + fila + " seient " + seient);
            //Esto se hace porque el cliente estará reservando entradas y mientras lo hace y aun no ha pagado se quedaran en estado de reserva para posteriormente ser pagadas
            if (seients[fila - 1][seient - 1].getDisponibilitat() == Estat.RESERVANT && this.listaAsientos.get(i).getIdCliente() == idCliente) {
                seients[fila - 1][seient - 1].alliberaSeient();
            }

            if (seients[fila - 1][seient - 1].verificaSeient()) { //Si SEIENT lliure -> reserva SEIENT y si esta reservando tambien se puede ocupar
                seients[fila - 1][seient - 1].reservantSeient();
                listaAsientosTemporal.add(seients[fila - 1][seient - 1]);
                System.out.println(this.getName() + " Seient reservat" + listaAsientosTemporal.get(listaAsientosTemporal.size() - 1).toString());
                Thread.sleep(1000);

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
        System.out.println(listaAsientosOcupados.size() + "\n");
        System.out.println(seients.length + "\n");

        for (int i = 0; i < listaAsientosOcupados.size(); i++) {
            seients[listaAsientosOcupados.get(i).getFilaSeient() - 1][listaAsientosOcupados.get(i).getNumeroSeient() - 1].alliberaSeient();
        }
        listaAsientosOcupados.clear();
        System.out.print(this.getName() + " Aliberados con exito " + listaAsientosOcupados.size() + " asientos \n");
        System.out.println(listaAsientosOcupados.size() + "\n");
        System.out.println(seients.length + "\n");
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

    //PAGAMENT ENTRADA TCP
    public String pagamentEntradaTCP(BigDecimal preu) {
        System.out.println(this.getName() + " Import a pagar: " + preu);
        System.out.println(this.getName() + " \nPagant...(2seg)");
        String res = " Import a pagar: " + preu + " \nPagant...(2seg)";

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(FilServidorCompraEntrades.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;

    }
}
