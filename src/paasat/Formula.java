package paasat;

import java.util.ArrayList;
import java.util.List;

/**
 * Trida, ktera implementuje jednu matematickou formuli
 * @author Bc. Vojtěch Svoboda <svobovo3@fit.cvut.cz>
 */
public class Formula {

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
    public Formula(List vstup) {
        this.klauzule = vstup;
        this.ohodnoceni = new boolean[vstup.size()];
        this.vahy = new byte[vstup.size()];
    }

    /**
     * Vrati, jestli je formule resitelna a to tak, ze projde vsechny klauzule
     * Pokud je jakakoliv klauzule nulova, vrati false
     * @return boolean
     */
    public boolean isFormulaSatisfable() {
        for (int i = 0; i < klauzule.size(); i++) {
            if ( !isClauseSatisfable((byte[]) klauzule.get(i)))
                return false;
        }
        return true;
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
        byte bit;
        // projdeme vsechny prvky klauzule
        for (int i = 0; i < clause.length; i++) {
            bit = clause[i];
            // pokud je promenna kladna a ohodnocena jako jednicka
            if ( (bit > 0) && (this.ohodnoceni[bit]) ) {
                return true;

            // pokud je zaporny
            } else if ( bit < 0 ){
                bit = (byte) (bit - (2 * bit));
                if ( !this.ohodnoceni[bit] ) return true;
            }
        }
        return false;
    }

    //== GETTERS AND SETTERS ===================================================

    public void setOhodnoceni(boolean[] ohodnoceni) {
        this.ohodnoceni = ohodnoceni;
    }

    public byte[] getVahy() {
        return vahy;
    }

    public void setVahy(byte[] weights) {
        this.vahy = weights;
    }

    //== TESTY =================================================================

    /**
     * Testovaci metoda
     * @param args
     */
    public static void main(String[] args) {
        Formula f = new Formula(new ArrayList());

        byte[] neco = {1,2,3,4};
        boolean[] ohodnoceni = {false,false,false,false,false};
        f.setOhodnoceni(ohodnoceni);
        boolean clauseSatisfable = f.isClauseSatisfable(neco);
        System.out.println("Ma byt false je " + clauseSatisfable);

        byte[] neco2 = {1,-2,3,4};
        boolean[] ohodnoceni2 = {false,true,true,false,false};
        f.setOhodnoceni(ohodnoceni2);
        clauseSatisfable = f.isClauseSatisfable(neco2);
        System.out.println("Ma byt true je " + clauseSatisfable);

        byte[] neco3 = {1,-2,3,4};
        boolean[] ohodnoceni3 = {false,false,true,false,false};
        f.setOhodnoceni(ohodnoceni3);
        clauseSatisfable = f.isClauseSatisfable(neco3);
        System.out.println("Ma byt false je " + clauseSatisfable);

        byte[] neco4 = {1,-2,3,4};
        boolean[] ohodnoceni4 = {false,false,false,false,false};
        f.setOhodnoceni(ohodnoceni4);
        clauseSatisfable = f.isClauseSatisfable(neco4);
        System.out.println("Ma byt true je " + clauseSatisfable);
    }

}
