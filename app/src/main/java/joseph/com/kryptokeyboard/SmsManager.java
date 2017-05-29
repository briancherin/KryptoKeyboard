package joseph.com.kryptokeyboard;

/**
 * Created by shrey on 5/28/16.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;


public class  SmsManager extends BroadcastReceiver {
    private String TAG = SmsManager.class.getSimpleName();
    public String message;
    AesCbcWithIntegrity.SecretKeys keys;

    Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;

        // Get the data (SMS data) bound to intent
        Bundle bundle = intent.getExtras();

        SmsMessage[] msgs = null;

        String str = "";

        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received
            for (int i = 0; i < msgs.length; i++) {
                // Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                // Sender's phone number
                str += "SMS from " + msgs[i].getOriginatingAddress() + " : ";
                // Fetch the text message
                str += msgs[i].getMessageBody().toString();
                message = msgs[i].getMessageBody().toString();
                str += "\n";
                System.out.println("it got the message");
            }

            // Display the entire SMS Message
            try {
                if (message.split(":").length == 3) {

                /*keys = AesCbcWithIntegrity.generateKey();*/
                    keys = AesCbcWithIntegrity.keys(Encryption.defaultKey);
                    System.out.println("The key is: " + keys.toString());
                    //String encrypttext = MainActivity.encrypt(message);
                    String decryptedtext = Encryption.decrypt(message);
                    System.out.println("The message is: " + message);
                    //System.out.println("Encrypted text: " + encrypttext);
                    System.out.println("Decrypted text: " + decryptedtext);


                    EncryptInputIME.changeLabels(decryptedtext);    //set key labels to message

                }
                else {
                    System.out.println("improper input");
                    System.out.println(new ArrayList<String>(Arrays.asList(message.split(":"))));
                }

            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }

    }

    public String getMessage(){
        return message;
    }
}