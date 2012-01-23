package paasat;

import paasat.utils.FileLoader;
import java.util.List;
import paasat.algorithms.*;
import paasat.utils.DirectoryLoader;

/**
 * Reseni ulohy SAT pro predmet PAA @ FIT, CVUT
 * @author Bc. Vojtěch Svoboda <svobovo3@fit.cvut.cz>
 */
public class Main {

    public static IStrategy itsStrategy = null;

    /* pocet opakovani celeho vypoctu */
    final static int ITERATION_NO = 1;
    /* pocet opakovani celeho vypoctu */
    final static int ITERATION_MAX = 20;
    /* pocatecni teplota */
    final static double INIT_TEMP = 1000; // 100
    /* minimalni teplota */
    final static double MIN_TEMP = 1;
    /* zchlazovaci koeficient 0,8 - 0,999 */
    final static double ZCHLAZOVACI_KOEF = 0.85;
    /* equilibrum koeficient */
    final static int EQUILIBRUM_KOEF = 100; // 100
    /* slozka s daty */
    final static String DATA_FOLDER = "data-75";
    /* maximalni pocet klauzuli */
    final static int MAX_CLAUSULA_COUNT = 80; // pro data (1000), data-50 (80)

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // nacteme vstup
        FileLoader fl = new FileLoader(DATA_FOLDER);
        fl.setMaxLinesCount(MAX_CLAUSULA_COUNT);
        DirectoryLoader dl = new DirectoryLoader();
        String[] files = dl.getDirectoryFiles(DATA_FOLDER);

        // init
        Formula formula = null;
        String file = null;
        List vstup = null;
        byte[] vahy = null;
        int bestWeight = 0;
        int expandovanoStavu = 0;
        int splnitelne = 0;
        int celkemSplnitelnych = 0;
        int pocetSplnitelnych = 0;
        int celkemProchazeno = 0;
        int soucetVah = 0;

        /* jake konfigurace chci prochazet */
        int[] konfigurace = {1,2,3,4,5,10,20,50,100,200,500,1000,2000,5000,10000,50000,100000,1000000};
        // int[] konfigurace = {1};

        for (int x = 0; x < konfigurace.length; x++) {
            int config = konfigurace[x];

            /* zapnu mereni casu */
            long startTime = System.currentTimeMillis();

            /* restart */
            pocetSplnitelnych = 0;
            soucetVah = 0;
            celkemProchazeno = 0;
            celkemSplnitelnych = 0;

            // projdeme vsechny soubory (formule)
            for (int i = 0; i < files.length & i < ITERATION_MAX; i++) {
                file = files[i];
                // System.out.println("Nacitame soubor " + file);
                vstup = fl.loadFile(file);
                vahy = fl.loadWeights(file + ".sol");
                formula = new Formula(vstup, vahy, fl.getVariablesCount());

                // vytvorime strategii reseni a spustime
                // BruteForce sim = new BruteForce(formula);
                SimulatedCooling sim = new SimulatedCooling(formula);
                // sim.setPocatecniTeplota(INIT_TEMP);
                sim.setPocatecniTeplota(config);
                sim.setMinimalniTeplota(MIN_TEMP);
                sim.setZchlazeniKoef(ZCHLAZOVACI_KOEF);
                sim.setEquilibrumKoef(EQUILIBRUM_KOEF);
                sim.solve();

                // zjistime nejlepsi reseni
                bestWeight = sim.getBestWeight();
                expandovanoStavu = sim.getExpandovano();
                splnitelne = sim.getSplnitelne();
                celkemSplnitelnych += splnitelne;
                celkemProchazeno++;
                soucetVah += bestWeight;
                if ( splnitelne > 0 ) pocetSplnitelnych++;

                // System.out.println("Soubor " + file + ", bestWeight " + bestWeight + ", expandovano " + expandovanoStavu + ", splnitelne " + splnitelne);

            }

            /* konec mereni casu */
            long endTime = System.currentTimeMillis();
            startTime = endTime - startTime;

            System.out.println("# splnitelnych formuli je " + pocetSplnitelnych + " z " +
                    celkemProchazeno + ", soucet vah " + soucetVah + ", cas " +
                    startTime + "ms, prumerne " + (double) celkemSplnitelnych / 20 + " splnitel, konfigurace " + config);
        }
    }
}
