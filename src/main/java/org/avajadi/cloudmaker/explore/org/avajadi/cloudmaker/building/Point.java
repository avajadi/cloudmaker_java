package org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.building;

/**
 * Created by eddie on 2017-08-01.
 */
public class Point {
    private Polar polar;

    public static final String XML_TEMPLATE = "<point><x unit=\"cm\">%.0f</x><y unit=\"cm\">%.0f</y><angle unit=\"degrees\">%.1f</angle><distance unit=\"cm\">%.0f</distance></point>";
    public Point( double angle, double distance ) {
        this.polar = new Polar( angle, distance );
    }

    public Polar getPolar() {
        return polar;
    }

    @Override
    public String toString() {
        Cartesian cart = getCartesian();
        return String.format( "x: %.0fcm ; y: %.0fcm ; angle: %.0f°; distance: %.0fcm", cart.x, cart.y, polar.angle, polar.distance );
    }

    public String toXML() {
        Cartesian cart = getCartesian();
        return String.format( XML_TEMPLATE, cart.x, cart.y, polar.angle, polar.distance );
    }
    public Cartesian getCartesian() {
        double x = getPolar().distance * Math.cos( getPolar().radianAngle() );
        double y = getPolar().distance * Math.sin( getPolar().radianAngle() );
        return new Cartesian( x, y );
    }

    public class Polar {
        public double angle;
        public double distance;

        public Polar( double angle, double distance ) {
            this.angle = angle;
            this.distance = distance;
        }
        public double radianAngle() {
            return 2 * Math.PI * angle / 360;
        }
    }

    public static class Cartesian {
        public double x;
        public double y;

        public Cartesian( double x, double y ) {
            this.x = x;
            this.y = y;
        }

        public Cartesian offset(Cartesian offset) {
            return new Cartesian( x - offset.x, y - offset.y );
        }

        public Cartesian offset(double offset) {
            return new Cartesian( x - offset, y - offset );
        }

        public Cartesian scale(double scale) {
            return new Cartesian( x * scale, y * scale );
        }
    }

}
