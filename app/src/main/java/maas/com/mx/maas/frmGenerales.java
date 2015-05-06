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
    Boolean againEstado;
    Boolean againIdentificacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmgenerales);

        try{
            this.idSolicitud= getIntent().getStringExtra("idSolicitud");
            this.objSolicitud=getIntent().getStringExtra("objSolicitud");
            //-------------------------------------------------
            preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
            //----------------------------------------------
            cargaCatalogos();
            configuraRfc();
            configuraControlesRequeridos();

            Gson gson = new Gson();

            objSol=gson.fromJson(objSolicitud, SolicitudType.class);

            againEstado=false;
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

        EditText txtCalleDomicilio = (EditText) findViewById(R.id.txtCalleDomicilio);
        txtCalleDomicilio.setText(objSol.domicilio.Calle);

        EditText txtNoInteriorDomicilio = (EditText) findViewById(R.id.txtNoInteriorDomicilio);
        txtNoInteriorDomicilio.setText(objSol.domicilio.NoInt);

        EditText txtNoExteriorDomicilio = (EditText) findViewById(R.id.txtNoExteriorDomicilio);
        txtNoExteriorDomicilio.setText(objSol.domicilio.NoExt);

        EditText txtCpDomicilio = (EditText) findViewById(R.id.txtCpDomicilio);
        txtCpDomicilio.setText(objSol.domicilio.Cpdom);

        Spinner cboEstdoDomicilio = (Spinner) findViewById(R.id.cboEstdoDomicilio);
        SelectSpinnerItemByValue(cboEstdoDomicilio, objSol.domicilio.Estado );


        try {
            String selectedId = objSol.domicilio.Estado;
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


        Spinner cboDelegMunicipio = (Spinner) findViewById(R.id.cboDelegMunicipio);
        SelectSpinnerItemByValue(cboDelegMunicipio, objSol.domicilio.Delegacion );

        EditText txtColDomicilio = (EditText) findViewById(R.id.txtColDomicilio);
        txtColDomicilio.setText(objSol.domicilio.Colonia);

        EditText txtTiempoResidDomicilio = (EditText) findViewById(R.id.txtTiempoResidDomicilio);
        txtTiempoResidDomicilio.setText(objSol.domicilio.TiempoResidencia);

        Spinner cboEstatusResidenciaDomicilio = (Spinner) findViewById(R.id.cboEstatusResidenciaDomicilio);
        SelectSpinnerItemByValue(cboEstatusResidenciaDomicilio, objSol.domicilio.EstatusResidencia );

        EditText txtMontoViviendaDomicilio = (EditText) findViewById(R.id.txtMontoViviendaDomicilio);
        txtMontoViviendaDomicilio.setText(objSol.domicilio.MontoVivienda);

        EditText txtECorreoDomicilio = (EditText) findViewById(R.id.txtECorreoDomicilio);
        txtECorreoDomicilio.setText(objSol.domicilio.Email);

        EditText txtTelDomicilio = (EditText) findViewById(R.id.txtTelDomicilio);
        txtTelDomicilio.setText(objSol.domicilio.Telcasa);

        EditText txtCelDomicilio = (EditText) findViewById(R.id.txtCelDomicilio);
        txtCelDomicilio.setText(objSol.domicilio.Telmovil);



    }



    public static void SelectSpinnerItemByValue(Spinner spnr, String value)
    {
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

        EditText txtCalleDomicilio = (EditText) findViewById(R.id.txtCalleDomicilio);
        txtCalleDomicilio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    validaEstatus();
                }
            }
        });


        EditText txtNoExteriorDomicilio = (EditText) findViewById(R.id.txtNoExteriorDomicilio);
        txtNoExteriorDomicilio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    validaEstatus();
                }
            }
        });

        EditText txtCpDomicilio = (EditText) findViewById(R.id.txtCpDomicilio);
        txtCpDomicilio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    validaEstatus();
                }
            }
        });


        EditText txtTiempoResidDomicilio = (EditText) findViewById(R.id.txtTiempoResidDomicilio);
        txtTiempoResidDomicilio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    validaEstatus();
                }
            }
        });

        EditText txtECorreoDomicilio = (EditText) findViewById(R.id.txtECorreoDomicilio);
        txtECorreoDomicilio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    validaEstatus();
                }
            }
        });

        EditText txtTelDomicilio = (EditText) findViewById(R.id.txtTelDomicilio);
        txtTelDomicilio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    validaEstatus();
                }
            }
        });

        EditText txtCelDomicilio = (EditText) findViewById(R.id.txtCelDomicilio);
        txtCelDomicilio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    validaEstatus();
                }
            }
        });



    }

    private void validaEstatus() {

        TextView lblAvance = (TextView) findViewById(R.id.lblAvance);

        //12 requeridos
        total=0;

        EditText txtNombreSolicitanteGeneral = (EditText) findViewById(R.id.txtNombreSolicitanteGeneral);
        total+= getPointAvance(txtNombreSolicitanteGeneral.getText().toString().trim());

        EditText txtPaternoGeneral = (EditText) findViewById(R.id.txtPaternoGeneral);
        total+= getPointAvance(txtPaternoGeneral.getText().toString().trim());

        EditText txtMaternoGeneral = (EditText) findViewById(R.id.txtMaternoGeneral);
        total+= getPointAvance(txtMaternoGeneral.getText().toString().trim());

        EditText txtNumIdentificacionGeneral = (EditText) findViewById(R.id.txtNumIdentificacionGeneral);
        total+= getPointAvance(txtNumIdentificacionGeneral.getText().toString().trim());

        /*RadioButton rdoRadioHombre = (RadioButton) findViewById(R.id.rdoRadioHombre);
        total+= getPointAvance(rdoRadioHombre.isChecked()==true?"true":"");

        RadioButton rdoRadioMujer = (RadioButton) findViewById(R.id.rdoRadioHombre);
        total+= getPointAvance(rdoRadioMujer.isChecked() ==true?"true":"");*/

        EditText txtRFCGeneral = (EditText) findViewById(R.id.txtRFCGeneral);
        total+= getPointAvance(txtRFCGeneral.getText().toString().trim());

        EditText txtCalleDomicilio = (EditText) findViewById(R.id.txtCalleDomicilio);
        total+= getPointAvance(txtCalleDomicilio.getText().toString().trim());

        EditText txtNoExteriorDomicilio = (EditText) findViewById(R.id.txtNoExteriorDomicilio);
        total+= getPointAvance(txtNoExteriorDomicilio.getText().toString().trim());

        EditText txtCpDomicilio = (EditText) findViewById(R.id.txtCpDomicilio);
        total+= getPointAvance(txtCpDomicilio.getText().toString().trim());

        EditText txtTiempoResidDomicilio = (EditText) findViewById(R.id.txtTiempoResidDomicilio);
        total+= getPointAvance(txtTiempoResidDomicilio.getText().toString().trim());

        EditText txtECorreoDomicilio = (EditText) findViewById(R.id.txtECorreoDomicilio);
        total+= getPointAvance(txtECorreoDomicilio.getText().toString().trim());

        EditText txtTelDomicilio = (EditText) findViewById(R.id.txtTelDomicilio);
        total+= getPointAvance(txtTelDomicilio.getText().toString().trim());

        EditText txtCelDomicilio = (EditText) findViewById(R.id.txtCelDomicilio);
        total+= getPointAvance(txtCelDomicilio.getText().toString().trim());

        ImageButton imgEstatusGrales = (ImageButton) findViewById(R.id.imgEstatusGrales);
        if((total*100/12)>=100){
            imgEstatusGrales.setImageResource(R.drawable.complete);
            imgEstatusGrales.setBackgroundColor(Color.TRANSPARENT);

        }else{
            imgEstatusGrales.setImageResource(R.drawable.incomplete);
            imgEstatusGrales.setBackgroundColor(Color.TRANSPARENT);
        }

        lblAvance.setText(Integer.toString(total*100/12)+"%");
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

    private String getNumero(int day)
    {
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


    private void configuraCalendario() {

        DatePicker dpResult = (DatePicker) findViewById(R.id.dateFechaNacGeneralX);
        dpResult.setCalendarViewShown(false);

        DatePicker.OnDateChangedListener onDateChanged=new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i2, int i3) {
                AsyncTaskGeneraRFC runner = new AsyncTaskGeneraRFC();
                runner.execute("");
            }
        };

        Calendar c = Calendar.getInstance();
        dpResult.init(c.get(Calendar.YEAR)-18, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), onDateChanged);


        dpResult.init(Integer.valueOf(year), Integer.valueOf(month ), Integer.valueOf(day), onDateChanged);

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

            //Estados
            Spinner cboEstdoDomicilio = (Spinner) findViewById(R.id.cboEstdoDomicilio);
            objectItem[]  cboitems5;
            cboitems5 = negocio.CargarCatalogoComun("5");
            MySpinnerAdapter dataAdapter5 = new MySpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_item, cboitems5);
            cboEstdoDomicilio.setAdapter(dataAdapter5);


            cboEstdoDomicilio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if(againEstado==false){againEstado=true;return;}

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

        EditText txtCalleDomicilio = (EditText) findViewById(R.id.txtCalleDomicilio);
        objSol.domicilio.Calle=txtCalleDomicilio.getText().toString().toUpperCase();

        EditText txtNoInteriorDomicilio = (EditText) findViewById(R.id.txtNoInteriorDomicilio);
        objSol.domicilio.NoInt=txtNoInteriorDomicilio.getText().toString().toUpperCase();

        EditText txtNoExteriorDomicilio = (EditText) findViewById(R.id.txtNoExteriorDomicilio);
        objSol.domicilio.NoExt=txtNoExteriorDomicilio.getText().toString().toUpperCase();

        EditText txtCpDomicilio = (EditText) findViewById(R.id.txtCpDomicilio);
        objSol.domicilio.Cpdom=txtCpDomicilio.getText().toString().toUpperCase();

        Spinner cboEstdoDomicilio = (Spinner) findViewById(R.id.cboEstdoDomicilio);
        objSol.domicilio.Estado= ((objectItem) cboEstdoDomicilio.getSelectedItem()).VALUE;

        Spinner cboDelegMunicipio = (Spinner) findViewById(R.id.cboDelegMunicipio);
        objSol.domicilio.Delegacion= ((objectItem) cboDelegMunicipio.getSelectedItem()).VALUE;

        EditText txtColDomicilio = (EditText) findViewById(R.id.txtColDomicilio);
        objSol.domicilio.Colonia=txtColDomicilio.getText().toString().toUpperCase();

        EditText txtTiempoResidDomicilio = (EditText) findViewById(R.id.txtTiempoResidDomicilio);
        objSol.domicilio.TiempoResidencia=txtTiempoResidDomicilio.getText().toString().toUpperCase();

        Spinner cboEstatusResidenciaDomicilio = (Spinner) findViewById(R.id.cboEstatusResidenciaDomicilio);
        objSol.domicilio.EstatusResidencia= ((objectItem) cboEstatusResidenciaDomicilio.getSelectedItem()).VALUE;

        EditText txtMontoViviendaDomicilio = (EditText) findViewById(R.id.txtMontoViviendaDomicilio);
        objSol.domicilio.MontoVivienda=txtMontoViviendaDomicilio.getText().toString().toUpperCase();

        EditText txtECorreoDomicilio = (EditText) findViewById(R.id.txtECorreoDomicilio);
        objSol.domicilio.Email=txtECorreoDomicilio.getText().toString().toUpperCase();

        EditText txtTelDomicilio = (EditText) findViewById(R.id.txtTelDomicilio);
        objSol.domicilio.Telcasa=txtTelDomicilio.getText().toString().toUpperCase();

        EditText txtCelDomicilio = (EditText) findViewById(R.id.txtCelDomicilio);
        objSol.domicilio.Telmovil=txtCelDomicilio.getText().toString().toUpperCase();


        return objSol;
    }

}
