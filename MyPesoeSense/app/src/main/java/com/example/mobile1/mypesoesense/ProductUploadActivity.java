package com.example.mobile1.mypesoesense;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ProductUploadActivity extends ActionBarActivity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 150;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String IMAGE_DIR_NAME = "Peso Sense";

    private Uri fileUri;
    private LinearLayout container;
    private ImageView imgPreview;
    private Dialog dialog;
    private EditText prodName;  private TextView lblProdName;
    private EditText prodQnty;  private TextView lblProdQnty;
    private EditText prodDesc;  private TextView lblProdDesc;
    private EditText prodBrand; private TextView lblProdBrand;
    private EditText prodPrice; private TextView lblProdPrice;
    private EditText prodAvail; private TextView lblProdAvail;

    private Boolean uploadImageIsEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_upload);

        container = (LinearLayout) findViewById(R.id.layout_root);
        UtilsApp.hideSoftKeyboard(ProductUploadActivity.this, container);

        Button btnUpload = (Button) findViewById(R.id.btn_upload);
        imgPreview = (ImageView) findViewById(R.id.upload_thumbnail);
        imgPreview.setPadding(72, 144, 72, 144);
        uploadImageIsEmpty = true;
        initFields();
        initLabels();
        initSpinners();

        imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddPhotoDialog();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToAPI();
            }
        });
    }

    public void initFields() {
        prodName = (EditText) findViewById(R.id.prod_name);
        prodQnty = (EditText) findViewById(R.id.prod_qnty);
        prodDesc = (EditText) findViewById(R.id.prod_desc);
        prodBrand = (EditText) findViewById(R.id.prod_brand);
        prodPrice = (EditText) findViewById(R.id.prod_price);
        prodAvail = (EditText) findViewById(R.id.prod_avail);

        prodName.setTypeface(UtilsApp.opensansNormal());
        prodQnty.setTypeface(UtilsApp.opensansNormal());
        prodDesc.setTypeface(UtilsApp.opensansNormal());
        prodBrand.setTypeface(UtilsApp.opensansNormal());
        prodPrice.setTypeface(UtilsApp.opensansNormal());
        prodAvail.setTypeface(UtilsApp.opensansNormal());

        prodName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                validView(prodName);
            }
        });

        prodQnty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                validView(prodQnty);
            }
        });

        prodDesc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                validView(prodDesc);
            }
        });
    }

     private void initLabels() {
         lblProdName = (TextView) findViewById(R.id.lbl_prod_name);
         lblProdQnty = (TextView) findViewById(R.id.lbl_prod_qnty);
         lblProdDesc = (TextView) findViewById(R.id.lbl_prod_desc);
         lblProdBrand = (TextView) findViewById(R.id.lbl_prod_brand);
         lblProdPrice = (TextView) findViewById(R.id.lbl_prod_price);
         lblProdAvail = (TextView) findViewById(R.id.lbl_prod_avail);

         lblProdName.setTypeface(UtilsApp.opensansNormal());
         lblProdQnty.setTypeface(UtilsApp.opensansNormal());
         lblProdDesc.setTypeface(UtilsApp.opensansNormal());
         lblProdBrand.setTypeface(UtilsApp.opensansNormal());
         lblProdPrice.setTypeface(UtilsApp.opensansNormal());
         lblProdAvail.setTypeface(UtilsApp.opensansNormal());
     }

    public void initSpinners() {
        Spinner spinnerCurrency = (Spinner) findViewById(R.id.spinner_currency);
        String[] currency = {"PHP", "USD"};
        ArrayAdapter<String> adapterCurrency = new ArrayAdapter<>(this, R.layout.my_spinner, currency);
        spinnerCurrency.setAdapter(adapterCurrency);
    }

    private void openAddPhotoDialog() {
        dialog = new Dialog(ProductUploadActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_photo);
        dialog.show();

        Button btnGallery = (Button) dialog.findViewById(R.id.btn_gallery);
        Button btnCamera = (Button) dialog.findViewById(R.id.btn_camera);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
                dialog.dismiss();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
                dialog.dismiss();
            }
        });

    }

    private boolean isDeviceSupportCamera() {
        // this function is for checking camera availability through java code
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void pickImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GALLERY_IMAGE_REQUEST_CODE);//one can be replaced with any action code
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to capture image", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                previewPickedImage(data);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to capture image", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void previewCapturedImage() {

        try {

            imgPreview.setVisibility(View.VISIBLE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            imgPreview.setImageBitmap(bitmap);
            imgPreview.setPadding(0, 0, 0, 0);
            uploadImageIsEmpty = false;

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

    }

    private void previewPickedImage(Intent data) {

        try {

            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);

            cursor.moveToFirst();

            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            final Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            imgPreview.setVisibility(View.VISIBLE);
            imgPreview.setImageBitmap(bitmap);
            imgPreview.setPadding(0, 0, 0, 0);
            uploadImageIsEmpty = false;

            cursor.close();

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

    }


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
                IMAGE_DIR_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIR_NAME, "Oops! Failed create "
                        + IMAGE_DIR_NAME + " directory");
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


    // API uploading part

    public void uploadToAPI() {
        String pName = prodName.getText().toString();
        String pQnty = prodQnty.getText().toString();
        String pDesc = prodDesc.getText().toString();
        String pBrand = prodBrand.getText().toString();
        String pPrice = prodPrice.getText().toString();
        String pAvail = prodAvail.getText().toString();

        if (uploadImageIsEmpty) {
            Toast.makeText(getApplicationContext(), "Please provide thumbnail", Toast.LENGTH_SHORT).show();
            animateOnError(imgPreview);
        }

        // indicate errors
        if (isEmpty(pName)) {
            showError(prodName);
        } else if (isEmpty(pQnty)) {
            showError(prodQnty);
        } else if (isEmpty(pDesc)) {
            showError(prodDesc);
        } else if (isEmpty(pBrand)) {
            showError(prodBrand);
        } else if (isEmpty(pPrice)) {
            showError(prodPrice);
        } else if (isEmpty(pAvail)) {
            showError(prodAvail);
        }

        if (pName.length() != 0 && pQnty.length() != 0 && pDesc.length() != 0 && !uploadImageIsEmpty) {
            Toast.makeText(getApplicationContext(), "Uploading product to API...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Please provide enough information", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isEmpty(String s) {
        return s.length() == 0;
    }


    public void showError(TextView v) {
        v.setError("Field required");
        v.setBackgroundColor(0xFFFFEB8E);
    }

    public void validView(TextView v) {
        if (v.getText().toString().length() != 0) {
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.fields_bg));
        }
    }

    public void animateOnError(final View v) {
        AnimationSet errorAnimation = new AnimationSet(true);
        RotateAnimation anim2 = new RotateAnimation(10.0f, -10.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim2.setInterpolator(new LinearInterpolator());
        anim2.setRepeatCount(0);
        anim2.setDuration(100);
        anim2.setStartOffset(100);
        errorAnimation.addAnimation(anim2);
        v.startAnimation(errorAnimation);
    }

    public void uploadImageToAPI() {

    }

    public void sendFormToAPI() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_upload, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
