package ba.unsa.etf.rma.vedad.a17491projekt;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class FragmentPreporuci extends Fragment {
   private Knjiga k;
   private ArrayList<Contact_Model> modeli = new ArrayList<Contact_Model>();
   private ArrayList<String> imena = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.preporuci_fragment,container,false);
        modeli = readContacts();
        for(int i = 0; i < modeli.size(); i++)
        {
            imena.add(modeli.get(i).getIme());
        }
        Button dgm = (Button)vi.findViewById(R.id.dPosalji);
       if(getArguments() != null && getArguments().containsKey("knjiga"))
        {
            k = getArguments().getParcelable("knjiga");
        }
        TextView tvNaziv = (TextView) vi.findViewById(R.id.nazivKnjige);
        TextView tvAutor = (TextView) vi.findViewById(R.id.imeAutora);
        TextView tvBrojStranica = (TextView) vi.findViewById(R.id.brojStranica);
        TextView tvOpis = (TextView) vi.findViewById(R.id.opis);
        TextView tvDatum = (TextView) vi.findViewById(R.id.datum);
        ImageView ivSlika = (ImageView) vi.findViewById(R.id.eNaslovna2);
        final Spinner spinnerKontakata = (Spinner)  vi.findViewById(R.id.sKontakti);
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, imena);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        if(spinnerKontakata != null)
        {
            spinnerKontakata.setAdapter(adapter);
        }
        if(k.getBrojStranica() != 0)
        {
            tvBrojStranica.setText("Broj stranica:"+Integer.toString(k.getBrojStranica()));
        }
        else
        {
            tvBrojStranica.setText("Broj stranica nepoznat");
        }
        tvOpis.setText(k.getOpis());
        if(!k.getDatumObjavljivanja().equals("nepoznat"))
        {
            tvDatum.setText(k.getDatumObjavljivanja());
        }
        else
        {
            tvDatum.setText("Nepoznat datum objavljivanja");
        }
        tvNaziv.setText(k.getNaziv());
        tvAutor.setText(k.getAutori().get(0).getImeiPrezime());
        if(!k.getId().equals("nepostoji"))
        {
            if(k.getSlika() != null)
            {
                Uri uri =  Uri.parse(k.getSlika().toString());
                Picasso.get().load(uri).into(ivSlika);
            }else
            {
                ivSlika.setImageResource(R.drawable.slikaknjige);
            }
        } else
        {
            try {
                ivSlika.setImageBitmap(BitmapFactory.decodeStream(getActivity().openFileInput(k.getNaziv())));
            }
            catch (IOException E)
            {
                E.printStackTrace();
            }
        }
        dgm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact_Model cm;
                for(int i = 0; i < modeli.size(); i++)
                {
                    if(modeli.get(i).getIme().equals(spinnerKontakata.getSelectedItem().toString()))
                    {
                        cm = modeli.get(i);
                        sendEmail(cm,k);
                        break;
                    }
                }

            }
        });


        return vi;
    }
    protected void sendEmail(Contact_Model contactModel, Knjiga k) {
        Log.i("Send email", "");
        String[] TO = {contactModel.geteMail()};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Zdravo "+contactModel.getIme()+","+ "\n" +
                "Pročitaj knjigu " + k.getNaziv() + " od " + k.getAutori().get(0).getImeiPrezime() +  "!");
      //  emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            getActivity().finish();
            Log.i("Finished sending email", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Contact_Model> readContacts(){
        ArrayList<Contact_Model> contactList = new ArrayList<Contact_Model>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI; // Contact URI
        Cursor contactsCursor = getContext().getContentResolver().query(uri, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC "); // Return

        if (contactsCursor.moveToFirst()) {
            do {
                long contctId = contactsCursor.getLong(contactsCursor.getColumnIndex("_ID")); // Get contact ID
                Uri dataUri = ContactsContract.Data.CONTENT_URI; // URI to get

                Cursor dataCursor = getContext().getContentResolver().query(dataUri, null, ContactsContract.Data.CONTACT_ID + " = " + contctId, null, null);// Retrun data cusror represntative to
                String displayName = "";
                String homeEmail = "";
                String workEmail = "";
                String contactEmailAddresses = "";
                if (dataCursor.moveToFirst()) { displayName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                do {
                    if (dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE))
                    {

                            switch (dataCursor.getInt(dataCursor.getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                                    homeEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                    contactEmailAddresses +=   homeEmail;
                                    break;

                            }
                        }
                }while (dataCursor.moveToNext());
                contactList.add(new Contact_Model(Long.toString(contctId), displayName, contactEmailAddresses));
                }
                } while (contactsCursor.moveToNext());
        }
        return contactList;
    }
}
