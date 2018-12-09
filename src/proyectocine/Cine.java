package proyectocine;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Scanner;

public class Cine {

    public static Pelicula peliculaPagament;
    public static Sessio sesionPagament;
    public static Seient asientoPagament;

    public static void main(String[] args) throws InterruptedException {
        //...
        // IMPLEMENTAR CODI ACÍ
        //...

        int opcio = 999, opcionElegida = 999, numSala, numFilas, numAsientosXFila, dia, mes, año, hora, minutos, duracionPelicula;
        String nombreSession, nombrePelicula, nacionalidad, director, interprete, arguments, genere, clasificacio;
        Calendar calendario;
        boolean es3d;
        BigDecimal dec;
        Scanner teclado = new Scanner(System.in);
        Sales sales = new Sales();
        Sessions sesiones = new Sessions();
        Pelicules pelicules = new Pelicules();

        Pelicula peliActiva = new Pelicula();
        Sala salaActiva = new Sala();
        Sessio sesionActiva = new Sessio();

        do {
            opcio = menu();

            switch (opcio) {

                case 1:
                    numSala = Validacio.validaSencer(" Introduzca numero de sala", 1000);
                    es3d = Validacio.validaBoolea(" Es 3D? S/N");
                    numFilas = Validacio.validaSencer("Introduce numero de filas", 1000);
                    numAsientosXFila = Validacio.validaSencer("Introduzca tamaño de fila", 1000);
                    Sala sal = new Sala(numSala, es3d, numFilas, numAsientosXFila);
                    System.out.println(sal.toString());
                    Sales.afegirSala(sal);

                    break;

                case 2:
                    Sales.llistarSales();
                    opcionElegida = Validacio.validaSencer("Elige una sala a modificar , introduce su indice", Sales.quantitatSales());
                    Sala s = Sales.retornaSala(opcionElegida);
                    System.out.println("Introduce nuevos datos en el siguiente orden : NUMERO SALA - NUMERO FILAS - CANTIDAD DE ASIENTOS POR FILA - ES 3D (False o True)");
                    numSala = teclado.nextInt();
                    if (Sales.validaIdSala(numSala) == false) {
                        numFilas = teclado.nextInt();
                        numAsientosXFila = teclado.nextInt();
                        es3d = teclado.nextBoolean();
                        s.setNumeroSala(numSala);
                        s.setFiles(numFilas);
                        s.setTamanyFila(numAsientosXFila);
                        s.setSala3D(es3d);
                        Sales.modificaSala(s, opcionElegida);
                        System.out.println("Modificada con exito" + s.toString());
                    } else {
                        System.out.println("Ya existe una sala con ese numero de sala");
                    }

                    break;

                case 3: //Esborrar SALA
                    if (Sales.quantitatSales() > 0) {
                        Sales.llistarSales();
                        opcionElegida = Validacio.validaSencer("Elige una sala a borrar , introduce su indice", Sales.quantitatSales());
                        Sales.esborraSala(opcionElegida);
                        System.out.println("Eliminado con exito");
                    } else {
                        System.out.println("No hay salas");
                    }
                    break;

                case 4:	//Crear SESSIO
                    if (Sales.quantitatSales() > 0) {
                        Sales.llistarSales();
                        opcionElegida = Validacio.validaSencer("Elige una sala para la sesion, introduce su indice", Sales.quantitatSales());
                        salaActiva = Sales.retornaSala(opcionElegida);
                        System.out.println("sala seleccionada" + salaActiva);
                        nombreSession = Validacio.validaCadena("Introduzca nombre de la sesion");
                        calendario = Validacio.validaData("Introduzca la fecha con el siguiente formato DD/MM/AAAA");
                        dec = Validacio.validaMoneda("Introduzca precio");
                        Sessio sesion = new Sessio(nombreSession, calendario, salaActiva, dec);
                        Seient[][] asiento = new Seient[salaActiva.getFiles()][salaActiva.getTamanyFila()];
                        sesion.setSeients(asiento);
                        Sessions.afegirSessio(sesion);
                        sesion.setMapaSessio();
                        sesion.mapaSessio();
                        System.out.println("Deseas añadir salas a la sesion? [S] [N]");
                        String eleccion = teclado.nextLine();
                        if (eleccion.equalsIgnoreCase("S") && Sales.quantitatSales() > 0) {
                            Sales.llistarSales();
                            opcionElegida = Validacio.validaSencer("Elija una sala a para añadir a la sesion", Sales.quantitatSales());
                            sesion.setSala(Sales.retornaSala(opcionElegida));
                            System.out.println("Añadida con exito");
                        } else {
                            System.out.println("No hay salas ");
                        }

                    } else {
                        System.out.println("Debes dar de alta alguna sala primero..");
                    }
                    break;

                case 5: //Modifica SESSIO

                    Sessions.llistarSessions();
                    opcionElegida = Validacio.validaSencer("Elija una sesion a modificar", Sessions.quantitatSessions());
                    sesionActiva = Sessions.retornaSessio(opcionElegida);
                    String nombreSesion = Validacio.validaCadena("Introduzca nombre de la sesion");
                    calendario = Validacio.validaData("Introduzca la fecha con el siguiente formato DD/MM/AAAA");
                    Sales.llistarSales();
                    salaActiva = Sales.retornaSala(Validacio.validaSencer("Elige la nueva sala", Sales.quantitatSales()));
                    if (salaActiva != null) {
                        dec = Validacio.validaMoneda("Introduce precio");
                        sesionActiva = new Sessio(nombreSesion, calendario, salaActiva, dec);
                        sesionActiva.setSeients(new Seient[salaActiva.getFiles()][salaActiva.getTamanyFila()]);
                        System.out.println("Datos de la nueva sesion" + sesionActiva);
                    } else {
                        System.out.println("Introduciste una sala incorrecta");
                    }

                    break;

                case 6: //Esborrar SESSSIO
                    if (Sessions.quantitatSessions() > 0) {
                        Sessions.llistarSessions();
                        opcionElegida = Validacio.validaSencer("Elija una sesion a eliminar", Sessions.quantitatSessions());
                        Sessions.esborraSessio(opcionElegida);
                    } else {
                        System.out.println("No hay sesiones");
                    }
                    break;

                case 7: //Crear PELICULA
                    nombrePelicula = Validacio.validaCadena("Introduzca nombre de la pelicula");
                    nacionalidad = Validacio.validaCadena("Introduzca nacionalidad");
                    duracionPelicula = Validacio.validaSencer("Introduzca duracion pelicula en minutos", 1000);
                    director = Validacio.validaCadena("Introduzca nombre del director");
                    interprete = Validacio.validaCadena("Introduzca nombre de los interpretes");
                    arguments = Validacio.validaCadena("Introduzca argumento");
                    genere = Validacio.validaCadena("Introduzca genero");
                    clasificacio = Validacio.validaCadena("Introduzca clasificacion");
                    Pelicula pelicula = new Pelicula(nombrePelicula, nacionalidad, duracionPelicula, director, interprete, arguments, genere, clasificacio);
                    Pelicules.afegirPelicula(pelicula);
                    System.out.println("Deseas añadir sesiones a la pelicula? [S] [N]");
                    String eleccion = teclado.nextLine();
                    if (eleccion.equalsIgnoreCase("S")) {
                        Sessions.llistarSessions();
                        opcionElegida = Validacio.validaSencer("Elija una session para añadir a la pelicula", Sessions.quantitatSessions());
                        pelicula.getSessionsPeli().add(Sessions.retornaSessio(opcionElegida));
                        System.out.println("Añadida con exito");

                    }
                    break;

                case 8: //Modifica PELICULA
                    Pelicules.llistarPelicules();
                    opcionElegida = Validacio.validaSencer("Elige pelicula a modificar", Pelicules.quantitatPelicules());
                    peliActiva = Pelicules.retornaPelicula(opcionElegida);
                    System.out.println("Seleccionada " + peliActiva.toString());
                    peliActiva.setNomPeli(Validacio.validaCadena("Introduzca nombre de la pelicula"));
                    peliActiva.setNacionalitat(Validacio.validaCadena("Introduzca nacionalidad"));
                    peliActiva.setDuracio(Validacio.validaSencer("Introduzca duracion pelicula", 1000));
                    peliActiva.setDirector(Validacio.validaCadena("Introduzca nombre del director"));
                    peliActiva.setInterprets("Introduzca nombre de los interpretes");
                    peliActiva.setArgument(Validacio.validaCadena("Introduzca argumento"));
                    peliActiva.setGenere(Validacio.validaCadena("Introduzca generoº"));
                    peliActiva.setClassificacio(Validacio.validaCadena("Introduzca clasificacion"));
                    System.out.println("Nuevos datos... " + peliActiva.toString());
                    break;

                case 9: //Esborrar PELICULA
                    Pelicules.llistarPelicules();
                    opcionElegida = Validacio.validaSencer("Elige pelicula a eliminar", Pelicules.quantitatPelicules());
                    Pelicules.esborraPelicula(opcionElegida);
                    System.out.println("Pelicula eliminada con exito!");

                    break;

                case 10: //Associar PELICULA a SESSIO 
                    Pelicules.llistarPelicules();
                    opcionElegida = Validacio.validaSencer("Elige una pelicula", Pelicules.quantitatPelicules());
                    peliActiva = Pelicules.retornaPelicula(opcionElegida);
                    System.out.println("Elige una sesion que quieres añadir ala pelicula" + peliActiva.toString());
                    Sessions.llistarSessions();
                    opcionElegida = Validacio.validaSencer("Elige una sesion", Sessions.quantitatSessions());
                    sesionActiva = Sessions.retornaSessio(opcionElegida);
                    System.out.println("Elegiste la sesion " + sesionActiva.toString() + " para la pelicula " + peliActiva.toString());
                    Thread.sleep(1000);
                    Pelicules.asociarPeliculaSesion(peliActiva, sesionActiva);
                    System.out.println("Asignados con exito");

                    break;

                case 11: //Comprar ENTRADA
                    Pelicules.llistarPelicules();
                    opcionElegida = Validacio.validaSencer("Selecciona una pelicula", Pelicules.quantitatPelicules());
                    peliculaPagament = Pelicules.retornaPelicula(opcionElegida);
                    peliculaPagament.llistarSessionsPeli();
                    opcionElegida = Validacio.validaSencer("Selecciona una sesion", Sessions.quantitatSessions());
                    sesionPagament = peliculaPagament.retornaSessioPeli(opcionElegida);
                    System.out.println("Selecciona un asiento");
                    sesionPagament.mapaSessio();
                    int fila = Validacio.validaSencer("Introduzca fila", sesionPagament.getSala().getFiles());
                    int col = Validacio.validaSencer("Introduzca columna", sesionPagament.getSala().getTamanyFila());
                    asientoPagament = sesionPagament.getSeients()[fila][col];
                    System.out.println(asientoPagament.toString());
                    compraEntradaPelicula();
                    System.out.println("La información es correcta? [S] [N]");
                    String aux = teclado.nextLine();
                    if (aux.equalsIgnoreCase("S")) {
                        compraEntradaPelicula();
                        System.out.println("Introduzca metodo de pago");
                        String metodo = teclado.nextLine();
                        System.out.println("Introduzca datos tarjeta");
                        String datosTarjeta = teclado.nextLine();
                        pagamentEntrada(sesionPagament.getPreu(), metodo, datosTarjeta);

                    } else if (aux.equalsIgnoreCase("N")) {
                        System.out.println("Saliendo...");
                        break;
                    } else {
                        System.out.println("Has introducido un valor incorrecto");
                    }

                    break;
                default:
            }

        } while (opcio
                != 0);

    } //*********************************************************
    //COMPRA INTERACTIVA D'UNA ENTRADA

