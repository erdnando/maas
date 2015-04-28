package maas.com.mx.maas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.koushikdutta.ion.Ion;
import java.util.ArrayList;
import maas.com.mx.maas.entidades.BUZON_A;
import maas.com.mx.maas.entidades.CatalogoX;
import maas.com.mx.maas.entidades.CitasMetrica;
import maas.com.mx.maas.entidades.Documento;
import maas.com.mx.maas.entidades.Productos;
import maas.com.mx.maas.entidades.Promotor;
import maas.com.mx.maas.entidades.RegistroBuzon;
import maas.com.mx.maas.entidades.RegistroImagen;
import maas.com.mx.maas.entidades.Usuario;
import maas.com.mx.maas.negocio.Negocio;


public class main extends Activity  {

    Usuario _logeado;
    Negocio negocio;
    Boolean cargaTodo;
    Boolean salir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        StrictMode.enableDefaults();
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this._logeado=new Usuario();


        try {
            this.negocio=new Negocio(getApplicationContext() );
            this.negocio.preparaBDLocal();

        }catch(Exception ex){
          String h="";
        }

        setContentView(R.layout.main);

        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);*/
        return true;
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

    //Thread1 t;
    public void btnLogin(View view) {

        cargaTodo = false;
        salir=false;
        TableLayout waitingfor = (TableLayout) findViewById(R.id.waitingfor);
        TableLayout login = (TableLayout) findViewById(R.id.login);


        EditText txtUsuario=(EditText)findViewById(R.id.txtUsuario);
        EditText txtPassword=(EditText)findViewById(R.id.txtPassword);
        EditText txtEmpresa=(EditText)findViewById(R.id.txtEmpresa);

        if(!validaEntradas(txtUsuario.getText().toString().trim() ,txtPassword.getText().toString().trim(),txtEmpresa.getText().toString().trim())){
            Toast.makeText(getApplicationContext(),getString(R.string.campoRequerido),Toast.LENGTH_LONG).show();
            return;
        }else {

            txtUsuario.setText(txtUsuario.getText().toString().toLowerCase());
            txtEmpresa.setText(txtEmpresa.getText().toString().toUpperCase());

            CheckBox checkNuevoPerfil=(CheckBox)findViewById(R.id.checkNuevoPerfil);

            if(!checkNuevoPerfil.isChecked()){
                //busqueda local
                try{
                    Usuario userRes=negocio.BuscarUsuario(txtUsuario.getText().toString().trim(), txtPassword.getText().toString().trim(), txtEmpresa.getText().toString().trim().toUpperCase());

                    if(userRes.Logeado!="-1" ){

                        this._logeado=userRes;

                        waitingfor.setVisibility(View.INVISIBLE);
                        login.setVisibility(View.VISIBLE);

                        txtPassword=(EditText)findViewById(R.id.txtPassword);
                        txtPassword.setText("");

                        //Toast.makeText(getApplicationContext(),"Perfil creado,  Catálogo, Buzón y Citas Actualizados",Toast.LENGTH_LONG).show();
                        //TO_DO  INICIALIZAR OBJETOS DE LA BANDEJA
                        Intent myIntent = new Intent(main.this, frmMenuPrincipal.class);
                        myIntent.putExtra("User",this._logeado.User);
                        myIntent.putExtra("Compania",this._logeado.Compania);
                        myIntent.putExtra("Logeado",this._logeado.Logeado);
                        myIntent.putExtra("Tipo_Usuario",this._logeado.Tipo_Usuario);
                        startActivity(myIntent);

                        //startActivity(new Intent(main.this, frmMenuPrincipal.class));
                    }else{

                        Toast.makeText(getApplicationContext(),"Usuario y/o contraseña incorrecta",Toast.LENGTH_LONG).show();
                        waitingfor.setVisibility(View.INVISIBLE);
                        login.setVisibility(View.VISIBLE);
                    }

                }catch(Exception ex){
                    Toast.makeText(getApplicationContext(),"Error: "+ex.getMessage(),Toast.LENGTH_LONG).show();
                    waitingfor.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.VISIBLE);
                }

            }else{
                //busqueda en ws

                //prepara layouts
                //TableLayout waitingfor = (TableLayout) findViewById(R.id.waitingfor);
                waitingfor.setVisibility(View.VISIBLE);

                //TableLayout login = (TableLayout) findViewById(R.id.login);
                login.setVisibility(View.INVISIBLE);

                ImageView imageView = (ImageView) findViewById(R.id.imageViewAnimated);
                Ion.with(imageView).load("http://www.indiandefencereview.com/wp-content/themes/idr/images/loading.gif");

                try {

                    //prepara paquete de parametro
                    Promotor prom=new Promotor();
                    prom.Usuario=txtUsuario.getText().toString();
                    prom.Contrasenia=txtPassword.getText().toString();
                    prom.Compania=txtEmpresa.getText().toString();

                    //BUSCA EN WS
                    this._logeado = this.negocio.buscarUsuarioEnWSX(prom);

                    //valida resultado
                    if(_logeado.IdUsuario.toString().equals("-404")){
                        Toast.makeText(getApplicationContext(),R.string.nodisponible,Toast.LENGTH_LONG).show();
                        waitingfor.setVisibility(View.INVISIBLE);
                        login.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (this._logeado.IdUsuario.toString().equals("0")){
                        Toast.makeText(getApplicationContext(),R.string.usuarioinvalido,Toast.LENGTH_LONG).show();
                        waitingfor.setVisibility(View.INVISIBLE);
                        login.setVisibility(View.VISIBLE);
                        return;

                    }
                    //continua con SINCRONIACION
                    this._logeado.Logeado = "Promotor:" + this._logeado.User + " Compañia:" + this._logeado.Compania;

                    String bznActivo = this.negocio.GetBuzonActivo();
                    ArrayList<BUZON_A> sols = negocio.getSolicitudesPendientes(bznActivo);

                    int countSol = sols.size();
                    if (countSol == 0) { cargaTodo = true; }

                    // countSol=8;
                    if (countSol > 0)
                    {
                        String promotor=this._logeado.User;
                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(main.this);
                        // Setting Dialog Title
                        alertDialog2.setTitle("Confirme sincronización...");
                        // Setting Dialog Message
                        alertDialog2.setMessage("El buzón actual tiene solicitudes pendientes por enviar del promotor: "+promotor+"\n¿Está seguro que desea eliminar el contenido previo?");
                        // Setting Positive "Yes" Btn
                        alertDialog2.setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        sincronizando("NO");
                                    }
                                });
                        // Setting Negative "NO" Btn
                        alertDialog2.setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        sincronizando("YES");
                                        dialog.cancel();

                                    }
                                });
                        // Showing Alert Dialog
                        alertDialog2.show();


                    }//end if
                    else{
                        sincronizando("YES");
                    }

                }catch(Exception ex){
                    Toast.makeText(getApplicationContext(),ex.getMessage().toString(),Toast.LENGTH_LONG).show();
                    waitingfor.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.VISIBLE);
                    return;
                }
            }

        }

        // startActivity(i);

    }


    private void sincronizando(String valor) {

        TableLayout waitingfor = (TableLayout) findViewById(R.id.waitingfor);
        TableLayout login = (TableLayout) findViewById(R.id.login);

        if(valor.toString().equals("YES")){
           //waitingfor.setVisibility(View.VISIBLE);
           //login.setVisibility(View.INVISIBLE);

            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute("");
        }else{
            waitingfor.setVisibility(View.VISIBLE);
            login.setVisibility(View.INVISIBLE);
        }
    }

    TextView lblLog;
    private void cargaTodo() {

        Boolean bRegreso=false;
        lblLog = (TextView) findViewById(R.id.lblOutput);
        try{
            //obtiene el UUID del dispositivo
            String UUID = GetHardwareId();
            lblLog.setText("Validando UUID ...");

            Boolean bUUID = negocio.validaUUID(this._logeado.IdUsuario.toString(), UUID);
            bUUID=true;//just to test
            if(!bUUID){
                EditText txtPassword=(EditText)findViewById(R.id.txtPassword);
                EditText userTxt=(EditText)findViewById(R.id.txtUsuario);
                txtPassword.setText("");
                Toast.makeText(getApplicationContext(),"Este perfil del promotor: " + userTxt.getText() + " no puede instalarse en este dispositivo (" + UUID + "). \nYa esta instalado en otro dispositivo. \nSolicite al administrador el permiso correspondiente",Toast.LENGTH_LONG).show();
                bRegreso= false;
            }else{

                //obteniendo...-----------------------------------------------------------------------
                String versionCatBdLocal ="1.0";
                lblLog.setText("Obteniendo buzón ...");
                ArrayList<RegistroBuzon> buzonDesdeWs =  negocio.obtenerBuzonDesdeWSX(this._logeado);
                lblLog.setText("Obteniendo productos ...");
                ArrayList<Productos> objProductoResp = negocio.obtieneProductosWSX(this._logeado);
                lblLog.setText("Obteniendo metricas ...");
                ArrayList<CitasMetrica> MetricaCita = negocio.obtenerMetricaCitasWSX(this._logeado);
                //lblLog =  "Obteniendo citas ...";
                //List<RegAgenda> citasDesdeWs =  await Negocio.obtenerCitasDesdeWSX(this._logeado);
                lblLog.setText("Obteniendo catálogos ...");
                ArrayList<CatalogoX> catalogodesdeWs = negocio.obtenerCatalogosWSX(this._logeado);
                lblLog.setText("Finalizando ...");
                //-------------------------------------------------------------------------------------

                //persistiendo
                if (buzonDesdeWs != null){
                    lblLog.setText("Borrando imagenes anteriores ...");
                    Boolean b = negocio.BorrarimagenesX(getApplicationContext());
                    lblLog.setText("Insertando solicitudes ...");
                    Boolean bb = negocio.InsertarBuzonBdX(buzonDesdeWs);
                }

                bRegreso= true;

                 //terminado....
                TableLayout waitingfor = (TableLayout) findViewById(R.id.waitingfor);
                TableLayout login = (TableLayout) findViewById(R.id.login);
                waitingfor.setVisibility(View.INVISIBLE);
                login.setVisibility(View.VISIBLE);
            }

        }catch(Exception ex){


        }


       // return bRegreso;
    }

    private String GetHardwareId() {
        return  Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    private boolean validaEntradas(String txtUsuario,String txtPassword,String txtEmpresa) {


        if(txtUsuario.isEmpty() || txtPassword.isEmpty()||txtEmpresa.isEmpty()){
            return false;
        }else{

            return true;
        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        int intRegresoAsync;
        String strUUID;
        String strError;
        @Override
        protected String doInBackground(String... params) {

            publishProgress("Iniciando..."); // Calls onProgressUpdate()
            try {
                // Do your long operations here and return the result
                //int time = Integer.parseInt(params[0]);
                // Sleeping for given time period
                //Thread.sleep(time);
                //--------------------------------------------------------------------------------------------
                strError="";
                lblLog = (TextView) findViewById(R.id.lblOutput);
                try{
                    //obtiene el UUID del dispositivo
                    strUUID = GetHardwareId();
                    //lblLog.setText("Validando UUID ...");
                    publishProgress("Validando UUID ...");
                    Boolean bUUID = negocio.validaUUID(_logeado.IdUsuario.toString(), strUUID);
                    //bUUID=true;//just to test
                    if(!bUUID){
                        //EditText txtPassword=(EditText)findViewById(R.id.txtPassword);
                        //EditText userTxt=(EditText)findViewById(R.id.txtUsuario);
                        //txtPassword.setText("");
                        //Toast.makeText(getApplicationContext(),"Este perfil del promotor: " + userTxt.getText() + " no puede instalarse en este dispositivo (" + UUID + "). \nYa esta instalado en otro dispositivo. \nSolicite al administrador el permiso correspondiente",Toast.LENGTH_LONG).show();
                        intRegresoAsync= 1;//"EUUID";
                    }else{

                        //obteniendo...-----------------------------------------------------------------------
                        String versionCatBdLocal ="1.0";
                        publishProgress("Obteniendo buzón ...");
                        ArrayList<RegistroBuzon> buzonDesdeWs =  negocio.obtenerBuzonDesdeWSX(_logeado);
                        publishProgress("Obteniendo productos ...");
                        ArrayList<Productos> objProductoResp = negocio.obtieneProductosWSX(_logeado);
                        publishProgress("Obteniendo metricas ...");
                        ArrayList<CitasMetrica> MetricaCita = negocio.obtenerMetricaCitasWSX(_logeado);
                        //lblLog =  "Obteniendo citas ...";
                        //List<RegAgenda> citasDesdeWs =  await Negocio.obtenerCitasDesdeWSX(this._logeado);
                        publishProgress("Obteniendo catálogos ...");
                        ArrayList<CatalogoX> catalogodesdeWs = negocio.obtenerCatalogosWSX(_logeado);
                        publishProgress("Finalizando ...");
                        //-------------------------------------------------------------------------------------

                        //persistiendo
                        if (buzonDesdeWs != null){
                            publishProgress("Borrando imagenes anteriores ...");
                            Boolean b = negocio.BorrarimagenesX(getApplicationContext());
                            publishProgress("Insertando solicitudes ...");
                            Boolean bb = negocio.InsertarBuzonBdX(buzonDesdeWs);
                        }

                        if (catalogodesdeWs != null)
                        {
                            publishProgress("Insertando catalogos ...");
                            Boolean b = negocio.InsertCatalogoBdX(catalogodesdeWs, versionCatBdLocal, objProductoResp);
                        }

                        publishProgress("Insertando usuario ...");
                        EditText txtEmpresa=(EditText)findViewById(R.id.txtEmpresa);
                        Boolean bResult = negocio.InsertarUsuarioBDX(_logeado, txtEmpresa.getText().toString().toUpperCase());


                        publishProgress("Obteniendo imagenes ...");
                        //----------------------------------------------------------------
                        int caracteres = 10;
                        int i=0;
                        for(RegistroBuzon row: buzonDesdeWs){

                            Documento docs = negocio.getDocsFromXml(row.ID_SOLICITUD);
                            String idSol = row.ID_SOLICITUD;
                            //asigna el doc para evir ir otra vez por ws mas adelante
                            row.doc=docs;
                            if (!docs.IdentificacionFrentePath.toString().startsWith("...."))
                            {
                                publishProgress("..." + docs.IdentificacionFrentePath.toString().substring(caracteres, docs.IdentificacionFrentePath.length() - caracteres));
                                ArrayList<RegistroImagen> aa = negocio.getImageWSX("IF", idSol);

                                for(RegistroImagen item: aa)
                                {

                                    try
                                    {
                                        row.DOC_IF = item.img;
                                    }
                                    catch (Exception rr)
                                    {
                                        row.DOC_IF = null;
                                        String hh = rr.getMessage();
                                    }
                                }
                            }
                            if (!docs.IdentificacionAtrasPath.toString().startsWith("...."))
                            {
                                publishProgress("..." + docs.IdentificacionAtrasPath.toString().substring(caracteres, docs.IdentificacionAtrasPath.length() - caracteres));
                                ArrayList<RegistroImagen> aa = negocio.getImageWSX("IA", idSol);
                                for (RegistroImagen item : aa)
                                {
                                    try
                                    {
                                        row.DOC_IA = item.img;
                                    }
                                    catch (Exception rr)
                                    {
                                        row.DOC_IA = null;
                                        String hh = rr.getMessage();
                                    }
                                }

                            }
                            if (!docs.Contrato1Path.toString().startsWith("...."))
                            {
                                publishProgress("..." + docs.Contrato1Path.toString().substring(caracteres, docs.Contrato1Path.length() - caracteres));
                                ArrayList<RegistroImagen> aa = negocio.getImageWSX("C1", idSol);
                                for (RegistroImagen item : aa)
                                {
                                    try
                                    {
                                        row.DOC_C1 = item.img;
                                    }
                                    catch (Exception rr)
                                    {
                                        row.DOC_C1 = null;
                                        String hh = rr.getMessage();
                                    }
                                }

                            }
                            if (!docs.Contrato2Path.toString().startsWith("...."))
                            {
                                publishProgress("..." + docs.Contrato2Path.toString().substring(caracteres, docs.Contrato2Path.length() - caracteres));
                                ArrayList<RegistroImagen> aa = negocio.getImageWSX("C2", idSol);
                                for (RegistroImagen item : aa)
                                {
                                    try
                                    {
                                        row.DOC_C2 = item.img;
                                    }
                                    catch (Exception rr)
                                    {
                                        row.DOC_C2 = null;
                                        String hh = rr.getMessage();
                                    }
                                }

                            }
                            if (!docs.Extra1.toString().startsWith("...."))
                            {
                                publishProgress("..." + docs.Extra1.toString().substring(caracteres, docs.Extra1.length() - caracteres));
                                ArrayList<RegistroImagen> aa = negocio.getImageWSX("E1", idSol);
                                for (RegistroImagen item : aa)
                                {
                                    try
                                    {
                                        row.E1 = item.img;
                                    }
                                    catch (Exception rr)
                                    {
                                        row.E1 = null;
                                        String hh = rr.getMessage();
                                    }
                                }

                            }
                            if (!docs.Extra2.toString().startsWith("...."))
                            {
                                publishProgress("..." + docs.Extra2.toString().substring(caracteres, docs.Extra2.length() - caracteres));
                                ArrayList<RegistroImagen> aa = negocio.getImageWSX("E2", idSol);
                                for(RegistroImagen item : aa)
                                {
                                    try
                                    {
                                        row.E2 = item.img;
                                    }
                                    catch (Exception rr)
                                    {
                                        row.E2 = null;
                                        String hh = rr.getMessage();
                                    }
                                }

                            }
                            if (!docs.Extra3.toString().startsWith("...."))
                            {
                                publishProgress("..." + docs.Extra3.toString().substring(caracteres, docs.Extra3.length() - caracteres));
                                ArrayList<RegistroImagen> aa = negocio.getImageWSX("E3", idSol);
                                for (RegistroImagen item : aa)
                                {
                                    try
                                    {
                                        row.E3 = item.img;
                                    }
                                    catch (Exception rr)
                                    {
                                        row.E3 = null;
                                        String hh = rr.getMessage();
                                    }
                                }

                            }
                            if (!docs.Extra4.toString().startsWith("...."))
                            {
                                publishProgress("..." + docs.Extra4.toString().substring(caracteres, docs.Extra4.length() - caracteres));
                                ArrayList<RegistroImagen> aa = negocio.getImageWSX("E4", idSol);
                                for (RegistroImagen item : aa)
                                {
                                    try
                                    {
                                        row.E4 = item.img;
                                    }
                                    catch (Exception rr)
                                    {
                                        row.E4 = null;
                                        String hh = rr.getMessage();
                                    }
                                }

                            }
                            if (!docs.Extra5.toString().startsWith("...."))
                            {
                                publishProgress("..." + docs.Extra5.toString().substring(caracteres, docs.Extra5.length() - caracteres));
                                ArrayList<RegistroImagen> aa = negocio.getImageWSX("E5", idSol);
                                for (RegistroImagen item : aa)
                                {
                                    try
                                    {
                                        row.E5 = item.img;
                                    }
                                    catch (Exception rr)
                                    {
                                        row.E5 = null;
                                        String hh = rr.getMessage();
                                    }
                                }

                            }
                            if (!docs.FirmaPath.toString().startsWith("...."))
                            {
                                publishProgress("..." + docs.FirmaPath.toString().substring(caracteres, docs.FirmaPath.length() - caracteres));
                                ArrayList<RegistroImagen> aa = negocio.getImageWSX("F1", idSol);
                                for (RegistroImagen item : aa)
                                {
                                    try
                                    {
                                        row.F1 = item.img;
                                    }
                                    catch (Exception rr)
                                    {
                                        row.F1 = null;
                                        String hh = rr.getMessage();
                                    }
                                }

                            }

i++;
                            if(i>=2)break;
                        }
                        //----------------------------------------------------------------
                        Thread.sleep(2000);
                        publishProgress("Creando imagenes ...");
                        Boolean cc = negocio.InsertarImagenesBdX(buzonDesdeWs,getApplicationContext());

                        intRegresoAsync= 2;//"OK";
                       // publishProgress("reseteando formularios ...");

                    }

                }catch(Exception ex){
                    intRegresoAsync=3;//"ERROR";
                    strError=ex.getMessage().toString();

                }
                //---------------------------------------------------------------------------------------------
                resp = "Terminado....";
            } catch (Exception e) {
                e.printStackTrace();
                strError = e.getMessage();
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

            TableLayout waitingfor = (TableLayout) findViewById(R.id.waitingfor);
            TableLayout login = (TableLayout) findViewById(R.id.login);

            switch (intRegresoAsync){
                case 1:{
                    EditText userTxt=(EditText)findViewById(R.id.txtUsuario);
                    EditText txtPassword=(EditText)findViewById(R.id.txtPassword);
                    txtPassword.setText("");

                    Toast.makeText(getApplicationContext(),"Este perfil del promotor: " + userTxt.getText() + " no puede instalarse en este dispositivo (" + strUUID + "). \nYa esta instalado en otro dispositivo. \nSolicite al administrador el permiso correspondiente",Toast.LENGTH_LONG).show();
                    //hubo algun problema con el uuid
                    waitingfor.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.VISIBLE);
                }break;
                case 2:{
                    //ok  avanza
                    waitingfor.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.VISIBLE);

                    EditText txtPassword=(EditText)findViewById(R.id.txtPassword);
                    txtPassword.setText("");

                    Toast.makeText(getApplicationContext(),"Perfil creado,  Catálogo, Buzón y Citas Actualizados",Toast.LENGTH_LONG).show();
                    //TO_DO  INICIALIZAR OBJETOS DE LA BANDEJA
                    startActivity(new Intent(main.this, frmMenuPrincipal.class));
                }break;
                case 3:{
                    EditText txtPassword=(EditText)findViewById(R.id.txtPassword);
                    txtPassword.setText("");

                    Toast.makeText(getApplicationContext(),"Error: " + strError ,Toast.LENGTH_LONG).show();
                    //hubo algun problema
                    waitingfor.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.VISIBLE);
                }break;
            }

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
            TableLayout waitingfor = (TableLayout) findViewById(R.id.waitingfor);
            TableLayout login = (TableLayout) findViewById(R.id.login);
            waitingfor.setVisibility(View.VISIBLE);
            login.setVisibility(View.INVISIBLE);
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(String... text) {
            lblLog.setText(text[0]);
        }
    }


    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }


}//end parent class
