package maas.com.mx.maas.negocio;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.text.Editable;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javeros.anonimos.code.Rfc;
import com.javeros.anonimos.code.dto.PersonaRfcDto;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import maas.com.mx.maas.db.sql;
import maas.com.mx.maas.entidades.BUZON_A;
import maas.com.mx.maas.entidades.CatalogoX;
import maas.com.mx.maas.entidades.CitasMetrica;
import maas.com.mx.maas.entidades.Documento;
import maas.com.mx.maas.entidades.Productos;
import maas.com.mx.maas.entidades.Promotor;
import maas.com.mx.maas.entidades.RArgs;
import maas.com.mx.maas.entidades.RegistroBuzon;
import maas.com.mx.maas.entidades.RegistroImagen;
import maas.com.mx.maas.entidades.Solicitud;
import maas.com.mx.maas.entidades.Usuario;
import maas.com.mx.maas.entidades.objectItem;

/**
 * Created by damserver on 14/04/2015.
 */
public class Negocio  {

    sql db;
    String urlws;

    public  Negocio(Context context) throws Exception {
        db=new sql(context);
        urlws="http://74.208.98.86/OriginacionRest/mx.com.homebanking.service.svc/api/";

    }

    public void preparaBDLocal() throws Exception {

             db.cargaParametrosInicio();
             db.cambiaModalidad("OFF");
    }

    public Usuario buscarUsuarioEnWSX(Promotor prom) throws Exception {

        Usuario _user = new Usuario();

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(urlws+"login2/");
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        Gson gson=new Gson();

        //{"Compania":"SOFOM2","Contrasenia":"pmtrs","Coordinador":null,"Formato":null,"Gerente":null,"Promotoria":null,"RegPromotor":null,"TipoUsuario":"4","Usuario":"lilia"}
        HashMap<String, String> postParameters = new HashMap<String, String>();
        postParameters.put("Compania",prom.Compania );
        postParameters.put("Contrasenia", prom.Contrasenia);
        postParameters.put("Usuario", prom.Usuario);
        postParameters.put("TipoUsuario", "4");

        try {
            String jsonParameters = gson.toJson(postParameters);
            post.setEntity(new StringEntity(jsonParameters));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();

            //Regreso
            String hh=convertStreamToString(entity.getContent());
            //Cast to usuario
            Usuario strUsuario=gson.fromJson(hh, Usuario.class);
            //return
            _user=strUsuario;
        }catch(Exception ex){
             throw ex;
        }
     return _user;
    }

