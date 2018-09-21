package ba.unsa.etf.rma.vedad.a17491projekt;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DodavanjeKnjigeAkt extends AppCompatActivity
{
    private ArrayList<String> arrayListKategorije;
    private ArrayList<Knjiga> arrayListKnjige = new ArrayList<Knjiga>();
    Spinner spinner;
    private static int RESULT_LOAD_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_knjige_akt);

        Button buttonLoadImage = (Button) findViewById(R.id.dNadjiSliku);
        Button buttonDodavanjeKnjige = (Button) findViewById(R.id.dUpisiKnjigu);
        Button buttonNazad = (Button) findViewById(R.id.dPonisti);

        final EditText editTextIme = (EditText) findViewById(R.id.imeAutora);
        final EditText editTextNaziv = (EditText) findViewById(R.id.imeAutora);
        final ImageView imageViewNaslovna = (ImageView) findViewById(R.id.naslovnaStr);

        Bundle bdl = getIntent().getExtras();

        arrayListKategorije = bdl.getStringArrayList("Kategorije");
        arrayListKnjige = bdl.getParcelableArrayList("Knjige");

        spinner = (Spinner) findViewById(R.id.sKategorijaKnjige);

        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(DodavanjeKnjigeAkt.this, android.R.layout.simple_spinner_item, arrayListKategorije);
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

        buttonDodavanjeKnjige.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(arrayListKategorije.size() != 0 && imageViewNaslovna.getDrawable() != null  )
                {

                    Bitmap bitmap = ((BitmapDrawable)imageViewNaslovna.getDrawable()).getBitmap();





                    Knjiga k = new Knjiga(editTextIme.getText().toString(), editTextNaziv.getText().toString(), spinner.getSelectedItem().toString());

                    FileOutputStream outputStream;
                    try {
                        outputStream = openFileOutput(k.getNaziv(), Context.MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
                        outputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    arrayListKnjige.add(k);
                   editTextIme.setText("");

                   editTextNaziv.setText("");
                   imageViewNaslovna.setImageBitmap(null);
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("Kategorije", arrayListKategorije);
                    intent.putParcelableArrayListExtra("Knjige", arrayListKnjige);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        buttonNazad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("Kategorije", arrayListKategorije);
                intent.putParcelableArrayListExtra("Knjige", arrayListKnjige);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();


            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.naslovnaStr);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }


}
