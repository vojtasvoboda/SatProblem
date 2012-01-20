package paasat;

import paasat.utils.FileLoader;
import java.util.List;
import paasat.algorithms.*;

/**
 * Reseni ulohy SAT pro predmet PAA @ FIT, CVUT
 * @author Bc. Vojtěch Svoboda <svobovo3@fit.cvut.cz>
 */
public class Main {

    public static IStrategy itsStrategy = null;

    /* pocet opakovani celeho vypoctu */
    final static int ITERATION_NO = 1;
    /* pocatecni teplota */
    final static double INIT_TEMP = 500;
    /* minimalni teplota */
    final static double MIN_TEMP = 1;
    /* zchlazovaci koeficient 0,8 - 0,999 */
    final static double ZCHLAZOVACI_KOEF = 0.85;
    /* equilibrum koeficient */
    final static int EQUILIBRUM_KOEF = 100;

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
        // itsStrategy = new BruteForce(formula);
        SimulatedCooling sim = new SimulatedCooling(formula);
        sim.setPocatecniTeplota(INIT_TEMP);
        sim.setMinimalniTeplota(MIN_TEMP);
        sim.setZchlazeniKoef(ZCHLAZOVACI_KOEF);
        sim.setEquilibrumKoef(EQUILIBRUM_KOEF);
        sim.solve();

    }

}
