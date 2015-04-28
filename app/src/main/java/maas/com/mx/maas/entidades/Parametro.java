package maas.com.mx.maas.entidades;

/**
 * Created by damserver on 16/04/2015.
 */
public class Parametro {

        private long ID_PARAMETRO;
        private String PARMETRO;
        private String VALOR;
        private String ESTATUS;

        public long getID_PARAMETRO() {
            return ID_PARAMETRO;
        }

        public void setID_PARAMETRO(long valor) {
            this.ID_PARAMETRO = valor;
        }

        public String getPARMETRO() {
            return PARMETRO;
        }

        public void setPARMETRO(String valor) {
            this.PARMETRO = valor;
        }

        public String getVALOR() {
            return VALOR;
        }

        public void setVALOR(String valor) {
            this.VALOR = valor;
        }

        public String getESTATUS() {
            return ESTATUS;
        }

        public void setESTATUS(String valor) {
            this.ESTATUS = valor;
        }

        // Will be used by the ArrayAdapter in the ListView
        @Override
        public String toString() {
            return PARMETRO;
        }

}
