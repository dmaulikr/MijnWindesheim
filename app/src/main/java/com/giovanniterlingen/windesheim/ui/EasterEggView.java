package com.giovanniterlingen.windesheim.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import com.giovanniterlingen.windesheim.ApplicationLoader;
import com.giovanniterlingen.windesheim.objects.EasterEggHead;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * A schedule app for students and teachers of Windesheim
 *
 * @author Giovanni Terlingen
 */
public class EasterEggView extends View {

    private int xMax;
    private int yMax;
    private EasterEggHead[] heads;

    private float density;
    private Random random;

    public EasterEggView(Context context) {
        super(context);

        // how much teachers do we have
        final int teacherCount = 215;
        this.density = context.getResources().getDisplayMetrics().density;
        this.random = new Random();

        heads = new EasterEggHead[teacherCount];
        for (int i = 0; i < teacherCount; i++) {
            heads[i] = this.addHead(i);
        }
    }

    public EasterEggHead addHead(int i) {
        int velX = 1 + random.nextInt(15);
        int velY = 1 + random.nextInt(15);
        int radius = dp(16 + random.nextInt(24));

        Bitmap icon = getTeacherBitmap(i);

        return new EasterEggHead(radius, velX, velY, icon);
    }

    private Bitmap getTeacherBitmap(int teacher) {
        AssetManager assetManager = ApplicationLoader.applicationContext.getAssets();
        Bitmap bitmap = null;
        try {
            InputStream istr = assetManager.open(Integer.toString(teacher) + ".jpg");
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (EasterEggHead head : heads) {
            head.draw(canvas);
            head.update(xMax, yMax);
        }
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.xMax = w - 1;
        this.yMax = h - 1;
    }

    private int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }
}
