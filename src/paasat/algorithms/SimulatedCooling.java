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
        int pocetPromennych = myFormula.getPromennychCount();
        int pocetKlauzuli = myFormula.getClausuleCount();

        /* pocatecni stavy */
        aktualniStav = getInitialState();
        aktualniTeplota = (pocatecniTeplota * pocetKlauzuli) / Math.log(2);
        System.out.print("Pocatecni stav: ");
        Printer.printBooleanArray(aktualniStav);

        /* dokud mame spravnou teplotu */
        while( !isFrozen(aktualniTeplota) ) {

            System.out.println("Aktualni teplota je " + aktualniTeplota);
            innerCycle = 0;

            while ( equilibrum(innerCycle, pocetPromennych) ) {
                /* pocet stavu counter */
                expandovano++; innerCycle++;

                /* ziskame dalsi stav */
                novyStav = getNextState(aktualniStav);
                System.out.print("Tepolota je " + aktualniTeplota + ", vybrany dalsi stav je ");
                Printer.printBooleanArray(novyStav);

                /* mrkneme jake je ohodnoceni formule */
                myFormula.setOhodnoceni(novyStav);
                if ( myFormula.isFormulaSatisfable() ) {
                    System.out.println("Klauzule JE splnitelna.");
                    aktualniVaha = myFormula.getFormulaSum();
                    aktualniPocet = myFormula.getPocetSplnitelnychKlauzuli();
                    System.out.println("Soucet vah je " + aktualniVaha + ", pocet je " + aktualniPocet);
                    if ( ( aktualniVaha > this.bestVaha ) &
                         ( aktualniPocet > this.bestPocet ) ) {
                        System.out.println("Nasli jsme lepsi soucet vah: " + aktualniVaha);
                        this.bestVaha = aktualniVaha;
                        this.bestPocet = aktualniPocet;
                        this.bestOhodnoceni = novyStav;
                    }
                } else {
                    System.out.println("Klauzule NENI splnitelna.");
                }
            }
            /* zchladime */
            aktualniTeplota = coolDown(aktualniTeplota);
        }
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
     * Vrati stav, ktery se lisi v nahodnem bitu
     * @param aktualniStav
     * @return
     */
    private boolean[] getNextState(boolean[] aktualniStav) {

        // puvodni stav
        myFormula.setOhodnoceni(aktualniStav);
        int pocet1 = myFormula.getPocetSplnitelnychKlauzuli();
        int vaha1 = myFormula.getFormulaSum();

        // ziskame novy vedlejsi stav
        boolean[] novyStav = getRandomState(aktualniStav);
        myFormula.setOhodnoceni(novyStav);
        int pocet2 = myFormula.getPocetSplnitelnychKlauzuli();
        int vaha2 = myFormula.getFormulaSum();
        System.out.print("Novy stav je ");
        Printer.printBooleanArray(novyStav);
        
        // pokud je novy stav lepsi, tak vratime lepsi
        if ( isBetter(pocet1, pocet2, vaha1, vaha2) ) {
            return novyStav;

        // pokud novy stav neni lepsi
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
     * @param pocet1
     * @param pocet2
     * @param vaha1
     * @param vaha2
     * @return boolean
     */
    public boolean isBetter(int pocet1, int pocet2, int vaha1, int vaha2) {
        if ( pocet2 > pocet1 ) return true;
        if ( pocet2 < pocet1 ) return false;
        // pokud se rovnaji, jedeme dale
        if ( vaha2 > vaha1 ) return true;
        return false;
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

    /**
     * Zjistime si, kde je prvni jednicka v poli
     * @param poleBitu
     * @return
     */
    private int getLastOne(int[] poleBitu) {
        for (int i = poleBitu.length - 1; i > -1 ; i--) {
            if ( poleBitu[i] == 1 ) return i;
        }
        return -1;
    }

    //== GETTERS AND SETTERS ===================================================

    public void setMyFormula(Formula myFormula) {
        this.myFormula = myFormula;
    }

    public boolean[] getBestSolution() {
        return this.bestOhodnoceni;
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

    //== TESTY =================================================================

    /**
     * Testovaci metoda
     * @param args
     */
    public static void main(String[] args) {

    }

}
