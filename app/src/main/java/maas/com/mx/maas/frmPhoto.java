package maas.com.mx.maas;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import maas.com.mx.maas.R;
import maas.com.mx.maas.entidades.SolicitudType;
import maas.com.mx.maas.negocio.Negocio;

public class frmPhoto extends Activity {

    SharedPreferences preferences=null;
    SolicitudType objSol=null;
    String objSolicitud="";
    String idSolicitud="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmphoto);

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






        }catch(Exception ex){

            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
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
