package paasat.utils;

import paasat.utils.FileLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Trida pro nacteni cele slozky
 * Decorator nad FileLoader
 *
 * @author Bc. Vojtěch Svoboda <svobovo3@fit.cvut.cz>
 */
public class DirectoryLoader {

    private FileLoader myFileLoader = null;

    public DirectoryLoader(FileLoader myFileLoader) {
        this.myFileLoader = myFileLoader;
    }

    /**
     * Nacte vsechny soubory ve slozce
     * @param path
     * @return
     */
    public List[] loadDirectory(String path) {
        String[] soubory = getDirectoryFiles(path);
        // List[] vystup = new ArrayList();
        for (String file: soubory) {

        }
        return null;
    }

    /**
     * Vrati vsechny soubory ve slozce
     * @param directory
     * @return
     */
    public String[] getDirectoryFiles(String directory) {
        File slozka = new File(directory);
        if ( !slozka.isDirectory() ) return null;

        return null;
    }

}
