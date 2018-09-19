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
    Emo e1;
    Emo e2;
    ArrayList<DyadButton> dbs;
    LinearLayout lo1 = null;
    LinearLayout lo2 = null;

    Reducer(ChatClient plutchik_activity) {
        a = plutchik_activity;
        state = INIT;
        emos = new ArrayList<>();
        dbs = new ArrayList<>();
        e1 = null;
        e2 = null;
    }

    private void addEmo(Emo emo)
    {
        emos.add(emo);
    }

    public void setLayout1(LinearLayout lo)
    {
        lo1 = lo;
    }

    public void setLayout2(LinearLayout lo)
    {
        lo2 = lo;
    }

//    public Emo[] getEmos()
//    {
//        Emo[] exported = new Emo[]();
//        emos.toArray(exported);
//        return exported;
//    }

    public String getEmosAsString()
    {
        StringBuilder sb = new StringBuilder();
        for (Emo e : emos)
        {
            sb.append(e.name);
            sb.append(", ");
        }
        return sb.toString();
    }

    public void emoPicked(Emo emo)
    {
        if (state.equals(INIT) || state.equals(CLEAN_UP))
        {
            state = PICKED_EMO1;
            addEmo(emo);
            a.pickedEmo1(emo);
            addDyads(emo);
            e1 = emo;
        }
        else if (state.equals(PICKED_EMO1))
        {
            state = PICKED_EMO2;
            addEmo(emo);
            a.pickedEmo2(emo);
            addDyads(emo);
            e2 = emo;
        }
        else
            {

        }
    }

    public void dyadPicked(Dyad d)
    {

    }

    public void cleanUp() {
        if (state.equals(PICKED_EMO2) || state.equals(PICKED_EMO1)) {
            state = CLEAN_UP;
            a.cleanUpPickedEmos();
        }
    }




    private void addDyads (Emo e)
    {
        if (e1 == null)
        {
            String[][] diads = Constants.PLUTCHIK.getDiads(e.name);
            for (String[] diad : diads) {
                Dyad d = new Dyad(diad[0], diad[1], diad[2], diad[3], diad[4]);
                DyadButton db = new DyadButton(a, d);
                a.addDyadButton(lo1, db.getButton());
            }
        }
        else if (e2 == null)
        {
            String[][] diads = Constants.PLUTCHIK.getDiads(e.name, e1.name);
            for (String[] diad : diads) {
                Dyad d = new Dyad(diad[0], diad[1], diad[2], diad[3], diad[4]);
                DyadButton db = new DyadButton(a, d);
                a.addDyadButton(lo2, db.getButton());
            }
        }
        else {

        }
    }

}
