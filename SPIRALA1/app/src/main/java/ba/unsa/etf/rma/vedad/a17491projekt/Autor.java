package ba.unsa.etf.rma.vedad.a17491projekt;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by HP on 9.4.2018.
 */

public class Autor implements Parcelable {
    private String imeiPrezime;
    private int BrojKnjiga;
    private ArrayList<String> knjige = new ArrayList<String>();

    public ArrayList<String> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<String> knjige) {
        this.knjige = knjige;
    }

    public Autor (String imeiprezime, String id){
       imeiPrezime = imeiprezime;
       knjige.add(id);
       BrojKnjiga = 1;

   }
   public void dodajKnjigu(String id){
      for(int i = 0; i < knjige.size(); i++)
      {
          if(knjige.get(i).equals(id))
          {
              return;
          }
          knjige.add(id);
      }
   }

    protected Autor(Parcel in) {
        imeiPrezime = in.readString();
        BrojKnjiga = in.readInt();
    }

    public static final Creator<Autor> CREATOR = new Creator<Autor>() {
        @Override
        public Autor createFromParcel(Parcel in) {
            return new Autor(in);
        }

        @Override
        public Autor[] newArray(int size) {
            return new Autor[size];
        }
    };

    public String getImeiPrezime() {
        return imeiPrezime;
    }

    public void setImeiPrezime(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    public int getBrojKnjiga() {
        return BrojKnjiga;
    }

    public void setBrojKnjiga(int brojKnjiga) {
        BrojKnjiga = brojKnjiga;
    }

    public Autor(String imeiPrezime) {
        BrojKnjiga = 1;
        this.imeiPrezime = imeiPrezime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imeiPrezime);
        parcel.writeInt(BrojKnjiga);
    }
}
