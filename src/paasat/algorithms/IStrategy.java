package paasat.algorithms;

/**
 * Rozhrani pro algoritmy
 * @author Bc. Vojtěch Svoboda <svobovo3@fit.cvut.cz>
 */
public interface IStrategy {

    public void solve();

    public byte[] getBestSolution();

}
