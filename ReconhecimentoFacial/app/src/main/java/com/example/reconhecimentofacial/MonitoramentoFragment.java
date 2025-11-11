package com.example.reconhecimentofacial;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class MonitoramentoFragment extends Fragment {

    private PreviewView previewView;
    private FaceOverlayView faceOverlayView;
    private ImageCapture imageCapture;
    private float[] knownEmbedding;
    private FaceEmbeddingExtractor embeddingExtractor;

    private static final int REQUEST_CAMERA_PERMISSION = 101;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitoramento, container, false);
        previewView = view.findViewById(R.id.previewView);
        faceOverlayView = view.findViewById(R.id.faceOverlay);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initRecognition();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initRecognition();
        } else {
            Toast.makeText(requireContext(), "Camera permission required", Toast.LENGTH_SHORT).show();
        }
    }

    private void initRecognition() {
        try {
            embeddingExtractor = new FaceEmbeddingExtractor(requireContext().getAssets());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to load model", Toast.LENGTH_LONG).show();
            return;
        }

        knownEmbedding = FaceUtils.loadStoredEmbedding(requireContext(), "my_embedding.json");
        startCamera();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                CameraSelector selector = CameraSelector.DEFAULT_FRONT_CAMERA;

                ImageAnalysis analysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                analysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext()),
                        new FaceAnalyzer(requireContext(), faceOverlayView,
                                knownEmbedding, embeddingExtractor));

                imageCapture = new ImageCapture.Builder().build();

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(
                        getViewLifecycleOwner(),
                        selector,
                        preview,
                        analysis,
                        imageCapture
                );

            } catch (ExecutionException | InterruptedException e) {
                Log.e("CameraX", "Camera init failed", e);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    // --- Inner class copied from your MainActivity, trimmed for clarity ---
    private static class FaceAnalyzer implements ImageAnalysis.Analyzer {
        private final Context context;
        private final FaceOverlayView overlay;
        private final float[] knownEmb;
        private final FaceEmbeddingExtractor extractor;
        private final FaceDetector detector;

        public FaceAnalyzer(Context ctx, FaceOverlayView overlayView,
                            float[] knownEmbedding, FaceEmbeddingExtractor embeddingExtractor) {
            context = ctx;
            overlay = overlayView;
            knownEmb = knownEmbedding;
            extractor = embeddingExtractor;

            FaceDetectorOptions opts = new FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                    .enableTracking()
                    .build();
            detector = FaceDetection.getClient(opts);
        }

        @Override
        public void analyze(@NonNull ImageProxy imageProxy) {
            @androidx.annotation.OptIn(markerClass = androidx.camera.core.ExperimentalGetImage.class)
            Image mediaImage = imageProxy.getImage();
            if (mediaImage == null) {
                imageProxy.close();
                return;
            }
            InputImage image = InputImage.fromMediaImage(
                    mediaImage, imageProxy.getImageInfo().getRotationDegrees());

            detector.process(image)
                    .addOnSuccessListener(faces -> {
                        List<Rect> boxes = new ArrayList<>();
                        for (Face f : faces) {
                            boxes.add(f.getBoundingBox());
                            // You can add your recognition logic here using extractor + knownEmb
                        }
                        overlay.setFaces(boxes);
                    })
                    .addOnCompleteListener(t -> imageProxy.close());
        }
    }
}
