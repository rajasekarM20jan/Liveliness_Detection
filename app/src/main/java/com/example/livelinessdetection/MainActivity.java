package com.example.livelinessdetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

public class MainActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surfaceView);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                camera = Camera.open(1);
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                    camera.setDisplayOrientation(90);
//                    Camera.Parameters parameters = camera.getParameters();
//                    Camera.Size previewSize = parameters.getPreviewSize();
//
//
//                    float aspectRatio = (float) previewSize.width / previewSize.height;
//                    int surfaceViewWidth = 100;
//                    int surfaceViewHeight = (int) (surfaceViewWidth / aspectRatio);
//                    surfaceView.getLayoutParams().width = surfaceViewWidth;
//                    surfaceView.getLayoutParams().height = surfaceViewHeight;
//                    // Set the aspect ratio of the SurfaceView to match the aspect ratio of the camera preview
//                    ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
//
//                        layoutParams.width = (int) (surfaceViewHeight * aspectRatio);
//                        layoutParams.height = surfaceViewHeight;
//
//                    surfaceView.setLayoutParams(layoutParams);

                    startCapturingFrames();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // Not used
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                camera.stopPreview();
                camera.release();
            }
        });
    }

    private void startCapturingFrames() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                captureImage();
            }
        }, 0, 5000); // Capture an image frame every 5 second (5000 milliseconds)
    }



    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // Convert byte array to bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            // Save bitmap to file or do something else with it
            // ...

            // Restart camera preview

            FaceDetectorOptions options =
                    new FaceDetectorOptions.Builder()
                            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
                            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                            .build();

            FaceDetector faceDetector = FaceDetection.getClient(options);

            InputImage image = InputImage.fromBitmap(bitmap, 90);

            faceDetector.process(image)
                    .addOnSuccessListener(
                            new OnSuccessListener<List<Face>>() {
                                @Override
                                public void onSuccess(List<Face> faces) {
                                    // Handle the results
                                    if(faces.size() >=1)
                                    {
                                        String value = "new value";
                                        String value1 = value;
                                        for (Face face : faces) {
                                            Rect bounds = face.getBoundingBox();
                                            float rotX = face.getHeadEulerAngleX();
                                            Log.d("rotX", Float. toString(rotX));
                                            float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                                            Log.d("rotY", Float. toString(rotY));
                                            float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees
                                            Log.d("rotZ", Float. toString(rotZ));

                                            if((rotY>=20 || rotY<=-20)){
                                                Toast.makeText(MainActivity.this, "Face Detected", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(MainActivity.this,dummyActivity.class));
                                                stopCapturingFrames();
                                                finish();
                                            }
//                                            String s = Float. toString(rotY);
//                                            String snew = s;
//                                            String snew1 = Float. toString(rotZ);
//                                            String snew2 = snew1;
//                                             If landmark detection was enabled (mouth, ears, eyes, cheeks, and
//                                             nose available):
//                                            FaceLandmark leftEar = face.getLandmark(FaceLandmark.LEFT_EAR);
//                                            if (leftEar != null) {
//                                                PointF leftEarPos = leftEar.getPosition();
//                                                Log.i("leftEarPosX", Float.toString(leftEarPos.x));
//                                                Log.i("leftEarPosY", Float.toString(leftEarPos.y));
//                                            }

                                            // If contour detection was enabled:
//                                            List<PointF> leftEyeContour =
//                                                    face.getContour(FaceContour.LEFT_EYE).getPoints();
//                                            List<PointF> upperLipBottomContour =
//                                                    face.getContour(FaceContour.UPPER_LIP_BOTTOM).getPoints();
//
//                                            // If classification was enabled:
//                                            if (face.getSmilingProbability() != null) {
//                                                float smileProb = face.getSmilingProbability();
//                                            }
//                                            if (face.getRightEyeOpenProbability() != null) {
//                                                float rightEyeOpenProb = face.getRightEyeOpenProbability();
//                                            }

                                            // If face tracking was enabled:
//                                            if (face.getTrackingId() != null) {
//                                                int id = face.getTrackingId();
//                                            }
                                        }
                                    }
                                    else
                                    {
                                        String value = "new value";
                                        String value1 = value;
                                    }

                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle the error
                                    String value = "new value";
                                    String value1 = value;
                                }
                            });


            camera.startPreview();
        }
    };

    public void captureImage() {
        camera.takePicture(null, null, pictureCallback);
    }

    private void stopCapturingFrames() {
        timer.cancel();
    }
}