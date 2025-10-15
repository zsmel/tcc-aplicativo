package com.example.reconhecimentofacial.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class ColumnChartView extends View {

    private float[] values = new float[0];
    private String[] labels = new String[0];

    private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint labelPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint valuePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private final float padL = dp(12), padR = dp(12), padT = dp(8), padB = dp(36);

    public ColumnChartView(Context c) { super(c); init(); }
    public ColumnChartView(Context c, AttributeSet a) { super(c, a); init(); }
    public ColumnChartView(Context c, AttributeSet a, int s) { super(c, a, s); init(); }

    private void init() {
        barPaint.setColor(Color.parseColor("#5A5A5A"));
        axisPaint.setColor(Color.parseColor("#DDDDDD"));
        axisPaint.setStrokeWidth(dp(1));
        gridPaint.setColor(Color.parseColor("#EEEEEE"));
        gridPaint.setStrokeWidth(dp(1));

        labelPaint.setColor(Color.parseColor("#666666"));
        labelPaint.setTextSize(sp(11));

        valuePaint.setColor(Color.parseColor("#222222"));
        valuePaint.setTextSize(sp(11));
    }

    public void setData(float[] values, String[] labels) {
        if (values == null || labels == null || values.length != labels.length) return;
        this.values = values;
        this.labels = labels;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        if (values.length == 0) return;

        float left = padL, right = getWidth() - padR;
        float top = padT, bottom = getHeight() - padB;

        // eixo X
        c.drawLine(left, bottom, right, bottom, axisPaint);

        // grid horizontal (4 linhas)
        float chartH = bottom - top;
        for (int i = 1; i <= 4; i++) {
            float y = bottom - (chartH * i / 4f);
            c.drawLine(left, y, right, y, gridPaint);
        }

        // escala
        float max = 0f;
        for (float v : values) if (v > max) max = v;
        if (max <= 0f) max = 1f;

        float chartW = right - left;
        float slot = chartW / values.length;
        float barW = slot * 0.55f;

        for (int i = 0; i < values.length; i++) {
            float cx = left + slot * i + slot / 2f;
            float h = (values[i] / max) * chartH;
            float l = cx - barW / 2f;
            float r = cx + barW / 2f;
            float t = bottom - h;

            c.drawRect(l, t, r, bottom, barPaint);

            String val = (values[i] == Math.round(values[i])) ?
                    String.valueOf((int) values[i]) : String.valueOf(values[i]);
            float vw = valuePaint.measureText(val);
            c.drawText(val, cx - vw / 2f, t - dp(4), valuePaint);

            c.save();
            c.translate(cx, bottom + dp(14));
            c.rotate(-45);
            String lab = labels[i];
            float lw = labelPaint.measureText(lab);
            c.drawText(lab, -lw / 2f, 0, labelPaint);
            c.restore();
        }
    }

    private float dp(float v) { return v * getResources().getDisplayMetrics().density; }
    private float sp(float v) { return v * getResources().getDisplayMetrics().scaledDensity; }
}