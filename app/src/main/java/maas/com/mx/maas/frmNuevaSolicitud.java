package maas.com.mx.maas;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import maas.com.mx.maas.R;
import maas.com.mx.maas.entidades.Solicitud;
import maas.com.mx.maas.entidades.SolicitudType;
import maas.com.mx.maas.negocio.Negocio;

public class frmNuevaSolicitud extends Activity {

    SharedPreferences preferences=null;
    SolicitudType objSol=null;
    String idSolicitud="0";
    String objSolicitud="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmnuevasolicitud);
        preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);


        try{
            Negocio negocio = new Negocio(getApplicationContext());
            this.idSolicitud= getIntent().getStringExtra("idSolicitud");


            if(this.idSolicitud==null) {
                this.idSolicitud = preferences.getString("idSolicitud", "");
            }

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("idSolicitud", this.idSolicitud);
            editor.apply();

            Gson gson = new Gson();
            if(!this.idSolicitud.toString().equals("0")) {


                getActionBar().setTitle(this.idSolicitud.toString());

                this.objSolicitud=preferences.getString("objSolicitud", "");

                if(this.objSolicitud==""){
                //get from db .......
                objSol=negocio.getSolicitud(this.idSolicitud);

                this.objSolicitud = gson.toJson(objSol);
                editor.putString("objSolicitud", this.objSolicitud);
                editor.apply();
                }

            }else{
                this.objSolicitud = preferences.getString("objSolicitud", "");
                if(this.objSolicitud==""){
                    objSol=new SolicitudType();
                    this.objSolicitud = gson.toJson(objSol);

                    editor.putString("objSolicitud", this.objSolicitud);
                    editor.apply();
                }else{
                    objSol=gson.fromJson(this.objSolicitud, SolicitudType.class);
                    String h="";
                }


            }

        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.frm_nueva_solicitud, menu);

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

    public void clickGuardar(View view) {
    }

    public void clickLimpiar(View view) {
    }

    public void navegaDocumentos(View view) {
        Intent myIntent = new Intent(frmNuevaSolicitud.this, frmDocumentos.class);
        myIntent.putExtra("idSolicitud",this.idSolicitud);
        myIntent.putExtra("objSolicitud",this.objSolicitud);

        startActivity(myIntent);
    }

    public void navegaRefFamiliares(View view) {
        Intent myIntent = new Intent(frmNuevaSolicitud.this, frmRefFamiliares.class);
        myIntent.putExtra("idSolicitud",this.idSolicitud);
        myIntent.putExtra("objSolicitud",this.objSolicitud);

        startActivity(myIntent);
    }

    public void navegaPersonaPol(View view) {
        Intent myIntent = new Intent(frmNuevaSolicitud.this, frmPersonaPol.class);
        myIntent.putExtra("idSolicitud",this.idSolicitud);
        myIntent.putExtra("objSolicitud",this.objSolicitud);

        startActivity(myIntent);
    }

    public void navegaDatosEco(View view) {
        Intent myIntent = new Intent(frmNuevaSolicitud.this, frmDatosEco.class);
        myIntent.putExtra("idSolicitud",this.idSolicitud);
        myIntent.putExtra("objSolicitud",this.objSolicitud);

        startActivity(myIntent);
    }

    public void navegaDomicilio(View view) {
        Intent myIntent = new Intent(frmNuevaSolicitud.this, frmDomicilio.class);
        myIntent.putExtra("idSolicitud",this.idSolicitud);
        myIntent.putExtra("objSolicitud",this.objSolicitud);

        startActivity(myIntent);
    }

    public void navegaGenerales(View view) {
        Intent myIntent = new Intent(frmNuevaSolicitud.this, frmGenerales.class);
        myIntent.putExtra("idSolicitud",this.idSolicitud);
        myIntent.putExtra("objSolicitud",this.objSolicitud);

       startActivity(myIntent);
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }


}
