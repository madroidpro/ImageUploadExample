package madroid.imageuploadexample.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import madroid.imageuploadexample.R;
import madroid.imageuploadexample.rest.ApiClient;
import madroid.imageuploadexample.rest.ApiInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int TAKE_PICTURE = 1;
    private static Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button uploadBtn=(Button)findViewById(R.id.uploadButton);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadimage();
            }
        });

        Button openCamera = (Button)findViewById(R.id.cameraButton);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraOperation();
            }
        });
    }

    private void openCameraOperation() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       // File pic = new File(Environment.getExternalStorageDirectory(),"testpic.jpg");
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pic));
        //imageUri=Uri.fromFile(pic);
        if(intent.resolveActivity(getPackageManager())!=null){

            File pic = null;
            try {
                pic = createFile();
            }catch (Exception e){

            }
            if(pic != null){
                imageUri = Uri.fromFile(pic);
               // imageUri = FileProvider.getUriForFile(this,getCallingPackage(),pic);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
               startActivityForResult(intent,TAKE_PICTURE);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK){

//            ImageView img = (ImageView)findViewById(R.id.imageThumbnail);
//            img.setImageBitmap();
        }
    }

    private File createFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imageFileName,".jpg",storageDir);
        return image;

    }

    private void uploadimage() {

        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        File file = new File(imageUri.getPath());
        Log.d("imgfile",imageUri.getPath());
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
       Call<ResponseBody>call = apiInterface.uploadfile(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("imgInfo",response.raw()+"");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("imgInfofailed", t+"");
            }
        });
    }
}
