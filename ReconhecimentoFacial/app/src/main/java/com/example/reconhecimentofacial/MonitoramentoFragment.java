package com.example.reconhecimentofacial;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
        private final boolean isFrontCamera = true; // ajuste conforme câmera

        private long lastProcessTime = 0;
        private static final long PROCESS_INTERVAL_MS = 600; // 0,6s por análise

        public FaceAnalyzer(Context ctx,
                            FaceOverlayView overlayView,
                            float[] knownEmbedding,
                            FaceEmbeddingExtractor embeddingExtractor) {

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

            long now = System.currentTimeMillis();
            if (now - lastProcessTime < PROCESS_INTERVAL_MS) {
                imageProxy.close();
                return;
            }
            lastProcessTime = now;

            @androidx.annotation.OptIn(markerClass = androidx.camera.core.ExperimentalGetImage.class)
            Image mediaImage = imageProxy.getImage();
            if (mediaImage == null) {
                imageProxy.close();
                return;
            }

            InputImage inputImage = InputImage.fromMediaImage(
                    mediaImage, imageProxy.getImageInfo().getRotationDegrees());

            detector.process(inputImage)
                    .addOnSuccessListener(faces -> {

                        List<Rect> boxes = new ArrayList<>();

                        // Converte ImageProxy para Bitmap
                        Bitmap bitmap = FaceUtils.imageProxyToBitmap(imageProxy);

                        // Rotaciona conforme sensor
                        int rotation = imageProxy.getImageInfo().getRotationDegrees();
                        bitmap = FaceUtils.rotateBitmap(bitmap, rotation);

                        int viewWidth = overlay.getWidth();
                        int viewHeight = overlay.getHeight();
                        int imageWidth = bitmap.getWidth();
                        int imageHeight = bitmap.getHeight();

                        for (Face face : faces) {
                            Rect box = face.getBoundingBox();

                            // Mapeia bounding box do bitmap para PreviewView
                            Rect mappedBox = mapToPreview(box, imageWidth, imageHeight, viewWidth, viewHeight, isFrontCamera);

                            boxes.add(mappedBox);

                            // Recorta o rosto do bitmap
                            Bitmap croppedFace = FaceUtils.cropFace(bitmap, box);
                            if (croppedFace == null) continue;

                            try {
                                float[] currentEmb = extractor.getEmbedding(croppedFace);
                                float distance = FaceEmbeddingExtractor.euclideanDistance(currentEmb, knownEmb);

                                float THRESHOLD = 1.1f;
                                if (distance < THRESHOLD) {
                                    Toast.makeText(context, "Pessoa reconhecida!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        overlay.setFaces(boxes);
                    })
                    .addOnCompleteListener(task -> imageProxy.close());
        }

        private Rect mapToPreview(Rect faceBox, int imageWidth, int imageHeight,
                                  int viewWidth, int viewHeight, boolean isFrontCamera) {

            float scaleX = (float) viewWidth / imageWidth;
            float scaleY = (float) viewHeight / imageHeight;

            int left = (int) (faceBox.left * scaleX);
            int top = (int) (faceBox.top * scaleY);
            int right = (int) (faceBox.right * scaleX);
            int bottom = (int) (faceBox.bottom * scaleY);

            if (isFrontCamera) {
                // espelha horizontalmente
                int tempLeft = left;
                left = viewWidth - right;
                right = viewWidth - tempLeft;
            }

            return new Rect(left, top, right, bottom);
        }
    }

}
