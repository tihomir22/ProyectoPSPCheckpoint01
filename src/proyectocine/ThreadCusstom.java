package proyectocine;
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
	
	
	private Pelicula pelicula=null;
	private Sessio sesion=null;
	private int numeroEntradas;
	private String nombreHilo;
	
    public ThreadCusstom(String nombre,Pelicula pel,Sessio ses,int num) {
    	this.setName(nombre);
    	this.pelicula=pel;
    	this.sesion=ses;
    	this.numeroEntradas=num;
    }

    @Override
    public void run() {
        System.out.println("Hilo Comprando ENTRADA...");
			try {
				this.comprarEntradaPelicula();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        System.out.println("\n\n");
    }
    
    public void comprarEntradaPelicula() throws InterruptedException {
    	Sala sa=null;
        System.out.println(this.getName()+" Pelicula elegida "+pelicula);
        System.out.println();
        System.out.println();
        Thread.sleep(1000);
        //Selecció de la SESSIO
        System.out.println(this.getName()+" Sesion elegida" + sesion);
        Thread.sleep(1000);
        sa = sesion.getSala();
        sesion.mapaSessio();
        Thread.sleep(1000);
        Seient[][] seients = sesion.getSeients();
        ArrayList<Seient>listaAsientosTemporal=new ArrayList<>();
        
     
        int i = 0;
        while (i < numeroEntradas) {
       
        	int fila=(int)(Math.random() * ((sa.getFiles() - 1) + 1)) + 1;
            int seient=(int)(Math.random() * ((sa.getTamanyFila() - 1) + 1)) + 1;
            
            System.out.println(this.getName()+" Va a intentar reservar asiento fila: " +fila+" seient "+seient);
            
            if (seients[fila - 1][seient - 1].verificaSeient()) { //Si SEIENT lliure -> reserva SEIENT
            	seients[fila - 1][seient - 1].reservantSeient();
            	listaAsientosTemporal.add(seients[fila-1][seient-1]);
                System.out.println(this.getName()+" Seient reservat" + listaAsientosTemporal.get(listaAsientosTemporal.size()-1).toString());
                Thread.sleep(1000);
                //se.imprimirTicket(seients[fila - 1][seient - 1], se, sa, p);
            } else { //NO Reserva
                System.out.println(this.getName()+ " \t ERROR Cine:compraEntradaPelicula: No sha pogut fer reserva Seient fila" +fila+" seient "+seient);
                aliberar_a_los_perros(listaAsientosTemporal,seients);
                return;
            };
            Thread.sleep(1000);
            sesion.mapaSessio();
            i++;
        }
        if(pagamentEntrada(new BigDecimal(listaAsientosTemporal.size()).multiply(sesion.getPreu()))) {
	        for(int j=0;j<listaAsientosTemporal.size();j++) { // Una vez pagada la entrada, se disponen a ocupar los asientos en si y a imprimir los tickets
	        	seients[listaAsientosTemporal.get(j).getFilaSeient()][listaAsientosTemporal.get(j).getNumeroSeient()].ocupaSeient();
	        	sesion.imprimirTicket(seients[listaAsientosTemporal.get(j).getFilaSeient()][listaAsientosTemporal.get(j).getNumeroSeient()], sesion, sa, pelicula);
	        }
	       
        }else {
        	aliberar_a_los_perros(listaAsientosTemporal,seients);
        }
    }
    
    public  void aliberar_a_los_perros(ArrayList<Seient>listaAsientosOcupados,Seient[][]seients) {
    	for(int i=0;i<listaAsientosOcupados.size();i++) {
    		seients[listaAsientosOcupados.get(i).getFilaSeient()][listaAsientosOcupados.get(i).getNumeroSeient()].alliberaSeient();
    	}
    	System.out.print(this.getName()+" Aliberados con exito " + listaAsientosOcupados.size()+ " asientos");
    	
    }
    
  //*********************************************************
    //PAGAMENT D'UNA ENTRADA
     boolean pagamentEntrada(BigDecimal preu) {
        System.out.println(this.getName()+" Import a pagar: " + preu);
        System.out.println(this.getName()+" \nPagant...(2seg)");
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
