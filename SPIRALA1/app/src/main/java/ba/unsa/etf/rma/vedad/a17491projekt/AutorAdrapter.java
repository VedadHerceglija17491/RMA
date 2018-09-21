package ba.unsa.etf.rma.vedad.a17491projekt;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HP on 9.4.2018.
 */

public class AutorAdrapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<Autor> autorArrayList;

    public AutorAdrapter(Context mContext, ArrayList<Autor> autorArrayList) {
        this.mContext = mContext;
        this.autorArrayList = autorArrayList;
    }


    @Override
    public int getCount() {
        return autorArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return autorArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext,R.layout.element_autor,null);

        TextView tvImeiPrezime = (TextView) v.findViewById(R.id.tvImeiPrezime);
        TextView tvBrojKnjiga = (TextView) v.findViewById(R.id.tvBrojKnjiga);



        tvImeiPrezime.setText(autorArrayList.get(i).getImeiPrezime());
        tvBrojKnjiga.setText(Integer.toString(autorArrayList.get(i).getBrojKnjiga()));

        return v;
    }
}
