package maas.com.mx.maas;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import maas.com.mx.maas.entidades.MySpinnerAdapter;
import maas.com.mx.maas.entidades.objectItem;
import maas.com.mx.maas.negocio.Negocio;
import android.os.AsyncTask;
import android.os.Bundle;

public class frmGenerales extends Activity  {
//Erdnando1 github
    String idSolicitud="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmgenerales);

        try{
            Intent i= getIntent();
            this.idSolicitud= getIntent().getStringExtra("idSolicitud");
            Negocio negocio = new Negocio(getApplicationContext());

            cargaCatalogos();
            configuraCalendario();
            configuraRfc();

           if(!this.idSolicitud.toString().equals("0")){
               getActionBar().setTitle(this.idSolicitud.toString());


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

    private void configuraRfc() {

        //EditText txtRFCGeneral = (EditText) findViewById(R.id.txtRFCGeneral);

        EditText txtNombreSolicitanteGeneral = (EditText) findViewById(R.id.txtNombreSolicitanteGeneral);
        txtNombreSolicitanteGeneral.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    generaRFC();
                }
            }
        });

        EditText txtPaternoGeneral = (EditText) findViewById(R.id.txtPaternoGeneral);
        txtPaternoGeneral.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    generaRFC();
                }
            }
        });

        EditText txtMaternoGeneral = (EditText) findViewById(R.id.txtMaternoGeneral);
        txtMaternoGeneral.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    generaRFC();
                }
            }
        });

       // DatePicker dateFechaNacGeneralX = (DatePicker) findViewById(R.id.dateFechaNacGeneralX);


