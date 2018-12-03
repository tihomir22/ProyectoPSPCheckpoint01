package proyectocine;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Scanner;

public class Cine {

    public static void main(String[] args) throws InterruptedException {
        //...
        // IMPLEMENTAR CODI ACÍ
        //...

        int opcio = 999, opcionElegida = 999, numSala, numFilas, numAsientosXFila, dia, mes, año, hora, minutos, duracionPelicula;
        String nombreSession, nombrePelicula, nacionalidad, director, interprete, arguments, genere, clasificacio;
        boolean es3d;
        BigDecimal dec;
        Scanner teclado = new Scanner(System.in);
        Sales sales = new Sales();
        Sessions sesiones = new Sessions();
        Pelicules pelicules = new Pelicules();

        Pelicula peliActiva = new Pelicula();
        Sala salaActiva = new Sala();
        Sessio sesionActiva = new Sessio();

        Pelicula peliculaPagament;
        Sessio sesionPagament;
        Seient asientoPagament;
        do {
            opcio = menu();

            switch (opcio) {

                case 1:
                    System.out.println("Introduzca numero de sala");
                    numSala = teclado.nextInt();
                    System.out.println("Es 3D ? true/false");
                    es3d = teclado.nextBoolean();
                    System.out.println("Introduce numero de filas");
                    numFilas = teclado.nextInt();
                    System.out.println("Introduzca tamaño de fila");
                    numAsientosXFila = teclado.nextInt();
                    Sala sal = new Sala(numSala, es3d, numFilas, numAsientosXFila);
                    System.out.println(sal.toString());
                    Sales.afegirSala(sal);

                    break;

                case 2:
                    System.out.println("Elige una sala a modificar , introduce su indice");
                    Sales.llistarSales();
                    opcionElegida = teclado.nextInt();
                    if (opcionElegida < Sales.quantitatSales()) {
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

                    } else {
                        System.out.println("Valor introducido erroneo");
                    }
                    break;

                case 3: //Esborrar SALA
                    System.out.println("Elige una sala a borrar , introduce su indice");
                    Sales.llistarSales();
                    opcionElegida = teclado.nextInt();
                    if (opcionElegida < Sales.quantitatSales()) {
                        Sales.esborraSala(opcionElegida);
                        System.out.println("Eliminado con exito");
                    } else {
                        System.out.println("Valor introducido erroneo");
                    }
                    break;

                case 4:	//Crear SESSIO
                    if (Sales.quantitatSales() > 0) {
                        System.out.println("Elige una sala para la sesion , introduce su indice");
                        Sales.llistarSales();
                        opcionElegida = teclado.nextInt();
                        if (opcionElegida < Sales.quantitatSales() && opcionElegida >= 0) {
                            salaActiva = Sales.retornaSala(opcionElegida);
                            System.out.println("sala seleccionada" + salaActiva);
                            System.out.println("Introduzca nombre de la sesion");
                            teclado.nextLine();
                            nombreSession = teclado.nextLine();
                            System.out.println("Introduzca la fecha en el siguiente orden : dia mes año hora minutos (todo son integer)");
                            dia = teclado.nextInt();
                            mes = teclado.nextInt();
                            año = teclado.nextInt();
                            hora = teclado.nextInt();
                            minutos = teclado.nextInt();
                            System.out.println("Introduzca precio");
                            dec = teclado.nextBigDecimal();
                            Sessio sesion = new Sessio(nombreSession, dia, mes, año, hora, minutos, salaActiva, dec);
                            Seient[][] asiento = new Seient[salaActiva.getFiles()][salaActiva.getTamanyFila()];
                            sesion.setSeients(asiento);
                            Sessions.afegirSessio(sesion);
                            sesion.setMapaSessio();
                            sesion.mapaSessio();

                        }
                    } else {
                        System.out.println("Debes dar de alta alguna sala primero..");
                    }
                    break;

                case 5: //Modifica SESSIO
                    System.out.println("Elija una session a modificar");
                    Sessions.llistarSessions();
                    opcionElegida = teclado.nextInt();
                    if (opcionElegida < Sessions.quantitatSessions() && opcionElegida >= 0) {
                        sesionActiva = Sessions.retornaSessio(opcionElegida);
                        if (sesionActiva != null) {
                            System.out.println("Introduce los nuevos datos de la sesion nombre sesion - fecha DD/MM/AA/HH/MM  - sala  - precio");
                            teclado.nextLine();
                            String nombreSesion = teclado.nextLine();
                            dia = teclado.nextInt();
                            mes = teclado.nextInt();
                            año = teclado.nextInt();
                            hora = teclado.nextInt();
                            minutos = teclado.nextInt();
                            System.out.println("Elige la nueva sala");
                            Sales.llistarSales();
                            salaActiva = Sales.retornaSala(teclado.nextInt());
                            if (salaActiva != null) {
                                System.out.println("Introduce precio");
                                dec = new BigDecimal(teclado.nextInt());
                                sesionActiva = new Sessio(nombreSesion, dia, mes, año, hora, minutos, salaActiva, dec);
                                sesionActiva.setSeients(new Seient[salaActiva.getFiles()][salaActiva.getTamanyFila()]);
                                System.out.println("Datos de la nueva sesion" + sesionActiva);
                            } else {
                                System.out.println("Introduciste una sala incorrecta");
                            }
                        } else {
                            System.out.println("No se ha encontrado dicha session");
                        }
                    } else {
                        System.out.println("Has introducido un numero incorrecto");
                    }
                    break;

                case 6: //Esborrar SESSSIO
                    System.out.println("Elija una session a eliminar");
                    Sessions.llistarSessions();
                    opcionElegida = teclado.nextInt();
                    if (opcionElegida < Sessions.quantitatSessions() && opcionElegida >= 0) {
                        Sessions.esborraSessio(opcionElegida);
                    } else {
                        System.out.println("Introduciste un dato incorrecto");
                    }
                    break;

                case 7: //Crear PELICULA
                    System.out.println("Introduzca nombre de pelicula");
                    teclado.nextLine();
                    nombrePelicula = teclado.nextLine();
                    System.out.println("Introduzca nacionalidad");
                    nacionalidad = teclado.nextLine();
                    System.out.println("Introduzca duracion pelicula");
                    duracionPelicula = teclado.nextInt();
                    System.out.println("Introduzca nombre del director");
                    teclado.nextLine();
                    director = teclado.nextLine();
                    System.out.println("Introduzca nombre de los interpretes");
                    interprete = teclado.nextLine();
                    System.out.println("Introduzca argumento");
                    arguments = teclado.nextLine();
                    System.out.println("Introduzca genero");
                    genere = teclado.nextLine();
                    System.out.println("Introduzca clasificación");
                    clasificacio = teclado.nextLine();
                    Pelicula pelicula = new Pelicula(nombrePelicula, nacionalidad, duracionPelicula, director, interprete, arguments, genere, clasificacio);
                    Pelicules.afegirPelicula(pelicula);
                    break;

                case 8: //Modifica PELICULA
                    System.out.println("Elige pelicula a modificar");
                    Pelicules.llistarPelicules();
                    opcionElegida = teclado.nextInt();
                    if (opcionElegida < Pelicules.quantitatPelicules() && opcionElegida >= 0) {
                        peliActiva = Pelicules.retornaPelicula(opcionElegida);
                        System.out.println("Seleccionada " + peliActiva.toString());
                        teclado.nextLine();
                        System.out.println("Introduzca nombre de pelicula");
                        peliActiva.setNomPeli(teclado.nextLine());
                        System.out.println("Introduzca nacionalidad");
                        peliActiva.setNacionalitat(teclado.nextLine());
                        System.out.println("Introduzca duracion pelicula");
                        peliActiva.setDuracio(teclado.nextInt());
                        teclado.nextLine();
                        System.out.println("Introduzca nombre del director");
                        peliActiva.setDirector(teclado.nextLine());
                        System.out.println("Introduzca nombre de los interpretes");
                        peliActiva.setInterprets(teclado.nextLine());
                        System.out.println("Introduzca argumento");
                        peliActiva.setArgument(teclado.nextLine());
                        System.out.println("Introduzca genero");
                        peliActiva.setGenere(teclado.nextLine());
                        System.out.println("Introduzca clasificación");
                        peliActiva.setClassificacio(teclado.nextLine());
                        System.out.println("Nuevos datos... " + peliActiva.toString());
                    } else {
                        System.out.println("Has elegido una pelicula incorrecta");
                    }
                    break;

                case 9: //Esborrar PELICULA
                    System.out.println("Elige pelicula a eliminar");
                    Pelicules.llistarPelicules();
                    opcionElegida = teclado.nextInt();
                    if (opcionElegida < Pelicules.quantitatPelicules() && opcionElegida >= 0) {
                        Pelicules.esborraPelicula(opcionElegida);
                        System.out.println("Pelicula eliminada con exito!");
                    }
                    break;

                case 10: //Associar PELICULA a SESSIO 
                    System.out.println("Elige una pelicula");
                    Pelicules.llistarPelicules();
                    opcionElegida = teclado.nextInt();
                    if (opcionElegida < Pelicules.quantitatPelicules() && opcionElegida >= 0) {
                        peliActiva = Pelicules.retornaPelicula(opcionElegida);
                        System.out.println("Elige una sesion que quieres añadir ala pelicula" + peliActiva.toString());
                        Sessions.llistarSessions();
                        opcionElegida = teclado.nextInt();
                        if (opcionElegida < Sessions.quantitatSessions() && opcionElegida >= 0) {
                            sesionActiva = Sessions.retornaSessio(opcionElegida);
                            System.out.println("Elegiste la sesion " + sesionActiva.toString() + " para la pelicula " + peliActiva.toString());
                            Thread.sleep(1000);
                            Pelicules.asociarPeliculaSesion(peliActiva, sesionActiva);
                            System.out.println("Asignados con exito");
                        }
                    } else {
                        System.out.println("Indice incorrecto");
                    }

                    break;

                case 11: //Comprar ENTRADA
                    System.out.println("Selecciona una pelicula");
                    Pelicules.llistarPelicules();
                    opcionElegida = teclado.nextInt();
                    if (opcionElegida < Pelicules.quantitatPelicules() && opcionElegida >= 0) {
                        peliculaPagament = Pelicules.retornaPelicula(opcionElegida);
                        System.out.println("Selecciona una sesion");
                        peliculaPagament.llistarSessionsPeli();
                        opcionElegida = teclado.nextInt();
                        if (opcionElegida < peliculaPagament.getSessionsPeli().size() && opcionElegida >= 0) {
                            sesionPagament = peliculaPagament.retornaSessioPeli(opcionElegida);
                            System.out.println("Selecciona un asiento");
                            sesionPagament.mapaSessio();
                            System.out.println("Introduzca fila");
                            int fila = teclado.nextInt();
                            System.out.println("Introduzca columna");
                            int col = teclado.nextInt();
                            asientoPagament = sesionPagament.getSeients()[fila][col];
                            System.out.println("Hemos llegado...");
                        }
                    }
                    break;

                default:
                //...
                // IMPLEMENTAR CODI ACÍ
                //...

            }
        } while (opcio != 0);

    }

    //*********************************************************
    //COMPRA INTERACTIVA D'UNA ENTRADA
    public static void compraEntradaPelicula() {

    }

    //*********************************************************
    //PAGAMENT D'UNA ENTRADA
    static boolean pagamentEntrada(BigDecimal preu) {
        //...
        // IMPLEMENTAR CODI ACÍ
        //...
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
