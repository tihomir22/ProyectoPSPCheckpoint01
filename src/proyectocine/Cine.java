package proyectocine;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Scanner;

public class Cine {

    public static void main(String[] args) {
        //...
        // IMPLEMENTAR CODI ACÍ
        //...

        int opcio = 999, opcionElegida = 999, numSala, numFilas, numAsientosXFila;
        boolean es3d;
        Scanner teclado = new Scanner(System.in);
        Sales sales = new Sales();
        Sessions sesiones = new Sessions();

        Sala salaActiva = new Sala();
        Sessio sesionActiva = new Sessio();
        do {
            opcio = menu();

            switch (opcio) {

                case 1:
                    Sala sal = new Sala(14, 10, 10);
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
                        if (opcionElegida < Sales.quantitatSales()) {
                            salaActiva = Sales.retornaSala(opcionElegida);
                            System.out.println(salaActiva);
                            Sessio sesion = new Sessio("sesion actual", Calendar.getInstance(), salaActiva, new BigDecimal(12));
                            sesion.setSeients(new Seient[salaActiva.getFiles()][salaActiva.getTamanyFila()]);
                            Sessions.afegirSessio(sesion);

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
                            int dia = teclado.nextInt();
                            int mes = teclado.nextInt();
                            int año = teclado.nextInt();
                            int hora = teclado.nextInt();
                            int minutos = teclado.nextInt();
                            System.out.println("Elige la nueva sala");
                            Sales.llistarSales();
                            salaActiva = Sales.retornaSala(teclado.nextInt());
                            if (salaActiva != null) {
                                System.out.println("Introduce precio");
                                BigDecimal dec = new BigDecimal(teclado.nextInt());
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
                    //...
                    // IMPLEMENTAR CODI ACÍ
                    //...
                    break;

                case 8: //Modifica PELICULA
                    //...
                    // IMPLEMENTAR CODI ACÍ
                    //...
                    break;

                case 9: //Esborrar PELICULA
                    //...
                    // IMPLEMENTAR CODI ACÍ
                    //...
                    break;

                case 10: //Associar PELICULA a SESSIO 
                    //...
                    // IMPLEMENTAR CODI ACÍ
                    //...
                    break;

                case 11: //Comprar ENTRADA
                    //...
                    // IMPLEMENTAR CODI ACÍ
                    //...			
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

        //...
        // IMPLEMENTAR CODI ACÍ
        //...
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
