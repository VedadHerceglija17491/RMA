package ba.unsa.etf.rma.vedad.a17491projekt;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;

public class KategorijeAkt extends AppCompatActivity implements ListeFragment.OnDodajKClick​, DodavanjeKnjigeFragment.OnDodajClick​, DodavanjeKnjigeFragment.OnPonistiClick​, ListeFragment.OnItemKategorijeClick​, KnjigeFragment.OnNazadClick​,ListeFragment.OnItemAutoriClick​, ListeFragment.OnDodajKOnlineClick, FragmentOnline.OnNazadFOClick​, KnjigaAdapter.OnPreporuciClick {
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    private static final int THIRD_ACTRIVITY_REQUEST_CODE = 1;
    private ArrayList<Knjiga> arrayListKnjige = new ArrayList<Knjiga>();
    private ArrayList<String> arrayListKategorije = new ArrayList<String>();
    private ArrayList<Knjiga> knjigeIzKategorije = new ArrayList<Knjiga>();

    private boolean siriL = false;
    ArrayAdapter<String> stringArrayAdapterKatrgorije2;
    private BazaOpenHelper bazaOpenHelper;
    int data_size = 0;

    @Override
    public void OnItemAutoriClicked(View v, ArrayList<Knjiga> arrayListknjige, ArrayList<String> arrayListkategorije, ArrayList<Knjiga> knjigeautora) {
        arrayListKnjige = arrayListknjige;
        arrayListKategorije = arrayListkategorije;

        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList("knjige", arrayListKnjige);
        arguments.putStringArrayList("kategorije",arrayListKategorije);
        arguments.putParcelableArrayList("knjigeizkategorije", knjigeautora); //ili knjige autora

        KnjigeFragment kf = new KnjigeFragment();

        kf.setArguments(arguments);
        if(siriL == false)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF1,kf).commit();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF2,kf).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_kategorije_akt);

        bazaOpenHelper = new BazaOpenHelper(KategorijeAkt.this);
        arrayListKategorije = bazaOpenHelper.getKategorije();
        arrayListKnjige = bazaOpenHelper.dajKnjige();
        if(savedInstanceState != null)//bitno
        {
       //     arrayListKnjige = savedInstanceState.getParcelableArrayList("knjige");
         //   arrayListKategorije = savedInstanceState.getStringArrayList("kategorije");
        }

        FragmentManager fm = getSupportFragmentManager();

        FrameLayout fKnjige= (FrameLayout) findViewById(R.id.mjestoF2);
        if(fKnjige != null)
        {
            siriL = true;
            KnjigeFragment kf;
            kf = (KnjigeFragment)fm.findFragmentById(R.id.mjestoF2);
            if(kf == null)
            {
                kf = new KnjigeFragment();
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("knjige", arrayListKnjige);
                arguments.putStringArrayList("kategorije",arrayListKategorije);
                arguments.putParcelableArrayList("knjigeizkategorije",knjigeIzKategorije);
                kf.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF2,kf).commit();

            }
        }

        ListeFragment f1 = (ListeFragment) fm.findFragmentByTag("flista");

        if (f1 == null) {
            f1 = new ListeFragment();
            Bundle argumenti = new Bundle();
            argumenti.putParcelableArrayList("knjige", arrayListKnjige);
            argumenti.putStringArrayList("kategorije", arrayListKategorije);
            f1.setArguments(argumenti);
            fm.beginTransaction().replace(R.id.mjestoF1, f1, "flista").commit();
        } else {

            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void OnItemKategorijeClicked(View v, ArrayList<Knjiga> arrayListknjige, ArrayList<String> arrayListkategorije, ArrayList<Knjiga> knjigeIzKategorije) {
        arrayListKnjige = arrayListknjige;
        arrayListKategorije = arrayListkategorije;
        if(siriL == false)
        {
            Bundle arguments = new Bundle();
            arguments.putParcelableArrayList("knjige", arrayListKnjige);
            arguments.putStringArrayList("kategorije",arrayListKategorije);
            arguments.putParcelableArrayList("knjigeizkategorije",knjigeIzKategorije);

            KnjigeFragment kf = new KnjigeFragment();

            kf.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF1,kf).commit();
        }
        else
            {
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("knjige", arrayListKnjige);
                arguments.putStringArrayList("kategorije",arrayListKategorije);
                arguments.putParcelableArrayList("knjigeizkategorije",knjigeIzKategorije);

                KnjigeFragment kf = new KnjigeFragment();

                kf.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF2,kf).commit();
            }


    }

    @Override
    public void OnDodajkClicked(View v, ArrayList<Knjiga> arrayListknjige, ArrayList<String> arrayListkategorije) {
        arrayListKnjige = arrayListknjige;
        arrayListKategorije = arrayListkategorije;
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList("knjige", arrayListKnjige);
        arguments.putStringArrayList("kategorije",arrayListKategorije);

        DodavanjeKnjigeFragment dkf = new DodavanjeKnjigeFragment();

        dkf.setArguments(arguments);
        if(siriL == false)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF1, dkf).commit();
        }
        else {
            FrameLayout mjestof1 = (FrameLayout) findViewById(R.id.mjestoF1);
            mjestof1.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF2, dkf).commit();
        }
    }
    @Override
    public void OnDodajClicked(View v, ArrayList<Knjiga> arrayListKnjige1, ArrayList<String> arrayListKategorije1) {
        arrayListKnjige = arrayListKnjige1;
        arrayListKategorije = arrayListKategorije1;

        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList("knjige", arrayListKnjige);
        arguments.putStringArrayList("kategorije",arrayListKategorije);

        ListeFragment lf = new ListeFragment();

        lf.setArguments(arguments);
        if(siriL == true)
        {
            FrameLayout mjestof1 = (FrameLayout) findViewById(R.id.mjestoF1);
            mjestof1.setVisibility(View.VISIBLE);
            KnjigeFragment kf = new KnjigeFragment();
            Bundle arguments2= new Bundle();
            arguments2.putParcelableArrayList("knjige", arrayListKnjige);
            arguments2.putStringArrayList("kategorije",arrayListKategorije);
            arguments2.putParcelableArrayList("knjigeizkategorije",knjigeIzKategorije);
            kf.setArguments(arguments2);
            getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF2,kf).commit();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF1,lf).commit();

    }
    @Override
    protected void onSaveInstanceState(final Bundle outState) { //bitno
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("kategorije",arrayListKategorije);
        outState.putParcelableArrayList("knjige",arrayListKnjige);
    }

    @Override
    public void OnPonistiClicked(View v, ArrayList<Knjiga> arrayListKnjige1, ArrayList<String> arrayListKategorije1) {
        arrayListKnjige = arrayListKnjige1;
        arrayListKategorije = arrayListKategorije1;
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList("knjige", arrayListKnjige);
        arguments.putStringArrayList("kategorije",arrayListKategorije);

        ListeFragment lf = new ListeFragment();

        lf.setArguments(arguments);
        if(siriL == true)
        {
            FrameLayout mjestof1 = (FrameLayout) findViewById(R.id.mjestoF1);
            mjestof1.setVisibility(View.VISIBLE);

           KnjigeFragment kf = new KnjigeFragment();
            Bundle arguments2= new Bundle();
            arguments2.putParcelableArrayList("knjige", arrayListKnjige);
            arguments2.putStringArrayList("kategorije",arrayListKategorije);
            arguments2.putParcelableArrayList("knjigeizkategorije",knjigeIzKategorije);
            kf.setArguments(arguments2);
            getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF2,kf).commit();

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF1,lf).commit();
    }

    @Override
    public void OnNazadClicked(View v, ArrayList<Knjiga> arrayListKnjige1, ArrayList<String> arrayListKategorije1) {
        arrayListKnjige = arrayListKnjige1;
        arrayListKategorije = arrayListKategorije1;
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList("knjige", arrayListKnjige);
        arguments.putStringArrayList("kategorije",arrayListKategorije);

        ListeFragment lf = new ListeFragment();


        lf.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF1,lf).commit();

    }

    @Override
    public void OnDodajKOnlineCliceked(View v, ArrayList<Knjiga> arrayListknjige, ArrayList<String> arrayListkategorije) {
        arrayListKnjige = arrayListknjige;
        arrayListKategorije = arrayListkategorije;
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList("knjige", arrayListKnjige);
        arguments.putStringArrayList("kategorije",arrayListKategorije);

        FragmentOnline fragmentOnline = new FragmentOnline();

        fragmentOnline.setArguments(arguments);
        if(siriL == false)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF1, fragmentOnline).commit();
        }
        else {
            FrameLayout mjestof1 = (FrameLayout) findViewById(R.id.mjestoF1);
            mjestof1.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF2, fragmentOnline).commit();
        }

    }

    @Override
    public void OnNazadFOClicked(View v, ArrayList<Knjiga> arrayListKnjige1, ArrayList<String> arrayListKategorije1) {
        arrayListKnjige = arrayListKnjige1;
        arrayListKategorije = arrayListKategorije1;
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList("knjige", arrayListKnjige);
        arguments.putStringArrayList("kategorije",arrayListKategorije);

        ListeFragment lf = new ListeFragment();

        lf.setArguments(arguments);
        if(siriL == true)
        {
            FrameLayout mjestof1 = (FrameLayout) findViewById(R.id.mjestoF1);
            mjestof1.setVisibility(View.VISIBLE);

            KnjigeFragment kf = new KnjigeFragment();
            Bundle arguments2= new Bundle();
            arguments2.putParcelableArrayList("knjige", arrayListKnjige);
            arguments2.putStringArrayList("kategorije",arrayListKategorije);
            arguments2.putParcelableArrayList("knjigeizkategorije",knjigeIzKategorije);
            kf.setArguments(arguments2);
            getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF2,kf).commit();

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF1,lf).commit();

    }


    @Override
    public void OnPreporuciClicked(View view, int i) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("knjiga",arrayListKnjige.get(i));

        FragmentPreporuci fragmentPreporuci = new FragmentPreporuci();

        fragmentPreporuci.setArguments(arguments);
        if(siriL == false)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF1, fragmentPreporuci).commit();
        }
        else {
            FrameLayout mjestof1 = (FrameLayout) findViewById(R.id.mjestoF1);
            mjestof1.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF2, fragmentPreporuci).commit();
        }
    }

}
/*
        final Button buttondodadjKategoriju = (Button) findViewById(R.id.dDodajKategoriju);
        Button buttondodadjKnjigu = (Button) findViewById(R.id.dDodajKnjigu);
        Button buttonPretraga = (Button) findViewById(R.id.dPretraga);

        final ListView listViewlistaKategorija = (ListView) findViewById(R.id.listaKategorija);

        final EditText editTexttekstPretraga = (EditText) findViewById(R.id.tekstPretraga);

        final ArrayAdapter<String> stringArrayAdapterKatrgorije;
        stringArrayAdapterKatrgorije = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListKategorije);

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
                stringArrayAdapterKatrgorije.clear();
                stringArrayAdapterKatrgorije.addAll(arrayListKategorije);

                editTexttekstPretraga.setText("");

                stringArrayAdapterKatrgorije2 = new ArrayAdapter<String>(KategorijeAkt.this, android.R.layout.simple_list_item_1, arrayListKategorije);

                listViewlistaKategorija.setAdapter(stringArrayAdapterKatrgorije2);

                buttondodadjKategoriju.setEnabled(false);
            }
        });
        buttondodadjKnjigu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(KategorijeAkt.this, DodavanjeKnjigeAkt.class);
                intent.putStringArrayListExtra("Kategorije", arrayListKategorije);
                intent.putParcelableArrayListExtra("Knjige", arrayListKnjige);
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });
        listViewlistaKategorija.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(KategorijeAkt.this, ListaKnjigaAkt.class);
                knjigeIzKategorije = new ArrayList<Knjiga>();
                for (int brojac = 0; brojac < arrayListKnjige.size(); brojac++)
                {
                    if (arrayListKnjige.get(brojac).getKategorija().equals(arrayListKategorije.get(i)))
                    {
                        knjigeIzKategorije.add(arrayListKnjige.get(brojac));
                    }
                }
                intent.putStringArrayListExtra("Kategorije", arrayListKategorije);
                intent.putParcelableArrayListExtra("Knjige", arrayListKnjige);
                intent.putParcelableArrayListExtra("KnjigeIzKategorije", knjigeIzKategorije);
                startActivityForResult(intent,THIRD_ACTRIVITY_REQUEST_CODE );
            }
        });
    }
*/
 /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check that it is the SecondActivity with an OK result

        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK )
            {
                // get String data from Intent
                arrayListKategorije = data.getStringArrayListExtra("Kategorije");
                arrayListKnjige = data.getParcelableArrayListExtra("Knjige");
            }
        }
       if (requestCode == THIRD_ACTRIVITY_REQUEST_CODE)
       {
            if (resultCode == RESULT_OK )
            {
                // get String data from Intent
                arrayListKategorije = data.getStringArrayListExtra("Kategorije");
                arrayListKnjige = data.getParcelableArrayListExtra("Knjige");
            }
       }

    }*/
