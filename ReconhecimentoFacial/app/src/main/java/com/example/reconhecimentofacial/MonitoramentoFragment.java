package com.example.reconhecimentofacial;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private List<PersonEmbedding> storedEmbeddings = new ArrayList<>();
    private FaceEmbeddingExtractor embeddingExtractor;

    private PreviewView previewView;
    private FaceOverlayView faceOverlayView;

    // Variáveis para controle manual (Botão)
    private Button btnAnalisar;
    private boolean analisarAgora = false;
    private boolean processando = false;

    private static final float THRESHOLD = 0.8f; // Limiar de similaridade

    public static class PersonEmbedding {
        public String label;
        public float[] embedding;

        public PersonEmbedding(String label, float[] embedding) {
            this.label = label;
            this.embedding = embedding;
        }
    }

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
        btnAnalisar = view.findViewById(R.id.btnAnalisar);

        cameraExecutor = Executors.newSingleThreadExecutor();

        // Configurações do ML Kit
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                        .build();
        faceDetector = FaceDetection.getClient(options);

        // Carrega embeddings (Base de dados local)
        storedEmbeddings = FaceUtils.loadAllEmbeddingsWithLabels(requireContext());

        try {
            embeddingExtractor = new FaceEmbeddingExtractor(requireContext().getAssets());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Erro ao carregar modelo TFLite", Toast.LENGTH_LONG).show();
        }

        // Ação do Botão: Inicia a análise de um único frame
        btnAnalisar.setOnClickListener(v -> {
            if (!processando) {
                analisarAgora = true;
                btnAnalisar.setEnabled(false);
                btnAnalisar.setText("Processando...");
                faceOverlayView.setFaces(new ArrayList<>()); // Limpa a tela
            }
        });

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
        // Se o botão NÃO foi clicado ou já estamos ocupados, descarta o frame imediatamente
        if (!analisarAgora || processando) {
            imageProxy.close();
            return;
        }

        processando = true;
        analisarAgora = false; // Desliga o gatilho para não processar o próximo frame automaticamente

        Bitmap bitmap = FaceUtils.imageProxyToBitmap(imageProxy);
        int rotation = imageProxy.getImageInfo().getRotationDegrees();
        bitmap = FaceUtils.rotateBitmap(bitmap, rotation);
        bitmap = mirrorBitmap(bitmap);

        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        Bitmap finalBitmap = bitmap;

        faceDetector.process(inputImage)
                .addOnSuccessListener(faces -> {
                    if (faces.isEmpty()) {
                        resetarBotaoUI("Nenhum rosto detectado");
                    } else {
                        // Processa e obtém a mensagem exata (Match ou No Match)
                        String resultado = handleFaces(faces, finalBitmap);
                        resetarBotaoUI(resultado);
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    resetarBotaoUI("Erro na detecção: " + e.getMessage());
                })
                .addOnCompleteListener(t -> {
                    imageProxy.close();
                    processando = false;
                });
    }

    private void resetarBotaoUI(String mensagem) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                btnAnalisar.setEnabled(true);
                btnAnalisar.setText("Analisar Rosto");
                if (mensagem != null) {
                    // Exibe o resultado específico na tela
                    Toast.makeText(getContext(), mensagem, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private Bitmap mirrorBitmap(Bitmap bitmap) {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }

    // Modificado para retornar a String de resultado e desenhar o quadrado
    private String handleFaces(List<Face> faces, Bitmap bitmap) {
        List<Rect> mappedRects = new ArrayList<>();
        String resultMessage = "Rosto detectado, mas sem correspondência.";

        // Processa os rostos (assume-se 1 rosto principal na self)
        for (Face face : faces) {
            Rect box = face.getBoundingBox();
            Bitmap faceBmp = FaceUtils.cropFace(bitmap, box);
            if (faceBmp == null) continue;

            float[] emb = embeddingExtractor.ccgetEmbedding(faceBmp);

            float bestDistance = Float.MAX_VALUE;
            String bestLabel = "Desconhecido";

            // Compara com os embeddings salvos (Local ou Firebase futuramente)
            for (PersonEmbedding stored : storedEmbeddings) {
                float dist = FaceEmbeddingExtractor.euclideanDistance(emb, stored.embedding);
                if (dist < bestDistance) {
                    bestDistance = dist;
                    bestLabel = stored.label;
                }
            }

            // Verifica se deu Match baseado no limiar
            boolean match = bestDistance < THRESHOLD;

            // Define a mensagem final que o usuário quer ver
            if (match) {
                resultMessage = "MATCH ✔ " + bestLabel + String.format(" (%.2f)", bestDistance);
            } else {
                resultMessage = "NO MATCH ✘ (" + String.format("%.2f", bestDistance) + ")";
            }

            Log.d("FACE_RECOGNITION", resultMessage);

            // Desenha o quadrado na tela para feedback visual
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

        // Atualiza o desenho na tela
        faceOverlayView.setFaces(mappedRects);

        // Retorna a mensagem para ser exibida no Toast
        return resultMessage;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
    }
}