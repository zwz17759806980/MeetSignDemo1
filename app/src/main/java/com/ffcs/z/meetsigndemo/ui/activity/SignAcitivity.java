package com.ffcs.z.meetsigndemo.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.ffcs.z.meetsigndemo.R;
import com.ffcs.z.meetsigndemo.bean.Api;
import com.ffcs.z.meetsigndemo.bean.BaseBean;
import com.ffcs.z.meetsigndemo.bean.FaceWebInfoApi;
import com.ffcs.z.meetsigndemo.bean.SignBean;
import com.ffcs.z.meetsigndemo.bean.SignRecoderBean;
import com.ffcs.z.meetsigndemo.ui.adapter.SignAdapter;
import com.ffcs.z.meetsigndemo.ui.base.BaseActivity;
import com.ffcs.z.meetsigndemo.utils.BitmapUtils;
import com.ffcs.z.meetsigndemo.utils.SoundPlayUtils;
import com.ffcs.z.meetsigndemo.utils.SystemUtils;
import com.ffcs.z.meetsigndemo.wight.MultiStateView;
import com.ffcs.z.meetsigndemo.wight.face.CameraErrorCallback;
import com.ffcs.z.meetsigndemo.wight.face.FaceOverlayView;
import com.ffcs.z.meetsigndemo.wight.face.FaceResult;
import com.ffcs.z.meetsigndemo.wight.face.ImageUtils;
import com.ffcs.z.meetsigndemo.wight.face.Util;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

import static android.hardware.Camera.getNumberOfCameras;

public class SignAcitivity extends BaseActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {
    @Bind(R.id.sign_real_time)
    TextView readTime;
    @Bind(R.id.sign_msv)
    MultiStateView multiStateView;
    @Bind(R.id.sign_rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.sign_sfv)
    SurfaceView mView;
    @Bind(R.id.sign_face)
    ImageView faceShow;
    @Bind(R.id.sign_show)
    TextView signShow;
    @Bind(R.id.showNetState)
    ImageView showNetState;

    // Number of Cameras in device.
    private int numberOfCameras;
    private Camera mCamera;
    private int cameraId = 0;
    //跟踪显示器的旋转和方向
    private int mDisplayRotation;
    private int mDisplayOrientation;
    private int previewWidth;
    private int previewHeight;
    // The surface view for the camera data
    private Button btn_switch;
    // Draw rectangles and other fancy stuff:
    private FaceOverlayView mFaceView;
    // Log all errors:
    private final CameraErrorCallback mErrorCallback = new CameraErrorCallback();


    private static final int MAX_FACE = 10;
    private boolean isThreadWorking = false;
    private Handler handler;
    private FaceDetectThread detectThread = null;
    private int prevSettingWidth;
    private int prevSettingHeight;
    private android.media.FaceDetector fdet;

    private FaceResult faces[];
    private FaceResult faces_previous[];
    private int Id = 0;
    private int currentNet = 3;
    private String BUNDLE_CAMERA_ID = "camera";


    //RecylerView face image
    private HashMap<Integer, Integer> facesCount = new HashMap<>();
    private ArrayList<Bitmap> facesBitmap;
    private TimeThread timeThread;
    private static final int msgKey1 = 1;
    private SignReceiver mReceiver;

    private String dinnerInfoId;
    private SignAdapter mAdapter;
    HashMap<Integer, Integer> musicId = new HashMap<Integer, Integer>();
    SoundPool soundPool;

    private final static String SING_SUCCESS="1";
    private final static String SING_FAILE="2";
    private final static String SING_REPEAT="3";


    @Override
    protected int getLayoutID() {
        return R.layout.activity_sign;
    }

