package ba.unsa.etf.rma.vedad.a17491projekt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class



FragmentOnline extends Fragment implements DohvatiKnjige.IDohvatiKnjigeDone, DohvatiNajnovije.IDohvatiNajnovijeDone, MojResultReceiver.Receiver {
    private ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();
    private ArrayList<String> naziviKnjiga = new ArrayList<String>();
    private ArrayList<Knjiga> arrayListKnjige = new ArrayList<Knjiga>();
    private ArrayList<String> arrayListKategorije = new ArrayList<String>();
    private OnNazadFOClick​ onFOc;
    private MojResultReceiver mReceiver;

    @Override
    public void onNajnovijeDone(ArrayList<Knjiga> knjige2) {
        knjige = knjige2;
        naziviKnjiga.clear();

        for(int i = 0; i < knjige.size(); i++)
        {
            naziviKnjiga.add(knjige.get(i).getNaziv());
        }
        Spinner spinerKnjiga = (Spinner) getView().findViewById(R.id.sRezultat);
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, naziviKnjiga);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(spinerKnjiga != null)
        {
            spinerKnjiga.setAdapter(adapter);
        }

    }

    @Override
    public void OnReciveResult(int resultCode, Bundle resultData) {
        switch (resultCode){
            case KnjigePoznanika.STATUS_START:
                Toast.makeText(getActivity(),"pozivUpucen",Toast.LENGTH_LONG).show();

                break;
            case KnjigePoznanika.STATUS_FINISH:
                knjige = resultData.getParcelableArrayList("result");
                naziviKnjiga.clear();

                for(int i = 0; i < knjige.size(); i++)
                {
                    naziviKnjiga.add(knjige.get(i).getNaziv());
                }
                Spinner spinerKnjiga = (Spinner) getView().findViewById(R.id.sRezultat);
                final ArrayAdapter<String> adapter;
                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, naziviKnjiga);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if(spinerKnjiga != null)
                {
                    spinerKnjiga.setAdapter(adapter);
                }

                break;
            case KnjigePoznanika.STATUS_ERROR:
                //String error ​=​ resultData​.​getString​(​Intent​.​EXTRA_TEXT​);
                //Toast​.​makeText​(​this​,​ error​,​ Toast​.​LENGTH_LONG​).​show​();
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
                break;
        }
    }

    public interface OnNazadFOClick​
    {
        public void  OnNazadFOClicked(View v ,ArrayList<Knjiga> arrayListKnjige1, ArrayList<String> arrayListKategorije1);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View vi = inflater.inflate(R.layout.fragment_online,container,false);
        Button dgm = (Button)vi.findViewById(R.id.dRun);
        final EditText editText = (EditText) vi.findViewById(R.id.tekstUpit);
        if(getArguments() != null && getArguments().containsKey("knjige")) {
           // arrayListKnjige = getArguments().getParcelableArrayList("knjige");
          //  arrayListKategorije = getArguments().getStringArrayList("kategorije");
        }
        BazaOpenHelper bazaOpenHelper = new BazaOpenHelper(getContext());
        arrayListKategorije = bazaOpenHelper.getKategorije();
        arrayListKnjige = bazaOpenHelper.dajKnjige();
        final Spinner spinnerKategorija = (Spinner)  vi.findViewById(R.id.sKategorije);
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayListKategorije);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(spinnerKategorija != null)
        {
            spinnerKategorija.setAdapter(adapter);
        }
        dgm.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String stringZaPretragu = editText.getText().toString();

                                        if(jeLiDohvatiNajnovije(stringZaPretragu))
                                        {
                                            new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone)FragmentOnline.this).execute(stringZaPretragu);
                                        }
                                        else if(jeLiKnjigeKorisnika((stringZaPretragu)))
                                        {

                                          Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(),KnjigePoznanika.class);
                                           // mReceiver ​=​ ​new​ MojResultReceiver​(​new​ Handler​());
                                           // mReceiver​.​setReceiver​(​this​);
                                            mReceiver = new MojResultReceiver(new Handler());
                                            mReceiver.setReceiver(FragmentOnline.this);
                                            intent.putExtra("parametar1", stringZaPretragu);
                                            intent.putExtra("receiver",mReceiver);
                                            getActivity().startService(intent);
                                        }
                                        else
                                        {
                                            new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone) FragmentOnline. this ).execute( stringZaPretragu );
                                        }

                                    }
                                }
        );
        Button buttonNazad = (Button) vi.findViewById(R.id.dPovratak);
        try
        {
            onFOc = (OnNazadFOClick​) getActivity();
        }  catch (ClassCastException e)  //catch (ClassCastException e)​
        {
            throw new ClassCastException(getActivity().toString()+"Treba implementirati OnItem Click");
        }
        buttonNazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFOc.OnNazadFOClicked(view,arrayListKnjige,arrayListKategorije);
            }
        });
        final Spinner spinerKnjiga = (Spinner) vi.findViewById(R.id.sRezultat);
        Button buttonDodaj = (Button) vi.findViewById(R.id.dAdd);
        buttonDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(knjige.size() != 0 && arrayListKategorije.size() != 0)
                {
                    String naziv  = spinerKnjiga.getSelectedItem().toString();
                    for(int i = 0; i < knjige.size(); i++)
                    {
                        if(knjige.get(i).getNaziv().equals(naziv))
                        {
                           // Knjiga k = new Knjiga(editTextNaziv.getText().toString(),editTextIme.getText().toString(), spinner.getSelectedItem().toString());
                           // Knjiga k = new Knjiga(knjige.get(i).getNaziv(),knjige.get(i).getAutori(),spinnerKategorija.getSelectedItem().toString());
                            //  Knjiga K = new Knjiga(null,k.getNaziv(),autors,"bezopisa","nepoznat",null,0,k.getKategorija());
                           Knjiga k = new Knjiga(knjige.get(i).getId(),knjige.get(i).getNaziv(),knjige.get(i).getAutori(),knjige.get(i).getOpis(),knjige.get(i).getDatumObjavljivanja(),knjige.get(i).getSlika(),knjige.get(i).getBrojStranica(),spinnerKategorija.getSelectedItem().toString(),"nekiWeb");
                            BazaOpenHelper baza = new BazaOpenHelper(getContext());
                            long knjigaId = baza.dodajKnjigu(k);
                            for(int j = 0; j < k.getAutori().size(); j++)
                            {
                                long autorId =  baza.dodajAutora(k.getAutori().get(j).getImeiPrezime());
                                baza.dodajAutorstvo((int)autorId,(int)knjigaId);
                            }
                            arrayListKnjige.add(k);
                                break;
                        }
                    }

                }

            }
        });
        if(naziviKnjiga.size() != 0)
        {
            final ArrayAdapter<String> adapter2;
            adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, naziviKnjiga);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            if(spinerKnjiga != null)
            {
                spinerKnjiga.setAdapter(adapter2);
            }
        }



        return vi;
    }

    @Override
    public void OnDohvatiDone(ArrayList<Knjiga> knjige2) {
    knjige = knjige2;
    naziviKnjiga.clear();

    for(int i = 0; i < knjige.size(); i++)
    {
        naziviKnjiga.add(knjige.get(i).getNaziv());
    }
    Spinner spinerKnjiga = (Spinner) getView().findViewById(R.id.sRezultat);
    final ArrayAdapter<String> adapter;
    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, naziviKnjiga);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    if(spinerKnjiga != null)
    {
        spinerKnjiga.setAdapter(adapter);
    }

    }
    private boolean jeLiDohvatiNajnovije (String s)
    {

        String pomocni = new String();
        String rezultat = new String();
        if(s.length() < 6)
        {
            return false;
        }
            for(int i = 0; i < 6; i++)
        {
            pomocni += s.charAt(i);

        }
        if(pomocni.equals("autor:") )
        {

            return true;
        }
        return false;

    }
    private boolean jeLiKnjigeKorisnika (String s)
    {

        String pomocni = new String();
        String rezultat = new String();
        if(s.length() < 9)
        {
            return false;
        }
        for(int i = 0; i < 9; i++)
        {
            pomocni += s.charAt(i);

        }
        if(pomocni.equals("korisnik:") )
        {

            return true;
        }
        return false;

    }

}
