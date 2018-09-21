package ba.unsa.etf.rma.vedad.a17491projekt;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.IdentityHashMap;

/**
 * Created by HP on 30.3.2018.
 */

public class KnjigaAdapter extends BaseAdapter
{
    private Context Pcontext;
    private ArrayList<Knjiga> KnjigaArrayList;
    private  OnPreporuciClick opc;
    public interface OnPreporuciClick
    {
        public void OnPreporuciClicked(View view, int i);
    }

    public KnjigaAdapter(Context pcontext, ArrayList<Knjiga> knjigaArrayList)
    {
        Pcontext = pcontext;
        KnjigaArrayList = knjigaArrayList;
    }

    @Override
    public int getCount() {
        return KnjigaArrayList.size() ;
    }

    @Override
    public Object getItem(int i) {
        return KnjigaArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView( int i, View view, ViewGroup viewGroup)
    {
        View v = View.inflate(Pcontext, R.layout.element_liste_knjiga, null);

        TextView tvNaziv = (TextView) v.findViewById(R.id.eNaziv);
        TextView tvAutor = (TextView) v.findViewById(R.id.eAutor);
        TextView tvBrojStranica = (TextView) v.findViewById(R.id.eBrojStranica);
        TextView tvOpis = (TextView) v.findViewById(R.id.eOpis);
        TextView tvDatum = (TextView) v.findViewById(R.id.eDatumObjavljivanja);
        ImageView ivSlika = (ImageView) v.findViewById(R.id.eNaslovna);
        final int j = i;
        if(KnjigaArrayList.get(i).getBrojStranica() != 0)
        {
            tvBrojStranica.setText("Broj stranica:"+Integer.toString(KnjigaArrayList.get(i).getBrojStranica()));
        }
        else
        {
            tvBrojStranica.setText("Broj stranica nepoznat");
        }
        tvOpis.setText(KnjigaArrayList.get(i).getOpis());
        if(!KnjigaArrayList.get(i).getDatumObjavljivanja().equals("nepoznat"))
        {
            tvDatum.setText(KnjigaArrayList.get(i).getDatumObjavljivanja());
        }
        else
        {
            tvDatum.setText("Nepoznat datum objavljivanja");
        }

        tvNaziv.setText(KnjigaArrayList.get(i).getNaziv());
        tvAutor.setText(KnjigaArrayList.get(i).getAutori().get(0).getImeiPrezime());

        if(KnjigaArrayList.get(i).isPritisnut())
        {
            v.setBackgroundResource(R.color.LightBlue);
        }
        if(!KnjigaArrayList.get(i).getIdWebServis().equals("nepostoji"))
        {

            if(KnjigaArrayList.get(i).getSlika() != null)
            {
                Uri uri =  Uri.parse(KnjigaArrayList.get(i).getSlika().toString());
                Picasso.get().load(uri).into(ivSlika);
            }else
            {
                ivSlika.setImageResource(R.drawable.slikaknjige);
            }
        } else
        {
            try {
                ivSlika.setImageBitmap(BitmapFactory.decodeStream(Pcontext.openFileInput(KnjigaArrayList.get(i).getNaziv())));
            }
            catch (IOException E)
            {
                E.printStackTrace();
            }
        }
        Button buttonPreporuci = (Button) v.findViewById(R.id.dPreporuci);
        try
        {
            opc = (OnPreporuciClick) (Activity) Pcontext;
        }  catch (ClassCastException e)  //catch (ClassCastException e)​
        {
            throw new ClassCastException(Pcontext.toString()+"Treba implementirati OnItem Click");
        }
            buttonPreporuci.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( final View viv) {
                    opc.OnPreporuciClicked(viv,j);
                }
            });
        return  v;
    }





}
