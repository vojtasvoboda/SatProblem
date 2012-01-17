package paasat;

import java.util.List;
import paasat.algorithms.*;

/**
 * Reseni ulohy SAT pro predmet PAA @ FIT, CVUT
 * @author Bc. Vojtěch Svoboda <svobovo3@fit.cvut.cz>
 */
public class Main {

    public static IStrategy itsStrategy = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // nacteme vstup
        FileLoader fl = new FileLoader("uf20-01.txt");
        List vstup = fl.loadFile();

        // vytvorime formuli
        Formula formula = new Formula(vstup);

        // vytvorime strategii reseni
        itsStrategy = new BruteForce(formula);

    }

}
