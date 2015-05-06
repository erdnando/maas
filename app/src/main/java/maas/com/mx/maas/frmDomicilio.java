package maas.com.mx.maas;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import maas.com.mx.maas.R;
import maas.com.mx.maas.entidades.MySpinnerAdapter;
import maas.com.mx.maas.entidades.Solicitud;
import maas.com.mx.maas.entidades.SolicitudType;
import maas.com.mx.maas.entidades.objectItem;
import maas.com.mx.maas.negocio.Negocio;

public class frmDomicilio extends Activity {
    String idSolicitud="0";
    int total=0;
    SharedPreferences preferences=null;
    SolicitudType objSol=null;
    String objSolicitud="";
    Boolean againEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frmdomicilio);

        try{
            this.idSolicitud= getIntent().getStringExtra("idSolicitud");
            this.objSolicitud=getIntent().getStringExtra("objSolicitud");

            preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);

            //metodos de inicializacion
            cargaCatalogos();
            configuraControlesRequeridos();

            Gson gson = new Gson();
            objSol=gson.fromJson(objSolicitud, SolicitudType.class);
            againEstado=false;

            cargaFormulario(objSol);
            validaEstatus();

            if(!this.idSolicitud.toString().equals("0")){
                getActionBar().setTitle(this.idSolicitud.toString());
            }

        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void cargaCatalogos() {


        try {
            Negocio negocio = new Negocio(getApplicationContext());



            //Estados
            Spinner cboEstdoDomicilio = (Spinner)findViewById(R.id.cboEstdoDomicilio);
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
String h="";
        }

    }

    private void configuraControlesRequeridos() {

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

    private void cargaFormulario(SolicitudType objSol) {

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

    private void validaEstatus() {
        TextView lblAvance = (TextView) findViewById(R.id.lblAvance);

        //7 requeridos
        total=0;

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
        if((total*100/7)>=100){
            imgEstatusGrales.setImageResource(R.drawable.complete);
            imgEstatusGrales.setBackgroundColor(Color.TRANSPARENT);


        }else{
            imgEstatusGrales.setImageResource(R.drawable.incomplete);
            imgEstatusGrales.setBackgroundColor(Color.TRANSPARENT);

        }

        lblAvance.setText(Integer.toString(total*100/7)+"%");

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
        if(((objectItem) cboDelegMunicipio.getSelectedItem())!=null)
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
