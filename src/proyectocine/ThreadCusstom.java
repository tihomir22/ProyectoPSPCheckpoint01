/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectocine;

/**
 *
 * @author mati
 */
public class ThreadCusstom extends Thread {

    public ThreadCusstom() {
    }

    @Override
    public void run() {
        System.out.println("Hilo Comprando ENTRADA...");
        Cine.compraEntradaPelicula();
        System.out.println("\n\n");
    }

}
