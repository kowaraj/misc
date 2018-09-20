package com.example.kj.lexithymia;

import android.app.Activity;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by kapashnin on 19.09.18.
 */

public class Reducer {

    private Integer state = INIT;
    private static final Integer INIT = 0;
    private static final Integer PICKED_EMO1 = 1;
    private static final Integer PICKED_EMO2 = 2;
    private static final Integer CLEAN_UP = 3;

    ChatClient a;
    ArrayList<Emo> emos;
    ArrayList<Dyad> ds;
    Emo e1;
    String e1_oppositeName;
    Emo e2;
    ArrayList<DyadButton> dbs;
    LinearLayout lo1 = null;
    LinearLayout lo2 = null;

    Reducer(ChatClient plutchik_activity) {
        a = plutchik_activity;
        state = INIT;
        emos = new ArrayList<>();
        dbs = new ArrayList<>();
        ds  = new ArrayList<>();
        e1 = null;
        e1_oppositeName = null;
        e2 = null;
    }

    private void addEmo(Emo emo)
    {
        emos.add(emo);
    }

    private void removeEmo(Emo emo)
    {
        emos.remove(emos.indexOf(emo));
    }

    private void addDyad(Dyad d)
    {
        ds.add(d);
    }

    private void removeDyad(Dyad d)
    {
        int i = ds.indexOf(d);
        ds.remove(i);
    }

    public void setLayout1(LinearLayout lo)
    {
        lo1 = lo;
    }

    public void setLayout2(LinearLayout lo)
    {
        lo2 = lo;
    }

    public String getEmosAsString()
    {
        StringBuilder sb = new StringBuilder();
        for (Emo e : emos)
        {
            sb.append(e.name);
            sb.append(", ");
        }
        for (Dyad d: ds)
        {
            sb.append(d.name);
            sb.append(", ");
        }

        if ((sb.length() >= 2) &&
                (sb.substring(sb.length()-2, sb.length()).equalsIgnoreCase(", ")))
            return sb.substring(0, sb.length()-2).toString();
        else
            return sb.toString();
    }

    public void emoPicked(Emo emo)
    {
        if (state.equals(INIT) || state.equals(CLEAN_UP))
        {
            state = PICKED_EMO1;
            addEmo(emo);
            a.pickedEmo1(emo);
            e1 = addDyads(emo);
            e1_oppositeName = Constants.PLUTCHIK.getOpposite(e1.name);
        }
        else if (state.equals(PICKED_EMO1))
        {
            if (emo.name.equalsIgnoreCase(e1.name)) // chose again the same emotion
                return;

            if (emo.name.equalsIgnoreCase(e1_oppositeName)) // chose the opposite of e1
                return;

            state = PICKED_EMO2;
            addEmo(emo);
            a.pickedEmo2(emo);
            e2 = addDyads(emo);
        }
        else
            {

        }
    }

    public void unpickEmo1()
    {
        if (state.equals(PICKED_EMO1) || state.equals(PICKED_EMO2)) {
            emos.remove(e1);
            e1 = null;
            e1_oppositeName = null;
            emos.remove(e2);
            e2 = null;
            state = CLEAN_UP;

            a.removeDyadsOfEmo1();
            a.removeDyadsOfEmo2();

        }
    }

    public void unpickEmo2()
    {
        if (state.equals(PICKED_EMO2)) {
            emos.remove(e2);
            e2 = null;
            state = PICKED_EMO1;
            a.removeDyadsOfEmo2();
        }
    }

    public void dyadPicked(String dyad_name)
    {
        Dyad d = new Dyad(dyad_name);
        addDyad(d);
    }

    public void dyadUnpicked(String dyad_name)
    {
        Dyad d_to_delete = null;
        for (Dyad d :ds) {
             if (d.name.equalsIgnoreCase(dyad_name)) {
                 d_to_delete = d;
                 break;
             }
        }
        removeDyad(d_to_delete);
    }


    private Emo addDyads (Emo e)
    {

        if (!e.isBasic()) {
            return e;
        }

        if (e1 == null)
        {

            String[][] diads = Constants.PLUTCHIK.getDiads(e.name);
            for (String[] diad : diads) {
                Dyad d = new Dyad(diad[0], diad[1], diad[2], diad[3], diad[4]);
                DyadButton db = new DyadButton(a, d, true);
                dbs.add(db);
                a.addDyadButton(lo1, db.getButton());
            }
            return e;
        }
        else if (e2 == null)
        {

            String[][] diads = Constants.PLUTCHIK.getDiads(e.name, e1.name);
            for (String[] diad : diads) {
                Dyad d = new Dyad(diad[0], diad[1], diad[2], diad[3], diad[4]);
                DyadButton db = new DyadButton(a, d, false);
                dbs.add(db);
                a.addDyadButton(lo2, db.getButton());
            }
            return e;
        }
        else {
            return null;
        }
    }

}
