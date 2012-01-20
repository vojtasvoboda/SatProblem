package paasat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import paasat.utils.Printer;

/**
 * Trida, ktera implementuje jednu matematickou formuli
 * @author Bc. Vojtěch Svoboda <svobovo3@fit.cvut.cz>
 */
public class Formula {

    /* pocet promennych */
    private int promennychCount = 0;

    /* ArrayList skladajici se z Arrays (klauzuli) */
    private List klauzule = null;

    /* ohodnoceni promennych */
    private boolean[] ohodnoceni = null;

    /* vahy promennych */
    private byte[] vahy = null;

    /**
     * Konstruktor
     * @param vstup
     */
    public Formula(List vstup, byte[] vahy, int pocetPromennych) {
        this.klauzule = vstup;
        this.ohodnoceni = new boolean[pocetPromennych];
        this.vahy = vahy;
        this.promennychCount = pocetPromennych;
    }

    /**
     * Vrati, jestli je formule resitelna a to tak, ze projde vsechny klauzule
     * Pokud je jakakoliv klauzule nulova, vrati false
     * @return boolean
     */
    public boolean isFormulaSatisfable() {
        // System.out.println("Formula: isFormulaSatisfable(): pocet " + klauzule.size());
        Iterator it = klauzule.iterator();
        while( it.hasNext() ) {
            if ( !isClauseSatisfable((byte[]) it.next())) return false;
        }
        return true;
    }

    /**
     * Vrati pocet splnitelnych klauzuli
     * @return
     */
    public int getPocetSplnitelnychKlauzuli() {
        int pocetSplnitelnych = 0;
        Iterator it = klauzule.iterator();
        while( it.hasNext() ) {
            if ( isClauseSatisfable((byte[]) it.next()))
                pocetSplnitelnych++;
        }
        return pocetSplnitelnych;
    }

    /**
     * Zjisti, jestli je klauzule splnitelna (hledame svedka toho, ze neni splnitelna)
     * - tato fnce by mela byt spis ve tride Clause, ale vzhledem k rychlosti je to zde
     *
     * Vstup:   1     2   -3    -4
     * Znamena: x1 + x2 + x3' + x4'
     *
     * @param clause
     * @param ohodnoceni
     * @return boolean
     */
    private boolean isClauseSatisfable(byte[] clause) {
        byte item;
        // System.out.print("Testuji klauzuli: "); Printer.printByteArray(clause);
        // projdeme vsechny prvky klauzule
        for (int i = 0; i < clause.length; i++) {
            item = clause[i];
            // pokud je promenna kladna a ohodnocena jako jednicka
            if ( (item > 0) && (this.ohodnoceni[item - 1]) ) {
                return true;

            // pokud je zaporny
            } else if ( item < 0 ){
                // znegujeme
                item = (byte) (item - (2 * item));
                // a ohodnocena jako nula
                if ( !this.ohodnoceni[item - 1] ) return true;
            }
        }
        // nenasli jsme svedka splnitelnosti
        return false;
    }

    /**
     * Vrati soucet vah promenych, ktere jsou nastavene na jednicku
     * @return
     */
    public int getFormulaSum() {
        int suma = 0;
        for (int i = 0; i < ohodnoceni.length; i++) {
            if ( ohodnoceni[i] ) suma += vahy[i];
        }
        return suma;
    }

    //== GETTERS AND SETTERS ===================================================

    public void setOhodnoceni(boolean[] ohodnoceni) {
        this.ohodnoceni = ohodnoceni;
    }

    public boolean[] getOhodnoceni() {
        return ohodnoceni;
    }

    public byte[] getVahy() {
        return vahy;
    }

    public void setVahy(byte[] weights) {
        this.vahy = weights;
    }

    public int getPromennychCount() {
        return promennychCount;
    }

    public void setPromennychCount(int promennychCount) {
        this.promennychCount = promennychCount;
    }

    public int getClausuleCount() {
        return this.klauzule.size();
    }

    public List getKlauzule() {
        return klauzule;
    }

    //== TESTY =================================================================

    /**
     * Testovaci metoda
     * @param args
     */
    public static void main(String[] args) {
        Formula f = new Formula(new ArrayList(), new byte[4], 4);

        byte[] neco = {1,2,3,4};
        boolean[] ohodnoceni = {false,false,false,false};
        f.setOhodnoceni(ohodnoceni);
        boolean clauseSatisfable = f.isClauseSatisfable(neco);
        System.out.println("Ma byt false je " + clauseSatisfable);

        byte[] neco2 = {1,-2,3,4};
        boolean[] ohodnoceni2 = {false,true,true,false};
        f.setOhodnoceni(ohodnoceni2);
        clauseSatisfable = f.isClauseSatisfable(neco2);
        System.out.println("Ma byt true je " + clauseSatisfable);

        byte[] neco3 = {1,-2,3,4};
        boolean[] ohodnoceni3 = {false,true,false,false};
        f.setOhodnoceni(ohodnoceni3);
        clauseSatisfable = f.isClauseSatisfable(neco3);
        System.out.println("Ma byt false je " + clauseSatisfable);

        byte[] neco4 = {1,-2,3,4};
        boolean[] ohodnoceni4 = {false,false,false,false};
        f.setOhodnoceni(ohodnoceni4);
        clauseSatisfable = f.isClauseSatisfable(neco4);
        System.out.println("Ma byt true je " + clauseSatisfable);
    }

}
