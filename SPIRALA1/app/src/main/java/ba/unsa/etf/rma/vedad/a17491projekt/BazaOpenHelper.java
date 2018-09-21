package ba.unsa.etf.rma.vedad.a17491projekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BazaOpenHelper extends SQLiteOpenHelper {
    // ime baze
    private static final  String ime_baze = "baza";
    //verzija baze
    private static final int verzija_baze = 1;
    // imeTabele
    private static final String imeTabeleKategroja = "Kategorija";
    private static final String imeTabeleKnjiga = "Knjiga";
    public static final  String imeTabeleAUtora = "Autor";
    public static final  String imeTebeleAutorstvo = "Autorstvo";


    private static final String idKategroja = "_id";
    private static final String nazivKategroja = "naziv";

    private static final String idKnjige = "_id";
    private static final String nazivKnjige = "naziv";
    private static final String opisKnjige = "opis";
    private static final String datumObjavljivanjaKnjige = "datumObjavljivanja";
    private static final String brojStranicaKnjige = "brojStranica";
    private static final String idWebServisKnjige = "idWebServis";
    private static final String idkategorijeKnjige = "idkategorije";
    private static final String slikaKnjige = "slika";
    private static final String pregledanaKnjiga = "pregledana";

    private static final String idAutora = "_id";
    private static final String imeAutora = "ime";

    private static final String idAutorstva = "_id";
    private static final String idautoraAutorstva = "idautora";
    private static final String idknjigeAutorstva = "idknjige";

    //upit za kreiranje tabele
    private static final String Create_Table_Kategorija = "Create table " + imeTabeleKategroja
            + " ( " + idKategroja + " integer primary key autoincrement, " + nazivKategroja
            + " text not null );";
    private static final String Create_Table_Knjiga= "Create table " + imeTabeleKnjiga
            + " ( " + idKnjige + " integer primary key autoincrement, " + nazivKnjige+ " text not null, " + opisKnjige + " text not null, "+datumObjavljivanjaKnjige+ " text not null, "
            +brojStranicaKnjige+ " integer not null, "+idWebServisKnjige+ " text not null, "+idkategorijeKnjige+ " integer not null, "+slikaKnjige+ " text not null, "
            +pregledanaKnjiga + " integer not null );";
    private static final String Create_Table_Autor= "Create table " + imeTabeleAUtora
            + " ( " + idAutora + " integer primary key autoincrement, " + imeAutora
            + " text not null );";

    private static final String Create_Table_Autorstva = "Create table " + imeTebeleAutorstvo
            + " ( " + idAutorstva + " integer primary key autoincrement, "   +idautoraAutorstva+ " integer not null, "+ idknjigeAutorstva
            + " text not null );";
    // odbacivanje tabele
    private static final String Drop_Table = "Drop table if exists "
            + imeTabeleKategroja;
    public BazaOpenHelper(Context context) {
        super(context,ime_baze,null,verzija_baze);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_Table_Kategorija);
        db.execSQL(Create_Table_Knjiga);
        db.execSQL(Create_Table_Autor);
        db.execSQL(Create_Table_Autorstva);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Drop_Table);
        onCreate(db);
    }

    // Insert data into database method
    public long dodajKategoriju(String naziv) {
        ArrayList<String> data = getKategorije();
        if(data.contains(naziv))
        {
          return -1;
        }
        else
        {
            SQLiteDatabase db = this.getWritableDatabase();
            // Content values used for editing/writing data into database
            ContentValues values = new ContentValues();
            values.put(nazivKategroja, naziv);
            // Now inserting content values data into table
            long x = db.insert(imeTabeleKategroja, null, values);
            db.close();
            return x;
        }

    }
    public long dodajAutora(String autor)
    {
        ArrayList<Autor> data = getAutori();
        long x = -1;
        for(int i = 0; i < data.size(); i++)
        {
            if(data.get(i).getImeiPrezime().equals(autor))
            {
                SQLiteDatabase db = this.getReadableDatabase();
                // Select query for selecting whole table data
                String select_query = "Select * from " + imeTabeleAUtora;
                // Cursor for traversing whole data into database
                Cursor cursor = db.rawQuery(select_query, null);
                try {
                    // check if cursor move to first
                    if (cursor.moveToFirst()) {
                        // looping through all data and adding to arraylist
                        do {
                            if(cursor.getString(1).equals(autor))
                            {
                                x = cursor.getInt(0);
                            }

                        } while (cursor.moveToNext());
                    }
                } finally {
                    // After using cursor we have to close it
                    cursor.close();
                }
                // Closing database
                db.close();
                return  x;
            }
        }
        SQLiteDatabase db = this.getWritableDatabase();
        // Content values used for editing/writing data into database
        ContentValues values = new ContentValues();
        values.put(imeAutora, autor);
        // Now inserting content values data into table
        long y = db.insert(imeTabeleAUtora, null, values);
        db.close();
        return y;
    }
    public ArrayList<Autor> getAutori(){
        ArrayList<Autor> data = new ArrayList<Autor>();
        // Accessing database for reading data
        SQLiteDatabase db = this.getReadableDatabase();
        // Select query for selecting whole table data
        String select_query = "Select * from " + imeTabeleAUtora;
        // Cursor for traversing whole data into database
        Cursor cursor = db.rawQuery(select_query, null);
        try {
            // check if cursor move to first
            if (cursor.moveToFirst()) {
                // looping through all data and adding to arraylist
                do {

                    data.add(new Autor(cursor.getString(1)));
                } while (cursor.moveToNext());
            }
        } finally {
            // After using cursor we have to close it
            cursor.close();
        }
        // Closing database
        db.close();
        // returning list
        return data;
    }
    public ArrayList<String> getKategorije() {
       // Data model list in which we have to return the data
        ArrayList<String> data = new ArrayList<String>();
        // Accessing database for reading data
        SQLiteDatabase db = this.getReadableDatabase();
        // Select query for selecting whole table data
        String select_query = "Select * from " + imeTabeleKategroja;
        // Cursor for traversing whole data into database
        Cursor cursor = db.rawQuery(select_query, null);
        try {
            // check if cursor move to first
            if (cursor.moveToFirst()) {
                // looping through all data and adding to arraylist
                do {
                    data.add(cursor.getString(1));
                    } while (cursor.moveToNext());
                }
        } finally {
            // After using cursor we have to close it
            cursor.close();
            }
            // Closing database
        db.close();
        // returning list
        return data;
    }
    public Integer getIdKategorije(String naziv){
        Integer rezultat = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        // Select query for selecting whole table data
        String select_query = "Select * from " + imeTabeleKategroja;
        // Cursor for traversing whole data into database
        Cursor cursor = db.rawQuery(select_query, null);
        try {
            // check if cursor move to first
            if (cursor.moveToFirst()) {
                // looping through all data and adding to arraylist
                do {
                    if(cursor.getString(1).equals(naziv))
                    {
                        rezultat = cursor.getInt(0);
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            // After using cursor we have to close it
            cursor.close();
        }
        // Closing database
        db.close();
        return rezultat;
    }
    public String getNazivKategorije(int id){
        String rezultat = "getNazivKategorije";

        SQLiteDatabase db = this.getReadableDatabase();
        // Select query for selecting whole table data
        String select_query = "Select * from " + imeTabeleKategroja;
        // Cursor for traversing whole data into database
        Cursor cursor = db.rawQuery(select_query, null);
        try {
            // check if cursor move to first
            if (cursor.moveToFirst()) {
                // looping through all data and adding to arraylist
                do {
                   if(cursor.getInt(0) == id)
                    {
                        rezultat = cursor.getString(1);
                        break;
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            // After using cursor we have to close it
            cursor.close();
        }
        // Closing database
        db.close();
        return rezultat;
    }
    public long dodajAutorstvo(Integer idautor, Integer idknjige)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // Content values used for editing/writing data into database
        ContentValues values = new ContentValues();
        values.put(idautoraAutorstva, idautor);
        values.put(idknjigeAutorstva,idknjige);
        // Now inserting content values data into table
        long y=  db.insert(imeTebeleAutorstvo, null, values);
        db.close();
        return y;
    }
    public long dodajKnjigu(Knjiga knjiga){
        Integer idKategotije = getIdKategorije(knjiga.getKategorija());
        Integer pregeldana = 0;
        if(knjiga.isPritisnut())
        {
            pregeldana = 1;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        // Content values used for editing/writing data into database
        ContentValues values = new ContentValues();
        values.put(nazivKnjige, knjiga.getNaziv());
        values.put(opisKnjige,knjiga.getOpis());
        values.put(datumObjavljivanjaKnjige,knjiga.getDatumObjavljivanja());
        values.put(brojStranicaKnjige,knjiga.getBrojStranica());
        values.put(idWebServisKnjige,knjiga.getIdWebServis());
        values.put(idkategorijeKnjige,idKategotije);
        if(knjiga.getSlika() != null)
        {
            values.put(slikaKnjige,knjiga.getSlika().toString());
        }else
        {
            values.put(slikaKnjige,"null");
        }


        values.put(pregledanaKnjiga,pregeldana);


        // Now inserting content values data into table
        long x= db.insert(imeTabeleKnjiga, null, values);
        db.close();
        return x;
    }
    public Autor dajAutoraZaId (Integer idAutora){
        Autor a = new Autor("nepoznat");
        SQLiteDatabase db = this.getReadableDatabase();
        // Select query for selecting whole table data
        String select_query = "Select * from " + imeTabeleAUtora;
        // Cursor for traversing whole data into database
        Cursor cursor = db.rawQuery(select_query, null);
        try {
            // check if cursor move to first
            if (cursor.moveToFirst()) {
                // looping through all data and adding to arraylist
                do {
                    if(cursor.getInt(0)== idAutora)
                    {
                        a = new Autor(cursor.getString(1));
                        break;
                    }

                } while (cursor.moveToNext());
            }
        } finally {
            // After using cursor we have to close it
            cursor.close();
        }
        // Closing database
        db.close();
        // returning list
        return a;
    }
    public long  dajIdZaImeAutora(String ime ){
        long x = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        // Select query for selecting whole table data
        String select_query = "Select * from " + imeTabeleAUtora;
        // Cursor for traversing whole data into database
        Cursor cursor = db.rawQuery(select_query, null);
        try {
            // check if cursor move to first
            if (cursor.moveToFirst()) {
                // looping through all data and adding to arraylist
                do {
                    if(cursor.getString(1).equals(ime))
                    {
                        x = cursor.getInt(0);
                        break;
                    }

                } while (cursor.moveToNext());
            }
        } finally {
            // After using cursor we have to close it
            cursor.close();
        }
        // Closing database
        db.close();
        // returning list
        return x;

    }
    public Knjiga DajKnjiguZaId(long id){
        // Data model list in which we have to return the data
        Knjiga data = null;
        // Accessing database for reading data
        SQLiteDatabase db = this.getReadableDatabase();
        // Select query for selecting whole table data
        String select_query = "Select * from " + imeTabeleKnjiga;
        // Cursor for traversing whole data into database
        Cursor cursor = db.rawQuery(select_query, null);
        try {
            // check if cursor move to first
            if (cursor.moveToFirst()) {
                // looping through all data and adding to arraylist
                do {
                    //  data.add(cursor.getString(1));
                    if(cursor.getInt(0) == id )
                    {
                        ArrayList<Autor> autori = dajAutoreKnjige(cursor.getInt(0));
                        String stringUrl = cursor.getString(7);
                        String nazivKategorije = getNazivKategorije(cursor.getInt(6));
                        URL url2 = null;
                        if(!stringUrl.equals("null"))
                        {
                            url2 = new URL(stringUrl);
                        }

                        Knjiga K = new Knjiga("nebitan",cursor.getString(1),autori,cursor.getString(2),
                                cursor.getString(3),url2,cursor.getInt(4),nazivKategorije,cursor.getString(5));
                        if(cursor.getInt(8) == 1)
                        {
                            K.setPritisnut(true);
                        }
                        data = K;
                        break;
                    }
                } while (cursor.moveToNext());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            // After using cursor we have to close it
            cursor.close();
        }
        // Closing database
        db.close();
        // returning list
        return data;
    }

    public ArrayList<Autor> dajAutoreKnjige(Integer idKnjige)
    {
        ArrayList<Autor> data = new ArrayList<Autor>();
        // Accessing database for reading data
        SQLiteDatabase db = this.getReadableDatabase();
        // Select query for selecting whole table data
        String select_query = "Select * from " + imeTebeleAutorstvo;
        // Cursor for traversing whole data into database
        Cursor cursor = db.rawQuery(select_query, null);
        try {
            // check if cursor move to first
            if (cursor.moveToFirst()) {
                // looping through all data and adding to arraylist
                do {


                    if(cursor.getInt(2) == idKnjige)
                    {
                        data.add(new Autor(dajAutoraZaId(cursor.getInt(1)).getImeiPrezime()));
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            // After using cursor we have to close it
            cursor.close();
        }
        // Closing database
        db.close();
        // returning list
        return data;
    }
    public ArrayList<Knjiga> knjigeKategorije(long idKategorije)
    {
        // Data model list in which we have to return the data
        ArrayList<Knjiga> data = new ArrayList<Knjiga>();
        // Accessing database for reading data
        SQLiteDatabase db = this.getReadableDatabase();
        // Select query for selecting whole table data
        String select_query = "Select * from " + imeTabeleKnjiga;
        // Cursor for traversing whole data into database
        Cursor cursor = db.rawQuery(select_query, null);
        try {
            // check if cursor move to first
            if (cursor.moveToFirst()) {
                // looping through all data and adding to arraylist
                do {
                  //  data.add(cursor.getString(1));
                    if(cursor.getInt(6) == (int) idKategorije )
                    {
                       ArrayList<Autor> autori = dajAutoreKnjige(cursor.getInt(0));
                        String stringUrl = cursor.getString(7);
                        String nazivKategorije = getNazivKategorije(cursor.getInt(6));
                        URL url2 = null;
                        if(!stringUrl.equals("null"))
                        {
                            url2 = new URL(stringUrl);
                        }

                        Knjiga K = new Knjiga("nebitan",cursor.getString(1),autori,cursor.getString(2),
                                cursor.getString(3),url2,cursor.getInt(4),nazivKategorije,cursor.getString(5));
                        if(cursor.getInt(8) == 1)
                        {
                            K.setPritisnut(true);
                        }
                        data.add(K);
                    }
                } while (cursor.moveToNext());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            // After using cursor we have to close it
            cursor.close();
        }
        // Closing database
        db.close();
        // returning list
        return data;
    }
    public ArrayList<Knjiga> dajKnjige(){
        ArrayList<Knjiga> data = new ArrayList<Knjiga>();
        // Accessing database for reading data
        SQLiteDatabase db = this.getReadableDatabase();
        // Select query for selecting whole table data
        String select_query = "Select * from " + imeTabeleKnjiga;
        // Cursor for traversing whole data into database
        Cursor cursor = db.rawQuery(select_query, null);
        try {
            // check if cursor move to first
            if (cursor.moveToFirst()) {
                // looping through all data and adding to arraylist
                do {

                        ArrayList<Autor> autori = dajAutoreKnjige(cursor.getInt(0));
                        String stringUrl = cursor.getString(7);
                        String nazivKategorije = getNazivKategorije(cursor.getInt(6));
                        URL url2 = null;
                        if(!stringUrl.equals("null"))
                        {
                            url2 = new URL(stringUrl);
                        }
                        Knjiga K = new Knjiga("nebitan",cursor.getString(1),autori,cursor.getString(2),
                                cursor.getString(3),url2,cursor.getInt(4),nazivKategorije,cursor.getString(5));
                        if(cursor.getInt(8) == 1)
                        {
                            K.setPritisnut(true);
                        }
                        data.add(K);

                } while (cursor.moveToNext());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            // After using cursor we have to close it
            cursor.close();
        }
        // Closing database
        db.close();
        // returning list
        return data;

    }
    public ArrayList<Knjiga> knjigeAutora(long idAutora){
        ArrayList<Knjiga> data = new ArrayList<Knjiga>();
        // Accessing database for reading data
        SQLiteDatabase db = this.getReadableDatabase();
        // Select query for selecting whole table data
        String select_query = "Select * from " + imeTebeleAutorstvo;
        // Cursor for traversing whole data into database
        Cursor cursor = db.rawQuery(select_query, null);
        try {
            // check if cursor move to first
            if (cursor.moveToFirst()) {
                // looping through all data and adding to arraylist
                do {
                    //  data.add(cursor.getString(1));
                    if(cursor.getInt(1) == (int) idAutora)
                    {
                        Knjiga k = DajKnjiguZaId(cursor.getInt(2));
                        Knjiga K =new Knjiga(k.getId(),k.getNaziv(),k.getAutori(),k.getOpis(),k.getDatumObjavljivanja(),k.getSlika(),k.getBrojStranica(),k.getKategorija(),k.getIdWebServis());
                        if(k.isPritisnut()) K.setPritisnut(true);
                        data.add(K);
                    }
                } while (cursor.moveToNext());
            }
        }  finally {
            // After using cursor we have to close it
            cursor.close();
        }
        // Closing database
        db.close();
        // returning list
        return data;

    }
  /*  public void updatePritisnut(String naziv){
        // kreiranje ContentValues instance
        ContentValues updatedValues = new ContentValues();
// Dodjeljivanje novih vrijednosti
        updatedValues.put(pregledanaKnjiga, 1);
// Specificiramo koje redove treba izmjeniti
        String where = nazivKnjige + "=" + naziv;
        String whereArgs[] = null;
// Update reda sa novim vrijednostima
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(imeTabeleKnjiga, updatedValues, where, whereArgs);
        db.close();

    }*/
  public void updatePritisnut(String naziv)
  {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues updatedValues = new ContentValues();
      updatedValues.put(pregledanaKnjiga,1);
      String where = nazivKnjige + " = '"  + naziv+ "'";
      String x = where;
      String whereArgs[] = null;
     // SQLiteDatabase db = this.getWritableDatabase();
      db.update(imeTabeleKnjiga,updatedValues,where,whereArgs);
      db.close();
  }
    public void deleteTable() {

        SQLiteDatabase db = this.getWritableDatabase();

        // Deleting table
        db.delete(imeTabeleKategroja, null, null);

        // Closing database
        db.close();

    }
}
