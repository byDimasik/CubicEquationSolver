package ru.nsu.fit.g15205.shishlyannikov;

import java.util.LinkedList;

class Solver {
    private double a, b, c, d, e;

    public interface FunctionInterface {
        double function(double x);
    }

    private FunctionInterface f = x -> x*x*x+a*x*x+b*x+c;
    private double sdes; // дискриминант производной

    Solver(double a, double b, double c, double d, double e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;

        // f' = 3*(x^2) + 2a*x + b
        sdes = Math.sqrt(a * a - 3 * b); // sqrt(D1) = sqrt((2a / 2) ^ 2 - 3 * b)
    }

    LinkedList<Double> solve(){
        LinkedList<Double> res = new LinkedList<>();

        // Case 1: D < e^2
        if (Double.isNaN(sdes) || sdes*sdes < e*e) {
            if (Math.abs(f.function(0)) < e) {
                res.add(0.0);
                return res;
            }

            if (f.function(0) > e) {
                res.add(findSolutionAtRay(0, false));
            }
            if (f.function(0) < -e) {
                res.add(findSolutionAtRay(0, true));
            }

            return res;
        }

        double z1 = (-a - sdes) / 3; // z1 = (-(b / 2) - sqrt(D1)) / ac
        double z2 = (-a + sdes) / 3; // z1 = (-(b / 2) + sqrt(D1)) / ac

        // Special case
        if ((Math.abs(f.function(z1)) < e) && (Math.abs(f.function(z2)) < e)) {
            res.add((z1 + z2) / 2);
            return res;
        }

        // Case 2.5
        // f(beta) < -e and f(alpha) > e
        if ((f.function(z2) < -e) && (f.function(z1) > e)) {
            res.add(findSolutionAtRay(z1,false));
            res.add(findSolutionAtSegment(z1,z2));
            res.add(findSolutionAtRay(z2,true));
            return res;
        }

        // Case 2.1 and 2.3
        // f(alpha) > e
        if (f.function(z1) > e) {
            // |f(beta)| < e
            if (Math.abs(f.function(z2)) < e) {
                res.add(z2);
            }
            res.add(findSolutionAtRay(z1,false)); // (-inf ; alpha]
            return res;
        }

        // Case 2.2 and 2.4
        // f(beta) < -e
        if (f.function(z2) < -e) {
            // |f(alpha)| < e
            if (Math.abs(f.function(z1)) < e) {
                res.add(z1);
            }
            res.add(findSolutionAtRay(z2,true)); // [beta ; +inf)
            return res;
        }


        return res;
    }

    private double findSolutionAtSegment(double x, double y) {
        double z = (Math.abs(f.function(x)) <= e) ? x : y;

        while ((Math.abs(f.function(x) * f.function(y))) >= (e * e)) {
            z = (x + y) / 2;

            if (f.function(x) * f.function(z) <= -e*e)
                y = z;
            else
                x = z;

            if (Math.abs(f.function(x)) <= e)
                return x;
        }

        return z;
    }

    /***
     * @param sgn - true => [x, +inf), false => (-inf, x]
     */
    private double findSolutionAtRay(double x, boolean sgn){
        int i = 0;
        int sg = sgn ? 1 : -1;

        if (sgn)
            while (f.function(x + d * (++i)) < e);
        else
            while (f.function(x - d * (++i)) > e);

        return findSolutionAtSegment(x + sg * d * (i - 1),x + sg * d * i);
    }

}
