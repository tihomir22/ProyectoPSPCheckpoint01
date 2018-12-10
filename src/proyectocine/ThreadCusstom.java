import java.math.BigDecimal;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author mati
 */
public class ThreadCusstom extends Thread {
	
	
	static Pelicula pelicula=null;
	
    public ThreadCusstom() {
    
    }

    @Override
    public void run() {
        System.out.println("Hilo Comprando ENTRADA...");
			this.comprarEntradaPelicula();
		
        System.out.println("\n\n");
    }
    
    public void comprarEntradaPelicula() {
    	Pelicula p = null;
        Sala sa = null;
        Sessio se = null;
        //		String nsala, nsessio, npelicula, nfila, nseient;
        //		int sala, 
        int sessio, pelicula, fila, seient, numEntrades;

        //Si NO hi ha PELICULES, s'ix del procés de compra
        if (Pelicules.llistarPelicules() == 0) {
            System.out.println("\t ERROR Cine:compraEntradaPelicula: No hi ha PELICULES");
            return;
        }
        //Selecció de PELICULA
        pelicula = Validacio.validaSencer("\t Tria PELICULA:", Pelicules.quantitatPelicules());
        p = Pelicules.retornaPelicula(pelicula);
        System.out.println(p);
        System.out.println();
        System.out.println();

        //Si NO hi ha SESSIONS per la PELICULA, s'ix del procés de compra
        if (p.llistarSessionsPeli() == 0) {
            System.out.println("\t ERROR Cine:compraEntradaPelicula: No hi ha SESSIONS per a esta PELICULA");
            return;
        }
        //Selecció de la SESSIO
        sessio = Validacio.validaSencer("\t Tria la sessió per a " + p.getNomPeli() + ":", p.getSessionsPeli().size());

        se = p.retornaSessioPeli(sessio);
        sa = se.getSala();
        se.mapaSessio();
        Seient[][] seients = se.getSeients();
        ArrayList<Seient>listaAsientosTemporal=new ArrayList<>();
        
        int numeroAsientos = Validacio.validaSencer("Introduzca numero de entradas", (seients.length * seients[0].length));
        int i = 0;
        while (i < numeroAsientos) {
            fila = Validacio.validaSencer("\tTria FILA: [1-" + sa.getFiles() + "] ", sa.getFiles());
            seient = Validacio.validaSencer("\t Tria SEIENT en la fila: [1-" + sa.getTamanyFila() + "]", sa.getTamanyFila());

            if (seients[fila - 1][seient - 1].verificaSeient()) { //Si SEIENT lliure -> reserva SEIENT
                //pagament entrada
                //pagamentEntrada(se.getPreu());
                //seients[fila - 1][seient - 1].ocupaSeient();
            	seients[fila - 1][seient - 1].reservantSeient();
            	listaAsientosTemporal.add(seients[fila-1][seient-1]);
                System.out.println("Seient reservat" + listaAsientosTemporal.get(listaAsientosTemporal.size()-1).toString());
                //se.imprimirTicket(seients[fila - 1][seient - 1], se, sa, p);
            } else { //NO Reserva
                System.out.println("\t ERROR Cine:compraEntradaPelicula: No sha pogut fer reserva Seient");
            };
            se.mapaSessio();
            i++;
        }
        pagamentEntrada(new BigDecimal(listaAsientosTemporal.size()).multiply(se.getPreu()));
        for(int j=0;j<listaAsientosTemporal.size();j++) { // Una vez pagada la entrada, se disponen a ocupar los asientos en si y a imprimir los tickets
        	seients[listaAsientosTemporal.get(j).getFilaSeient()][listaAsientosTemporal.get(j).getNumeroSeient()].ocupaSeient();
        	se.imprimirTicket(seients[listaAsientosTemporal.get(j).getFilaSeient()][listaAsientosTemporal.get(j).getNumeroSeient()], se, sa, p);
        }
    	
    }
    public  void reserva_numEntrades(Pelicula p, Sessio se, Sala sa, int numEntrades) throws InterruptedException {
        boolean isReservat = true;
        int fila, seient;
        //ArrayList de la quantitat de seients que es volen comprar
        ArrayList<Seient> seientsAcomprar = new ArrayList<Seient>();

        for (int i = 0; i < numEntrades; i++) {
            System.out.println("\tSeient " + (i + 1) + " :");
            fila = Validacio.validaSencer("\t\t Tria FILA: [1-" + sa.getFiles() + "] ", sa.getFiles());
            seient = Validacio.validaSencer("\t\t Tria SEIENT en la fila: [1-" + sa.getTamanyFila() + "]", sa.getTamanyFila());
            Seient[][] seients = se.getSeients();
            if (seients[fila - 1][seient - 1].verificaSeient()) { //Reserva SEIENT
                seients[fila - 1][seient - 1].reservantSeient();
                seientsAcomprar.add(seients[fila - 1][seient - 1]);//afegeix seient a llista SEIENTS RESERVATS
            } else { //NO Reserva
                System.out.println("\t ERROR Cine:validaSeient: Seient reservat/ocupat");
                isReservat = false;
            };
        }//endfor

        if (isReservat) { //Compra seients
            System.out.println("\nSEIENT RESERVATS: " + seientsAcomprar.size());
            pagamentEntrada(new BigDecimal(numEntrades).multiply(se.getPreu()));
            for (int i = 0; i < seientsAcomprar.size(); i++) {
                Seient s = seientsAcomprar.get(i);
                s.ocupaSeient(); 		//ocupa seient
                se.imprimirTicket(s, se, sa, p);
                System.out.println();
            }//for
        } else {// Llibera seients
            System.out.println("\t\tNO sha pogut fer la compra de " + numEntrades + " entrades. Es queden Lliures");
            for (int i = 0; i < seientsAcomprar.size(); i++) {
                Seient s = seientsAcomprar.get(i);
                s.alliberaSeient(); 		//ocupa seient
            }//for
        }
        for (int i = seientsAcomprar.size(); i > 0; i--) {
            seientsAcomprar.remove(i - 1); //elimina seient de la llista
        }
    }

    //*********************************************************
    //PAGAMENT D'UNA ENTRADA
     boolean pagamentEntrada(BigDecimal preu) {
        System.out.println("Import a pagar: " + preu);
        System.out.println("\nPagant...(2seg)");
        //pagant
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Validacio.validaBoolea("Pagat? (S/N)");

    }

}
