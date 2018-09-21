package ba.unsa.etf.rma.vedad.a17491projekt;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class KnjigePoznanika extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */


    public static final int STATUS_START = 0;
    public static final int STATUS_FINISH = 1;
    public static final int STATUS_ERROR = 2;
    private ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();

    public KnjigePoznanika() {
        super(null);
    }

    public KnjigePoznanika(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        ArrayList<Integer> Idijevi = new ArrayList<Integer>();

        String korisnik =intent.getStringExtra("parametar1");
        String stringZaPretragu = odvoji(korisnik);
        Bundle bundle = new Bundle();
        //stringZaPretragu = bundle.getString("parametar1");
      //  receiver​.​send​(​STATUS_RUNNING​,​ Bundle​.​EMPTY​);
        receiver.send(STATUS_START,Bundle.EMPTY);
        {
            String query = null;
            try {
                query = URLEncoder.encode(stringZaPretragu, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR,bundle);
            }
            String url1 = "https://www.googleapis.com/books/v1/users/" + query + "/bookshelves";
//www.googleapis.com/books/v1/users/113602182477322648699/bookshelves
            try {
                URL url = new URL(url1);
                URLConnection connection = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                }
                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                String rezultat = convertStreamToString(in);
                JSONObject jo = new JSONObject(rezultat);
                JSONArray items = jo.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject JSONbookshelve = items.getJSONObject(i);
                    int volumeCount = JSONbookshelve.getInt("volumeCount");
                    if (volumeCount != 0) {
                        Idijevi.add(JSONbookshelve.getInt("id"));
                    }

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR,bundle);
            } catch (IOException e) {
                e.printStackTrace();
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR,bundle);
            } catch (JSONException e) {
                e.printStackTrace();
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR,bundle);
            }
        }
        for(int j = 0; j < Idijevi.size(); j++)
        {
            String query = null;
            try
            {
                query = URLEncoder.encode(stringZaPretragu, "utf-8");
            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR,bundle);
            }
            String url1 = "https://www.googleapis.com/books/v1/users/" + query +"/bookshelves"+"/"+Idijevi.get(j).toString()+"/"+"volumes?key=AIzaSyDy9dOTxrOWqWRANkx4hiOJCpGuiatkH20";
           // https://www.googleapis.com/books/v1/users/113602182477322648699/bookshelves/2/volumes?key=AIzaSyDy9dOTxrOWqWRANkx4hiOJCpGuiatkH20
            try
            {
                URL url = new URL(url1);
                URLConnection connection = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK)
                {

                }
                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                String rezultat = convertStreamToString(in);
                JSONObject jo = new JSONObject(rezultat);
                JSONArray items = jo.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject JSONknjiga = items.getJSONObject(i);
                    JSONObject objekt = JSONknjiga.getJSONObject("volumeInfo");
                    String naziv = objekt.getString("title");
                    String id = JSONknjiga.getString("id");
                    ArrayList<Autor> autors = new ArrayList<Autor>();
                    autors.add(new Autor("nepoznat","def"));
                    if (objekt.has("authors"))
                    {
                        autors.clear();
                        JSONArray nizAutora = objekt.getJSONArray("authors");
                        for(int l = 0; l < nizAutora.length(); l++)
                        {
                            autors.add(new Autor(nizAutora.getString(l),id));
                        }
                    }
                    String opis = "bezopisa";
                    if(objekt.has("description"))
                    {
                        opis = objekt.getString("description");
                    }
                    String datumObjavljivanja = "nepoznat";
                    if (objekt.has("publishedDate"))
                    {
                        datumObjavljivanja = objekt.getString("publishedDate");
                    }
                    URL url2 = null;
                    if(objekt.has("imageLinks"))
                    {
                        JSONObject objekatURL = objekt.getJSONObject("imageLinks");
                        String stringUrl = objekatURL.getString("thumbnail");
                        url2 = new URL(stringUrl);
                    }

                    //    new getBitMap((getBitMap.OngetBitMapDone) DohvatiKnjige. this ).execute( stringUrl );
                    int brojStranica = 0;
                    if (objekt.has("pageCount"))
                    {
                        brojStranica = objekt.getInt("pageCount");
                    }
                    knjige.add(new Knjiga(id,naziv,autors,opis,datumObjavljivanja,url2,brojStranica));

                    String naziv2 = naziv;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR,bundle);
            } catch (IOException e) {
                e.printStackTrace();
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR,bundle);
            } catch (JSONException e) {
                e.printStackTrace();
               // bundle​.​putString​(​Intent​.​EXTRA_TEXT​,​ e​.​toString​());
                //receiver​.​send​(​STATUS_ERROR​,​ bundle​);
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR,bundle);

            }
         //   bundle​.​putStringArray​(​"result"​,​ results​);
           // receiver​.​send​(​STATUS_FINISHED​,​ bundle​);
         //   if(knjige.size()!=0)
            bundle.putParcelableArrayList("result",knjige);
            receiver.send(STATUS_FINISH,bundle);
        }
}
    private String odvoji (String s)
    {
        String pomocni = new String();
        String rezultat = new String();
        for(int i = 0; i < 9; i++)
        {
            pomocni += s.charAt(i);
        }
        if(pomocni.equals("korisnik:") )
        {
            for(int i = 9; i < s.length(); i++)
            {
                rezultat += s.charAt(i);
            }
            return rezultat;
        }
        return "greska";

    }
    private String convertStreamToString(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally
        {
            try
            {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }
}
