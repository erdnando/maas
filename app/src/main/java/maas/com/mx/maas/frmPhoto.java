package maas.com.mx.maas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import maas.com.mx.maas.R;
import maas.com.mx.maas.entidades.SolicitudType;
import maas.com.mx.maas.negocio.Negocio;

public class frmPhoto extends Activity {

    String imageName;
    String strPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmphoto);

        this.imageName= getIntent().getStringExtra("imageName");
        this.strPath= getIntent().getStringExtra("strPath");

        File imgFile = new File(strPath);
        Bitmap myBitmap =null;


        try {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.imageViewTest);
            /*
            //-------------------------------
            // Get the dimensions of the View
            int targetW = myImage.getWidth();
            int targetH = myImage.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imgFile.getAbsolutePath(), bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), bmOptions);
            myImage.setImageBitmap(bitmap);
            //-------------------------------
            */

            myImage.setImageBitmap(myBitmap);
        }catch (Exception ex){
            String h="";
        }





    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.frm_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        objSol=getLastVersion(objSol);
        this.objSolicitud = gson.toJson(objSol);
        editor.putString("objSolicitud", this.objSolicitud);
        editor.apply();*/
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
