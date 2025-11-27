package com.example.reconhecimentofacial;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FaceEmbeddingExtractor {

    private final Interpreter interpreter;

    public FaceEmbeddingExtractor(AssetManager assetManager) throws IOException {
        MappedByteBuffer tfliteModel = loadModelFile(assetManager, "MobileFaceNet.tflite");
        Interpreter.Options options = new Interpreter.Options();
        interpreter = new Interpreter(tfliteModel, options);
    }

    private MappedByteBuffer loadModelFile(AssetManager assets, String path) throws IOException {
        AssetFileDescriptor fileDescriptor = assets.openFd(path);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public float[] ccgetEmbedding(Bitmap bitmap) {
        // Resize
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 112, 112, true);

        // Preprocess: normalize to [-1, 1]
        int width = resized.getWidth();
        int height = resized.getHeight();
        float[][][][] input = new float[2][height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int px = resized.getPixel(x, y);
                float r = ((px >> 16) & 0xFF) / 255.0f;
                float g = ((px >> 8) & 0xFF) / 255.0f;
                float b = (px & 0xFF) / 255.0f;

                input[0][y][x][0] = (r - 0.5f) / 0.5f;
                input[0][y][x][1] = (g - 0.5f) / 0.5f;
                input[0][y][x][2] = (b - 0.5f) / 0.5f;
            }
        }

        // Duplicate to batch of 2
        input[1] = input[0];

        float[][] output = new float[2][192];
        interpreter.run(input, output);

        // Normalize embedding
        float[] embedding = output[0];
        return l2Normalize(embedding);
    }

    private float[] l2Normalize(float[] embedding) {
        float sum = 0f;
        for (float v : embedding) sum += v * v;
        float norm = (float) Math.sqrt(sum);
        float[] normalized = new float[embedding.length];
        for (int i = 0; i < embedding.length; i++) normalized[i] = embedding[i] / norm;
        return normalized;
    }

    public static float euclideanDistance(float[] a, float[] b) {
        if (a.length != b.length) throw new IllegalArgumentException("Embedding size mismatch");

        float sum = 0f;
        for (int i = 0; i < a.length; i++) {
            float diff = a[i] - b[i];
            sum += diff * diff;
        }
        return (float) Math.sqrt(sum);
    }
}
