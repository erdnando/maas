package maas.com.mx.maas;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import maas.com.mx.maas.R;
import maas.com.mx.maas.entidades.DataGridLayout;
import maas.com.mx.maas.entidades.Solicitud;
import maas.com.mx.maas.negocio.Negocio;

public class frmBandeja extends Activity {

    String user="";
    String compania= "";
    String logeado= "";
    String tipousuario= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmbandeja);
       // setContentView(new DataGridLayout(this));

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            Negocio ng = new Negocio(getApplicationContext());

            ArrayList<Solicitud> solicitudes = ng.getSolicitudes();

            TableLayout GridSpace = (TableLayout) findViewById(R.id.GridSpace);
            GridSpace.addView(new DataGridLayout(this,solicitudes ));
        }catch(Exception exx)
        {
String hhhh="";
        }

        try{
            Intent i= getIntent();
            this.user= getIntent().getStringExtra("User");
            this.compania= getIntent().getStringExtra("Compania");
            this.logeado= getIntent().getStringExtra("Logeado");
            this.tipousuario= getIntent().getStringExtra("Tipo_Usuario");

            Negocio negocio = new Negocio(getApplicationContext());

            TextView txtPromotorNombre=(TextView)findViewById(R.id.txtPromotorNombre);
            txtPromotorNombre.setText(this.logeado);

            TextView txtMetricaGris=(TextView)findViewById(R.id.txtMetricaGris);
            TextView txtMetricaVerde=(TextView)findViewById(R.id.txtMetricaVerde);
            TextView txtMetricaAzul=(TextView)findViewById(R.id.txtMetricaAzul);
            TextView txtMetricaRojo=(TextView)findViewById(R.id.txtMetricaRojo);
            TextView txtMetricaAmarillo=(TextView)findViewById(R.id.txtMetricaAmarillo);

            txtMetricaGris.setText(negocio.GetMetricaEstatus("6,7"));
            txtMetricaVerde.setText(negocio.GetMetricaEstatus("2"));
            txtMetricaAzul.setText(negocio.GetMetricaEstatus("1,5"));
            txtMetricaRojo.setText(negocio.GetMetricaEstatus("3"));
            txtMetricaAmarillo.setText(negocio.GetMetricaEstatus("9"));


        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.frm_bandeja, menu);
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
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
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
}
