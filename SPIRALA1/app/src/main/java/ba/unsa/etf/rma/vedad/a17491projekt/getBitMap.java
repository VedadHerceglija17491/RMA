package ba.unsa.etf.rma.vedad.a17491projekt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class getBitMap extends AsyncTask<String, Integer    , Void> {

    public interface OngetBitMapDone
    {
        public void onDone(Bitmap bitmap);
    }
    private Bitmap bitmap;
    private OngetBitMapDone pozivatelj;
    public getBitMap(OngetBitMapDone p)
    {
        pozivatelj = p;
    }


    @Override
    protected Void doInBackground(String... strings) {
        String Url = strings [0];
        bitmap = null;
            try {
                InputStream in = new java.net.URL(Url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();

        } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pozivatelj.onDone(bitmap);
    }
}
