package ba.unsa.etf.rma.vedad.a17491projekt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by HP on 9.4.2018.
 */

public class ListeFragment extends Fragment {
    private ArrayList<Knjiga> arrayListKnjige = new ArrayList<Knjiga>();
    private ArrayList<String> arrayListKategorije = new ArrayList<String>();
    private ArrayList<Knjiga>  knjigeIzKategorije;
    private ArrayList<Autor> autori = new ArrayList<Autor>();
    ArrayAdapter<String> stringArrayAdapterKatrgorije2;
    private OnDodajKClick​ odkc;
    private OnItemKategorijeClick​ oikc;
    private OnItemKategorijeClick​ oiac;
    private  OnDodajKOnlineClick odkoc;
    public interface OnDodajKOnlineClick
    {
        public void OnDodajKOnlineCliceked(View v, ArrayList<Knjiga> arrayListknjige,ArrayList<String> arrayListkategorije);
    }
    public interface OnItemAutoriClick​
    {
        public void  OnItemAutoriClicked(View v,ArrayList<Knjiga> arrayListknjige, ArrayList<String> arrayListkategorije, ArrayList<Knjiga> knjigeautora);
    }
    public interface OnItemKategorijeClick​
    {
        public void  OnItemKategorijeClicked(View v,ArrayList<Knjiga> arrayListknjige, ArrayList<String> arrayListkategorije, ArrayList<Knjiga> knjigeIzKategorije);
    }
    public interface OnDodajKClick​
    {
        public void  OnDodajkClicked(View v,ArrayList<Knjiga> arrayListknjige, ArrayList<String> arrayListkategorije);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View vi = inflater.inflate(R.layout.liste_fragment,container,false);
        return vi;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if(getArguments().containsKey("knjige"))
        {
         //   arrayListKnjige = getArguments().getParcelableArrayList("knjige");
       //     arrayListKategorije = getArguments().getStringArrayList("kategorije");
        }

        final BazaOpenHelper bazaOpenHelper = new BazaOpenHelper(getContext());
        arrayListKategorije = bazaOpenHelper.getKategorije();
        arrayListKnjige = bazaOpenHelper.dajKnjige();
            final ArrayAdapter<String> stringArrayAdapterKatrgorije;
            stringArrayAdapterKatrgorije = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayListKategorije);

            final Button buttondodadjKategoriju = (Button) getView().findViewById(R.id.dDodajKategoriju);
            Button buttondodadjKnjigu = (Button) getView().findViewById(R.id.dDodajKnjigu);
            final Button buttonPretraga = (Button) getView().findViewById(R.id.dPretraga);

            final ListView listViewlistaKategorija = (ListView) getView().findViewById(R.id.listaKategorija);
            final EditText editTexttekstPretraga = (EditText) getView().findViewById(R.id.tekstPretraga);

            listViewlistaKategorija.setAdapter(stringArrayAdapterKatrgorije);
            buttondodadjKategoriju.setEnabled(false); //onemogucen button

