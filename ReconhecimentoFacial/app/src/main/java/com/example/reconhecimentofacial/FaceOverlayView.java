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

    private List<Rect> faceBounds = new ArrayList<>();
    private final Paint paint;

    public FaceOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xFFFF0000); // Red
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
    }

    public void setFaces(List<Rect> bounds) {
        faceBounds = bounds;
        invalidate(); // Triggers onDraw
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Rect rect : faceBounds) {
            canvas.drawRect(rect, paint);
        }
    }
}