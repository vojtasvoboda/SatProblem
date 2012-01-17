package paasat.algorithms;

import paasat.Formula;

/**
 * Implementace algoritmu hrubou silou pomoci iterativni metody
 * @author Bc. Vojtěch Svoboda <svobovo3@fit.cvut.cz>
 */
public class BruteForce implements IStrategy {

    public Formula myFormula;

    public BruteForce(Formula myFormula) {
        this.myFormula = myFormula;
    }

    public void solve() {

        // musime iterativne generovat novy stavy (ohodnoceni)
        // a nastavovat je do formule
        // this.myFormula.setOhodnoceni(ohodnoceni);

    }

    public void setMyFormula(Formula myFormula) {
        this.myFormula = myFormula;
    }

    public byte[] getBestSolution() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