            buttonPretraga.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                { // filtriramo listu omogucava button
                    stringArrayAdapterKatrgorije.getFilter().filter(editTexttekstPretraga.getText().toString(), new Filter.FilterListener()
                    {
                        @Override
                        public void onFilterComplete(int count)
                        {
                            if (count == 0 && !editTexttekstPretraga.getText().toString().isEmpty())
                            {
                                buttondodadjKategoriju.setEnabled(true);
                            }
                        }
                    });
                }
            });


            buttondodadjKategoriju.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    arrayListKategorije.add(editTexttekstPretraga.getText().toString());
                    BazaOpenHelper baza = new BazaOpenHelper(getContext());
                    baza.dodajKategoriju(editTexttekstPretraga.getText().toString());
                    stringArrayAdapterKatrgorije.clear();
                    stringArrayAdapterKatrgorije.addAll(arrayListKategorije);
                    editTexttekstPretraga.setText("");
                    stringArrayAdapterKatrgorije2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayListKategorije);
                    listViewlistaKategorija.setAdapter(stringArrayAdapterKatrgorije2);
                    buttondodadjKategoriju.setEnabled(false);

                }
            });
        try
        {
            odkc = (OnDodajKClick​) getActivity();
        }  catch (ClassCastException e)  //catch (ClassCastException e)​
        {
            throw new ClassCastException(getActivity().toString()+"Treba implementirati OnItem Click");
        }
        buttondodadjKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                odkc.OnDodajkClicked(view,arrayListKnjige,arrayListKategorije);
            }
        });
     final   AutorAdrapter aa = new AutorAdrapter(getActivity(), autori);

        final ListView ListViewListaAutora = (ListView) getView().findViewById(R.id.listaAutori);
        ListViewListaAutora.setVisibility(View.INVISIBLE);
        ListViewListaAutora.setAdapter(aa);

        Button buttonAutori=(Button) getView().findViewById(R.id.dAutori);
        buttonAutori.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean trebaDodat = true;

                autori.clear();
                for(int i = 0; i < arrayListKnjige.size(); i++) {
                    for (int j = 0; j < arrayListKnjige.get(i).getAutori().size(); j++)
                    {

                        for (int k = 0; k < autori.size(); k++)
                        {
                            if (autori.get(k).getImeiPrezime().equals(arrayListKnjige.get(i).getAutori().get(j).getImeiPrezime()))
                            {
                                autori.get(k).setBrojKnjiga(autori.get(k).getBrojKnjiga()+1);
                                trebaDodat = false;
                            }
                        }
                        if (trebaDodat == true)
                        {
                            autori.add(new Autor(arrayListKnjige.get(i).getAutori().get(j).getImeiPrezime()));
                        }
                        trebaDodat = true;
                    }
                }
                    buttonPretraga.setVisibility(View.GONE);
                    buttondodadjKategoriju.setVisibility(View.GONE);
                    editTexttekstPretraga.setVisibility(View.GONE);
                    aa.notifyDataSetChanged();
                    listViewlistaKategorija.setVisibility(View.INVISIBLE);
                    ListViewListaAutora.setVisibility(View.VISIBLE);
            }
        });
        Button buttonKategorije=(Button) getView().findViewById(R.id.dKategorije);
        buttonKategorije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPretraga.setVisibility(View.VISIBLE);
                buttondodadjKategoriju.setVisibility(View.VISIBLE);
                editTexttekstPretraga.setVisibility(View.VISIBLE);
                listViewlistaKategorija.setVisibility(View.VISIBLE);
                ListViewListaAutora.setVisibility(View.INVISIBLE);
            }
        });
        try
        {
            oikc = (OnItemKategorijeClick​) getActivity();
        }  catch (ClassCastException e)  //catch (ClassCastException e)​
        {
            throw new ClassCastException(getActivity().toString()+"Treba implementirati OnItem Click");
        }
        listViewlistaKategorija.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long idKategorije = bazaOpenHelper.getIdKategorije(arrayListKategorije.get(i));
                knjigeIzKategorije = bazaOpenHelper.knjigeKategorije(idKategorije);
             /*   for (int brojac = 0; brojac < arrayListKnjige.size(); brojac++)
                {
                    if (arrayListKnjige.get(brojac).getKategorija().equals(arrayListKategorije.get(i)))
                    {
                        knjigeIzKategorije.add(arrayListKnjige.get(brojac));
                    }
                }*/
                oikc.OnItemKategorijeClicked(view,arrayListKnjige,arrayListKategorije,knjigeIzKategorije);
            }
        });
        try
        {
            oiac = (OnItemKategorijeClick​) getActivity();
        }  catch (ClassCastException e)  //catch (ClassCastException e)​
        {
            throw new ClassCastException(getActivity().toString()+"Treba implementirati OnItem Click");
        }

        ListViewListaAutora.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
         ArrayList<Knjiga>knjigeAutora= new ArrayList<Knjiga>();
         long id = bazaOpenHelper.dajIdZaImeAutora(autori.get(i).getImeiPrezime());
         long x = id;
         knjigeAutora = bazaOpenHelper.knjigeAutora(id);
        /* for (int brojac = 0; brojac < arrayListKnjige.size(); brojac++)
                {

                    for(int j = 0; j < arrayListKnjige.get(brojac).getAutori().size(); j++)
                    {
                        if (arrayListKnjige.get(brojac).getAutori().get(j).getImeiPrezime().equals(autori.get(i).getImeiPrezime()))
                        {
                            knjigeAutora.add(arrayListKnjige.get(brojac));
                        }
                    }
                }*/
                oiac.OnItemKategorijeClicked(view,arrayListKnjige,arrayListKategorije,knjigeAutora);
            }
        });
        try
        {
            odkoc = (OnDodajKOnlineClick) getActivity();
        }  catch (ClassCastException e)  //catch (ClassCastException e)​
        {
            throw new ClassCastException(getActivity().toString()+"Treba implementirati OnItem Click");
        }
        final Button buttonOnline = (Button) getView().findViewById(R.id.dDodajOnline);
        buttonOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odkoc.OnDodajKOnlineCliceked(v,arrayListKnjige,arrayListKategorije);
            }
        });

    }
}
