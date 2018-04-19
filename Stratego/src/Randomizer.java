import java.util.Random;

/**
 * A simple class that allows me to not have to make Random objects
 * in other classes.
 * Created by Steven Bruman on 2/2017
 * Edited by Steven Bruman
 * Version 2/2017
 */
public class Randomizer
{
    public static Random rgen = new Random();

    /**
     * Returns a random number from 0 to 1 less than the parameter.
     * @param i The upper limit of the range.
     * @return int 0 to the limit - 1.
     */
    public static int getRgen(int i)
    {
        return rgen.nextInt(i);
    }
}
