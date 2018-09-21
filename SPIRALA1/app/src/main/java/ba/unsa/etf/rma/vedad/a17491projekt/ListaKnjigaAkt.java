package ba.unsa.etf.rma.vedad.a17491projekt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaKnjigaAkt extends AppCompatActivity
{


    private ListView listica;
    private KnjigaAdapter knjigaAdapter;
    private ArrayList<Knjiga> knjigaArrayList = new ArrayList<Knjiga>();
    private ArrayList<Knjiga> knjigeIzKategorije = new ArrayList<Knjiga>();
    private ArrayList<String> Kategorije = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_knjiga_akt);

        listica = (ListView) findViewById(R.id.listaKnjiga);
        Button buttonNazad = (Button) findViewById(R.id.dPovratak);


        Bundle bdl = getIntent().getExtras();
        knjigaArrayList = bdl.getParcelableArrayList("Knjige");
        knjigeIzKategorije = bdl.getParcelableArrayList("KnjigeIzKategorije");
        Kategorije = bdl.getStringArrayList("Kategorije");

        if(knjigeIzKategorije.size() != 0)
        {
            knjigaAdapter = new KnjigaAdapter(getApplicationContext(), knjigeIzKategorije);
            listica.setAdapter(knjigaAdapter);
        }
        buttonNazad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("Kategorije", Kategorije);
                intent.putParcelableArrayListExtra("Knjige", knjigaArrayList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
            listica.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    if(!knjigeIzKategorije.get(i).isPritisnut())
                    {
                        view.setBackgroundResource(R.color.LightBlue);
                        for(int brojac = 0; brojac < knjigaArrayList.size(); brojac++)
                        {
                            if(knjigeIzKategorije.get(i).getNaziv().toString().equals(knjigaArrayList.get(brojac).getNaziv().toString()) && knjigeIzKategorije.get(i).getImeAutora().toString().equals(knjigaArrayList.get(brojac).getImeAutora().toString())  )
                            {
                                knjigaArrayList.get(brojac).setPritisnut(true);
                            }
                        }
                    }
                }
            });
    }
}
