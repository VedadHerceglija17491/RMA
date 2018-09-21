package ba.unsa.etf.rma.vedad.a17491projekt;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class MojResultReceiver extends ResultReceiver {
    private Receiver mReceiver;

    public MojResultReceiver(Handler handler) {
        super(handler);
    }
    public void setReceiver (Receiver receiver){
        mReceiver = receiver;
    }
    public interface Receiver{
        public void OnReciveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(mReceiver != null){
            mReceiver.OnReciveResult(resultCode,resultData);
        }
    }
}
