package com.example.reconhecimentofacial;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.camera.view.PreviewView;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MonitoramentoFragment extends Fragment {

    private ExecutorService cameraExecutor;
    private FaceDetector faceDetector;

    private List<float[]> storedEmbeddings = new ArrayList<>();
    private FaceEmbeddingExtractor embeddingExtractor;

    private PreviewView previewView;
    private FaceOverlayView faceOverlayView;

    private static final float THRESHOLD = 1.25f; // you can adjust

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monitoramento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        previewView = view.findViewById(R.id.previewView);
        faceOverlayView = view.findViewById(R.id.faceOverlay);

        cameraExecutor = Executors.newSingleThreadExecutor();

        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                        .build();
        faceDetector = FaceDetection.getClient(options);

        // Load all embeddings from assets/embeddings/
        storedEmbeddings = FaceUtils.loadAllEmbeddings(requireContext());

        try {
            embeddingExtractor = new FaceEmbeddingExtractor(requireContext().getAssets());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Erro ao carregar modelo", Toast.LENGTH_LONG).show();
        }

        startCamera();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder()
                        .setTargetResolution(new Size(480, 640))
                        .build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(480, 640))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, this::analyzeImage);

                CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageAnalysis
                );

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void analyzeImage(ImageProxy imageProxy) {
        Bitmap bitmap = FaceUtils.imageProxyToBitmap(imageProxy);

        int rotation = imageProxy.getImageInfo().getRotationDegrees();
        bitmap = FaceUtils.rotateBitmap(bitmap, rotation);

        bitmap = mirrorBitmap(bitmap);

        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);

        Bitmap finalBitmap = bitmap;
        faceDetector.process(inputImage)
                .addOnSuccessListener(faces -> handleFaces(faces, finalBitmap))
                .addOnFailureListener(Throwable::printStackTrace)
                .addOnCompleteListener(t -> imageProxy.close());
    }

    private Bitmap mirrorBitmap(Bitmap bitmap) {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }

    private void handleFaces(List<Face> faces, Bitmap bitmap) {

        List<Rect> mappedRects = new ArrayList<>();

        for (Face face : faces) {
            Rect box = face.getBoundingBox();
            Bitmap faceBmp = FaceUtils.cropFace(bitmap, box);
            if (faceBmp == null) continue;

            float[] emb = embeddingExtractor.ccgetEmbedding(faceBmp);

            float bestDistance = Float.MAX_VALUE;

            for (float[] stored : storedEmbeddings) {
                float dist = FaceEmbeddingExtractor.euclideanDistance(emb, stored);
                if (dist < bestDistance) {
                    bestDistance = dist;
                }
            }

            boolean match = bestDistance < THRESHOLD;

            String msg = match
                    ? "MATCH ✔ (" + bestDistance + ")"
                    : "NO MATCH ✘ (" + bestDistance + ")";
            Log.d("FACE_RECOGNITION", msg);
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();

            Rect mapped = FaceUtils.mapRectToPreview(
                    box,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    faceOverlayView.getWidth(),
                    faceOverlayView.getHeight(),
                    true
            );

            mappedRects.add(mapped);
        }

        faceOverlayView.setFaces(mappedRects);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cameraExecutor.shutdown();
        faceDetector.close();
    }
}
