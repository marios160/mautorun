package pl.mario.mautorun;

import java.util.Comparator;

public class Struct implements Comparable<Struct>, Comparator<Struct> {

    String line;
    int num;

    public Struct(String line, int num) {
        this.line = line;
        this.num = num;
    }

    public Struct() {
    }

    @Override
    public int compareTo(Struct o) {
        return line.compareTo(o.line);
    }

    @Override
    public int compare(Struct o1, Struct o2) {
        if (o1.num < o2.num) {
            return -1;
        } else {
            return 1;
        }
    }

}
