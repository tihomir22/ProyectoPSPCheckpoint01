package proyectocine;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Scanner;

public class Cine {

    public static void main(String[] args) {
        //...
        // IMPLEMENTAR CODI ACÍ
        //...
        Sales sales = new Sales();
        Sessions sesiones = new Sessions();
        int opcio = 999, opcionElegida = 999, numSala, numFilas, numAsientosXFila;
        boolean es3d;
        Scanner teclado = new Scanner(System.in);
        Sala salaActiva = new Sala();

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
                    System.out.println("Elige una sala para la sesion , introduce su indice");
                    Sales.llistarSales();
                    opcionElegida = teclado.nextInt();
                    if (opcionElegida < Sales.quantitatSales()) {
                        salaActiva = Sales.retornaSala(opcionElegida);
                        System.out.println(salaActiva);
                        Sessio sesion = new Sessio("sesion actual", Calendar.getInstance(),salaActiva,new BigDecimal(12));
                        System.out.println(sesion.toString());
                    }
                    

                    break;

                case 5: //Modifica SESSIO
                    //...
                    // IMPLEMENTAR CODI ACÍ
                    //...
                    break;

                case 6: //Esborrar SESSSIO
                    //...
                    // IMPLEMENTAR CODI ACÍ
                    //...
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
