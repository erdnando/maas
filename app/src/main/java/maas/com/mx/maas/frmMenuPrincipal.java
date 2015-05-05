package maas.com.mx.maas;

import maas.com.mx.maas.entidades.Solicitud;
import maas.com.mx.maas.negocio.Negocio;
import maas.com.mx.maas.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;

import maas.com.mx.maas.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class frmMenuPrincipal extends Activity {

    String user="";
    String compania= "";
    String logeado= "";
    String tipousuario= "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.frmmenuprincipal);

        try{
            //Intent i= getIntent();
            this.user= getIntent().getStringExtra("User");
            this.compania= getIntent().getStringExtra("Compania");
            this.logeado= getIntent().getStringExtra("Logeado");
            this.tipousuario= getIntent().getStringExtra("Tipo_Usuario");


            TextView txtCountSolicitudes=(TextView)findViewById(R.id.txtCountSolicitudes);
            Negocio negocio = new Negocio(getApplicationContext());//

            txtCountSolicitudes.setText((negocio.GetMetricaEstatus("1,2,3,5,6,7")));
        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.frm_solicitudes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /* id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);*/
        return true;
    }

    public void btnBandeja(View view) {

        Intent myIntent = new Intent(frmMenuPrincipal.this, frmBandeja.class);
        myIntent.putExtra("User",this.user);
        myIntent.putExtra("Compania",this.compania);
        myIntent.putExtra("Logeado",this.logeado);
        myIntent.putExtra("Tipo_Usuario",this.tipousuario);

        startActivity(myIntent);
        //startActivity(new Intent(frmMenuPrincipal.this, frmBandeja.class));
    }

    public void btnNewsolicitud(View view) {
        Intent myIntent = new Intent(frmMenuPrincipal.this, frmNuevaSolicitud.class);
        myIntent.putExtra("idSolicitud","0");


        SharedPreferences preferencesx = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = preferencesx.edit();
        editor.putString("idSolicitud","0");

        Gson gson = new Gson();
        Solicitud objSol=new Solicitud();
        objSol.Nombre="Erdnando";
        String strObjSol = gson.toJson(objSol);

        editor.putString("strObjSol", strObjSol);

        editor.apply();

        startActivity(myIntent);

       // startActivity(new Intent(frmMenuPrincipal.this, frmNuevaSolicitud.class));
    }

    public void btnSalir(View view) {
        Intent intent = new Intent(this, main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit me", true);
        startActivity(intent);
        finish();
    }

    public void btnConfiguracion(View view) {

        Intent myIntent = new Intent(frmMenuPrincipal.this, frmConfiguracion.class);
        myIntent.putExtra("User",this.user);
        myIntent.putExtra("Compania",this.compania);
        myIntent.putExtra("Logeado",this.logeado);
        myIntent.putExtra("Tipo_Usuario",this.tipousuario);

        startActivity(myIntent);

        //startActivity(new Intent(frmMenuPrincipal.this, frmConfiguracion.class));
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
}
