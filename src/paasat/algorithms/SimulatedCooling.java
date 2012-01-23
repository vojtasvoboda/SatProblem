package paasat.algorithms;

import java.util.Random;
import paasat.Formula;
import paasat.utils.Printer;

/**
 * Algoritmus simulovaneho ochlazovani
 * @author Bc. Vojtěch Svoboda <svobovo3@fit.cvut.cz>
 */
public class SimulatedCooling implements IStrategy {

    private Formula myFormula;
    private int     bestVaha = 0;
    private int     bestPocet = 0;
    private boolean[] bestOhodnoceni = null;
    private boolean[] aktualniStav = null;
    private int     expandovano = 0;
    private int splnitelne = 0;
    private static boolean DEBUG = false;

    /* telpoty pro ochlazovani */
    private double  pocatecniTeplota;
    private double  minimalniTeplota;
    private double  aktualniTeplota;

    /* settings  - setujeme pres settery */
    private double  zchlazeniKoef; // 0,8 - 0,999
    private int     equilibrumKoef;

    /* konstruktor */
    public SimulatedCooling(Formula myFormula) {
        this.myFormula = myFormula;
    }

    /* vyres formuli */
    public void solve() {

        /* init */
        int aktualniVaha = 0;
        int aktualniPocet = 0;
        int innerCycle = 0;
        boolean[] novyStav = null;
        boolean splnitelna = false;
        int pocetPromennych = myFormula.getPromennychCount();
        int pocetKlauzuli = myFormula.getClausuleCount();
        expandovano = 0;
        splnitelne = 0;

        /* pocatecni stavy */
        aktualniStav = getInitialState();
        aktualniTeplota = (pocatecniTeplota * pocetKlauzuli) / Math.log(2);
        // System.out.print("Pocatecni stav: "); Printer.printBooleanArray(aktualniStav);

        /* dokud mame spravnou teplotu */
        while( !isFrozen(aktualniTeplota) ) {

            // System.out.println("Aktualni teplota je " + aktualniTeplota);
            innerCycle = 0;

            while ( equilibrum(innerCycle, pocetPromennych) ) {
                /* pocet stavu counter */
                expandovano++; innerCycle++;

                /* ziskame dalsi stav */
                novyStav = getNextState(aktualniStav);
                // System.out.print("Tepolota je " + aktualniTeplota + ", vybrany dalsi stav je ");
                // Printer.printBooleanArray(novyStav);

                /* mrkneme jake je ohodnoceni formule */
                myFormula.setOhodnoceni(novyStav);
                splnitelna = (myFormula.getPocetSplnitelnychKlauzuli() == myFormula.getClausuleCount());

                if ( splnitelna ) {
                    // System.out.println("Klauzule JE splnitelna.");
                    splnitelne++;
                    aktualniVaha = myFormula.getFormulaSum();
                    aktualniPocet = myFormula.getPocetSplnitelnychKlauzuli();
                    // System.out.println("Soucet vah je " + aktualniVaha + ", pocet je " + aktualniPocet);
                    if ( ( aktualniVaha > this.bestVaha ) &
                         ( aktualniPocet > this.bestPocet ) ) {
                        // System.out.println("Nasli jsme lepsi soucet vah: " + aktualniVaha);
                        this.bestVaha = aktualniVaha;
                        this.bestPocet = aktualniPocet;
                        this.bestOhodnoceni = novyStav;
                    }
                } else {
                    // System.out.println("Klauzule NENI splnitelna.");
                }
            }
            /* zchladime */
            aktualniTeplota = coolDown(aktualniTeplota);
        }
    }

    /**
     * Vrati stav, ktery se lisi v nahodnem bitu
     * @param aktualniStav
     * @return
     */
    private boolean[] getNextState(boolean[] aktualniStav) {

        // puvodni stav
        myFormula.setOhodnoceni(aktualniStav);
        int pocet1 = myFormula.getPocetSplnitelnychKlauzuli();
        int vaha1 = myFormula.getFormulaSum();
        boolean splnitelne1 = pocet1 >= myFormula.getClausuleCount();

        // ziskame novy vedlejsi stav
        boolean[] novyStav = getRandomState(aktualniStav);
        myFormula.setOhodnoceni(novyStav);
        int pocet2 = myFormula.getPocetSplnitelnychKlauzuli();
        int vaha2 = myFormula.getFormulaSum();
        boolean splnitelne2 = pocet2 >= myFormula.getClausuleCount();
        // System.out.print("Novy stav je "); Printer.printBooleanArray(novyStav);

        // pokud je novy stav lepsi, tak vratime lepsi
        if ( isBetter(pocet1, pocet2, vaha1, vaha2, splnitelne1, splnitelne2) ) {
            return novyStav;

        // pokud novy stav neni lepsi, zkusime prijmout trosku horsi reseni
        } else {
            int delta = pocet2 - pocet1;
            Random randomObj = new Random();
            double x = randomObj.nextDouble();
            // x < exp(-delta/T)?
            return ( x < Math.exp(-delta / this.zchlazeniKoef) ) ? novyStav : aktualniStav;
        }
    }

