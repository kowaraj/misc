package com.example.kj.lexithymia;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.solver.widgets.WidgetContainer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

/**
 * Created by kapashnin on 19.09.18.
 */

public class DyadButton
{
    private ToggleButton tg;
    private Dyad d;

    public ToggleButton getButton()
    {
        return tg;
    }

    public DyadButton(ChatClient parent, Dyad d_)
    {
        d = d_;
        String n = d.name;
        String c1 = d.c2;
        String c2 = d.c1;

        int cc1 = Color.parseColor(c1);
        int cc2 = Color.parseColor(c2);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(350, 50);
        lp.setMargins(0, 2, 0, 2);
        tg = new ToggleButton(parent);
        tg.setHeight(20);
        tg.setText(n);
        tg.setTextOff(n);
        tg.setTextOn(n);
        tg.setChecked(true);
        tg.setPadding(3, 0, 3, 0);

        Bitmap bmResult = Bitmap.createBitmap(200, 30, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmResult);
        Paint paint = new Paint();
        paint.setShader(new LinearGradient(0, 0, bmResult.getWidth() * 0.9f, 0, cc1, cc2, Shader.TileMode.CLAMP));
        canvas.drawPaint(paint);
        paint.setMaskFilter(new BlurMaskFilter(3, BlurMaskFilter.Blur.NORMAL));
        canvas.drawRect(0, 0, bmResult.getWidth(), bmResult.getHeight() / 2, paint);

        tg.setBackgroundDrawable(new BitmapDrawable(bmResult));
        if (Color.luminance(Color.parseColor(c1)) < 0.3)
            tg.setTextColor(Color.WHITE);
        tg.setTextSize(8f);
        tg.setPadding(0, 0, 0, 0);
        tg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        tg.setLayoutParams(lp);



    }
}
