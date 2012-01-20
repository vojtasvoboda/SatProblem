package paasat.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Soubor pro nacteni vstupnich dat
 * @author Bc. Vojtěch Svoboda <svobovo3@fit.cvut.cz>
 */
public class FileLoader {

    /* cesta k datovym souborum */
    public static String DATAPATH = System.getProperty("user.dir") + "\\data\\";

    /* pocet promennych */
    public int variablesCount = 0;

    /* pocet radku */
    public int linesCount = 0;

    /**
     * Nacte vstupni soubor do pole listu
     * Zkopirovano z http://www.roseindia.net/java/beginners/java-read-file-line-by-line.shtml
     * @return List pole klauzuli
     */
    public List loadFile(String fileName) {
        String fullPath = DATAPATH + fileName;
        System.out.println("FileLoader: loadFile(): Nacitam soubor: " + fullPath + ", existuje: " + new File(fullPath).exists());
        FileInputStream fstream = null;
        DataInputStream in = null;
        BufferedReader br = null;
        List vystup = null;
        try {
            vystup = new ArrayList();
            fstream = new FileInputStream(fullPath);
            // Get the object of DataInputStream
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            String strLine = "";
            // Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                System.out.println("FileLoader: loadFile(): Ctu radek " + strLine);
                if ( strLine.startsWith("c") ) continue;
                if ( strLine.startsWith("%") || (strLine.length() < 1) ) break;
                if ( strLine.startsWith("p") ) {
                    parseParameters(strLine);
                    continue;
                }
                vystup.add(parseLine(strLine));
            }
            // Close the input stream
            in.close();

        } catch (Exception e) {
            System.err.println("FileLoader: loadFile(): Chyba v nacitani souboru, error: " + e);
            return null;
        }
        return vystup;
    }

    /**
     * Pokusi se nacist soubor s vahama, pokud neexistuje, tak ho vytvori
     * @return
     */
    public byte[] loadWeights(String fileName) {
        String fullPath = DATAPATH + fileName;
        File soubor = new File(fullPath);
        System.out.println("FileLoader: loadWeights(): Nacitam soubor: " + fullPath +
                            ", existuje: " + soubor.exists());
        /* pokud neexistuje, vytvorime */
        if ( !soubor.exists() | !soubor.isFile() ) {
            System.out.println("FileLoader: loadWeights(): Vytvarim soubor s vahama.");
            createWeightFile(fullPath);
        }
        /* nacteme soubor */
        List vystup = loadFile(fileName);
        /* vahy jsou jenom prvni radek */
        return (byte[]) vystup.get(0);
    }

    /**
     * Vytvori soubor s vahama promennych
     */
    private void createWeightFile(String fullPath) {
        byte[] data = generateWeights(variablesCount);
        File soubor = new File(fullPath);
        FileWriter fstream = null;
        BufferedWriter out = null;
        try {
            soubor.createNewFile();
            soubor.setWritable(true);
            fstream = new FileWriter(fullPath);
            out = new BufferedWriter(fstream);
            out.write(byteToString(data));
            out.close();

        } catch (IOException ex) {
            System.err.println("FileLoader: createWeightFile(): nepovedlo se vytvořit soubor " + ex);
        }
    }

    /**
     * Vygeneruje vahy jednotlivych promennych
     * - vahy generujeme od 1 do 128
     * - ukoncime znakem 0
     *
     * @return byte[] pole nahodnych hodnot
     */
    private byte[] generateWeights(int delka) {
        byte[] vystup = new byte[delka + 1];
        Random randomGenerator = new Random();
        for (int i = 0; i <= delka; i++) {
            vystup[i] = (byte) (randomGenerator.nextInt(127) + 1);
        }
        vystup[vystup.length - 1] = 0;
        return vystup;
    }

    /**
     * Prevede byte na String
     * @param vstup
     * @return String vystup
     */
    private String byteToString(byte[] vstup) {
        String vystup = "";
        for (int i = 0; i < vstup.length; i++) {
            vystup += vstup[i] + " ";
        }
        return vystup.trim();
    }

    /**
     * Zjisti parametry z radku zacinajici p
     * Např. p cnf 20 91
     * @param line
     */
    private void parseParameters(String line) {
        System.out.println("FileLoader: parseParameters(): ctu parametry z radku " + line);
        String[] bits = line.split("\\s+");
        this.variablesCount = Integer.parseInt(bits[2].trim());
        System.out.println("FileLoader: parseParameters(): pocet promennych je " + this.variablesCount);
        this.linesCount = Integer.parseInt(bits[3].trim());
        System.out.println("FileLoader: parseParameters(): pocet radku je " + this.linesCount);
    }

    /**
     * Z jednoho radku zaznamu sestavi pole hodnot
     * @param strLine
     * @return
     */
    private byte[] parseLine(String line) {
        // System.out.println("FileLoader: parseLine(): ctu hodnoty z radku " + line);
        line = line.trim();
        String[] bits = line.split("\\s+");
        byte[] vystup = new byte[bits.length - 1];
        for (int i = 0; i < bits.length; i++) {
            if ( bits[i].isEmpty() ) continue;
            if ( bits[i].equals("0") ) break;
            vystup[i] = Byte.parseByte(bits[i]);
        }
        return vystup;
    }

    //== GETTERS AND SETTERS ===================================================

    public int getLinesCount() {
        return linesCount;
    }

    public int getVariablesCount() {
        return variablesCount;
    }

    //== TESTY =================================================================

    /**
     * Testovaci metoda
     * @param args
     */
    public static void main(String[] args) {

        String file = "small.txt";
        FileLoader fl = new FileLoader();
        List list = fl.loadFile(file);
        byte[] vahy = fl.loadWeights(file + ".sol");
        System.out.println("Vypisuji nactene klauzule: ");
        Printer.printArrayList(list);
        System.out.print("Vypisuji nactene vahy: ");
        Printer.printByteArray(vahy);

    }

}
