package com.mykobob.flyerstoevents;

import com.mykobob.flyerstoevents.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ParseInfoActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private String tag = "flyerstoevents";

    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    private File imageFile;
    private Uri uri;

    private Bitmap pic;

    private OCROperations operate;
    private List<Event> events;

    private ProgressBar progress;
    private TextView processing;

    private String DATA_PATH = Environment.getExternalStorageDirectory() + "/flyerstoevents/";
    private String language = "eng";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_parse_info);

        operate = new OCROperations();
        progress = (ProgressBar) findViewById(R.id.progressBar);
        processing = (TextView) findViewById(R.id.process_str);
        events = new ArrayList<>();

        final Button anotherPic = (Button) findViewById(R.id.anotherPic);
        anotherPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCameraWithInfo();
            }
        });

        createEnvironment();

        launchCameraWithInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progress.setVisibility(View.GONE);
        processing.setVisibility(View.GONE);
    }

    private void createEnvironment() {
        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.e(tag, "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {
                    Log.i(tag, "Created directory " + path + " on sdcard");
                }
            }
        }

        String suffix = "tessdata/" + language + ".traineddata";
        File trainedData = new File(DATA_PATH + suffix);
        if(!trainedData.exists()) {
            try {
                Log.i(tag, "before the load stuff");
                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open(suffix);
                OutputStream out = new FileOutputStream(DATA_PATH + suffix);

                byte[] buf = new byte[1024];
                int len;
                while((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                Log.i(tag, "after the load stuff");

                in.close();
                out.close();

                Log.i(tag, "Copied " + language + " complete");

            } catch (IOException e) {
                Log.e(tag, "couldn't copy " + language + ". Error is " + e.toString());
            }
        }

    }

    public void process(View view) {
        if(pic != null) {
            progress.setVisibility(View.VISIBLE);
            processing.setVisibility(View.VISIBLE);
            final boolean[] finished = new boolean[1];
            Thread t = new Thread() {
                @Override
                public void run() {
                    operate.setInfo(pic);
                    events = operate.getAllEvents(operate.getText());
                    Log.i(tag, "finished reading");
                    finished[0] = true;
                    showData();
                }
            };

            t.start();

        } else {
            Toast.makeText(ParseInfoActivity.this, "Take a picture first!", Toast.LENGTH_LONG).show();
        }

    }

    private void showData() {
        Intent showData = new Intent(this, DisplayText.class);
        showData.putExtra("OCR", events.get(0));

//        progress.setVisibility(View.GONE);
//        processing.setVisibility(View.GONE);

        startActivity(showData);
    }

    private void makeUpright() {

        try {
            ExifInterface exif = new ExifInterface(uri.getPath());
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotate = 0;
            int height = pic.getHeight(), width = pic.getWidth();
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    height = pic.getWidth();
                    width = pic.getHeight();
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    height = pic.getHeight();
                    width = pic.getWidth();
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    height = pic.getWidth();
                    width = pic.getHeight();
                    break;
            }

            if(rotate != 0) {
                Matrix mat = new Matrix();
                mat.postRotate(rotate);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(pic, height, width, true);
                Bitmap rotateBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), mat, true);
                pic = rotateBitmap;
            }



        } catch (IOException e) {
            Log.e(tag, "Invalid uri path: " + uri.getPath());
        }
    }

    private void launchCameraWithInfo() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = createImageFile(MEDIA_TYPE_IMAGE);
        uri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode != CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
//            Toast.makeText(this, "Good Pic at " + data.getData(), Toast.LENGTH_LONG).show();
//            pic = (Bitmap) data.getExtras().get("data");
//            deletePicTaken();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 8;
            pic = BitmapFactory.decodeFile(uri.getPath(), options);
            makeUpright();
            displayBitmap();


        } else if(resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Must take a picture!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Must take a picture!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void displayBitmap() {
        /* Set the imageView to the bitmap */
        ImageView showImage = (ImageView) findViewById(R.id.fullscreen_content);
        if(showImage != null) {
            showImage.setImageBitmap(pic);
        } else {
            Log.e(tag, "ImageView doesn't exist");
        }
    }

    // TODO improve this
    private void deletePicTaken(){
        final String[] imageColumns = { BaseColumns._ID, MediaStore.MediaColumns.DATA };
        final String imageOrderBy = BaseColumns._ID + " DESC";
        Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, imageOrderBy);
        if(imageCursor.moveToFirst()){
            //int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
            String fullPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            //imageCursor.close();
            File file = new File(fullPath);
            Toast.makeText(this, file.getPath(), Toast.LENGTH_LONG).show();
            Log.i(tag, "image is here " + file.getPath());
            boolean deleted = file.delete();
            if(deleted) {
                Log.i(tag, "image deleted");
            }
        }


    }

    private File createImageFile(int type) {

        if(type != MEDIA_TYPE_IMAGE) {
            return null;
        }

        File folder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Flyers To Events");

        if(!folder.exists()) {
            if(!folder.mkdirs()) {
                Log.e(tag, "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        mediaFile = new File(folder.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        return mediaFile;

    }
}