    public static void compraEntradaPelicula() throws InterruptedException {

        System.out.println("Se va a comprar el asiento " + asientoPagament.toString() + " que se encuentra en ");
        Thread.sleep(1000);
        System.out.println("Sesion " + sesionPagament.toString() + " que a su vez tiene la siguiente pelicula ");
        Thread.sleep(1000);
        System.out.println("Pelicula " + peliculaPagament.toString());
        Thread.sleep(1000);
    }

    //*********************************************************
    //PAGAMENT D'UNA ENTRADA
    static boolean pagamentEntrada(BigDecimal preu, String metodoPago, String datosTarjeta) throws InterruptedException {
        System.out.println("Total a pagar " + preu + " €");
        System.out.println("Metodo de pago " + metodoPago);
        System.out.println("Datos de la tarjeta " + datosTarjeta);
        int i = 0;
        while (i < 5) {
            System.out.println("Procesando pago...");
            Thread.sleep(1000);
            i++;
        }
        System.out.println("Pago realizado con exito");
        asientoPagament.reservaSeient();
        sesionPagament.mapaSessio();
        return false;
    }

    //*********************************************************
    //VISUALITZA EL MENU PRINCIPAL
    public static int menu() {
        int opcio;
        Scanner s = new Scanner(System.in);

        do {
            System.out.println("MENU Aplicació CINE:");
            System.out.println("====================");
            System.out.println("1.  Crear SALA");
            System.out.println("2.  Modificar SALA");
            System.out.println("3.  Eliminar SALA");

            System.out.println("4.  Crear SESSIO");
            System.out.println("5.  Modificar SESSIO");
            System.out.println("6.  Eliminar SESSIO");

            System.out.println("7.  Crear PELICULA");
            System.out.println("8.  Modificar PELICULA");
            System.out.println("9.  Eliminar PELICULA");

            System.out.println("10. Associar PELICULA a SESSIO");
            System.out.println("11. Comprar ENTRADA");
            System.out.println("0. Eixir Aplicació CINE");

            String stropcio = s.next();
            opcio = Integer.parseInt(stropcio);
        } while (opcio < 0 || opcio > 11);

        return opcio;
    }

}
