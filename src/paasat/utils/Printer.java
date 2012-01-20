package paasat.utils;

import java.util.List;

/**
 * Umi vypsat ruzne pole na konzoli
 * @author Bc. Vojtěch Svoboda <svobovo3@fit.cvut.cz>
 */
public class Printer {

    /**
     * Vypise pole bytu na konzoli
     * @param vstup
     */
    public static void printByteArray(byte[] vstup) {
        for (int i = 0; i < vstup.length; i++) {
            byte b = vstup[i];
            System.out.print(b + " ");
        }
        System.out.println("");
    }

    /**
     * Vypise pole booleanu
     * @param vstup
     */
    public static void printBooleanArray(boolean[] vstup) {
        for (int i = 0; i < vstup.length; i++) {
            boolean b = vstup[i];
            if (b) System.out.print("1");
            else System.out.print("0");
        }
        System.out.println("");
    }

    /**
     * Vytiskne list na konzoli pro testovani
     * @param list
     */
    public static void printArrayList(List vstup) {
        for (int i = 0; i < vstup.size(); i++) {
            byte[] radka = (byte[]) vstup.get(i);
            for (int j = 0; j < radka.length; j++) {
                byte b = radka[j];
                System.out.print(b + " ");
            }
            System.out.println("");
        }
    }

}
