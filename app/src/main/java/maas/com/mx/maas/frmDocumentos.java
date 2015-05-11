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
import java.io.FileOutputStream;
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

            Button btnFirmaX = (Button) findViewById(R.id.btnFirmaX);
            btnFirmaX.setText(preferences.getString("imagenFirma", "").toString());
            btnFirmaX.setTag(preferences.getString("imagenFirmaPath", "").toString());
            //preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);

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


    public void clickFirma(View v) {

        Intent myIntent = new Intent(frmDocumentos.this, frmSing.class);
       /* myIntent.putExtra("User",this.user);
        myIntent.putExtra("Compania",this.compania);
        myIntent.putExtra("Logeado",this.logeado);
        myIntent.putExtra("Tipo_Usuario",this.tipousuario);*/

        startActivityForResult(myIntent,1);
    }

    public void clickPhoto(View v) {
        int btnInvocador=0;
        switch(v.getId()) {
            case R.id.btnCamIF:
                btnInvocador=101;
                break;
            case R.id.btnCamIA:
                btnInvocador=102;
                break;
            case R.id.btnCamP1:
                btnInvocador=103;
                break;
            case R.id.btnCamP2:
                btnInvocador=104;
                break;
            case R.id.extraCam1:
                btnInvocador=105;
                break;
            case R.id.extraCam2:
                btnInvocador=106;
                break;
            case R.id.extraCam3:
                btnInvocador=107;
                break;
            case R.id.extraCam4:
                btnInvocador=108;
                break;
            case R.id.extraCam5:
                btnInvocador=109;
                break;
        }


        File photoFile = null;
        try {
                photoFile = createImageFile();

                Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (photoFile != null) {
                    imageName=photoFile.getName();
                    mCurrentPhotoPath=photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, btnInvocador);
                }
        } catch (IOException ex) {
            // Error occurred while creating the File
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {

            Button btnFirmaX = (Button) findViewById(R.id.btnFirmaX);
            btnFirmaX.setText(imageName);
            btnFirmaX.setTag(mCurrentPhotoPath);

        }else if (requestCode >100 && resultCode == RESULT_OK) {

            switch (requestCode){
                case 101:
                    Button btnIdentificacion1 = (Button) findViewById(R.id.btnIdentificacion1);
                    btnIdentificacion1.setText(imageName);
                    btnIdentificacion1.setTag(mCurrentPhotoPath);
                    break;
                case 102:
                    Button btnIdentificacion2 = (Button) findViewById(R.id.btnIdentificacion2);
                    btnIdentificacion2.setText(imageName);
                    btnIdentificacion2.setTag(mCurrentPhotoPath);
                    break;
                case 103:
                    Button btnContrato1 = (Button) findViewById(R.id.btnContrato1);
                    btnContrato1.setText(imageName);
                    btnContrato1.setTag(mCurrentPhotoPath);
                    break;
                case 104:
                    Button btnContrato2 = (Button) findViewById(R.id.btnContrato2);
                    btnContrato2.setText(imageName);
                    btnContrato2.setTag(mCurrentPhotoPath);
                    break;
                case 105:
                    Button extraPath1 = (Button) findViewById(R.id.extraPath1);
                    extraPath1.setText(imageName);
                    extraPath1.setTag(mCurrentPhotoPath);
                    break;
                case 106:
                    Button extraPath2 = (Button) findViewById(R.id.extraPath2);
                    extraPath2.setText(imageName);
                    extraPath2.setTag(mCurrentPhotoPath);
                    break;
                case 107:
                    Button extraPath3 = (Button) findViewById(R.id.extraPath3);
                    extraPath3.setText(imageName);
                    extraPath3.setTag(mCurrentPhotoPath);
                    break;
                case 108:
                    Button extraPath4 = (Button) findViewById(R.id.extraPath4);
                    extraPath4.setText(imageName);
                    extraPath4.setTag(mCurrentPhotoPath);
                    break;
                case 109:
                    Button extraPath5 = (Button) findViewById(R.id.extraPath5);
                    extraPath5.setText(imageName);
                    extraPath5.setTag(mCurrentPhotoPath);
                    break;
            }

           try {
                Bitmap b = BitmapFactory.decodeFile(mCurrentPhotoPath);
                File imagefile = new File(mCurrentPhotoPath);
                FileOutputStream fos = null;
                fos = new FileOutputStream(imagefile);

                b.compress(Bitmap.CompressFormat.JPEG, 85, fos);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }



        }
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

      /*  Bitmap b = BitmapFactory.decodeFile(image.getAbsolutePath());
        FileOutputStream fos = null;
        fos = new FileOutputStream(image);
        b.compress(Bitmap.CompressFormat.PNG, 95, fos);*/

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    public void clickBtnShowImage(View v) {

        String strImage=".............";
        String strPath="";

        switch(v.getId()) {
            case R.id.btnIdentificacion1:
                Button btnIdentificacion1 = (Button) findViewById(R.id.btnIdentificacion1);
                strImage=btnIdentificacion1.getText().toString();
                strPath=btnIdentificacion1.getTag()==null?"":btnIdentificacion1.getTag().toString();
                break;
            case R.id.btnIdentificacion2:
                Button btnIdentificacion2 = (Button) findViewById(R.id.btnIdentificacion2);
                strImage=btnIdentificacion2.getText().toString();
                strPath=btnIdentificacion2.getTag()==null?"":btnIdentificacion2.getTag().toString();
                break;
            case R.id.btnContrato1:
                Button btnContrato1 = (Button) findViewById(R.id.btnContrato1);
                strImage=btnContrato1.getText().toString();
                strPath=btnContrato1.getTag()==null?"":btnContrato1.getTag().toString();
                break;
            case R.id.btnContrato2:
                Button btnContrato2 = (Button) findViewById(R.id.btnContrato2);
                strImage=btnContrato2.getText().toString();
                strPath=btnContrato2.getTag()==null?"":btnContrato2.getTag().toString();
                break;
            case R.id.extraPath1:
                Button extraPath1 = (Button) findViewById(R.id.extraPath1);
                strImage=extraPath1.getText().toString();
                strPath=extraPath1.getTag()==null?"":extraPath1.getTag().toString();
                break;
            case R.id.extraPath2:
                Button extraPath2 = (Button) findViewById(R.id.extraPath2);
                strImage=extraPath2.getText().toString();
                strPath=extraPath2.getTag()==null?"":extraPath2.getTag().toString();
                break;
            case R.id.extraPath3:
                Button extraPath3 = (Button) findViewById(R.id.extraPath3);
                strImage=extraPath3.getText().toString();
                strPath=extraPath3.getTag()==null?"":extraPath3.getTag().toString();
                break;
            case R.id.extraPath4:
                Button extraPath4 = (Button) findViewById(R.id.extraPath4);
                strImage=extraPath4.getText().toString();
                strPath=extraPath4.getTag()==null?"":extraPath4.getTag().toString();
                break;
            case R.id.extraPath5:
                Button extraPath5 = (Button) findViewById(R.id.extraPath5);
                strImage=extraPath5.getText().toString();
                strPath=extraPath5.getTag()==null?"":extraPath5.getTag().toString();
                break;
            case R.id.btnFirmaX:
                Button btnFirmaX = (Button) findViewById(R.id.btnFirmaX);
                strImage=btnFirmaX.getText().toString();
                strPath=btnFirmaX.getTag()==null?"":btnFirmaX.getTag().toString();
                break;
        }


              if(!strImage.toString().startsWith("...")){
                Intent myIntent = new Intent(frmDocumentos.this, frmPhoto.class);
                myIntent.putExtra("imageName",strImage);
                myIntent.putExtra("strPath",strPath);

                startActivity(myIntent);
              }

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
