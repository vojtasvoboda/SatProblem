package paasat.algorithms;

/**
 * Rozhrani pro algoritmy
 * @author Bc. Vojtěch Svoboda <svobovo3@fit.cvut.cz>
 */
public interface IStrategy {

    public void solve();

    public boolean[] getBestOhodnoceni();

    public int getBestWeight();

    public int getExpandovano();

    public int getSplnitelne();

}
