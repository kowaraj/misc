package com.example.kj.lexithymia;

/**
 * Created by kapashnin on 19.09.18.
 */

public class Dyad {
    public String name;
    public String e1;
    public String e2;
    public String c1;
    public String c2;

    public Dyad(String name_) {
        name = name_;
        e1 = "";
        e2 = "";
        c1 = "#FFFFFFFF";
        c2 = "#FFFFFFFF";
    }

    public Dyad(String name_, String e1_, String e2_, String c1_, String c2_) {
        name = name_;
        e1 = e1_;
        e2 = e2_;
        c1 = c1_;
        c2 = c2_;
    }
}
