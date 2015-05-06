package maas.com.mx.maas.entidades;

/**
 * Created by damserver on 05/05/2015.
 */
public class SolicitudType {

    public SolicitudType(){
        this.generales=new Generales();
        this.Promotor=new Promotor();
        this.domicilio=new Domicilio();
        this.Resp=new Respuesta();
        this.Deconominos=new DatosEco();
        this.Personapolitica=new Personapol();
        this.Refer=new Referencias();
        this.Refer2=new Referencias();
        this.Refer3=new Referencias();
        this.doc=new Documento();
    }

    public int getEstatusGrales(){
        int total=0;
        int requeridos=5;

        this.generales.Pmrnombre=this.generales.Pmrnombre==null?"":this.generales.Pmrnombre;
        this.generales.Apaterno=this.generales.Apaterno==null?"":this.generales.Apaterno;
        this.generales.Amaterno=this.generales.Amaterno==null?"":this.generales.Amaterno;
        this.generales.Noidenficacion=this.generales.Noidenficacion==null?"":this.generales.Noidenficacion;
        this.generales.Rfc=this.generales.Rfc==null?"":this.generales.Rfc;

        total+=this.generales.Pmrnombre.length()>0?1:0;
        total+=this.generales.Apaterno.length()>0?1:0;
        total+=this.generales.Amaterno.length()>0?1:0;
        total+=this.generales.Noidenficacion.length()>0?1:0;
        total+=this.generales.Rfc.length()>0?1:0;

        return (total-requeridos)==0?1:0;
    }

    public int getEstatusdomicilio(){
        int total=0;
        int requeridos=7;
        this.domicilio.Calle=this.domicilio.Calle==null?"":this.domicilio.Calle;
        this.domicilio.NoExt=this.domicilio.NoExt==null?"":this.domicilio.NoExt;
        this.domicilio.Cpdom=this.domicilio.Cpdom==null?"":this.domicilio.Cpdom;
        this.domicilio.TiempoResidencia=this.domicilio.TiempoResidencia==null?"":this.domicilio.TiempoResidencia;
        this.domicilio.Email=this.domicilio.Email==null?"":this.domicilio.Email;
        this.domicilio.Telcasa=this.domicilio.Telcasa==null?"":this.domicilio.Telcasa;
        this.domicilio.Telmovil=this.domicilio.Telmovil==null?"":this.domicilio.Telmovil;

        total+=this.domicilio.Calle.length()>0?1:0;
        total+=this.domicilio.NoExt.length()>0?1:0;
        total+=this.domicilio.Cpdom.length()>0?1:0;
        total+=this.domicilio.TiempoResidencia.length()>0?1:0;
        total+=this.domicilio.Email.length()>0?1:0;
        total+=this.domicilio.Telcasa.length()>0?1:0;
        total+=this.domicilio.Telmovil.length()>0?1:0;

        return (total-requeridos)==0?1:0;
    }

    public Promotor Promotor;
    public Generales generales;
    public Domicilio domicilio;
    public Respuesta Resp;
    public DatosEco Deconominos;
    public Personapol Personapolitica;
    public Referencias Refer;
    public Referencias Refer2;
    public Referencias Refer3;
    public Documento doc;
    public String FolioLocal;
    public String DiaCreacion;
    public String MesCreacion;
    public String AnioCreacion;
    public String Lattitude;
    public String Longuitud;
}
