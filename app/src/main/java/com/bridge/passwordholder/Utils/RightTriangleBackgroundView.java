package com.bridge.passwordholder.Utils;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.bridge.passwordholder.R;

public class RightTriangleBackgroundView extends View {

    private Paint paint;
    private Paint bgPaint;
    private boolean mColor;
    Path path;
    public RightTriangleBackgroundView(Context context) {
        super(context);
        init();
    }

    public RightTriangleBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Triangle,
                0, 0);

        try {
            mColor = a.getBoolean(R.styleable.Triangle_tr_color_red, false);
        } finally {
            a.recycle();
        }
        init();
    }

    public RightTriangleBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStyle(Paint.Style.FILL);
        if(mColor) {
            paint.setColor(getResources().getColor(R.color.primary));
            bgPaint= new Paint();
            bgPaint.setStyle(Paint.Style.FILL);
            bgPaint.setColor(getResources().getColor(android.R.color.transparent));
        }
        else
        {
            paint.setColor(getResources().getColor(R.color.blur));
            bgPaint= new Paint();
            bgPaint.setStyle(Paint.Style.FILL);
            bgPaint.setColor(getResources().getColor(android.R.color.transparent));
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int h = getMeasuredHeight();
        int w = getMeasuredWidth();

        canvas.drawRect(0, 0, w, h, bgPaint);

        path= new Path();
        path.moveTo(0, h);
        path.lineTo(w, h);
        path.lineTo(0, 0);
        path.lineTo(0, h);
        path.close();
        canvas.drawPath(path,paint);
    }
}