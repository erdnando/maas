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

   /* private static final int CAMERA_REQUEST = 1888;

    SharedPreferences preferences=null;
    SolicitudType objSol=null;
    String objSolicitud="";
    String idSolicitud="0";


    static String str_Camera_Photo_ImagePath = "";
    private static File f;
    private static int Take_Photo = 2;
    private static String str_randomnumber = "";
    static String str_Camera_Photo_ImageName = "";
    public static String str_SaveFolderName;
    private static File path;
    Bitmap bitmap;
    int storeposition = 0;
    public static GridView gridview;
    public static ImageView imageView;
    ImageView imageView1;
    Bitmap myBitmap;*/
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
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());//imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.imageViewTest);

            myImage.setImageBitmap(myBitmap);
        }catch (Exception ex){
            String h="";
        }
        //File imgFile = new File("/data/data/maas.com.mx.maas/files/"+this.imageName);

        //File imgFile = new File(getApplicationContext().getFilesDir().getPath().toString(),this.imageName);

        /*try {
            Runtime.getRuntime().exec("chmod 777 " + imgFile.getAbsolutePath());
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }*/


            //Uri newImageUri = null;
            //newImageUri = Uri.fromFile(imgFile);

            //imgFile.setReadOnly();




        /*imageView1 = (ImageView) findViewById(R.id.imageViewTest);
        preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);

        try{
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

            Gson gson = new Gson();
            objSol=gson.fromJson(objSolicitud, SolicitudType.class);

            //-------------------------------------------------
            this.imageView = (ImageView)this.findViewById(R.id.imageViewTest);
            Button photoButton = (Button) this.findViewById(R.id.button1);
            photoButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                  String pathTaget=getApplicationContext().getFilesDir().getPath().toString();
                    String imageName="TEC_" + System.currentTimeMillis() + ".jpg";

                    Uri newImageUri = null;
                    path = new File(pathTaget);

                    if (!path.exists())
                        path.mkdirs();

                    boolean setWritable = false;

                    setWritable = path.setWritable(true, false);

                    File file = new File(path, imageName);
                    newImageUri = Uri.fromFile(file);

                    str_Camera_Photo_ImagePath = pathTaget + "/" + imageName;

                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, newImageUri);

                    startActivityForResult(intent, Take_Photo);
                }
            });
            //-----------------------------------------------------




        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }*/
    }

/*
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Take_Photo) {
            String filePath = null;

            filePath = str_Camera_Photo_ImagePath;

            File imgFile = new File(str_Camera_Photo_ImagePath);

            if(imgFile.exists()){

                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                //ImageView imageView1 = (ImageView) findViewById(R.id.imageViewTest);
                imageView1.setImageBitmap(myBitmap);

            }


        }
    }*/

/*
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

    // used to create randon numbers
    public String nextSessionId() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

*/
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
