package maas.com.mx.maas.db;

import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.DocumentsContract;
//import android.provider.DocumentsContract;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import maas.com.mx.maas.entidades.BUZON_A;
import maas.com.mx.maas.entidades.CatalogoX;
import maas.com.mx.maas.entidades.Productos;
import maas.com.mx.maas.entidades.RegistroBuzon;
import maas.com.mx.maas.entidades.Solicitud;
import maas.com.mx.maas.entidades.SolicitudType;
import maas.com.mx.maas.entidades.Usuario;
import maas.com.mx.maas.entidades.objectItem;


/**
 * Created by damserver on 16/04/2015.
 */
public class sql extends SQLiteOpenHelper {

    public sql(Context context) {
        super(context,"sql",null,1);
    }


    public void creaTablas(SQLiteDatabase db) throws SQLException {
        // database = dbHelper.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS PARAMETRO(ID_PARAMETRO integer primary key not null,"
                + "PARMETRO varchar(50),"
                + "VALOR varchar(100),"
                + "ESTATUS varchar(1)"
                + " );");

        db.execSQL("CREATE TABLE IF NOT EXISTS USUARIO(ID_USUARIO integer primary key not null,"
                + "USER varchar(25),"
                + "CONTRASENIA varchar(25),"
                + "TIPO_USUARIO text,"
                + "COMPANIA varchar(25)"
                + " );");

        db.execSQL("CREATE TABLE IF NOT EXISTS CATALOGO_A(ID_CATALOGO integer primary key not null,"
                + "DESCRIPCION varchar(100),"
                + "ID_TIPO_CATALOGO varchar(1),"
                + "PADRE varchar(1)"
                + " );");

        db.execSQL("CREATE TABLE IF NOT EXISTS CATALOGO_B(ID_CATALOGO integer primary key not null,"
                + "DESCRIPCION varchar(100),"
                + "ID_TIPO_CATALOGO varchar(1),"
                + "PADRE varchar(1)"
                + " );");

        db.execSQL("CREATE TABLE IF NOT EXISTS CitasMetrica("
                + "TOTAL varchar(10),"
                + "NUEVA varchar(10),"
                + "CANCELADA varchar(10),"
                + "REALIZADA varchar(10),"
                + "CADUCADA varchar(10),"
                + "EXITOSA varchar(10)"
                + " );");

        db.execSQL("CREATE TABLE IF NOT EXISTS ProductoA(ID_PRODUCTO integer primary key not null,"
                + "NOMBRE varchar(50),"
                + "DESCRIPCION varchar(200),"
                + "ESTATUS varchar(1),"
                + "FECHA_CREACION varchar(12),"
                + "ID_EMPRESA integer,"
                + "COMERCIAL text"
                + " );");

        db.execSQL("CREATE TABLE IF NOT EXISTS ProductoB(ID_PRODUCTO integer primary key not null,"
                + "NOMBRE varchar(50),"
                + "DESCRIPCION varchar(200),"
                + "ESTATUS varchar(1),"
                + "FECHA_CREACION varchar(12),"
                + "ID_EMPRESA integer,"
                + "COMERCIAL text"
                + " );");

        db.execSQL("CREATE TABLE IF NOT EXISTS BUZON_A(ID_SOLICITUD integer primary key not null,"
                + "FECHA_ALTA datetime,"
                + "ESTATUS integer,"
                + "ID_USUARIO integer,"
                + "COMENTARIO varchar(500),"
                + "MOTIVO integer,"
                + "FECHA_MODIFICACION datetime,"
                + "SOLICITUD_XML text"
                + " );");

