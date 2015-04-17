package domain;

import java.util.Comparator;

public class HintComparator implements Comparator<Hint> {

    @Override
    public int compare(Hint t, Hint t1) {
        return Integer.compare(t.getTime(), t1.getTime());
    }
}
