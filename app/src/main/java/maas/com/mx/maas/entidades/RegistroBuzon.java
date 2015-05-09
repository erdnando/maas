package maas.com.mx.maas.entidades;

/**
 * Created by damserver on 17/04/2015.
 */
public class RegistroBuzon {

    public RegistroBuzon(){
        this.COORDINADOR="";
        this.GERENTE="";
        this.PROMOTOR="";
        this.ID_SOLICITUD="";
        this.ESTATUS="";
        this.FECHA_ALTA="";
        this.SOLICITUD_XML="";
        this.VALIDADOR="";
        this.FECHA_MODIFICACION="";
        this.VALIDADO_POR="";
        this.NOMBRE_CLIENTE="";
        this.COMENTARIO="";

        this.doc=new Documento();

    }

    public String COORDINADOR;
    public String GERENTE;
    public String PROMOTOR;
    public String ID_SOLICITUD;
    public String ESTATUS;
    public String FECHA_ALTA;
    public String SOLICITUD_XML;
    public String VALIDADOR;
    public String FECHA_MODIFICACION;
    public String VALIDADO_POR;
    public String NOMBRE_CLIENTE;
    public String COMENTARIO;

    public byte[] DOC_IF;
    //[DataMember]
    public byte[] DOC_IA ;
    //[DataMember]
    public byte[] DOC_C1;
    //[DataMember]
    public byte[] DOC_C2;
    //[DataMember]
    public byte[] E1;
    //[DataMember]
    public byte[] E2;
    //[DataMember]
    public byte[] E3;
    //[DataMember]
    public byte[] E4;
    //[DataMember]
    public byte[] E5;
    //[DataMember]
    public byte[] F1;

    public Documento doc;
}
