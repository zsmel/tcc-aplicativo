package com.example.reconhecimentofacial;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private float[] storedEmbedding;
    private FaceEmbeddingExtractor embeddingExtractor;

    private PreviewView previewView;
    private FaceOverlayView faceOverlayView;

    private static final float THRESHOLD = 1.0f; // distância Euclidiana limite para correspondência

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

        storedEmbedding = FaceUtils.loadStoredEmbedding(requireContext(), "my_embedding.json");

        try {
            embeddingExtractor = new FaceEmbeddingExtractor(requireContext().getAssets());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Erro ao carregar modelo de embeddings", Toast.LENGTH_LONG).show();
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
        bitmap = FaceUtils.rotateBitmap(bitmap, 270); // ajuste conforme orientação da câmera

        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);

        Bitmap finalBitmap = bitmap;
        faceDetector.process(inputImage)
                .addOnSuccessListener(faces -> handleFaces(faces, finalBitmap))
                .addOnFailureListener(Throwable::printStackTrace)
                .addOnCompleteListener(task -> imageProxy.close());
    }

    private void handleFaces(List<Face> faces, Bitmap bitmap) {
        List<Rect> mappedRects = new ArrayList<>();

        for (Face face : faces) {
            Rect boundingBox = face.getBoundingBox();
            Bitmap faceBitmap = FaceUtils.cropFace(bitmap, boundingBox);
            if (faceBitmap == null) continue;

            // Calcula embedding real da face
            float[] faceEmbedding = embeddingExtractor.ccgetEmbedding(faceBitmap);

            // Compara com embedding salvo
            float distance = FaceEmbeddingExtractor.euclideanDistance(faceEmbedding, storedEmbedding);
            boolean match = distance < THRESHOLD;

            if (match) {
                Toast.makeText(requireContext(), "Rosto reconhecido!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Rosto não reconhecido", Toast.LENGTH_SHORT).show();
            }

            // Mapeia bounding box para overlay
            Rect mapped = FaceUtils.mapRectToPreview(
                    boundingBox,
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
