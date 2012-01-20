package paasat;

import utils.FileLoader;
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
        String file = "small.txt";
        FileLoader fl = new FileLoader();
        List vstup = fl.loadFile(file);
        byte[] vahy = fl.loadWeights(file + ".sol");

        // vytvorime formuli
        Formula formula = new Formula(vstup, vahy, fl.getVariablesCount());

        // vytvorime strategii reseni a spustime
        itsStrategy = new BruteForce(formula);
        itsStrategy.solve();

    }

}