    /**
     * Porovna dva stavy a zjisti, ktery je lepsi
     * is 2 better then 1
     *
     * @param pocet1
     * @param pocet2
     * @param vaha1
     * @param vaha2
     * @return boolean
     */
    public boolean isBetter(int pocet1, int pocet2, int vaha1, int vaha2, boolean splnitelne1, boolean splnitelne2) {

        // varianta A1)
        /*
        int skore1 = vaha1;
        int skore2 = vaha2;
        return skore2 > skore1;
        */

        // varianta A2)
        /*
        int skore1 = pocet1;
        int skore2 = pocet2;
        return skore2 > skore1;
        */

        // varianta B)
        /*
        int skore1 = pocet1 + vaha1;
        int skore2 = pocet2 + vaha2;
        return skore2 > skore1;
        */

        // varianta C)
        if ( splnitelne1 ) pocet1 += 50;
        if ( splnitelne2 ) pocet2 += 50;
        if ( pocet2 > pocet1 ) return true;
        if ( pocet2 < pocet1 ) return false;
        // pokud se rovnaji, jedeme dale
        if ( vaha2 > vaha1 ) return true;
        return false;

        // varianta D)
        /*
        // pokud je jeden splnitelny a druhy ne, neni co resit
        if ( splnitelne1 & !splnitelne2 ) {
            return false;
        } else if ( !splnitelne1 & splnitelne2 ) {
            return true;

        // pokud jsou ale oba splnitelne, nebo oba nesplnitelne,
        // resime pocet splnitelnych klauzuli
        } else {
            if ( pocet2 > pocet1 ) return true;
        }
        return false;
        */

    }

    /**
     * Vrati stav, ktery se lisi v nahodnem bitu
     * @param aktualniStav
     * @return
     */
    private boolean[] getRandomState(boolean[] aktualniStav) {
        boolean[] novyStav = aktualniStav;
        // System.out.println("getNextState vstup:"); printState(novyStav);
        // zvolime nahodne index
        Random index = new Random();
        int random = index.nextInt(novyStav.length);
        // odeberu, nebo pridam polozku na nahodnem indexu
        novyStav[random] = (novyStav[random] == false) ? true : false;
        // System.out.println("getNextState výstup:"); printState(novyStav);
        return novyStav;
    }

    /**
     * Vratime pocatecni stav
     * @return
     */
    private boolean[] getInitialState() {
        boolean[] pole = new boolean[myFormula.getPromennychCount()];
        pole[0] = true;
        return pole;
    }

    /**
     * Urci equilibrum
     * @param innerCycle
     * @param kapacita
     * @return
     */
    private boolean equilibrum(int innerCycle, double kapacita) {
        return ( innerCycle < ( equilibrumKoef * kapacita ));
    }

    /**
     * Vrati, jestli jsme prekrocili minimalni teplotu
     * @param aktualniTeplota
     * @return boolean
     */
    private boolean isFrozen(double aktualniTeplota) {
        return aktualniTeplota < this.minimalniTeplota;
    }

    /**
     * Provede zchlazeni
     * @param temperature
     * @return
     */
    private double coolDown(double aktualniTeplota) {
        return (aktualniTeplota * this.zchlazeniKoef);
    }

    //== GETTERS AND SETTERS ===================================================

    public void setMyFormula(Formula myFormula) {
        this.myFormula = myFormula;
    }

    public void setEquilibrumKoef(int equilibrumKoef) {
        this.equilibrumKoef = equilibrumKoef;
    }

    public void setMinimalniTeplota(double minimalniTeplota) {
        this.minimalniTeplota = minimalniTeplota;
    }

    public void setPocatecniTeplota(double pocatecniTeplota) {
        this.pocatecniTeplota = pocatecniTeplota;
    }

    public void setZchlazeniKoef(double zchlazeniKoef) {
        this.zchlazeniKoef = zchlazeniKoef;
    }

    public boolean[] getBestOhodnoceni() {
        return bestOhodnoceni;
    }

    public int getBestWeight() {
        return bestVaha;
    }

    public int getExpandovano() {
        return expandovano;
    }

    public int getSplnitelne() {
        return this.splnitelne;
    }

    //== TESTY =================================================================

    /**
     * Testovaci metoda
     * @param args
     */
    public static void main(String[] args) {

    }

}
