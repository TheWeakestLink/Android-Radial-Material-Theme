package com.example.mobile1.mypesoesense;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mobile1 on 7/16/15.
 */
public class PesoSenseCamera {

    // Camera request codes
    public static final int CAPTURE_IMAGE = 100;
    public static final int GALLERY_IMAGE = 150;
    public static final int CAPTURE_VIDEO = 200;
    public static final int GALLERY_VIDEO = 250;

    // Media types to be returned
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // Dynamic literals
    private static String DIR_NAME;

    // file saving
    private Uri fileUri;

    public void PesoSenseCamera(String dir) {
        //this.DIR_NAME = dir;
    }

    public void test() {
        Log.d("Pese Sense", "Try if this works");
    }

    public void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        //startActivityForResult(intent, CAPTURE_IMAGE);
    }

    public void pickImage() {

    }

    public void captureImagePreview(Intent data, ImageView imgPreview) {

    }

    public void pickImagePreview(Intent data, ImageView imgPreview) {

    }

    // Helper methods

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                DIR_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(DIR_NAME, "Oops! Failed create "
                        + DIR_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".png");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}
