package maas.com.mx.maas;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.gson.Gson;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmdocumentos);

        try{
            countExtras=0;

            this.idSolicitud= getIntent().getStringExtra("idSolicitud");
            this.objSolicitud=getIntent().getStringExtra("objSolicitud");

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
}