        db.execSQL("CREATE TABLE IF NOT EXISTS BUZON_B(ID_SOLICITUD integer primary key not null,"
                + "FECHA_ALTA datetime,"
                + "ESTATUS integer,"
                + "ID_USUARIO integer,"
                + "COMENTARIO varchar(500),"
                + "MOTIVO integer,"
                + "FECHA_MODIFICACION datetime,"
                + "SOLICITUD_XML text"
                + " );");

    }



    @Override
    public void onCreate(SQLiteDatabase db)throws SQLException {

        creaTablas(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public void cargaParametrosInicio()throws SQLException {
        SQLiteDatabase dbr = getReadableDatabase();
        Boolean bContinua=false;
        Cursor c=dbr.rawQuery("SELECT * FROM PARAMETRO WHERE PARMETRO='CONFIG_INICIO' AND VALOR='1'", null);
        if(c.moveToFirst())
        {
            bContinua =true;
        }
        else
        {
            bContinua =false;
        }
        c.close();

        if(bContinua==false){
            SQLiteDatabase db = getWritableDatabase();
            //inserta parametros
            db.execSQL("INSERT INTO  PARAMETRO VALUES(1,'BUZON_ACTIVO','A','1')");
            db.execSQL("INSERT INTO  PARAMETRO VALUES(2,'CATALOGO_ACTIVO','A','1');");
            db.execSQL("INSERT INTO  PARAMETRO VALUES(3,'CONFIG_INICIO','1','1');");
            db.execSQL("INSERT INTO  PARAMETRO VALUES(4,'CATALOGO_VERSION','1.0','1');");
            db.execSQL("INSERT INTO  PARAMETRO VALUES(5,'MODALIDAD','OFF','1');");
            db.execSQL("INSERT INTO  PARAMETRO VALUES(6,'AGENDA_ACTIVA','A','1');");
            db.execSQL("INSERT INTO  PARAMETRO VALUES(7,'REFRESH_MODALIDAD','0','1');");

        }

    }

    public void cambiaModalidad(String valor) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("update PARAMETRO set valor='"+valor+"' where  PARMETRO ='MODALIDAD'");

    }

    public String getBuzonActivo()throws SQLException {
        SQLiteDatabase db = getReadableDatabase();
        String regreso="A";
        Cursor c=db.rawQuery("select VALOR from PARAMETRO WHERE PARMETRO='BUZON_ACTIVO'", null);
        if(c.moveToFirst())
        {
           regreso= c.getString(0);
        }
        c.close();
        return regreso;
    }


    public ArrayList<BUZON_A> getSolicitudesPendientes(String bznActivo)throws SQLException {
        ArrayList<BUZON_A> buzonLocal=new ArrayList<BUZON_A>();

        SQLiteDatabase db = getReadableDatabase();

        if(bznActivo.toString().equals("A")) {
            Cursor c = db.rawQuery("select * from BUZON_A WHERE ESTATUS IN(6,7) LIMIT 10", null);
            while (c.moveToNext()) {
                buzonLocal.add((BUZON_A) c);
            }
            c.close();
        }else{
            Cursor c = db.rawQuery("select * from BUZON_B WHERE ESTATUS IN(6,7) LIMIT 10", null);
            while (c.moveToNext()) {
                buzonLocal.add((BUZON_A) c);
            }
            c.close();
        }


        return buzonLocal;
    }


    public void limpiaBuzon(String buzon) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE from BUZON_"+buzon);
    }

    public void insertaBuzon(ArrayList<RegistroBuzon> buzonDesdeWs, String buzon)throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        //inserta parametros

        /*for(int i=0;i<buzonDesdeWs.size()-1;i++){
            db.execSQL("INSERT INTO  BUZON_"+buzon+" VALUES("+buzonDesdeWs.get(i).ID_SOLICITUD.toString() +",'"+buzonDesdeWs.get(i).COMENTARIO.toString()+"',"+buzonDesdeWs.get(i).ESTATUS.toString()+",'"+buzonDesdeWs.get(i).SOLICITUD_XML.toString() +"','"+buzonDesdeWs.get(i).FECHA_MODIFICACION.toString()+"')");
        }*/

        for(RegistroBuzon item: buzonDesdeWs){

            item.COMENTARIO=item.COMENTARIO==null?"":item.COMENTARIO;
            if(item.ID_SOLICITUD==null)continue;
            db.execSQL("INSERT INTO  BUZON_"+buzon+"(ID_SOLICITUD,COMENTARIO,ESTATUS,SOLICITUD_XML,FECHA_MODIFICACION) VALUES("
                    +item.ID_SOLICITUD.toString() +",'"+item.COMENTARIO.toString()+"',"+item.ESTATUS.toString()+",'"+item.SOLICITUD_XML.toString() +"','"+item.FECHA_MODIFICACION.toString()+"')");
        }


        for (int i = 1; i <= 100; i++)
        {
            db.execSQL("INSERT INTO  BUZON_"+buzon+" (ID_SOLICITUD) VALUES("+Integer.toString(i) +")");
        }

        //db.execSQL("update PARAMETRO set valor='"+buzon+"' where  PARMETRO ='BUZON_ACTIVO'");
    }

    public void limpiaCatalogo(String buzon) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE from CATALOGO_"+buzon);
    }

    public void limpiaProductos(String buzon) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE from Producto"+buzon);
    }

    public void insertaCatalogos(ArrayList<CatalogoX> catalogodesdeWs, String buzon) {
        SQLiteDatabase db = getWritableDatabase();
        //inserta parametros

        for(CatalogoX item: catalogodesdeWs){
            db.execSQL("INSERT INTO  CATALOGO_"+buzon+" VALUES("+item.ID_CATALOGO.toString() +",'"+item.DESCRIPCION.toString()+"',"+item.ID_TIPO_CATALOGO.toString()+",'"+item.PADRE.toString() +"')");
        }

        db.execSQL("update PARAMETRO set valor='"+buzon+"' where  PARMETRO ='CATALOGO_ACTIVO'");
    }

    public void insertaProductos(ArrayList<Productos> objProductoResp, String buzon) {
        SQLiteDatabase db = getWritableDatabase();
        //inserta parametros

        for(Productos item: objProductoResp){
            if (item.COMERCIAL == null) item.COMERCIAL="";

                db.execSQL("INSERT INTO  Producto" + buzon + " VALUES(" + item.ID_PRODUCTO + ",'" + item.NOMBRE.toString() + "','" + item.DESCRIPCION.toString() + "','" + item.ESTATUS + "','" + item.FECHA_CREACION.toString() + "'," + item.ID_EMPRESA + ",'" + item.COMERCIAL.toString() + "')");

        }
    }

    public void limpiaUsuario() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE from USUARIO");
    }

    public void insertaUsuario(Usuario U, String compania) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO  USUARIO VALUES("+U.IdUsuario.toString() +",'"+U.User.toString()+"','"+U.Password.toString()+"','"+U.Tipo_Usuario.toString() +"','"+U.Compania.toString()+"')");
    }

    public Usuario BuscarUsuario(String usuario, String usuarioPass, String compania) {

        Usuario user=new Usuario();
        user.Logeado="-1";

        Boolean existe=false;
        SQLiteDatabase db = getReadableDatabase();
        String regreso="A";
        Cursor c=db.rawQuery("select * from USUARIO where USER='" + usuario + "' and CONTRASENIA='" + usuarioPass + "' and COMPANIA='" + compania + "'", null);
        if(c.moveToFirst())
        {
            user.Compania=c.getString(4);
            user.User=c.getString(1);
            user.Logeado= "Promotor:" + c.getString(1) + " Compañia:" + c.getString(4).toUpperCase();
            user.Tipo_Usuario=c.getString(3);
            user.Password=c.getString(2);
            existe=true;
        }
        c.close();

         /* "USER varchar(25),"                   1
                + "CONTRASENIA varchar(25),"    2
                + "TIPO_USUARIO text,"          3
                + "COMPANIA varchar(25)"        4*/

        return user;


    }

    public String GetMetricaEstatus(String buzon,String estatus) {
        SQLiteDatabase db = getReadableDatabase();
        String regreso="0";
        Cursor c=db.rawQuery("select COUNT(*) from BUZON_"+buzon+" WHERE ESTATUS IN("+estatus+") ", null);
        if(c.moveToFirst())
        {
            regreso= c.getString(0);
        }
        c.close();
        return regreso;




    }

    public ArrayList<Solicitud> GetSolicitudesDesdeBd_A(String tipo) {
        ArrayList<Solicitud> solicitudes=new ArrayList<Solicitud>();
        Solicitud sol;

        SQLiteDatabase db = getReadableDatabase();

        Cursor c=db.rawQuery("select * from BUZON_"+tipo+"  WHERE estatus IN(7,6,5,3,2,1,9) ORDER BY FECHA_MODIFICACION ASC", null);
        while (c.moveToNext())
        {
            String color = "";
            if (c.getString(2).toString().equals("1") || c.getString(2).toString().equals("5")) color = "Blue";
            if (c.getString(2).toString().equals("2")) color = "Green";
            if (c.getString(2).toString().equals("6") || c.getString(2).toString().equals("7")) color = "LightGray";
            if (c.getString(2).toString().equals("9")) color = "Yellow";
            if (c.getString(2).toString().equals("3")) color = "Red";

            sol=new Solicitud();
            sol.IdSolicitud=c.getString(0)==null?"":c.getString(0);
            sol.FechaAlta= obtenerFechaaltaDesdeSolIcitud(c.getString(7)==null?"":c.getString(7));
            sol.Nombre= obtenerNombreDesdeSolIcitud(c.getString(7)==null?"":c.getString(7));
            sol.Color=color;
            sol.Estatus=c.getString(2)==null?"":c.getString(2);
            sol.Comentario=c.getString(4)==null?"":c.getString(4);
            sol.SolicitudXML=c.getString(7)==null?"":c.getString(7);
            sol.FechaModificacion=c.getString(6)==null?"":c.getString(6);

            solicitudes.add(sol);
        }
        c.close();
        /*ID_SOLICITUD integer primary key not null,"
                + "FECHA_ALTA datetime,"
                + "ESTATUS integer,"
                + "ID_USUARIO integer,"
                + "COMENTARIO varchar(500),"
                + "MOTIVO integer,"
                + "FECHA_MODIFICACION datetime,"
                + "SOLICITUD_XML text"*/


        return solicitudes;
    }

    private String obtenerNombreDesdeSolIcitud(String strXml) {

        String Pmrnombre = "";
        String Sdonombre = "";
        String Apaterno = "";
        String Amaterno = "";


        if (strXml != null) {
        String xml = strXml.replace("xmlns=\"http://schemas.datacontract.org/2004/07/Originacion.Entidades\"", "");

            InputSource source = new InputSource(new StringReader(xml));
        try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(source);

                XPathFactory xpathFactory = XPathFactory.newInstance();
                XPath xpath = xpathFactory.newXPath();

                Pmrnombre = xpath.evaluate("/SolicitudType/generales/Pmrnombre", document);
                Sdonombre = xpath.evaluate("/SolicitudType/generales/Sdonombre", document);
            Apaterno = xpath.evaluate("/SolicitudType/generales/Apaterno", document);
            Amaterno = xpath.evaluate("/SolicitudType/generales/Amaterno", document);
        }catch(Exception ex){

        }


        }

        return Pmrnombre+" "+Sdonombre+" "+Apaterno+" "+Amaterno;
    }

    private String obtenerFechaaltaDesdeSolIcitud(String strXml) {

        String DiaCreacion = "";
        String MesCreacion = "";
        String AnioCreacion = "";


        if (strXml != null) {
            String xml = strXml.replace("xmlns=\"http://schemas.datacontract.org/2004/07/Originacion.Entidades\"", "");

            InputSource source = new InputSource(new StringReader(xml));
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(source);

                XPathFactory xpathFactory = XPathFactory.newInstance();
                XPath xpath = xpathFactory.newXPath();

                DiaCreacion = xpath.evaluate("/SolicitudType/DiaCreacion", document);
                MesCreacion = xpath.evaluate("/SolicitudType/MesCreacion", document);
                AnioCreacion = xpath.evaluate("/SolicitudType/AnioCreacion", document);
            }catch(Exception ex){

            }
        }

        return DiaCreacion+"/"+MesCreacion+"/"+AnioCreacion;

    }

    public objectItem[] CargarCatalogoComun(String tipo,String catActivo) {

        objectItem[] regreso = new objectItem[0];
        try {
            SQLiteDatabase db = getReadableDatabase();

            Cursor c = db.rawQuery("select * from CATALOGO_" + catActivo + " WHERE ID_TIPO_CATALOGO=" + tipo, null);
            while (c.moveToNext()) {

                regreso = (objectItem[]) appendValue(regreso, new objectItem(c.getString(0).toString(), c.getString(1).toString()));
            }
            c.close();
         }
        catch(Exception er){
            String hh="";
        }


        return regreso;
    }

    private objectItem[] appendValue(objectItem[] obj, objectItem newObj) {
        objectItem[] result = Arrays.copyOf(obj, obj.length +1);
        result[obj.length] = newObj;
        return result;
    }

    public void setBuzonActivo(String buzon) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("update PARAMETRO set valor='"+buzon+"' where  PARMETRO ='BUZON_ACTIVO'");
    }

    public objectItem[] CargarCatalogoDelegMunicipio(String catActivo, String idEstado) {
        objectItem[] regreso = new objectItem[0];
        try {
            SQLiteDatabase db = getReadableDatabase();

            Cursor c = db.rawQuery("select * from CATALOGO_"+catActivo+" WHERE ID_TIPO_CATALOGO=6 AND PADRE=" + idEstado, null);
            while (c.moveToNext()) {

                regreso = (objectItem[]) appendValue(regreso, new objectItem(c.getString(0).toString(), c.getString(1).toString()));
            }
            c.close();
        }
        catch(Exception er){
            String hh="";
        }


        return regreso;
    }

    public SolicitudType GetSolicitud(String idSolicitud, String tipo) {

        SolicitudType objSol=new SolicitudType();
        String strXml="";

        SQLiteDatabase sql = getReadableDatabase();

        Cursor c=sql.rawQuery("select SOLICITUD_XML from BUZON_"+tipo+"  WHERE ID_SOLICITUD="+idSolicitud, null);
        while (c.moveToNext())
        {
            strXml=c.getString(0)==null?"":c.getString(0);
        }
        c.close();


        if (!strXml.trim().equals("")) {
            String xml = strXml.replace("xmlns=\"http://schemas.datacontract.org/2004/07/Originacion.Entidades\"", "");

            InputSource source = new InputSource(new StringReader(xml));
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(source);

                XPathFactory xpathFactory = XPathFactory.newInstance();
                XPath xpath = xpathFactory.newXPath();

                objSol.generales.Tpoidentif= xpath.evaluate("/SolicitudType/generales/Tpoidentif", document);
                objSol.generales.Noidenficacion= xpath.evaluate("/SolicitudType/generales/Noidenficacion", document);
                objSol.generales.Pmrnombre = xpath.evaluate("/SolicitudType/generales/Pmrnombre", document);
                objSol.generales.Sdonombre = xpath.evaluate("/SolicitudType/generales/Sdonombre", document);
                objSol.generales.Apaterno = xpath.evaluate("/SolicitudType/generales/Apaterno", document);
                objSol.generales.Amaterno = xpath.evaluate("/SolicitudType/generales/Amaterno", document);
                objSol.generales.Sexo= xpath.evaluate("/SolicitudType/generales/Sexo", document);
                objSol.generales.Nacionalidad= xpath.evaluate("/SolicitudType/generales/Nacionalidad", document);
                objSol.generales.Fechanacdia= xpath.evaluate("/SolicitudType/generales/Fechanacdia", document);
                objSol.generales.Rfc= xpath.evaluate("/SolicitudType/generales/Rfc", document);
                objSol.generales.Edocivil= xpath.evaluate("/SolicitudType/generales/Edocivil", document);
                objSol.generales.Nodependiente= xpath.evaluate("/SolicitudType/generales/Nodependiente", document);
                objSol.generales.Cveperspol= xpath.evaluate("/SolicitudType/generales/Cveperspol", document);
                objSol.generales.FechasnacMes= xpath.evaluate("/SolicitudType/generales/FechasnacMes", document);
                objSol.generales.FechanacAnio= xpath.evaluate("/SolicitudType/generales/FechanacAnio", document);

                objSol.doc.IdentificacionFrentePath= xpath.evaluate("/SolicitudType/doc/IdentificacionFrentePath", document);
                objSol.doc.IdentificacionAtrasPath= xpath.evaluate("/SolicitudType/doc/IdentificacionAtrasPath", document);
                objSol.doc.Contrato1Path= xpath.evaluate("/SolicitudType/doc/Contrato1Path", document);
                objSol.doc.Contrato2Path= xpath.evaluate("/SolicitudType/doc/Contrato2Path", document);
                objSol.doc.FirmaPath= xpath.evaluate("/SolicitudType/doc/FirmaPath", document);
                objSol.doc.Extra1= xpath.evaluate("/SolicitudType/doc/Extra1", document);
                objSol.doc.Extra2= xpath.evaluate("/SolicitudType/doc/Extra2", document);
                objSol.doc.Extra3= xpath.evaluate("/SolicitudType/doc/Extra3", document);
                objSol.doc.Extra4= xpath.evaluate("/SolicitudType/doc/Extra4", document);
                objSol.doc.Extra5= xpath.evaluate("/SolicitudType/doc/Extra5", document);

                objSol.domicilio.Calle= xpath.evaluate("/SolicitudType/domicilio/Calle", document);
                objSol.domicilio.NoInt= xpath.evaluate("/SolicitudType/domicilio/NoInt", document);
                objSol.domicilio.NoExt= xpath.evaluate("/SolicitudType/domicilio/NoExt", document);
                objSol.domicilio.Cpdom= xpath.evaluate("/SolicitudType/domicilio/Cpdom", document);
                objSol.domicilio.Estado= xpath.evaluate("/SolicitudType/domicilio/Estado", document);
                objSol.domicilio.Delegacion= xpath.evaluate("/SolicitudType/domicilio/Delegacion", document);
                objSol.domicilio.Colonia= xpath.evaluate("/SolicitudType/domicilio/Colonia", document);
                objSol.domicilio.TiempoResidencia= xpath.evaluate("/SolicitudType/domicilio/TiempoResidencia", document);
                objSol.domicilio.EstatusResidencia= xpath.evaluate("/SolicitudType/domicilio/EstatusResidencia", document);
                objSol.domicilio.MontoVivienda= xpath.evaluate("/SolicitudType/domicilio/MontoVivienda", document);
                objSol.domicilio.Email= xpath.evaluate("/SolicitudType/domicilio/Email", document);
                objSol.domicilio.Telcasa= xpath.evaluate("/SolicitudType/domicilio/Telcasa", document);
                objSol.domicilio.Telmovil= xpath.evaluate("/SolicitudType/domicilio/Telmovil", document);
                objSol.domicilio.CompaniaMovil= xpath.evaluate("/SolicitudType/domicilio/CompaniaMovil", document);

                objSol.Personapolitica.TipoParentesco= xpath.evaluate("/SolicitudType/Personapolitica/TipoParentesco", document);
                objSol.Personapolitica.Descfuncion= xpath.evaluate("/SolicitudType/Personapolitica/Descfuncion", document);
                objSol.Personapolitica.Descparentesco= xpath.evaluate("/SolicitudType/Personapolitica/Descparentesco", document);
                objSol.Personapolitica.TieneParentesco= xpath.evaluate("/SolicitudType/Personapolitica/TieneParentesco", document);
                objSol.Personapolitica.EsPersonaPolitica= xpath.evaluate("/SolicitudType/Personapolitica/EsPersonaPolitica", document);

                objSol.Refer.Pmrnombre= xpath.evaluate("/SolicitudType/Refer/Pmrnombre", document);
                objSol.Refer.Sdonombre= xpath.evaluate("/SolicitudType/Refer/Sdonombre", document);
                objSol.Refer.Apaterno= xpath.evaluate("/SolicitudType/Refer/Apaterno", document);
                objSol.Refer.Amaterno= xpath.evaluate("/SolicitudType/Refer/Amaterno", document);
                objSol.Refer.Nacionalidad= xpath.evaluate("/SolicitudType/Refer/Nacionalidad", document);
                objSol.Refer.TelefonoCasa= xpath.evaluate("/SolicitudType/Refer/TelefonoCasa", document);

                objSol.Refer2.Pmrnombre= xpath.evaluate("/SolicitudType/Refer2/Pmrnombre", document);
                objSol.Refer2.Sdonombre= xpath.evaluate("/SolicitudType/Refer2/Sdonombre", document);
                objSol.Refer2.Apaterno= xpath.evaluate("/SolicitudType/Refer2/Apaterno", document);
                objSol.Refer2.Amaterno= xpath.evaluate("/SolicitudType/Refer2/Amaterno", document);
                objSol.Refer2.Nacionalidad= xpath.evaluate("/SolicitudType/Refer2/Nacionalidad", document);
                objSol.Refer2.TelefonoCasa= xpath.evaluate("/SolicitudType/Refer2/TelefonoCasa", document);

                objSol.Refer3.Pmrnombre= xpath.evaluate("/SolicitudType/Refer3/Pmrnombre", document);
                objSol.Refer3.Sdonombre= xpath.evaluate("/SolicitudType/Refer3/Sdonombre", document);
                objSol.Refer3.Apaterno= xpath.evaluate("/SolicitudType/Refer3/Apaterno", document);
                objSol.Refer3.Amaterno= xpath.evaluate("/SolicitudType/Refer3/Amaterno", document);
                objSol.Refer3.Nacionalidad= xpath.evaluate("/SolicitudType/Refer3/Nacionalidad", document);
                objSol.Refer3.TelefonoCasa= xpath.evaluate("/SolicitudType/Refer3/TelefonoCasa", document);

                objSol.Promotor.Compania= xpath.evaluate("/SolicitudType/Promotor/Compania", document);
                objSol.Promotor.Usuario= xpath.evaluate("/SolicitudType/Promotor/Usuario", document);
                objSol.Promotor.Contrasenia= xpath.evaluate("/SolicitudType/Promotor/Contrasenia", document);

                objSol.FolioLocal= xpath.evaluate("/SolicitudType/FolioLocal", document);
                objSol.DiaCreacion= xpath.evaluate("/SolicitudType/DiaCreacion", document);
                objSol.MesCreacion= xpath.evaluate("/SolicitudType/MesCreacion", document);
                objSol.AnioCreacion= xpath.evaluate("/SolicitudType/AnioCreacion", document);

                objSol.Deconominos.TipoContrato= xpath.evaluate("/SolicitudType/Deconominos/TipoContrato", document);
                objSol.Deconominos.AntiguedadEmpleo= xpath.evaluate("/SolicitudType/Deconominos/AntiguedadEmpleo", document);
                objSol.Deconominos.AniosCasada= xpath.evaluate("/SolicitudType/Deconominos/AniosCasada", document);
                objSol.Deconominos.Ingresos= xpath.evaluate("/SolicitudType/Deconominos/Ingresos", document);
                objSol.Deconominos.NombreEmpresa= xpath.evaluate("/SolicitudType/Deconominos/NombreEmpresa", document);
                objSol.Deconominos.GiroEmpresa= xpath.evaluate("/SolicitudType/Deconominos/GiroEmpresa", document);
                objSol.Deconominos.Puesto= xpath.evaluate("/SolicitudType/Deconominos/Puesto", document);

                objSol.Deconominos.Domicilio.Calle= xpath.evaluate("/SolicitudType/Deconominos/Domicilio/Calle", document);
                objSol.Deconominos.Domicilio.NoInt= xpath.evaluate("/SolicitudType/Deconominos/Domicilio/NoInt", document);
                objSol.Deconominos.Domicilio.NoExt= xpath.evaluate("/SolicitudType/Deconominos/Domicilio/NoExt", document);
                objSol.Deconominos.Domicilio.Cpdom= xpath.evaluate("/SolicitudType/Deconominos/Domicilio/Cpdom", document);
                objSol.Deconominos.Domicilio.Estado= xpath.evaluate("/SolicitudType/Deconominos/Domicilio/Estado", document);
                objSol.Deconominos.Domicilio.Delegacion= xpath.evaluate("/SolicitudType/Deconominos/Domicilio/Delegacion", document);
                objSol.Deconominos.Domicilio.Colonia= xpath.evaluate("/SolicitudType/Deconominos/Domicilio/Colonia", document);
                objSol.Deconominos.Domicilio.TiempoResidencia= xpath.evaluate("/SolicitudType/Deconominos/Domicilio/TiempoResidencia", document);
                objSol.Deconominos.Domicilio.EstatusResidencia= xpath.evaluate("/SolicitudType/Deconominos/Domicilio/EstatusResidencia", document);
                objSol.Deconominos.Domicilio.MontoVivienda= xpath.evaluate("/SolicitudType/Deconominos/Domicilio/MontoVivienda", document);
                objSol.Deconominos.Domicilio.Telcasa= xpath.evaluate("/SolicitudType/Deconominos/Domicilio/Telcasa", document);
                objSol.Deconominos.Domicilio.Telmovil= xpath.evaluate("/SolicitudType/Deconominos/Domicilio/Telmovil", document);

                objSol.Deconominos.OtrosIngresos= xpath.evaluate("/SolicitudType/Deconominos/OtrosIngresos", document);
                objSol.Deconominos.FuenteOtrosIngresos= xpath.evaluate("/SolicitudType/Deconominos/FuenteOtrosIngresos", document);

                objSol.Lattitude= xpath.evaluate("/SolicitudType/Lattitude", document);
                objSol.Longuitud= xpath.evaluate("/SolicitudType/Longuitud", document);

            }catch(Exception ex){

            }


        }

        return objSol;
    }
}
