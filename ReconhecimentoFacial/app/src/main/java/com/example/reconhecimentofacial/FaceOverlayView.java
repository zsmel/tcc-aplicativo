package com.example.reconhecimentofacial;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class FaceOverlayView extends View {

    private List<Rect> faces = new ArrayList<>();
    private final Paint paint;

    public FaceOverlayView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        paint = new Paint();
        paint.setColor(0xFFFF0000);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
    }

    public void setFaces(List<Rect> list) {
        faces = list;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Rect r : faces) {
            canvas.drawRect(r, paint);
        }
    }
}
