package paasat.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Trida pro nacteni cele slozky
 * Decorator nad FileLoader
 *
 * @author Bc. Vojtěch Svoboda <svobovo3@fit.cvut.cz>
 */
public class DirectoryLoader {

    /**
     * Vrati vsechny soubory ve slozce
     * @param directory
     * @return
     */
    public String[] getDirectoryFiles(String directory) {
        String fullPath = System.getProperty("user.dir") + "\\" + directory + "\\";
        File slozka = new File(fullPath);
        if ( !slozka.isDirectory() || !slozka.exists() ) return null;
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                int end = name.length() - 4;
                return name.substring(end).equals(".cnf");
            }
        };
        String[] list = slozka.list(filter);
        return list;
    }

}
