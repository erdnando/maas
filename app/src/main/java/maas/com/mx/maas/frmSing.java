package maas.com.mx.maas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import maas.com.mx.maas.R;
import maas.com.mx.maas.entidades.DrawView;
import maas.com.mx.maas.entidades.MyDrawView;

public class frmSing extends Activity {

    MyDrawView drawView;
    SharedPreferences preferences=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmsing);

        drawView=new MyDrawView(this);
        drawView.setBackgroundColor(Color.WHITE);
        drawView.requestFocus();

        TableLayout parent = (TableLayout) findViewById(R.id.signImageParent);
        drawView = new MyDrawView(this);
        parent.addView(drawView);
    }

    public void saveFirma(View v){
        TableLayout parent = (TableLayout) findViewById(R.id.signImageParent);

        parent.setDrawingCacheEnabled(true);
        Bitmap b = parent.getDrawingCache();

        FileOutputStream fos = null;
        try {
            File file=createImageFile();
            fos = new FileOutputStream(file);

            b.compress(Bitmap.CompressFormat.PNG, 95, fos);

            preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("imagenFirma", file.getName());
            editor.putString("imagenFirmaPath", file.getAbsolutePath());
            editor.apply();


            Intent myIntent = new Intent(frmSing.this, frmDocumentos.class);

            startActivity(myIntent);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void clear(View v){
        TableLayout parent = (TableLayout) findViewById(R.id.signImageParent);
        parent.removeAllViews();

        drawView=new MyDrawView(this);
        drawView.setBackgroundColor(Color.WHITE);
        drawView.requestFocus();

        drawView = new MyDrawView(this);
        parent.addView(drawView);
    }

    public void cancel(View v){
        Intent myIntent = new Intent(frmSing.this, frmDocumentos.class);

        startActivity(myIntent);
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        String imageFileName = "TEC_" ;//+ timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        //tempfile asign unique name to file by self
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.frm_sing, menu);
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
}
