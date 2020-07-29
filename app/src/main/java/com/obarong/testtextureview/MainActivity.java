package com.obarong.testtextureview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.TextureView;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private static final String TAG = "MainActivity";
    private TextureView mTextureView;
    private Camera mCamera;
    private int mWidth;
    private int mHeight;
    private Bitmap captureBitmap;
    private boolean firstCapture = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(this);
        setContentView(mTextureView);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.i(TAG, "onSurfaceTextureAvailable width=" + width + ", height=" + height);
//        mTextureView.setAlpha(0.5f);
//        mTextureView.setRotation(90.0f);

        mCamera = Camera.open(0);
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(0, cameraInfo);
        Camera.Parameters parameters = mCamera.getParameters();
        mWidth = parameters.getPreviewSize().width;
        mHeight = parameters.getPreviewSize().height;
        //当有旋转角度时需要将图片的宽高互换
        if (cameraInfo.orientation == 90 || cameraInfo.orientation == 270) {
            int tempH = mHeight;
            mHeight = mWidth;
            mWidth = tempH;
        }

        try {
            mCamera.setPreviewTexture(surface);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException ioe) {
            // Something bad happened
        }
        Log.i(TAG, "onSurfaceTextureAvailable: mWidth=" + mWidth + ", mHeight=" + mHeight);
        captureBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.i(TAG, "onSurfaceTextureSizeChanged");

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        Log.i(TAG, "onSurfaceTextureDestroyed");
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
//        Log.i(TAG, "onSurfaceTextureUpdated");
        if (firstCapture) {
            firstCapture = false;
            mTextureView.getBitmap(captureBitmap); //获取到渲染在textureView上面的图像
            FileUtils.writeFile(Bitmap2Bytes(captureBitmap), Environment.getExternalStorageDirectory().toString(), "texture.argb");
        }

    }

    public byte[] Bitmap2Bytes(Bitmap bitmap) {
        int bytes = bitmap.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
        return buffer.array();
    }
}