//implements DatePicker.OnDateChangedListener

       /* dateFechaNacGeneralX.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    generaRFC();
                }
            }
        });*/


    }

    private String strGeneraRFC() {

        EditText txtRFC = (EditText) findViewById(R.id.txtRFCGeneral);
        try {


            EditText txtNombre = (EditText) findViewById(R.id.txtNombreSolicitanteGeneral);
            EditText txtPaterno = (EditText) findViewById(R.id.txtPaternoGeneral);
            EditText txtMaterno = (EditText) findViewById(R.id.txtMaternoGeneral);
            DatePicker txtFechaNac = (DatePicker) findViewById(R.id.dateFechaNacGeneralX);

            Negocio negocio = new Negocio(getApplicationContext());

            String strFecha = Integer.toString(txtFechaNac.getYear()).toString() + getNumero(txtFechaNac.getMonth()+1) + getNumero(txtFechaNac.getDayOfMonth());
            //txtRFC.setText(negocio.RFC13Pocisiones(txtPaterno.getText().toString().toUpperCase(), txtMaterno.getText().toString().toUpperCase(), txtNombre.getText().toString().toUpperCase(), strFecha));

            return negocio.RFC13Pocisiones(txtPaterno.getText().toString().toUpperCase(), txtMaterno.getText().toString().toUpperCase(), txtNombre.getText().toString().toUpperCase(), strFecha);
        }catch(Exception ex){

            return "Incompleto";
        }

    }

    private void generaRFC() {

        EditText txtRFC = (EditText) findViewById(R.id.txtRFCGeneral);
        try {


            EditText txtNombre = (EditText) findViewById(R.id.txtNombreSolicitanteGeneral);
            EditText txtPaterno = (EditText) findViewById(R.id.txtPaternoGeneral);
            EditText txtMaterno = (EditText) findViewById(R.id.txtMaternoGeneral);
            DatePicker txtFechaNac = (DatePicker) findViewById(R.id.dateFechaNacGeneralX);

            Negocio negocio = new Negocio(getApplicationContext());

            String strFecha = Integer.toString(txtFechaNac.getYear()).toString() + getNumero(txtFechaNac.getMonth()+1) + getNumero(txtFechaNac.getDayOfMonth());
            txtRFC.setText(negocio.RFC13Pocisiones(txtPaterno.getText().toString().toUpperCase(), txtMaterno.getText().toString().toUpperCase(), txtNombre.getText().toString().toUpperCase(), strFecha));

        }catch(Exception ex){

            txtRFC.setText("Incompleto");
        }

    }

    private String getNumero(int day)
    {
        if (day < 10) return "0" + Integer.toString(day);
        else return Integer.toString(day);
    }


    private int year;
    private int month;
    private int day;

    private void configuraCalendario() {

        DatePicker dpResult = (DatePicker) findViewById(R.id.dateFechaNacGeneralX);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR)-18;
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        dpResult.setCalendarViewShown(false);



        DatePicker.OnDateChangedListener onDateChanged=new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i2, int i3) {
                AsyncTaskGeneraRFC runner = new AsyncTaskGeneraRFC();
                runner.execute("");
            }
        };

        dpResult.init(year, month+1, day, onDateChanged);
    }

    private void cargaCatalogos() {


        try {
            Negocio negocio = new Negocio(getApplicationContext());

            //Identificaciones... ... ...  -->
            Spinner cboIdentificacionGeneral = (Spinner) findViewById(R.id.cboIdentificacionGeneral);
            objectItem[]  cboitems2;
            cboitems2 = negocio.CargarCatalogoComun("2");
            MySpinnerAdapter dataAdapter2 = new MySpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_item, cboitems2);
            cboIdentificacionGeneral.setAdapter(dataAdapter2);

            cboIdentificacionGeneral.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    try {
                        String value = ((objectItem)parentView.getItemAtPosition(position)).VALUE.toString();
                        EditText txtNumIdentificacionGeneral = (EditText) findViewById(R.id.txtNumIdentificacionGeneral);

                        txtNumIdentificacionGeneral.setText("");

                        if (value.equals("2496")) {//ife
                            txtNumIdentificacionGeneral.setFilters(new InputFilter[] { new InputFilter.LengthFilter(13) });
                        } else if (value.equals("2497")) {//cedula
                            txtNumIdentificacionGeneral.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                        } else if (value.equals("2498")) {//pasaporte
                            txtNumIdentificacionGeneral.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                        } else if (value.equals("2499")) {//fm2
                            txtNumIdentificacionGeneral.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                        } else if (value.equals("2500")) {//fm3
                            txtNumIdentificacionGeneral.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                        }else  //default
                            txtNumIdentificacionGeneral.setFilters(new InputFilter[] { new InputFilter.LengthFilter(13) });

                    }catch(Exception ex){
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

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



            cboEstdoDomicilio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    try {
                        String selectedId = ((objectItem)parentView.getItemAtPosition(position)).VALUE.toString();//parentView.getItemAtPosition(position).toString();
                        Negocio negocio = new Negocio(getApplicationContext());
                        Spinner cboDelegMunicipio = (Spinner) findViewById(R.id.cboDelegMunicipio);
                        cboDelegMunicipio.setAdapter(null);

                        String catActivo=negocio.GetBuzonActivo().toString();

                        objectItem[] cboitems;
                        cboitems = negocio.CargarCatalogoDelegMunicipio(catActivo, selectedId);
                        MySpinnerAdapter dataAdapter = new MySpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, cboitems);
                        cboDelegMunicipio.setAdapter(dataAdapter);
                    }catch(Exception ex){

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


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


   private class AsyncTaskGeneraRFC extends AsyncTask<String, String, String> {

       private String resp;

       @Override
       protected String doInBackground(String... params) {
          // publishProgress("Sleeping..."); // Calls onProgressUpdate()
           try {
               // Do your long operations here and return the result
               //int time = Integer.parseInt(params[0]);
               resp=strGeneraRFC();
              // publishProgress(resp);
           } catch (Exception e) {
               e.printStackTrace();
               resp = e.getMessage();
           }

           return resp;
       }

       /*
        * (non-Javadoc)
        *
        * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
        */
       @Override
       protected void onPostExecute(String result) {
           // execution of result of Long time consuming operation
           EditText txtRFC = (EditText) findViewById(R.id.txtRFCGeneral);
           txtRFC.setText(result);

       }

       /*
        * (non-Javadoc)
        *
        * @see android.os.AsyncTask#onPreExecute()
        */
       @Override
       protected void onPreExecute() {
           // Things to be done before execution of long running operation. For
           // example showing ProgessDialog
       }

       /*
        * (non-Javadoc)
        *
        * @see android.os.AsyncTask#onProgressUpdate(Progress[])
        */
       @Override
       protected void onProgressUpdate(String... text) {
           //finalResult.setText(text[0]);
           // Things to be done while execution of long running operation is in
           // progress. For example updating ProgessDialog
       }
   }





}
