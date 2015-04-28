package maas.com.mx.maas;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import maas.com.mx.maas.entidades.MySpinnerAdapter;
import maas.com.mx.maas.entidades.objectItem;
import maas.com.mx.maas.negocio.Negocio;

public class frmGenerales extends Activity {

    String idSolicitud="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmgenerales);

        try{
            Intent i= getIntent();
            this.idSolicitud= getIntent().getStringExtra("idSolicitud");
            Negocio negocio = new Negocio(getApplicationContext());

           if(!this.idSolicitud.toString().equals("0")){
               getActionBar().setTitle(this.idSolicitud.toString());

               cargaCatalogos();
               configuraCalendario();
               //recrea solicitud
               //..........
            }
            else{
               //nueva solicitud
               //..........
           }
        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private int year;
    private int month;
    private int day;


    private void configuraCalendario() {

        DatePicker dpResult = (DatePicker) findViewById(R.id.dateFechaNacGeneralX);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        dpResult.setCalendarViewShown(false);
        // set current date into datepicker
        dpResult.init(year, month, day, null);
    }

    private void cargaCatalogos() {


        try {
            Negocio negocio = new Negocio(getApplicationContext());

            //Identificaciones...
            Spinner cboIdentificacionGeneral = (Spinner) findViewById(R.id.cboIdentificacionGeneral);
            objectItem[]  cboitems2;
            cboitems2 = negocio.CargarCatalogoComun("2");
            MySpinnerAdapter dataAdapter2 = new MySpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_item, cboitems2);
            cboIdentificacionGeneral.setAdapter(dataAdapter2);

            //Nacionalidades
            Spinner cboNacionalGeneral = (Spinner) findViewById(R.id.cboNacionalGeneral);
            objectItem[]  cboitems9;
            cboitems9 = negocio.CargarCatalogoComun("9");
            MySpinnerAdapter dataAdapter9 = new MySpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_item, cboitems9);
            cboNacionalGeneral.setAdapter(dataAdapter9);


            //EstadoCivil
            Spinner cboEdoCivilGeneral = (Spinner) findViewById(R.id.cboEdoCivilGeneral);
            objectItem[]  cboitems3;
            cboitems3 = negocio.CargarCatalogoComun("3");
            MySpinnerAdapter dataAdapter3 = new MySpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_item, cboitems3);
            cboEdoCivilGeneral.setAdapter(dataAdapter3);

            //Estados
            Spinner cboEstdoDomicilio = (Spinner) findViewById(R.id.cboEstdoDomicilio);
            objectItem[]  cboitems5;
            cboitems5 = negocio.CargarCatalogoComun("5");
            MySpinnerAdapter dataAdapter5 = new MySpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_item, cboitems5);
            cboEstdoDomicilio.setAdapter(dataAdapter5);

            //Estatus residencia domicilio
            Spinner cboEstatusResidenciaDomicilio = (Spinner) findViewById(R.id.cboEstatusResidenciaDomicilio);
            objectItem[]  cboitems4;
            cboitems4 = negocio.CargarCatalogoComun("4");
            MySpinnerAdapter dataAdapter4 = new MySpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_item, cboitems4);
            cboEstatusResidenciaDomicilio.setAdapter(dataAdapter4);

            //Compania movil
            Spinner cboCompaniaMovil = (Spinner) findViewById(R.id.cboCompaniaMovil);
            objectItem[]  cboitems1;
            cboitems1 = negocio.CargarCatalogoComun("1");
            MySpinnerAdapter dataAdapter1 = new MySpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_item, cboitems1);
            cboCompaniaMovil.setAdapter(dataAdapter1);

        }catch (Exception ex){

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.frm_generales, menu);
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



}
