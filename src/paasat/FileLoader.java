package paasat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Soubor pro nacteni vstupnich dat
 * @author Bc. Vojtěch Svoboda <svobovo3@fit.cvut.cz>
 */
public class FileLoader {

    public String fileName = null;

    public String fullPath = null;

    public int variablesCount = 0;

    public int linesCount = 0;

    List instance = null;

    public FileLoader(String fileName) {
        this.fileName = fileName;
        this.fullPath = System.getProperty("user.dir") + "\\data\\" + fileName;
        this.instance = new ArrayList();
    }

    /**
     * Nacte vstupni soubor do dvourozmerneho pole
     * @return
     */
    public List loadFile() {
        System.out.println("FileLoader: loadFile(): Nacitam soubor: " + this.fullPath + ", existuje: " + new File(this.fullPath).exists());
        FileInputStream fstream = null;
        DataInputStream in = null;
        BufferedReader br = null;
        try {
            /* Zkopirovano z http://www.roseindia.net/java/beginners/java-read-file-line-by-line.shtml */
            fstream = new FileInputStream(this.fullPath);
            // Get the object of DataInputStream
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            String strLine = "";
            int idecko = 0;
            // Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                System.out.println("FileLoader: loadFile(): Ctu radek " + strLine + ", ktery bude na indexu " + idecko++);
                if ( strLine.startsWith("c") ) continue;
                if ( strLine.startsWith("%") || (strLine.length() < 1) ) break;
                if ( strLine.startsWith("p") ) {
                    parseParameters(strLine);
                    continue;
                }
                instance.add(parseLine(strLine));
            }
            // Close the input stream
            in.close();

        } catch (Exception e) {
            System.err.println("FileLoader: loadFile(): Chyba v nacitani souboru, error: " + e);
            return null;
        }
        return instance;
    }

    // private List

    /**
     * Zjisti parametry z radku zacinajici p
     * Např. p cnf 20  91
     * @param line
     */
    private void parseParameters(String line) {
        System.out.println("FileLoader: parseParameters(): ctu parametry z radku " + line);
        String[] bits = line.split(" ");
        this.variablesCount = Integer.parseInt(bits[2]);
        System.out.println("FileLoader: parseParameters(): pocet promennych je " + this.variablesCount);
        this.linesCount = Integer.parseInt(bits[4]);
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
        String[] bits = line.split(" ");
        byte[] vystup = new byte[bits.length - 1];
        for (int i = 0; i < bits.length; i++) {
            if ( bits[i].isEmpty() ) continue;
            if ( bits[i].equals("0") ) break;
            // System.out.println("FileLoader: parseLine(): ctu hodnotu " + bits[i]);
            vystup[i] = Byte.parseByte(bits[i]);
        }
        return vystup;
    }

    /**
     * Vytiskne list na konzoli pro testovani
     * @param list
     */
    public void printArrayList(List vstup) {
        System.out.println("FileLoader: printArrayList(): tisknu list na konzoli:");
        for (int i = 0; i < vstup.size(); i++) {
            byte[] radka = (byte[]) vstup.get(i);
            for (int j = 0; j < radka.length; j++) {
                byte b = radka[j];
                System.out.print(b + " ");
            }
            System.out.println("");
        }
    }

    public int getLinesCount() {
        return linesCount;
    }

    public int getVariablesCount() {
        return variablesCount;
    }

}
