package ru.nsu.fit.g15205.shishlyannikov;

public class App
{
    public static void main( String[] args )
    {
        double a, b, c, d, e;

        a = 3;
        b = 8;
        c = -2;

        d = 1;
        e = 1e-5;

        Solver sol = new Solver(a, b, c, d, e);

        System.out.println(sol.solve());
    }
}
