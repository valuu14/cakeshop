package domain;

import java.util.Comparator;

public class BirthdayCakeComparator implements Comparator<BirthdayCake> {
    public int compare(BirthdayCake first, BirthdayCake second) {
        return (int) (first.getPrice() - second.getPrice());
    }
}