    @Override
    protected void initView() {
//        initSoundPool();


        mFaceView = new FaceOverlayView(this);
        addContentView(mFaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        handler = new Handler();
        faces = new FaceResult[MAX_FACE];
        faces_previous = new FaceResult[MAX_FACE];
        for (int i = 0; i < MAX_FACE; i++) {
            faces[i] = new FaceResult();
            faces_previous[i] = new FaceResult();
        }
        /////////////

        timeThread = new TimeThread();//实时时间
        timeThread.start();

        dinnerInfoId = getIntent().getStringExtra("meetInfoId");

        init();


    }


    private void init() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        RequestParams requestParams = new RequestParams(Api.QUERY_SING_RECODER);
        requestParams.addBodyParameter("meetingInfoId", dinnerInfoId);
        requestParams.addBodyParameter("deviceNum", SystemUtils.getSerialNumber());
//        requestParams.addBodyParameter("deviceNum", "ML5RR3OMKM");
        requestParams.addBodyParameter("total", "9");
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SignRecoderBean recoderBean = JSONObject.parseObject(result, SignRecoderBean.class);
                if (recoderBean.getReturnCode().equals("1")) {
                    List<FaceWebInfoApi> beans = recoderBean.getResult();
                    if (beans == null) {
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                    } else {
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                        mAdapter = new SignAdapter(beans);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                }

            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                faceShow.setVisibility(View.GONE);

            }
        });

    }

    @Override
    protected void initData() {
        mReceiver = new SignReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.ffcs.z.RECEIVER");
        registerReceiver(mReceiver, intentFilter);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SurfaceHolder holder = mView.getHolder();
        holder.addCallback(this);
        holder.setFormat(ImageFormat.NV21);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        if (!isThreadWorking) {
            if (counter == 0)
                start = System.currentTimeMillis();

            isThreadWorking = true;
            waitForFdetThreadComplete();
            detectThread = new FaceDetectThread(handler, this);
            detectThread.setData(data);
            detectThread.start();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //Find the total number of cameras available
        numberOfCameras = getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                if (cameraId == 0) cameraId = i;
            }

        }

        mCamera = Camera.open(cameraId);

        Camera.getCameraInfo(cameraId, cameraInfo);
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mFaceView.setFront(true);
        }

        try {
            mCamera.setPreviewDisplay(mView.getHolder());
        } catch (Exception e) {
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // We holder no surface, return immediately:
        if (holder.getSurface() == null) {
            return;
        }
        // Try to stop the current preview:
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // Ignore...
        }

        configureCamera(width, height);
        setDisplayOrientation();
        setErrorCallback();

        // Create media.FaceDetector
        float aspect = (float) previewHeight / (float) previewWidth;
        fdet = new android.media.FaceDetector(prevSettingWidth, (int) (prevSettingWidth * aspect), MAX_FACE);


        // Everything is configured! Finally start the camera preview again:
        startPreview();
    }

    private void setErrorCallback() {
        mCamera.setErrorCallback(mErrorCallback);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.setPreviewCallbackWithBuffer(null);
            mCamera.setErrorCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private void configureCamera(int width, int height) {
        Camera.Parameters parameters = mCamera.getParameters();
        // Set the PreviewSize and AutoFocus:
        setOptimalPreviewSize(parameters, width, height);
        setAutoFocus(parameters);
        // And set the parameters:
        mCamera.setParameters(parameters);
    }

    private void setDisplayOrientation() {
        // Now set the display orientation:
        mDisplayRotation = Util.getDisplayRotation(SignAcitivity.this);
        mDisplayOrientation = Util.getDisplayOrientation(mDisplayRotation, cameraId);

        mCamera.setDisplayOrientation(mDisplayOrientation);

        if (mFaceView != null) {
            mFaceView.setDisplayOrientation(mDisplayOrientation);
        }
    }

    private void setOptimalPreviewSize(Camera.Parameters cameraParameters, int width, int height) {
        List<Camera.Size> previewSizes = cameraParameters.getSupportedPreviewSizes();
        float targetRatio = (float) width / height;
        Camera.Size previewSize = Util.getOptimalPreviewSize(this, previewSizes, targetRatio);
        previewWidth = previewSize.width;
        previewHeight = previewSize.height;


        if (previewWidth / 4 > 360) {
            prevSettingWidth = 360;
            prevSettingHeight = 270;
        } else if (previewWidth / 4 > 320) {
            prevSettingWidth = 320;
            prevSettingHeight = 240;
        } else if (previewWidth / 4 > 240) {
            prevSettingWidth = 240;
            prevSettingHeight = 160;
        } else {
            prevSettingWidth = 160;
            prevSettingHeight = 120;
        }

        cameraParameters.setPreviewSize(previewSize.width, previewSize.height);

        mFaceView.setPreviewWidth(previewWidth);
        mFaceView.setPreviewHeight(previewHeight);
    }

    private void setAutoFocus(Camera.Parameters cameraParameters) {
        List<String> focusModes = cameraParameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
            cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

    private void startPreview() {
        if (mCamera != null) {
            isThreadWorking = false;
            mCamera.startPreview();
            mCamera.setPreviewCallback(this);
            counter = 0;
        }
    }


    private void waitForFdetThreadComplete() {
        if (detectThread == null) {
            return;
        }

        if (detectThread.isAlive()) {
            try {
                detectThread.join();
                detectThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    // fps detect face (not FPS of camera)
    long start, end;
    int counter = 0;
    double fps;

    /**
     * Do face detect in thread
     */
    private class FaceDetectThread extends Thread {
        private Handler handler;
        private byte[] data = null;
        private Context ctx;
        private Bitmap faceCroped;

        public FaceDetectThread(Handler handler, Context ctx) {
            this.ctx = ctx;
            this.handler = handler;
        }


        public void setData(byte[] data) {
            Log.i("DATA", "1");
            this.data = data;
        }

        public void run() {
//            Log.i("FaceDetectThread", "running");

            float aspect = (float) previewHeight / (float) previewWidth;
            int w = prevSettingWidth;
            int h = (int) (prevSettingWidth * aspect);

            Bitmap bitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.RGB_565);
            // face detection: first convert the image from NV21 to RGB_565
            YuvImage yuv = new YuvImage(data, ImageFormat.NV21,
                    bitmap.getWidth(), bitmap.getHeight(), null);
            // TODO: make rect a member and use it for width and height values above
            Rect rectImage = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            // TODO: use a threaded option or a circular buffer for converting streams?
            //see http://ostermiller.org/convert_java_outputstream_inputstream.html
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            if (!yuv.compressToJpeg(rectImage, 100, baout)) {
                Log.e("CreateBitmap", "compressToJpeg failed");
            }

            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeStream(
                    new ByteArrayInputStream(baout.toByteArray()), null, bfo);

            Bitmap bmp = Bitmap.createScaledBitmap(bitmap, w, h, false);

            float xScale = (float) previewWidth / (float) prevSettingWidth;
            float yScale = (float) previewHeight / (float) h;

            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            int rotate = mDisplayOrientation;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && mDisplayRotation % 180 == 0) {
                if (rotate + 180 > 360) {
                    rotate = rotate - 180;
                } else
                    rotate = rotate + 180;
            }

            switch (rotate) {
                case 90:
                    bmp = ImageUtils.rotate(bmp, 90);
                    xScale = (float) previewHeight / bmp.getWidth();
                    yScale = (float) previewWidth / bmp.getHeight();
                    break;
                case 180:
                    bmp = ImageUtils.rotate(bmp, 180);
                    break;
                case 270:
                    bmp = ImageUtils.rotate(bmp, 270);
                    xScale = (float) previewHeight / (float) h;
                    yScale = (float) previewWidth / (float) prevSettingWidth;
                    break;
            }

            fdet = new android.media.FaceDetector(bmp.getWidth(), bmp.getHeight(), MAX_FACE);

            android.media.FaceDetector.Face[] fullResults = new android.media.FaceDetector.Face[MAX_FACE];
            fdet.findFaces(bmp, fullResults);

            for (int i = 0; i < MAX_FACE; i++) {
                if (fullResults[i] == null) {
                    faces[i].clear();
                } else {
                    PointF mid = new PointF();
                    fullResults[i].getMidPoint(mid);

                    mid.x *= xScale;
                    mid.y *= yScale;

                    float eyesDis = fullResults[i].eyesDistance() * xScale;
                    float confidence = fullResults[i].confidence();
                    float pose = fullResults[i].pose(android.media.FaceDetector.Face.EULER_Y);
                    int idFace = Id;

                    Rect rect = new Rect(
                            (int) (mid.x - eyesDis * 1.20f),
                            (int) (mid.y - eyesDis * 0.55f),
                            (int) (mid.x + eyesDis * 1.20f),
                            (int) (mid.y + eyesDis * 1.85f));

                    /**
                     * Only detect face size > 100x100
                     */
                    if (rect.height() * rect.width() > 100 * 100) {
                        for (int j = 0; j < MAX_FACE; j++) {
                            float eyesDisPre = faces_previous[j].eyesDistance();
                            PointF midPre = new PointF();
                            faces_previous[j].getMidPoint(midPre);

                            RectF rectCheck = new RectF(
                                    (midPre.x - eyesDisPre * 1.5f),
                                    (midPre.y - eyesDisPre * 1.15f),
                                    (midPre.x + eyesDisPre * 1.5f),
                                    (midPre.y + eyesDisPre * 1.85f));

                            if (rectCheck.contains(mid.x, mid.y) && (System.currentTimeMillis() - faces_previous[j].getTime()) < 1000) {
                                idFace = faces_previous[j].getId();
                                break;
                            }
                        }

                        if (idFace == Id) Id++;

                        faces[i].setFace(idFace, mid, eyesDis, confidence, pose, System.currentTimeMillis());

                        faces_previous[i].set(faces[i].getId(), faces[i].getMidEye(), faces[i].eyesDistance(), faces[i].getConfidence(), faces[i].getPose(), faces[i].getTime());
                        if (facesCount.get(idFace) == null) {
                            facesCount.put(idFace, 0);
                        } else {
                            int count = facesCount.get(idFace) + 1;
                            if (count <= 5)
                                facesCount.put(idFace, count);
                            if (count == 5) {
                                faceCroped = ImageUtils.cropFace(faces[i], bitmap, rotate);
                                if (faceCroped != null) {
                                    handler.post(new Runnable() {
                                        public void run() {
                                            faceShow.setVisibility(View.VISIBLE);
                                            faceShow.setImageBitmap(faceCroped);
                                        }
                                    });

                                    RequestParams requestParams = new RequestParams(Api.SENDFACE);
                                    requestParams.addBodyParameter("pictureBase64", BitmapUtils.bitmapToBase64(faceCroped));
                                    requestParams.addBodyParameter("deviceNum", SystemUtils.getSerialNumber());
//                                    requestParams.addBodyParameter("deviceNum", "ML5RR3OMKM");
                                    x.http().post(requestParams, new Callback.CommonCallback<String>() {
                                        @Override
                                        public void onSuccess(String result) {
                                            Log.i("RESULT", "onSuccess: " + result);
                                            BaseBean baseBean = JSONObject.parseObject(result, BaseBean.class);
                                            FaceWebInfoApi bean = baseBean.getResult();
                                            //失败不处理
                                            if (baseBean.getReturnCode().equals("0")) {
                                                return;
                                            }

                                            if (bean.getAudioType().equals(SING_SUCCESS)){
                                                SoundPlayUtils.play(1);
                                                Log.d("SING_SUCCESS", "onSuccess: "+1);
                                                init();
                                            }else if(bean.getAudioType().equals(SING_FAILE)){
                                                SoundPlayUtils.play(2);
                                                Log.d("SING_FAILE", "FAILE: "+2);

                                            }else if(bean.getAudioType().equals(SING_REPEAT)){
                                                SoundPlayUtils.play(3);
                                                Log.d("SING_REPEAT", "REPEAT: "+3);

                                            }

                                            signShow.setText(bean.getDesc());


                                        }


                                        @Override
                                        public void onError(Throwable ex, boolean isOnCallback) {
                                        }

                                        @Override
                                        public void onCancelled(CancelledException cex) {
                                        }

                                        @Override
                                        public void onFinished() {
                                            faceShow.setVisibility(View.GONE);

                                        }
                                    });


                                }
                            }
                        }
                    }
                }
            }

            handler.post(new Runnable() {
                public void run() {
                    mFaceView.setFaces(faces);
                    end = System.currentTimeMillis();
                    counter++;
                    double time = (double) (end - start) / 1000;
                    if (time != 0)
                        fps = counter / time;
                    mFaceView.setFPS(fps);
                    if (counter == (Integer.MAX_VALUE - 1000))
                        counter = 0;
                    isThreadWorking = false;
                }
            });
        }
    }


    public class TimeThread extends Thread {
        @Override
        public void run() {
            super.run();
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
                    readTime.setText(format.format(date));
                    break;
                default:
                    break;
            }
        }
    };


    public class SignReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            SignBean bean = (SignBean) intent.getSerializableExtra("SignBean");
                if (bean.getMeetinginfos() == null) {
                    finish();
                    return;
                }

                if(bean.getMeetinginfos().getMeetingSignStatus() == 1){
                    finish();
                    return;
                }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("BBB","onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("BBB","onRestart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("BBB","onResume");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
