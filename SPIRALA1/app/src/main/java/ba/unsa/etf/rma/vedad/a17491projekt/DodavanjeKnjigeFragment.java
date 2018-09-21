package ba.unsa.etf.rma.vedad.a17491projekt;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by HP on 9.4.2018.
 */

public class DodavanjeKnjigeFragment extends Fragment {
    private ArrayList<Knjiga> arrayListKnjige = new ArrayList<Knjiga>();
    private ArrayList<String> arrayListKategorije = new ArrayList<String>();
    Spinner spinner;
    private static int RESULT_LOAD_IMAGE = 1;

    private OnDodajClick​ odc;
    private OnPonistiClick​ opc;


    public interface OnDodajClick​
    {
        public void  OnDodajClicked(View v ,ArrayList<Knjiga> arrayListKnjige1, ArrayList<String> arrayListKategorije1);
    }
    public interface OnPonistiClick​
    {
        public void  OnPonistiClicked(View v ,ArrayList<Knjiga> arrayListKnjige1, ArrayList<String> arrayListKategorije1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View vi = inflater.inflate(R.layout.dodavanjeknjige_fragment,container,false);
        if(getArguments() != null && getArguments().containsKey("knjige"))
        {
        //    arrayListKnjige = getArguments().getParcelableArrayList("knjige");
        //    arrayListKategorije = getArguments().getStringArrayList("kategorije");
            Button buttonLoadImage = (Button) vi.findViewById(R.id.dNadjiSliku);
            Button buttonDodavanjeKnjige = (Button)   vi.findViewById(R.id.dUpisiKnjigu);
            Button buttonNazad = (Button)   vi.findViewById(R.id.dPonisti);
            BazaOpenHelper bazaOpenHelper = new BazaOpenHelper(getContext());
            arrayListKategorije = bazaOpenHelper.getKategorije();
            arrayListKnjige = bazaOpenHelper.dajKnjige();
            final EditText editTextIme = (EditText)   vi.findViewById(R.id.imeAutora);
            final EditText editTextNaziv = (EditText)   vi.findViewById(R.id.nazivKnjige);
            final ImageView imageViewNaslovna = (ImageView)   vi.findViewById(R.id.naslovnaStr);
            spinner = (Spinner)  vi.findViewById(R.id.sKategorijaKnjige);

            final ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayListKategorije);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            if(spinner != null)
            {
                spinner.setAdapter(adapter);
            }
            buttonLoadImage.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            });
            try
            {
                odc = (OnDodajClick​) getActivity();
            }  catch (ClassCastException e)  //catch (ClassCastException e)​
            {
                throw new ClassCastException(getActivity().toString()+"Treba implementirati OnItem Click");
            }
            buttonDodavanjeKnjige.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    if (arrayListKategorije.size() != 0 && imageViewNaslovna.getDrawable() != null) {
                        Bitmap bitmap = ((BitmapDrawable) imageViewNaslovna.getDrawable()).getBitmap();
                        Knjiga k = new Knjiga(editTextIme.getText().toString(),editTextNaziv.getText().toString(), spinner.getSelectedItem().toString());
                      //   knjige.add(new Knjiga(id,naziv,autors,opis,datumObjavljivanja,url2,brojStranica));
                        ArrayList<Autor> autors = new ArrayList<Autor>();
                        autors.add(new Autor(k.getImeAutora()));
                        Knjiga K = new Knjiga("nepostoji",k.getNaziv(),autors,"bezopisa","nepoznat",null,0,k.getKategorija(),"nepostoji");
                        BazaOpenHelper baza = new BazaOpenHelper(getContext());
                        long autorId =  baza.dodajAutora(K.getAutori().get(0).getImeiPrezime());
                        long knjigaId = baza.dodajKnjigu(K);
                        baza.dodajAutorstvo((int)autorId,(int)knjigaId);
                        FileOutputStream outputStream;
                        try {
                            outputStream = getActivity().openFileOutput(k.getNaziv(), Context.MODE_PRIVATE);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                            outputStream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        arrayListKnjige.add(K);
                        editTextIme.setText("");
                        editTextNaziv.setText("");
                        imageViewNaslovna.setImageBitmap(null);
                        odc.OnDodajClicked(view,arrayListKnjige,arrayListKategorije);
                    }
                }
            });
            try
            {
                opc = (OnPonistiClick​) getActivity();
            }  catch (ClassCastException e)  //catch (ClassCastException e)​
            {
                throw new ClassCastException(getActivity().toString()+"Treba implementirati OnItem Click");
            }
            buttonNazad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    opc.OnPonistiClicked(view,arrayListKnjige,arrayListKategorije);
                }
            });
        }
        return vi;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) getActivity().findViewById(R.id.naslovnaStr);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
}
