package ba.unsa.etf.rma.vedad.a17491projekt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by HP on 11.4.2018.
 */

public class KnjigeFragment extends Fragment {
    private ArrayList<Knjiga> arrayListKnjige = new ArrayList<Knjiga>();
    private ArrayList<String> arrayListKategorije = new ArrayList<String>();
    private ArrayList<Knjiga> knjigeIzKategorije = new ArrayList<Knjiga>();
    private ListView listica;
    private KnjigaAdapter knjigaAdapter;
    private OnNazadClick​ onc;
    public interface OnNazadClick​
    {
        public void  OnNazadClicked(View v ,ArrayList<Knjiga> arrayListKnjige1, ArrayList<String> arrayListKategorije1);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.knjige_fragment, container, false);
        if(getArguments() != null && getArguments().containsKey("knjige")) {
           // arrayListKnjige = getArguments().getParcelableArrayList("knjige");
            //arrayListKategorije = getArguments().getStringArrayList("kategorije");
            knjigeIzKategorije = getArguments().getParcelableArrayList("knjigeizkategorije");
            listica = (ListView) vi.findViewById(R.id.listaKnjiga);
            Button buttonNazad = (Button) vi.findViewById(R.id.dPovratak);
         //   Button buttonPreporuci = (Button) vi.findViewById(R.id.dPreporuci);
            final BazaOpenHelper bazaOpenHelper = new BazaOpenHelper(getContext());
            arrayListKnjige = bazaOpenHelper.dajKnjige();
            arrayListKategorije = bazaOpenHelper.getKategorije();

                knjigaAdapter = new KnjigaAdapter(getActivity(), knjigeIzKategorije); // mozda ne valja getactivity
                listica.setAdapter(knjigaAdapter);

            listica.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                   // view.setBackgroundResource(R.color.LightBlue);
                    if(!knjigeIzKategorije.get(i).isPritisnut())
                    {
                        view.setBackgroundResource(R.color.LightBlue);
           /*             for(int brojac = 0; brojac < arrayListKnjige.size(); brojac++)
                        {
                            if(knjigeIzKategorije.get(i).getNaziv().toString().equals(arrayListKnjige.get(brojac).getNaziv().toString()))
                            {
                                arrayListKnjige.get(brojac).setPritisnut(true);
                            }
                        }*/
           bazaOpenHelper.updatePritisnut(knjigeIzKategorije.get(i).getNaziv());
           knjigeIzKategorije.get(i).setPritisnut(true);

          // knjigaAdapter.notifyDataSetChanged();
                    }
                }
            });
            try
            {
                onc = (OnNazadClick​) getActivity();
            }  catch (ClassCastException e)  //catch (ClassCastException e)​
            {
                throw new ClassCastException(getActivity().toString()+"Treba implementirati OnItem Click");
            }
            buttonNazad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onc.OnNazadClicked(view,arrayListKnjige,arrayListKategorije);
                }
            });


        }


        return vi;
    }

}
