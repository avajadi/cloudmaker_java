package org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.building;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;


/**
 * Created by eddie on 2017-08-01.
 */
public class Room implements Iterable<Point> {
    private static final String XML_TEMPLATE = "<room scanned=\"%s\"><name>%s</name><width unit=\"cm\">%.0f</width><length unit=\"cm\">%.0f</length><points>";
    private List<Point> points = new ArrayList<>();
    private String name;
    private List<ChangeListener> changeListeners = new ArrayList<>();

    public Room(String name) {
        this.name = name;
    }

    public void addPoint(Point point) {
        points.add(point);
        notifyChangeListeners();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        points.forEach(p -> {
            sb.append(p.toString() + "\n");
        });
        sb.append(String.format("Width: %.0fcm ; Length: %.0fcm\n", getWidth(), getLength()));
        return sb.toString();
    }

    public String toXML() {
        Instant now = Instant.now();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(XML_TEMPLATE, now.toString(), getName(), getWidth(), getLength()));
        sb.append('\n');
        points.forEach(p -> {
            sb.append(p.toXML() + "\n");
        });
        sb.append("</points></room>");
        return sb.toString();
    }

    @Override
    public Iterator<Point> iterator() {
        return points.iterator();
    }

    @Override
    public void forEach(Consumer<? super Point> action) {
        points.forEach(action);
    }

    @Override
    public Spliterator<Point> spliterator() {
        return points.spliterator();
    }

    public Point.Cartesian getOffset() {
        return new Point.Cartesian(getMinX(), getMinY());
    }

    public double getWidth() {
        double minX = getMinX();
        double maxX = getMaxX();
        return maxX - minX;
    }

    public double getLength() {
        double minY = getMinY();
        double maxY = getMaxY();
        return maxY - minY;
    }

    private double getMinX() {
        double min = 0;
        for (Point p : this) {
            Point.Cartesian c = p.getCartesian();
            if (c.x < min) {
                min = c.x;
            }
        }
        return min;
    }

    private double getMinY() {
        double min = 0;
        for (Point p : this) {
            Point.Cartesian c = p.getCartesian();
            if (c.y < min) {
                min = c.y;
            }
        }
        return min;
    }

    private double getMaxX() {
        double max = 0;
        for (Point p : this) {
            Point.Cartesian c = p.getCartesian();
            if (c.x > max) {
                max = c.x;
            }
        }
        return max;
    }

    private double getMaxY() {
        double max = 0;
        for (Point p : this) {
            Point.Cartesian c = p.getCartesian();
            if (c.y > max) {
                max = c.y;
            }
        }
        return max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyChangeListeners();
    }

    private void notifyChangeListeners() {
        changeListeners.forEach(l -> {
            l.stateChanged(new ChangeEvent(this));
        });
    }

    public void addChangeListeners(ChangeListener changeListener) {
        this.changeListeners.add(changeListener);
    }

    public void save(File file) throws FileNotFoundException {
        PrintStream out = null;
        try {
            out = new PrintStream(file);
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            out.print(toXML());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void clear() {
        points.clear();
        notifyChangeListeners();
    }
}
