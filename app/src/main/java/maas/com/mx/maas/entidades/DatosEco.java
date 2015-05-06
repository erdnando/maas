package maas.com.mx.maas.entidades;

/**
 * Created by damserver on 05/05/2015.
 */
public class DatosEco {

    public DatosEco()
    {
        this.Domicilio=new Domicilio();

        this.TipoContrato="";
        this.AntiguedadEmpleo="";
        this.Ingresos="";
        this.NombreEmpresa="";
        this.GiroEmpresa="";
        this.Puesto="";
        this.OtrosIngresos="";
        this.FuenteOtrosIngresos="";
    }
    public String TipoContrato;
    public String AntiguedadEmpleo;
    public String AniosCasada;
    public String Ingresos;
    public String NombreEmpresa;
    public String GiroEmpresa;
    public String Puesto;
    public Domicilio Domicilio;
    public String OtrosIngresos;
    public String FuenteOtrosIngresos;
}
