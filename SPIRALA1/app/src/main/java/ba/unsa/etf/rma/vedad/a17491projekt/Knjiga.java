package ba.unsa.etf.rma.vedad.a17491projekt;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by HP on 29.3.2018.
 */

@SuppressLint("ParcelCreator")
public class Knjiga implements Parcelable
{
    private String imeAutora; //IME autora
    private String naziv; // NAZIV knjige
    private String Kategorija;
    private  boolean pritisnut;
    private boolean onLineDodan;
    private String id;
    private ArrayList<Autor> autori;
    private String opis;
    private String datumObjavljivanja;
    private URL slika;

    public String getIdWebServis() {
        return idWebServis;
    }

    public void setIdWebServis(String idWebServis) {
        this.idWebServis = idWebServis;
    }

    private String idWebServis;
    private int brojStranica;

    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStranica) {
        this.naziv = naziv;
        this.id = id;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStranica = brojStranica;
    }
    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStranica, String kategorija, String idWebServis) {
        this.naziv = naziv;
        this.id = id;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStranica = brojStranica;
        this.Kategorija = kategorija;
        pritisnut = false;
        this.idWebServis = idWebServis;
    }

    protected Knjiga(Parcel in) {
        imeAutora = in.readString();
        naziv = in.readString();
        Kategorija = in.readString();
        pritisnut = in.readByte() != 0;
        onLineDodan = in.readByte() != 0;
        id = in.readString();
        autori = in.createTypedArrayList(Autor.CREATOR);
        opis = in.readString();
        datumObjavljivanja = in.readString();
        brojStranica = in.readInt();
        idWebServis = in.readString();
    }

    public static final Creator<Knjiga> CREATOR = new Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel in) {
            return new Knjiga(in);
        }

        @Override
        public Knjiga[] newArray(int size) {
            return new Knjiga[size];
        }
    };

    public boolean isOnLineDodan() {
        return onLineDodan;
    }

    public void setOnLineDodan(boolean onLineDodan) {
        this.onLineDodan = onLineDodan;
    }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public URL getSlika() {
        return slika;
    }

    public void setSlika(URL slika) {
        this.slika = slika;
    }

    public int getBrojStranica() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }

    public String getId() {
        return id;

    }

    public void setId(String id) {
        this.id = id;
    }



    public Knjiga(){}


    public Knjiga(String naziv, String imeAutora, String kategorija) {
        this.imeAutora = imeAutora;
        this.naziv = naziv;
        Kategorija = kategorija;
        pritisnut = false;
        onLineDodan = false;
    }
    public Knjiga(String naziv, String imeAutora, String kategorija, boolean dodan) {
        this.imeAutora = imeAutora;
        this.naziv = naziv;
        Kategorija = kategorija;
        pritisnut = false;
        onLineDodan = dodan;
    }

    public String getImeAutora() {
        return imeAutora;
    }

    public void setImeAutora(String imeAutora) {
        this.imeAutora = imeAutora;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getKategorija() {
        return Kategorija;
    }

    public void setKategorija(String kategorija) {
        Kategorija = kategorija;
    }

    public boolean isPritisnut() {
        return pritisnut;
    }

    public void setPritisnut(boolean pritisnut) {
        this.pritisnut = pritisnut;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imeAutora);
        dest.writeString(naziv);
        dest.writeString(Kategorija);
        dest.writeByte((byte) (pritisnut ? 1 : 0));
        dest.writeByte((byte) (onLineDodan ? 1 : 0));
        dest.writeString(id);
        dest.writeTypedList(autori);
        dest.writeString(opis);
        dest.writeString(datumObjavljivanja);
        dest.writeInt(brojStranica);
        dest.writeString(idWebServis);
    }
}