    public  String GetBuzonActivo() throws Exception {
           return db.getBuzonActivo();
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public ArrayList<BUZON_A> getSolicitudesPendientes(String bznActivo)throws Exception {

       return db.getSolicitudesPendientes(bznActivo);
    }

    public Boolean validaUUID(String idUsuario, String uuid)throws Exception {
       // RArgs res=new RArgs();
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(urlws+"uuid/");
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        Gson gson=new Gson();


        HashMap<String, String> postParameters = new HashMap<String, String>();
        postParameters.put("par1",idUsuario );
        postParameters.put("par2", uuid);


        try {
            String jsonParameters = gson.toJson(postParameters);
            post.setEntity(new StringEntity(jsonParameters));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();

            //Regreso
            String hh=convertStreamToString(entity.getContent());
            //Cast to usuario
            RArgs regreso=gson.fromJson(hh, RArgs.class);
            //return
            if(regreso.par1.toString().trim().startsWith("false"))return false;
            else return true;
        }catch(Exception ex){
            throw ex;
        }


    }

    public ArrayList<RegistroBuzon> obtenerBuzonDesdeWSX(Usuario prom) throws Exception {
        ArrayList<RegistroBuzon> registroBuzon=new ArrayList<RegistroBuzon>();

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(urlws+"buzon/");
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        Gson gson=new Gson();

        HashMap<String, String> postParameters = new HashMap<String, String>();
        postParameters.put("Compania",prom.Compania );
        postParameters.put("Contrasenia", prom.Password);
        postParameters.put("Usuario", prom.User);
        postParameters.put("TipoUsuario", "4");

            String jsonParameters = gson.toJson(postParameters);
            post.setEntity(new StringEntity(jsonParameters));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();

            //Regreso
            String strJsonResult=convertStreamToString(entity.getContent());

            //Cast to usuario
           Type fooType = new TypeToken<ArrayList<RegistroBuzon>>() {}.getType();
            ArrayList<RegistroBuzon> arrBuzon=gson.fromJson(strJsonResult, fooType);
            //return
            registroBuzon=arrBuzon;



        return registroBuzon;
    }

    public ArrayList<Productos> obtieneProductosWSX(Usuario prom)throws Exception {
        ArrayList<Productos> productos=new ArrayList<Productos>();


        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(urlws+"productos/");
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        Gson gson=new Gson();

        HashMap<String, String> postParameters = new HashMap<String, String>();
        postParameters.put("Compania",prom.Compania );
        postParameters.put("Contrasenia", prom.Password);
        postParameters.put("Usuario", prom.User);
        postParameters.put("TipoUsuario", "4");

        String jsonParameters = gson.toJson(postParameters);
        post.setEntity(new StringEntity(jsonParameters));

        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();

        //Regreso
        String strJsonResult=convertStreamToString(entity.getContent());

        //Cast to
        Type fooType = new TypeToken<ArrayList<Productos>>() {}.getType();
        ArrayList<Productos> arrBuzon=gson.fromJson(strJsonResult, fooType);
        //return
        productos=arrBuzon;


        return productos;
    }

    public ArrayList<CatalogoX> obtenerCatalogosWSX(Usuario prom)throws Exception {
        ArrayList<CatalogoX> catalogoX=new ArrayList<CatalogoX>();

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(urlws+"catalogos/");
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        Gson gson=new Gson();

        HashMap<String, String> postParameters = new HashMap<String, String>();
        postParameters.put("Compania",prom.Compania );
        postParameters.put("Contrasenia", prom.Password);
        postParameters.put("Usuario", prom.User);
        postParameters.put("TipoUsuario", "4");

        String jsonParameters = gson.toJson(postParameters);
        post.setEntity(new StringEntity(jsonParameters));

        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();

        //Regreso
        String strJsonResult=convertStreamToString(entity.getContent());

        //Cast to
        Type fooType = new TypeToken<ArrayList<CatalogoX>>() {}.getType();
        ArrayList<CatalogoX> arrBuzon=gson.fromJson(strJsonResult, fooType);
        //return
        catalogoX=arrBuzon;

        return catalogoX;
    }

    public ArrayList<CitasMetrica> obtenerMetricaCitasWSX(Usuario prom)throws Exception {
        ArrayList<CitasMetrica> citasMetrica=new ArrayList<CitasMetrica>();

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(urlws+"citasmetricas/");
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        Gson gson=new Gson();

        HashMap<String, String> postParameters = new HashMap<String, String>();
        postParameters.put("Compania",prom.Compania );
        postParameters.put("Contrasenia", prom.Password);
        postParameters.put("Usuario", prom.User);
        postParameters.put("TipoUsuario", "4");

        String jsonParameters = gson.toJson(postParameters);
        post.setEntity(new StringEntity(jsonParameters));

        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();

        //Regreso
        String strJsonResult=convertStreamToString(entity.getContent());

        //Cast to
        Type fooType = new TypeToken<ArrayList<CitasMetrica>>() {}.getType();
        ArrayList<CitasMetrica> arrBuzon=gson.fromJson(strJsonResult, fooType);
        //return
        citasMetrica=arrBuzon;

        return citasMetrica;
    }

    public Boolean BorrarimagenesX(Context context)throws Exception {

        File dirFiles = context.getFilesDir();
        for (String strFile : dirFiles.list())
        {
            if(strFile.startsWith("TEC_") )
                context.deleteFile(strFile);
        }


        return false;
    }

    public Boolean InsertarBuzonBdX(ArrayList<RegistroBuzon> buzonDesdeWs)throws Exception  {

        String bznActivo=GetBuzonActivo();

        if(bznActivo.toString().equals("A")){

            db.limpiaBuzon("B");
            db.insertaBuzon(buzonDesdeWs,"B");
            db.limpiaBuzon("A");
            db.setBuzonActivo("B");
        }else{
            db.limpiaBuzon("A");
            db.insertaBuzon(buzonDesdeWs,"A");
            db.limpiaBuzon("B");
            db.setBuzonActivo("A");
        }
        return true;
    }


    public Boolean InsertCatalogoBdX(ArrayList<CatalogoX> catalogodesdeWs, String versionCatBdLocal, ArrayList<Productos> objProductoResp)throws Exception {
        String bznActivo=GetBuzonActivo();

        /*if(bznActivo.toString().equals("A")){
            db.limpiaCatalogo("B");
            db.limpiaProductos("B");
            db.insertaCatalogos(catalogodesdeWs, "B");
            db.insertaProductos(objProductoResp, "B");
            db.limpiaCatalogo("A");
            db.limpiaProductos("A");
        }else{
            db.limpiaCatalogo("A");
            db.limpiaProductos("A");
            db.insertaCatalogos(catalogodesdeWs, "A");
            db.insertaProductos(objProductoResp, "A");
            db.limpiaCatalogo("B");
            db.limpiaProductos("B");
        }*/
        if(bznActivo.toString().equals("A")){
            db.limpiaCatalogo("A");
            db.limpiaProductos("A");
            db.insertaCatalogos(catalogodesdeWs, "A");
            db.insertaProductos(objProductoResp, "A");
            db.limpiaCatalogo("B");
            db.limpiaProductos("B");
        }else{
            db.limpiaCatalogo("B");
            db.limpiaProductos("B");
            db.insertaCatalogos(catalogodesdeWs, "B");
            db.insertaProductos(objProductoResp, "B");
            db.limpiaCatalogo("A");
            db.limpiaProductos("A");
        }
        return true;
     }

    public Boolean InsertarUsuarioBDX(Usuario logeado, String compania) {

        db.limpiaUsuario();
        db.insertaUsuario(logeado, compania);
        return true;
    }


    public Documento getDocsFromXml(String id_solicitud)throws Exception {

        Documento documento=new Documento();

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(urlws+"documento/");
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        Gson gson=new Gson();

        HashMap<String, String> postParameters = new HashMap<String, String>();
        postParameters.put("par1",id_solicitud );


        String jsonParameters = gson.toJson(postParameters);
        post.setEntity(new StringEntity(jsonParameters));

        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();

        //Regreso
        String strJsonResult=convertStreamToString(entity.getContent());

        //Cast to
        Documento arrDocs=gson.fromJson(strJsonResult, Documento.class);
        //return

        documento=arrDocs;

        return documento;
    }

    public ArrayList<RegistroImagen> getImageWSX(String tipoDoc, String idSolicitud)throws Exception {

        //byte[] dummy = new byte[4];
        ArrayList<RegistroImagen> listImagen = new ArrayList<RegistroImagen>();

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(urlws+"imagen/");
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        Gson gson=new Gson();

        HashMap<String, String> postParameters = new HashMap<String, String>();
        postParameters.put("tipoDoc",tipoDoc );
        postParameters.put("idSolicitud",idSolicitud );


        String jsonParameters = gson.toJson(postParameters);
        post.setEntity(new StringEntity(jsonParameters));

        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();

        //Regreso
        String strJsonResult=convertStreamToString(entity.getContent());

        //Cast to
        Type fooType = new TypeToken<ArrayList<RegistroImagen>>() {}.getType();
        ArrayList<RegistroImagen> arrDocs=gson.fromJson(strJsonResult, fooType);
        //return

        listImagen=arrDocs;



        return listImagen;
    }

    public Boolean InsertarImagenesBdX(ArrayList<RegistroBuzon> buzonDesdeWs,Context context)throws Exception {

        if (buzonDesdeWs != null) {
            for(RegistroBuzon row: buzonDesdeWs){
                 Documento docs=row.doc;

                if (row.DOC_IF != null){if(row.DOC_IF.length>4)generaArchivos(row.DOC_IF, docs.IdentificacionFrentePath,context);  }
                if (row.DOC_IA != null){if(row.DOC_IA.length>4)generaArchivos(row.DOC_IA, docs.IdentificacionAtrasPath,context);  }
                if (row.DOC_C1 != null){if(row.DOC_C1.length>4)generaArchivos(row.DOC_C1, docs.Contrato1Path,context);  }
                if (row.DOC_C2 != null){if(row.DOC_C2.length>4)generaArchivos(row.DOC_C2, docs.Contrato2Path,context);  }
                if (row.E1 != null){if(row.E1.length>4)generaArchivos(row.E1, docs.Extra1,context);  }
                if (row.E2 != null){if(row.E2.length>4)generaArchivos(row.E2, docs.Extra2,context);  }
                if (row.E3 != null){if(row.E3.length>4)generaArchivos(row.E3, docs.Extra3,context);  }
                if (row.E4 != null){if(row.E4.length>4)generaArchivos(row.E4, docs.Extra4,context);  }
                if (row.E5 != null){if(row.E5.length>4)generaArchivos(row.E5, docs.Extra5,context);  }
                if (row.F1 != null){if(row.F1.length>4)generaArchivos(row.F1, docs.FirmaPath,context);  }
            }
        }
        return true;
    }

    private void generaArchivos(byte[] fileBytes, String nameFile,Context context) throws Exception{

        if (fileBytes.length <= 4) return;



       String filePath = context.getFilesDir().getPath().toString() +"/"+ nameFile;

        File traceFile = new File(filePath);
        if (!traceFile.exists())
            traceFile.createNewFile();

        FileOutputStream fos =new FileOutputStream(traceFile,true);
        fos.write(fileBytes);
        fos.close();


        //File resolveMeSDCard = new File(filePath);
        //resolveMeSDCard.createNewFile();
        //FileOutputStream fos = new FileOutputStream(resolveMeSDCard);
        //fos.write(fileBytes);
        //fos.close();



    }

    public Usuario BuscarUsuario(String usuario, String pass, String empresa) throws Exception{
       return db.BuscarUsuario(usuario,pass,empresa);
    }

    public String GetMetricaEstatus(String estatus) throws Exception {
        String cantidad = "0";
        if (GetBuzonActivo().toString().equals("A"))
        {
            cantidad = db.GetMetricaEstatus("A",estatus);
        }
        else
        {
            cantidad = db.GetMetricaEstatus("B",estatus);
        }

        return cantidad;
    }

    public ArrayList<Solicitud> getSolicitudes()throws Exception {

        ArrayList<Solicitud> buzonLocal;
        if (GetBuzonActivo().toString().equals("A")) {
            buzonLocal = GetSolicitudesDesdeBd("A");

        }else{
            buzonLocal = GetSolicitudesDesdeBd("B");
        }

        return buzonLocal;
    }

    private ArrayList<Solicitud> GetSolicitudesDesdeBd(String tipo)throws Exception {

        return db.GetSolicitudesDesdeBd_A(tipo);

    }

    public objectItem[] CargarCatalogoComun(String tipo)throws Exception {

        return db.CargarCatalogoComun(tipo,GetBuzonActivo().toString());
    }

    public objectItem[] CargarCatalogoDelegMunicipio(String catActivo, String idEstado)throws Exception {
        return db.CargarCatalogoDelegMunicipio(catActivo, idEstado);
    }

    public String RFC13Pocisiones(String paterno, String materno, String nombre, String strFecha)throws Exception {

        Rfc r=new Rfc();
        PersonaRfcDto persona=new PersonaRfcDto();
        persona.setNombre(nombre);
        persona.setApPaterno(paterno);
        persona.setApMaterno(materno);
        persona.setFecha(strFecha);

        return r.generarRfc(persona);

    }

    public Solicitud getSolicitud(String idSolicitud)throws Exception {
        return db.GetSolicitud(idSolicitud,GetBuzonActivo().toString());
    }
}
