package com.assignment.canvas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment.canvas.models.Position;
import com.assignment.canvas.util.NetworkManager;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.Query;
import com.kinvey.java.core.DownloaderProgressListener;
import com.kinvey.java.core.MediaHttpDownloader;
import com.kinvey.java.model.FileMetaData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final String LAYOUT_FILENAME = "layout_file.png";
    DrawView drawView;
    FrameLayout frameLayout;
    ImageView imgvw;
    Client mKinveyClient;
    TextView txtvwStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        imgvw = (ImageView) findViewById(R.id.imgvw);
        txtvwStatus = (TextView) findViewById(R.id.status);
        mKinveyClient = MyApplication.getClient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        downloadFile();
    }

    private void downloadFile() {

        if(!NetworkManager.nManager.checkAndShowNetworkAlert(this))
        {
            return;
        }
        FileOutputStream fos= null;

        File file = new File(getFilesDir(), LAYOUT_FILENAME);
        if(file.exists())
        {
            loadLayoutFile();
            return;
        }
        try {
            fos = openFileOutput(LAYOUT_FILENAME, Context.MODE_PRIVATE);
            mKinveyClient.file().download(new FileMetaData("9f6bace2-8369-4871-a6ec-8df43289695f"), fos, new DownloaderProgressListener() {
                @Override
                public void onSuccess(Void result) {
                    Log.i(TAG, "File download succeeded " + result);
                    loadLayoutFile();
                }
                @Override
                public void onFailure(Throwable error) {
                    Log.e(TAG, "File download failed.", error);
                }
                @Override
                public void progressChanged(final MediaHttpDownloader downloader) throws IOException {
                    Log.i(TAG, "progress updated: "+downloader.getDownloadState());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtvwStatus.setText("Status : "+downloader.getDownloadState());
                        }
                    });
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void loadLayoutFile() {
        File file = new File(getFilesDir(), LAYOUT_FILENAME);
        if(file.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if(myBitmap != null) {
                imgvw.setImageBitmap(myBitmap);
                int width = myBitmap.getWidth();
                int height = myBitmap.getHeight();
                Log.i(TAG, "Width : " + width + ", Height : " + height);
                if(!NetworkManager.nManager.checkAndShowNetworkAlert(this))
                {
                    return;
                }
                loadPositionsData();
            }
        }
        else
        {
            Toast.makeText(MainActivity.this, "File doesn't exist", Toast.LENGTH_LONG).show();
        }
    }

    private void loadPositionsData() {

        final AsyncAppData<Position> position = MyApplication.getClient().appData("Position", Position.class);
        Query q = MyApplication.getClient().query();
        q.equals("fileId","9f6bace2-8369-4871-a6ec-8df43289695f");

        position.get(q, new KinveyListCallback<Position>() {
            @Override
            public void onSuccess(Position[] positions) {
                if(drawView == null){
                    drawView = new DrawView(MainActivity.this);
                    drawView.setLayoutParams(new FrameLayout.LayoutParams(imgvw.getWidth(), imgvw.getHeight()));
                    frameLayout.addView(drawView);
                }
                drawView.setPositions(positions);
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

    }

    public Bitmap getBitmapOfView()
    {
        frameLayout.setDrawingCacheEnabled(true);
        Bitmap bmp = frameLayout.getDrawingCache();
        return bmp;
    }

    public void createImageFromBitmap(Bitmap bmp)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        File file = new File( Environment.getExternalStorageDirectory() +
                "/capturedscreen.jpg");
        try
        {
            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            ostream.write(bytes.toByteArray());
            ostream.close();

            openFile(file);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void openFile(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
        case R.id.action_logout :
            mKinveyClient.user().logout().execute();
            finish();
            break;
        case R.id.action_export :
            createImageFromBitmap(getBitmapOfView());
            break;
            case R.id.action_refresh :
                downloadFile();
                break;
        }
        return true;
    }

}
