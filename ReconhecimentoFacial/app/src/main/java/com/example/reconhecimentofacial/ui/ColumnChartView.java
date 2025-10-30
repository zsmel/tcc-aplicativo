package com.example.reconhecimentofacial.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.example.reconhecimentofacial.R;
import java.text.DecimalFormat;

public class ColumnChartView extends View {

    private float[] values = new float[0];
    private String[] labels = new String[0];
    private int[] barColors = new int[0];
    private int[] defaultColors;

    private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint labelPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint valuePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private DecimalFormat decimalFormat;

    // Padding ajustado para dar espaço para os valores em cima e os labels retos embaixo
    private final float padL = dp(12), padR = dp(12), padT = dp(36), padB = dp(36);

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
        labelPaint.setTextAlign(Paint.Align.CENTER); // Centraliza os labels da base

        valuePaint.setColor(Color.parseColor("#222222"));
        valuePaint.setTextSize(sp(11));
        valuePaint.setTextAlign(Paint.Align.CENTER); // Centraliza os valores

        decimalFormat = new DecimalFormat("#,###");
        defaultColors = new int[] {
                ContextCompat.getColor(getContext(), R.color.chart_bar_1),
                ContextCompat.getColor(getContext(), R.color.chart_bar_2),
                ContextCompat.getColor(getContext(), R.color.chart_bar_3),
                ContextCompat.getColor(getContext(), R.color.chart_bar_4),
                ContextCompat.getColor(getContext(), R.color.chart_bar_5)
        };
    }

    // Método antigo (chama o novo)
    public void setData(float[] values, String[] labels) {
        if (values == null || labels == null || values.length != labels.length) {
            setData(new float[0], new String[0], new int[0]);
        } else {
            setData(values, labels, defaultColors);
        }
    }

    // Novo método que aceita cores
    public void setData(float[] values, String[] labels, int[] colors) {
        if (values == null || labels == null || colors == null || values.length != labels.length) {
            this.values = new float[0];
            this.labels = new String[0];
            this.barColors = new int[0];
        } else {
            this.values = values;
            this.labels = labels;
            this.barColors = colors;
        }
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
        if (chartH <= 0) chartH = 1;

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

            if (barColors != null && barColors.length > 0) {
                barPaint.setColor(barColors[i % barColors.length]);
            } else {
                barPaint.setColor(Color.parseColor("#5A5A5A"));
            }
            c.drawRect(l, t, r, bottom, barPaint);

            // Formata o valor (ex: "39.700") e desenha acima da barra
            String val = decimalFormat.format(values[i]);
            c.drawText(val, cx, t - dp(4), valuePaint);

            // === AQUI ESTÁ A CORREÇÃO DAS "LETRAS TORTAS" ===
            // Removemos a rotação e desenhamos o texto reto
            String lab = labels[i];
            c.drawText(lab, cx, bottom + dp(20), labelPaint);
        }
    }

    private float dp(float v) { return v * getResources().getDisplayMetrics().density; }
    private float sp(float v) { return v * getResources().getDisplayMetrics().scaledDensity; }
}