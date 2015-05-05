package maas.com.mx.maas.entidades;

/**
 * Created by damserver on 05/05/2015.
 */
public class SolicitudType {

    public SolicitudType()
    {
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
