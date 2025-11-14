package com.example.reconhecimentofacial;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import androidx.camera.core.ImageProxy;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;

public class FaceUtils {

    /**
     * Converte ImageProxy (YUV) em Bitmap (mantendo orientação original da câmera).
     */
    public static Bitmap imageProxyToBitmap(ImageProxy image) {
        ByteBuffer yBuffer = image.getPlanes()[0].getBuffer();
        ByteBuffer uBuffer = image.getPlanes()[1].getBuffer();
        ByteBuffer vBuffer = image.getPlanes()[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];

        // Formato NV21 (Y + V + U)
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(
                nv21,
                ImageFormat.NV21,
                image.getWidth(),
                image.getHeight(),
                null
        );

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(
                new Rect(0, 0, image.getWidth(), image.getHeight()),
                100,
                out
        );

        byte[] jpegBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.length);
    }

    /**
     * Rotaciona bitmap com base no ângulo retornado pela câmera.
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int rotationDegrees) {
        if (rotationDegrees == 0) return bitmap;

        Matrix matrix = new Matrix();
        matrix.postRotate(rotationDegrees);

        return Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight(),
                matrix,
                true
        );
    }

    /**
     * Recorta o rosto usando o bounding box do ML Kit.
     */
    public static Bitmap cropFace(Bitmap source, Rect boundingBox) {
        int x = Math.max(boundingBox.left, 0);
        int y = Math.max(boundingBox.top, 0);
        int width = Math.min(boundingBox.width(), source.getWidth() - x);
        int height = Math.min(boundingBox.height(), source.getHeight() - y);

        if (width <= 0 || height <= 0) return null;

        return Bitmap.createBitmap(source, x, y, width, height);
    }

    /**
     * Carrega embedding salvo em JSON dentro dos assets.
     */
    public static float[] loadStoredEmbedding(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray arr = new JSONArray(json);

            float[] emb = new float[arr.length()];
            for (int i = 0; i < arr.length(); i++) {
                emb[i] = (float) arr.getDouble(i);
            }

            return emb;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Mapeia o Rect do bitmap para as coordenadas do PreviewView.
     * Considera proporção e espelhamento.
     */
    public static Rect mapRectToPreview(Rect rect, int bitmapWidth, int bitmapHeight,
                                        int viewWidth, int viewHeight, boolean isFrontCamera) {
        // Calcula escala proporcional
        float scaleX = (float) viewWidth / bitmapWidth;
        float scaleY = (float) viewHeight / bitmapHeight;
        float scale = Math.min(scaleX, scaleY); // mantém proporção

        // Calcula offsets caso haja letterboxing
        float offsetX = (viewWidth - bitmapWidth * scale) / 2f;
        float offsetY = (viewHeight - bitmapHeight * scale) / 2f;

        int left = (int) (rect.left * scale + offsetX);
        int top = (int) (rect.top * scale + offsetY);
        int right = (int) (rect.right * scale + offsetX);
        int bottom = (int) (rect.bottom * scale + offsetY);

        // Espelhamento da câmera frontal
        if (isFrontCamera) {
            int tmpLeft = left;
            left = viewWidth - right;
            right = viewWidth - tmpLeft;
        }

        return new Rect(left, top, right, bottom);
    }

}
