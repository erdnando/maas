package maas.com.mx.maas;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.Calendar;
import maas.com.mx.maas.entidades.MySpinnerAdapter;
import maas.com.mx.maas.entidades.SolicitudType;
import maas.com.mx.maas.entidades.objectItem;
import maas.com.mx.maas.negocio.Negocio;

public class frmGenerales extends Activity  {
//Erdnando1 github
    String idSolicitud="0";
    int total=0;
    SharedPreferences preferences=null;
    SolicitudType objSol=null;
    String objSolicitud="";

    Boolean againIdentificacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmgenerales);

        try{
            this.idSolicitud= getIntent().getStringExtra("idSolicitud");
            this.objSolicitud=getIntent().getStringExtra("objSolicitud");

            preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);

            cargaCatalogos();
            configuraRfc();
            configuraControlesRequeridos();

            Gson gson = new Gson();
            objSol=gson.fromJson(objSolicitud, SolicitudType.class);

            againIdentificacion=false;
            //recrea solicitud
            cargaFormulario(objSol);

            validaEstatus();

           if(!this.idSolicitud.equals("0")){
               if(getActionBar()!=null) {
                   getActionBar().setTitle(this.idSolicitud);
               }
            }

        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void cargaFormulario(SolicitudType objSol) {

        EditText txtNombreSolicitanteGeneral = (EditText) findViewById(R.id.txtNombreSolicitanteGeneral);
        txtNombreSolicitanteGeneral.setText(objSol.generales.Pmrnombre);

        EditText txtSegNombreGeneral = (EditText) findViewById(R.id.txtSegNombreGeneral);
        txtSegNombreGeneral.setText(objSol.generales.Sdonombre);

        EditText txtPaternoGeneral = (EditText) findViewById(R.id.txtPaternoGeneral);
        txtPaternoGeneral.setText(objSol.generales.Apaterno);

        EditText txtMaternoGeneral = (EditText) findViewById(R.id.txtMaternoGeneral);
        txtMaternoGeneral.setText(objSol.generales.Amaterno);

        Spinner cboIdentificacionGeneral = (Spinner) findViewById(R.id.cboIdentificacionGeneral);
        SelectSpinnerItemByValue(cboIdentificacionGeneral, objSol.generales.Tpoidentif );

        EditText txtNumIdentificacionGeneral = (EditText) findViewById(R.id.txtNumIdentificacionGeneral);
        txtNumIdentificacionGeneral.setText(objSol.generales.Noidenficacion);

        RadioButton rdoRadioHombre = (RadioButton) findViewById(R.id.rdoRadioHombre);
        RadioButton rdoRadioMujer = (RadioButton) findViewById(R.id.rdoRadioMujer);
        if(objSol.generales.Sexo == "MASCULINO")
            rdoRadioHombre.setChecked(true);
        else rdoRadioMujer.setChecked(true);

        Spinner cboNacionalGeneral = (Spinner) findViewById(R.id.cboNacionalGeneral);
        SelectSpinnerItemByValue(cboNacionalGeneral, objSol.generales.Nacionalidad );

        configuraCalendario(objSol.generales.Fechanacdia.toString(),objSol.generales.FechasnacMes.toString(),objSol.generales.FechanacAnio.toString());

        EditText txtRFCGeneral = (EditText) findViewById(R.id.txtRFCGeneral);
        txtRFCGeneral.setText(objSol.generales.Rfc);

        Spinner cboEdoCivilGeneral = (Spinner) findViewById(R.id.cboEdoCivilGeneral);
        SelectSpinnerItemByValue(cboEdoCivilGeneral, objSol.generales.Edocivil );

        EditText txtNumDependienteGeneral = (EditText) findViewById(R.id.txtNumDependienteGeneral);
        txtNumDependienteGeneral.setText(objSol.generales.Nodependiente);




    }

    public static void SelectSpinnerItemByValue(Spinner spnr, String value){
        MySpinnerAdapter adapter = (MySpinnerAdapter) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++)
        {
            if( ((objectItem) adapter.getItem(position)).VALUE.equals(value)  )
            {
                spnr.setSelection(position);
                //spnr.setSelected(true);
                return;
            }
        }
    }

    private void configuraControlesRequeridos() {

        EditText txtNumIdentificacionGeneral = (EditText) findViewById(R.id.txtNumIdentificacionGeneral);
        txtNumIdentificacionGeneral.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    validaEstatus();
                }
            }
        });

        RadioButton rdoRadioHombre = (RadioButton) findViewById(R.id.rdoRadioHombre);
        rdoRadioHombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    validaEstatus();
                }
            }
        });

        RadioButton rdoRadioMujer = (RadioButton) findViewById(R.id.rdoRadioHombre);
        rdoRadioMujer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    validaEstatus();
                }
            }
        });


        EditText txtRFCGeneral = (EditText) findViewById(R.id.txtRFCGeneral);
        txtRFCGeneral.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    validaEstatus();
                }
            }
        });

    }
    //valida requeridos
    private void validaEstatus() {

        TextView lblAvance = (TextView) findViewById(R.id.lblAvance);

        //5 requeridos
        total=0;

        EditText txtNombreSolicitanteGeneral = (EditText) findViewById(R.id.txtNombreSolicitanteGeneral);
        total+= getPointAvance(txtNombreSolicitanteGeneral.getText().toString().trim());

        EditText txtPaternoGeneral = (EditText) findViewById(R.id.txtPaternoGeneral);
        total+= getPointAvance(txtPaternoGeneral.getText().toString().trim());

        EditText txtMaternoGeneral = (EditText) findViewById(R.id.txtMaternoGeneral);
        total+= getPointAvance(txtMaternoGeneral.getText().toString().trim());

        EditText txtNumIdentificacionGeneral = (EditText) findViewById(R.id.txtNumIdentificacionGeneral);
        total+= getPointAvance(txtNumIdentificacionGeneral.getText().toString().trim());

        EditText txtRFCGeneral = (EditText) findViewById(R.id.txtRFCGeneral);
        total+= getPointAvance(txtRFCGeneral.getText().toString().trim());


        ImageButton imgEstatusGrales = (ImageButton) findViewById(R.id.imgEstatusGrales);
        if((total*100/5)>=100){
            imgEstatusGrales.setImageResource(R.drawable.complete);
            imgEstatusGrales.setBackgroundColor(Color.TRANSPARENT);


        }else{
            imgEstatusGrales.setImageResource(R.drawable.incomplete);
            imgEstatusGrales.setBackgroundColor(Color.TRANSPARENT);

        }

        lblAvance.setText(Integer.toString(total*100/5)+"%");
    }

    private int getPointAvance(String valor) {

        if(valor.trim().length()>0){
            return 1;
        }
        else{
            return 0;
        }


    }

    private void configuraRfc() {

        EditText txtNombreSolicitanteGeneral = (EditText) findViewById(R.id.txtNombreSolicitanteGeneral);
        txtNombreSolicitanteGeneral.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    generaRFC();
                    validaEstatus();
                }
            }
        });

        EditText txtPaternoGeneral = (EditText) findViewById(R.id.txtPaternoGeneral);
        txtPaternoGeneral.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    generaRFC();
                    validaEstatus();
                }
            }
        });

        EditText txtMaternoGeneral = (EditText) findViewById(R.id.txtMaternoGeneral);
        txtMaternoGeneral.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    generaRFC();
                    validaEstatus();
                }
            }
        });


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

    private String getNumero(int day){
        if (day < 10) return "0" + Integer.toString(day);
        else return Integer.toString(day);
    }


    private int year;
    private int month;
    private int day;

    private void configuraCalendario(String day, String month, String year) {

        DatePicker dpResult = (DatePicker) findViewById(R.id.dateFechaNacGeneralX);
        dpResult.setCalendarViewShown(false);

        DatePicker.OnDateChangedListener onDateChanged=new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i2, int i3) {
                AsyncTaskGeneraRFC runner = new AsyncTaskGeneraRFC();
                runner.execute("");
            }
        };


        try {

            dpResult.init(Integer.valueOf(year), Integer.valueOf(month)-1, Integer.valueOf(day), onDateChanged);
        }catch(Exception ex){

            Calendar c = Calendar.getInstance();
            dpResult.init(c.get(Calendar.YEAR)-18, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), onDateChanged);
        }
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

                    if(againIdentificacion==false){againIdentificacion=true;return;}
                    try {
                        String value = ((objectItem)parentView.getItemAtPosition(position)).VALUE.toString();
                        EditText txtNumIdentificacionGeneral = (EditText) findViewById(R.id.txtNumIdentificacionGeneral);

                        txtNumIdentificacionGeneral.setText("");
                        validaEstatus();

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

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
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

        EditText txtNombreSolicitanteGeneral = (EditText) findViewById(R.id.txtNombreSolicitanteGeneral);
        objSol.generales.Pmrnombre=txtNombreSolicitanteGeneral.getText().toString().toUpperCase();

        EditText txtSegNombreGeneral = (EditText) findViewById(R.id.txtSegNombreGeneral);
        objSol.generales.Sdonombre=txtSegNombreGeneral.getText().toString().toUpperCase();

        EditText txtPaternoGeneral = (EditText) findViewById(R.id.txtPaternoGeneral);
        objSol.generales.Apaterno=txtPaternoGeneral.getText().toString().toUpperCase();

        EditText txtMaternoGeneral = (EditText) findViewById(R.id.txtMaternoGeneral);
        objSol.generales.Amaterno=txtMaternoGeneral.getText().toString().toUpperCase();

        Spinner cboIdentificacionGeneral = (Spinner) findViewById(R.id.cboIdentificacionGeneral);
        objSol.generales.Tpoidentif= ((objectItem) cboIdentificacionGeneral.getSelectedItem()).VALUE;

        EditText txtNumIdentificacionGeneral = (EditText) findViewById(R.id.txtNumIdentificacionGeneral);
        objSol.generales.Noidenficacion=txtNumIdentificacionGeneral.getText().toString().toUpperCase();

        RadioButton rdoRadioHombre = (RadioButton) findViewById(R.id.rdoRadioHombre);
        RadioButton rdoRadioMujer = (RadioButton) findViewById(R.id.rdoRadioMujer);
        if(rdoRadioHombre.isChecked())
            objSol.generales.Sexo = "MASCULINO";
        else objSol.generales.Sexo = "FEMENINO";

        Spinner cboNacionalGeneral = (Spinner) findViewById(R.id.cboNacionalGeneral);
        objSol.generales.Nacionalidad= ((objectItem) cboNacionalGeneral.getSelectedItem()).VALUE;

        DatePicker dateFechaNacGeneralX = (DatePicker) findViewById(R.id.dateFechaNacGeneralX);
        objSol.generales.Fechanacdia=Integer.toString(dateFechaNacGeneralX.getDayOfMonth());
        objSol.generales.FechasnacMes=Integer.toString(dateFechaNacGeneralX.getMonth()+1);
        objSol.generales.FechanacAnio=Integer.toString(dateFechaNacGeneralX.getYear());


        EditText txtRFCGeneral = (EditText) findViewById(R.id.txtRFCGeneral);
        objSol.generales.Rfc=txtRFCGeneral.getText().toString().toUpperCase();

        Spinner cboEdoCivilGeneral = (Spinner) findViewById(R.id.cboEdoCivilGeneral);
        objSol.generales.Edocivil= ((objectItem) cboEdoCivilGeneral.getSelectedItem()).VALUE;

        EditText txtNumDependienteGeneral = (EditText) findViewById(R.id.txtNumDependienteGeneral);
        objSol.generales.Nodependiente=txtNumDependienteGeneral.getText().toString().toUpperCase();



        return objSol;
    }

}
