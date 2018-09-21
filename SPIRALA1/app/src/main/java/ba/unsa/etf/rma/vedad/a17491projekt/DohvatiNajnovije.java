package ba.unsa.etf.rma.vedad.a17491projekt;

import android.os.AsyncTask;

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

public class DohvatiNajnovije extends AsyncTask<String, Integer, Void> {

    public interface IDohvatiNajnovijeDone
    {
        public void onNajnovijeDone(ArrayList<Knjiga> knjige);
    }
    private ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();
    private IDohvatiNajnovijeDone pozivatelj;

    public DohvatiNajnovije(IDohvatiNajnovijeDone p)
    {
        pozivatelj = p;
    }
    @Override
    protected Void doInBackground(String... params) {
        String autor = odvoji(params);
        //   ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();

            String query = null;
            try
            {
                query = URLEncoder.encode(autor, "utf-8");
            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            String url1 = "https://www.googleapis.com/books/v1/volumes?q=inauthor:" + query +"&maxResults=5" +"&orderBy=newest";
            //https://www.googleapis.com/books/v1/volumes?q=inauthor:Ivo&orderBy=newest
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
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        return  null;

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
    private String odvoji (String... strings)
    {
        String s = strings[0];
        String pomocni = new String();
        String rezultat = new String();

        for(int i = 0; i < 6; i++)
        {
            pomocni += s.charAt(i);
        }
        if(pomocni.equals("autor:") )
        {
            for(int i = 6; i < s.length(); i++)
            {
                rezultat += s.charAt(i);
            }
            return rezultat;
        }
        return "greska";

    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pozivatelj.onNajnovijeDone(knjige);
    }
}
