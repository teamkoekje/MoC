package domain;

import java.util.Comparator;

/**
 * Compares two hints dependant on when they are scheduled to be released.
 *
 * @author TeamKoekje
 */
public class HintComparator implements Comparator<Hint> {

    @Override
    public int compare(Hint t, Hint t1) {
        return Long.compare(t.getTime(), t1.getTime());
    }
}
