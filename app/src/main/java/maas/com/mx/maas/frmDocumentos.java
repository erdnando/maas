package maas.com.mx.maas;

import android.app.Activity;
import android.app.Service;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import maas.com.mx.maas.R;
import maas.com.mx.maas.entidades.Solicitud;
import maas.com.mx.maas.entidades.SolicitudType;
import maas.com.mx.maas.negocio.Negocio;

public class frmDocumentos extends Activity {
    String idSolicitud="0";
    int total=0;
    SharedPreferences preferences=null;
    SolicitudType objSol=null;
    String objSolicitud="";
    int countExtras=0;

    private static final int CAMERA_REQUEST = 1888;
    private static File path;
    static String str_Camera_Photo_ImagePath = "";
    private static int Take_Photo = 2;
    static String imageName="";
    Bitmap myBitmap;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmdocumentos);
        preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);

        try{
            countExtras=0;

            this.idSolicitud= getIntent().getStringExtra("idSolicitud");
            this.objSolicitud=getIntent().getStringExtra("objSolicitud");

            if(this.idSolicitud==null) {
                this.idSolicitud = preferences.getString("idSolicitud", "");
            }
            if(this.objSolicitud==null) {
                this.objSolicitud = preferences.getString("objSolicitud", "");
            }

            preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);

            Negocio negocio = new Negocio(getApplicationContext());
            //metodos de inicializacion
            //cargaCatalogos();
            configuraMasMenos();
            Gson gson = new Gson();
            objSol=gson.fromJson(objSolicitud, SolicitudType.class);
            //recrea solicitud
            cargaFormulario(objSol);
            validaEstatus();

            if(!this.idSolicitud.toString().equals("0")){
                getActionBar().setTitle(this.idSolicitud.toString());
            }

        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void configuraMasMenos() {
        ImageButton btnAgregar = (ImageButton) findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarExtrasiguiente(countExtras);
            }
        });

        ImageButton btnQuitar = (ImageButton) findViewById(R.id.btnQuitar);
        btnQuitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar(countExtras);
            }
        });
    }

    private void quitar(int count) {
        if (count < 1) return;

        TableRow tableRowExt1 = (TableRow) findViewById(R.id.tableRow7);
        TableRow tableRowExt2 = (TableRow) findViewById(R.id.tableRow8);
        TableRow tableRowExt3 = (TableRow) findViewById(R.id.tableRow9);
        TableRow tableRowExt4 = (TableRow) findViewById(R.id.tableRow10);
        TableRow tableRowExt5 = (TableRow) findViewById(R.id.tableRow11);

        switch(count){
            case 1:tableRowExt1.setVisibility(View.INVISIBLE); break;
            case 2:tableRowExt2.setVisibility(View.INVISIBLE); break;
            case 3:tableRowExt3.setVisibility(View.INVISIBLE); break;
            case 4:tableRowExt4.setVisibility(View.INVISIBLE); break;
            case 5:tableRowExt5.setVisibility(View.INVISIBLE); break;
        }

        countExtras--;


    }
    private void mostrarExtrasiguiente(int count) {
        if (count >= 5) return;

        TableRow tableRowExt1 = (TableRow) findViewById(R.id.tableRow7);
        TableRow tableRowExt2 = (TableRow) findViewById(R.id.tableRow8);
        TableRow tableRowExt3 = (TableRow) findViewById(R.id.tableRow9);
        TableRow tableRowExt4 = (TableRow) findViewById(R.id.tableRow10);
        TableRow tableRowExt5 = (TableRow) findViewById(R.id.tableRow11);

        switch(count){
            case 0:tableRowExt1.setVisibility(View.VISIBLE); break;
            case 1:tableRowExt2.setVisibility(View.VISIBLE); break;
            case 2:tableRowExt3.setVisibility(View.VISIBLE); break;
            case 3:tableRowExt4.setVisibility(View.VISIBLE); break;
            case 4:tableRowExt5.setVisibility(View.VISIBLE); break;
        }

        countExtras++;
    }

    private void cargaFormulario(SolicitudType objSol) {

    }

    private void validaEstatus() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.frm_datos_eco, menu);
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

    public void hideSoftKeyboard(View view) {
        if(view != null)
        {
            try {
                InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
        {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    private int getPointAvance(String valor) {

        if(valor.trim().length()>0){
            return 1;
        }
        else{
            return 0;
        }


    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        objSol=getLastVersion(objSol);
        this.objSolicitud = gson.toJson(objSol);
        editor.putString("objSolicitud", this.objSolicitud);
        editor.apply();
    }

    private SolicitudType getLastVersion(SolicitudType objSol) {

        return objSol;
    }

    public void clickPhoto(View view) {

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }

        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoFile != null) {
            imageName=photoFile.getName();
            mCurrentPhotoPath=photoFile.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
            startActivityForResult(takePictureIntent, Take_Photo);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Take_Photo && resultCode == RESULT_OK) {

            Button btnIdentificacion1 = (Button) findViewById(R.id.btnIdentificacion1);
            btnIdentificacion1.setText(imageName);
            btnIdentificacion1.setTag(mCurrentPhotoPath);


        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEC_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    public void clickBtnIdentificacion1(View view) {

        Button btnIdentificacion1 = (Button) findViewById(R.id.btnIdentificacion1);
        String strImage=btnIdentificacion1.getText().toString();
        String strPath=btnIdentificacion1.getTag().toString();

        Intent myIntent = new Intent(frmDocumentos.this, frmPhoto.class);
        myIntent.putExtra("imageName",strImage);
        myIntent.putExtra("strPath",strPath);

        startActivity(myIntent);

    }

    public static Bitmap new_decode(File f) {

        // decode image size

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        o.inDither = false; // Disable Dithering mode

        o.inPurgeable = true; // Tell to gc that whether it needs free memory,
        // the Bitmap can be cleared

        o.inInputShareable = true; // Which kind of reference will be used to
        // recover the Bitmap data after being
        // clear, when it will be used in the future
        try {


            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // Find the correct scale value. It should be the power of 2.
        final int REQUIRED_SIZE = 300;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 1.5 < REQUIRED_SIZE && height_tmp / 1.5 < REQUIRED_SIZE)
                break;
            width_tmp /= 1.5;
            height_tmp /= 1.5;
            scale *= 1.5;
        }

        // decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        // o2.inSampleSize=scale;
        o.inDither = false; // Disable Dithering mode

        o.inPurgeable = true; // Tell to gc that whether it needs free memory,
        // the Bitmap can be cleared

        o.inInputShareable = true; // Which kind of reference will be used to
        // recover the Bitmap data after being
        // clear, when it will be used in the future
        // return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        try {

//          return BitmapFactory.decodeStream(new FileInputStream(f), null,
//                  null);
            Bitmap bitmap= BitmapFactory.decodeStream(new FileInputStream(f), null, null);
            System.out.println(" IW " + width_tmp);
            System.out.println("IHH " + height_tmp);
            int iW = width_tmp;
            int iH = height_tmp;

            return Bitmap.createScaledBitmap(bitmap, iW, iH, true);

        } catch (OutOfMemoryError e) {
            // TODO: handle exception
            e.printStackTrace();
            // clearCache();

            // System.out.println("bitmap creating success");
            System.gc();
            return null;
            // System.runFinalization();
            // Runtime.getRuntime().gc();
            // System.gc();
            // decodeFile(f);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

}
