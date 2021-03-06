package paasat.algorithms;

import paasat.Formula;
import paasat.utils.Printer;

/**
 * Implementace algoritmu hrubou silou pomoci iterativni metody
 * @author Bc. Vojtěch Svoboda <svobovo3@fit.cvut.cz>
 */
public class BruteForce implements IStrategy {

    private Formula myFormula;
    private int bestVaha = 0;
    private boolean[] bestOhodnoceni = null;
    private static boolean DEBUG = false;
    private int expandovano = 0;
    private int splnitelne = 0;

    public BruteForce(Formula myFormula) {
        this.myFormula = myFormula;
    }

    /**
     * Projde vsechny mozne ohodnoceni a zkousi je nastavovat do formule
     * - pricemz sledujeme splnitelnost a soucet vah
     */
    public void solve() {
        /* zjistime si kolik mame promennych */
        int celkemPolozek = this.myFormula.getPromennychCount();
        /* kazda polozka je jednicka, nebo nula */
        int celkemMoznychStavu = (int) Math.pow(2, celkemPolozek);
        if ( DEBUG ) System.out.println("Celkem moznych polozek je " + 
                                        + celkemPolozek + ", celkem moznych stavu je " +
                                        + celkemMoznychStavu);        
        /* prochazime vse, takze iterujeme bitovy vektor */
        int aktualniVaha = 0;
        expandovano = 0;
        splnitelne = 0;
        boolean[] ohodnoceni = null;
        boolean splnitelna = false;
        for (int i = 0; i < celkemMoznychStavu; i++) {
            /* zjistime si bitovy vektor */
            ohodnoceni = intToBooleanArray(i, celkemPolozek);
            // System.out.print("Prochazime ohodnoceni: "); Printer.printBooleanArray(ohodnoceni);
            expandovano++;
            /* mrkneme jake je ohodnoceni formule */
            myFormula.setOhodnoceni(ohodnoceni);
            splnitelna = (myFormula.getPocetSplnitelnychKlauzuli() == myFormula.getClausuleCount());

            if ( splnitelna ) {
                // System.out.println("Klauzule JE splnitelna.");
                splnitelne++;
                aktualniVaha = myFormula.getFormulaSum();
                // System.out.println("Soucet vah je " + aktualniVaha);
                if ( aktualniVaha > this.bestVaha ) {
                    // System.out.println("Nasli jsme lepsi soucet vah: " + aktualniVaha);
                    this.bestVaha = aktualniVaha;
                    this.bestOhodnoceni = ohodnoceni;
                }
            } else {
                // System.out.println("Klauzule NENI splnitelna.");
            }
        }
    }

    /**
     * Prevede int na binarni pole
     * @return boolean[] binarni vektor ohodnoceni
     */
    public boolean[] intToBooleanArray(int i, int delka) {
        String poleBitu = Integer.toBinaryString(i);
        if ( DEBUG ) System.out.println("Pole bitu je " + poleBitu);
        /* vytvorime pole ohodnoceni promennych */
        char[] ohodnoceni = poleBitu.toCharArray();
        boolean[] vystup = new boolean[delka];
        /* prevedeme pole */
        int doplnek = 0;
        for (int j = 0; j < ohodnoceni.length; j++) {
            doplnek = ohodnoceni.length - j - 1;
            char c = ohodnoceni[j];
            vystup[doplnek] = (c == '1') ? true : false;
        }
        if ( DEBUG ) System.out.print("Vystupem je: ");
        if ( DEBUG ) Printer.printBooleanArray(vystup);
        return vystup;
    }

    //== GETTERS AND SETTERS ===================================================

    public void setMyFormula(Formula myFormula) {
        this.myFormula = myFormula;
    }

    public boolean[] getBestOhodnoceni() {
        return this.bestOhodnoceni;
    }

    public int getBestWeight() {
        return this.bestVaha;
    }

    public int getExpandovano() {
        return expandovano;
    }

    public int getSplnitelne() {
        return splnitelne;
    }

    //== TESTY =================================================================

    /**
     * Testovaci metoda
     * @param args
     */
    public static void main(String[] args) {
        BruteForce bf = new BruteForce(null);
        BruteForce.DEBUG = true;
        int cislo = 5;
        boolean[] ohodnoceni = bf.intToBooleanArray(cislo, 3);
        System.out.print("Ohodnoceni: ");
        Printer.printBooleanArray(ohodnoceni);
    }

}
