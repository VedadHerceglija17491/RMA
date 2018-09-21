package ba.unsa.etf.rma.vedad.a17491projekt;

public class Contact_Model {
    private String ime;
    private String eMail;
    private String id;

    public Contact_Model(String id,String ime, String eMail) {
        this.ime = ime;
        this.eMail = eMail;
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public String getId() {
        return id;
    }

    public String geteMail() {
        return eMail;
    }
}
