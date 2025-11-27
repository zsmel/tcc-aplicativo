package com.example.reconhecimentofacial;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;

import androidx.camera.core.ImageProxy;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FaceUtils {

    public static Bitmap imageProxyToBitmap(ImageProxy image) {
        ByteBuffer yBuf = image.getPlanes()[0].getBuffer();
        ByteBuffer uBuf = image.getPlanes()[1].getBuffer();
        ByteBuffer vBuf = image.getPlanes()[2].getBuffer();

        int ySize = yBuf.remaining();
        int uSize = uBuf.remaining();
        int vSize = vBuf.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        yBuf.get(nv21, 0, ySize);
        vBuf.get(nv21, ySize, vSize);
        uBuf.get(nv21, ySize + vSize, uSize);

        YuvImage yuv = new YuvImage(
                nv21,
                ImageFormat.NV21,
                image.getWidth(),
                image.getHeight(),
                null
        );

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuv.compressToJpeg(
                new Rect(0, 0, image.getWidth(), image.getHeight()),
                100,
                out
        );

        byte[] jpeg = out.toByteArray();
        return BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);
    }

    public static Bitmap rotateBitmap(Bitmap bmp, int degrees) {
        if (degrees == 0) return bmp;
        Matrix m = new Matrix();
        m.postRotate(degrees);
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, true);
    }

    public static Bitmap cropFace(Bitmap src, Rect box) {
        int x = Math.max(box.left, 0);
        int y = Math.max(box.top, 0);
        int w = Math.min(box.width(), src.getWidth() - x);
        int h = Math.min(box.height(), src.getHeight() - y);

        if (w <= 0 || h <= 0) return null;

        return Bitmap.createBitmap(src, x, y, w, h);
    }

    // Load all embeddings (multiple per person)
    public static List<float[]> loadAllEmbeddings(Context ctx) {
        List<float[]> embeddings = new ArrayList<>();
        try {
            String[] files = ctx.getAssets().list("embeddings");
            if (files == null) return embeddings;

            for (String file : files) {
                if (!file.endsWith(".json")) continue;

                InputStream is = ctx.getAssets().open("embeddings/" + file);
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                is.close();

                JSONArray arr = new JSONArray(new String(buffer, StandardCharsets.UTF_8));

                // If JSON contains multiple embeddings (array of arrays)
                if (arr.length() > 0 && arr.get(0) instanceof JSONArray) {
                    for (int i = 0; i < arr.length(); i++) {
                        JSONArray inner = arr.getJSONArray(i);
                        float[] emb = new float[inner.length()];
                        for (int j = 0; j < inner.length(); j++) {
                            emb[j] = (float) inner.getDouble(j);
                        }
                        embeddings.add(l2Normalize(emb));
                    }
                } else {
                    float[] emb = new float[arr.length()];
                    for (int j = 0; j < arr.length(); j++) {
                        emb[j] = (float) arr.getDouble(j);
                    }
                    embeddings.add(l2Normalize(emb));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return embeddings;
    }

    private static float[] l2Normalize(float[] embedding) {
        float sum = 0f;
        for (float v : embedding) sum += v * v;
        float norm = (float) Math.sqrt(sum);
        if (norm > 0) {
            for (int i = 0; i < embedding.length; i++) embedding[i] /= norm;
        }
        return embedding;
    }

    public static Rect mapRectToPreview(Rect rect,
                                        int bitmapWidth, int bitmapHeight,
                                        int viewWidth, int viewHeight,
                                        boolean isFrontCamera) {

        float scaleX = (float) viewWidth / bitmapWidth;
        float scaleY = (float) viewHeight / bitmapHeight;
        float scale = Math.min(scaleX, scaleY);

        float offsetX = (viewWidth - bitmapWidth * scale) / 2f;
        float offsetY = (viewHeight - bitmapHeight * scale) / 2f;

        int left = (int) (rect.left * scale + offsetX);
        int top = (int) (rect.top * scale + offsetY);
        int right = (int) (rect.right * scale + offsetX);
        int bottom = (int) (rect.bottom * scale + offsetY);

        if (isFrontCamera) {
            int temp = left;
            left = viewWidth - right;
            right = viewWidth - temp;
        }

        return new Rect(left, top, right, bottom);
    }
}
