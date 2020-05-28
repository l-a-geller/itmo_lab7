package data;

import java.util.Comparator;

public class CoordinatesComparator<LabWork> implements Comparator<data.LabWork> {

    @Override
    public int compare(data.LabWork t2, data.LabWork t1) {
        return (int)(t2.getCoordinates().getX() * t2.getCoordinates().getX() + t2.getCoordinates().getY() * t2.getCoordinates().getY() - (t1.getCoordinates().getX() * t1.getCoordinates().getX() + t1.getCoordinates().getY() * t1.getCoordinates().getY()));
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }
}
