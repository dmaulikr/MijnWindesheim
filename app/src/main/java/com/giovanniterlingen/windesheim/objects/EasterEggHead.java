package com.giovanniterlingen.windesheim.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * A schedule app for students and teachers of Windesheim
 *
 * @author Giovanni Terlingen
 */
public class EasterEggHead {

    private int headX = 0;
    private int headY = 0;

    private int headRadius;

    private int headSpeedX;
    private int headSpeedY;

    private RectF headBounds;
    private Bitmap bitmap;
    private Paint paint;

    public EasterEggHead(int radius, int headSpeedX, int headSpeedY, Bitmap bitmap) {
        this.headRadius = radius;

        this.headSpeedX = headSpeedX;
        this.headSpeedY = headSpeedY;

        this.headBounds = new RectF();
        this.bitmap = bitmap;
        this.paint = new Paint();
    }

    public void update(int xMax, int yMax) {
        this.headX += this.headSpeedX;
        this.headY += this.headSpeedY;

        if (this.headX + this.headRadius > xMax) {
            this.headSpeedX = -this.headSpeedX;
            this.headX = xMax - this.headRadius;
        } else if (this.headX - this.headRadius < 0) {
            this.headSpeedX = -this.headSpeedX;
            this.headX = this.headRadius;
        }
        if (this.headY + this.headRadius > yMax) {
            this.headSpeedY = -this.headSpeedY;
            this.headY = yMax - this.headRadius;
        } else if (this.headY - this.headRadius < 0) {
            this.headSpeedY = -this.headSpeedY;
            this.headY = this.headRadius;
        }
    }

    public void draw(Canvas canvas) {
        this.headBounds.set(this.headX - this.headRadius, this.headY - this.headRadius, this.headX + this.headRadius, this.headY + this.headRadius);
        canvas.drawBitmap(this.bitmap, null, this.headBounds, this.paint);
    }
}
