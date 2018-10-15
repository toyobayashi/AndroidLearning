package com.github.toyobayashi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ComponentProgress extends ProgressBar {

    private String text;
    private Paint paint;
    private Rect rect = new Rect();

    public ComponentProgress(Context context) {
        super(context);
        initText();
    }

    public ComponentProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initText();
    }

    public ComponentProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initText();
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        setText(progress);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        this.paint.getTextBounds(this.text, 0, this.text.length(), rect);

        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(this.text, x, y, this.paint);
    }

    private void initText() {
        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.paint.setTextSize(30);
        this.paint.setAntiAlias(true);
        setText(0);
    }

    private void setText(int progress) {
        int i = (progress * 100) / this.getMax();
        this.text = String.valueOf(i) + " %";
    }
}
