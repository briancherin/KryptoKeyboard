package joseph.com.antinasakeyboard;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Brian on 5/27/2017.
 */

//fix done button
    // so it doesnt send empty encrypted string
    // and so it doesnt do the clear function and the encrypt function


public class EncryptInputIME extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;


    public static KeyboardView kv;
    private Keyboard keyboard;

    private boolean caps = false;

    static List<CharSequence> defaultKeyLabels;

    public static Context context;

    public static String inputText = "";



    @Override
    public View onCreateInputView(){
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);



        context = this;

        Encryption.initializeEncryption();  //set up encryption keys

        defaultKeyLabels = new ArrayList<>();

        List<Keyboard.Key> keys = kv.getKeyboard().getKeys();
        for (int j = 0; j < keys.size() -1; j++) {
            Keyboard.Key currentKey = keys.get(j);
            defaultKeyLabels.add(currentKey.label);
        }

        return kv;
    }




    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

        try {
            InputConnection ic = getCurrentInputConnection();
            if (ic.getTextBeforeCursor(10, 0).length() == 0 && ic.getTextAfterCursor(10, 0).length() == 0) {
                inputText = "";
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }



    @Override
    public void onPress(int i) {

      //  System.out.println("just pressed key with code " + i);

       String output = "";

        switch(i){
            case 167:

                System.out.println("Clipboard: " + ClipboardHelper.getClipboardText());
                ClipboardHelper.writeToFile(ClipboardHelper.getClipboardText());

                ClipboardHelper.readFile();
                break;
            case 961:   //clip 1
                output = ClipboardHelper.readLineFromFile(1);

                break;
            case 945:   //clip 2
                output = ClipboardHelper.readLineFromFile(2);
                break;
            case 1109:  //clip 3
                output = ClipboardHelper.readLineFromFile(3);
                break;
            case 1090:  //clip 4
                output = ClipboardHelper.readLineFromFile(4);
                break;
            case 1108:  //clip 5
                output = ClipboardHelper.readLineFromFile(5);
             //   ClipboardHelper.clearFile();
                break;
            case 10005: //clear clipboard key
                ClipboardHelper.clearFile();
                break;
            case 9881:
                Intent intent = new Intent(EncryptInputIME.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                break;
            case 1012:  //camera
                //CameraHelper.dispatchTakePictureIntent();
                /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(EncryptInputIME.context.getPackageManager()) != null) {
                    ((Activity)context).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }*/
                Intent intent2 = new Intent(EncryptInputIME.this, SettingsActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



                break;
            default:

        }

        InputConnection ic = getCurrentInputConnection();
        inputText += output;
   //     ic.setComposingText((output == null) ? "" : output, 1);
       // ic.setComposingText(output, 1);




        /*//Modify the key labels
        Keyboard currentKeyboard = kv.getKeyboard();
        try {
            List<Keyboard.Key> keys = currentKeyboard.getKeys();
            for (int j = 0; j < keys.size() -1; j++){
                Keyboard.Key currentKey = keys.get(j);
                    //reset to default key labels
                    if (i == Keyboard.KEYCODE_DONE){ //if press done
                        System.out.println("i = " + i);
                        System.out.println("Pressed done");
                        for (int k = 0; k < keys.size() -1; k++) {

                            Keyboard.Key currKey = keys.get(k);
                            kv.invalidateAllKeys();
                            currKey.label = defaultKeyLabels.get(k);
                        }
                    }

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }*/
    }



    @Override
    public void onRelease(int i) {

    }


    @Override
    public void onKey(int i, int[] ints) {
        InputConnection ic = getCurrentInputConnection();
        switch(i){
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1,0);
                inputText = (inputText.length() <= 0) ? inputText : inputText.substring(0, inputText.length()-1);
                ic.setComposingText(inputText, 1);

                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                //ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                /*StringBuilder sb = new StringBuilder();
                sb.append(inputText).reverse();*/
                if (inputText.length()>0) {
                    ic.commitText(Encryption.encrypt(inputText), 1);
                }
                //SEND inputText TO ENCRYPTION FUNCTION HERE, then do ic.commitText(encryptedText, 1);
                inputText = "";

                break;
            //Ignore these key presses:
            case 961:
                break;
            case 945:
                break;
            case 1109:
                break;
            case 1090:
                break;
            case 1108:
                break;
            case 167:   //symbol above comma to save the clipboard contents
                break;
            case 10005: //clear clipboard symbol
                break;
            case 9881:
                break;
            case 1012:  //camera
                break;

            default:
                char code = (char)i;
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                inputText += String.valueOf(code);
                ic.setComposingText(inputText, 1);


        }
    }

    public static void changeLabels(String text) {
        //Modify the key labels
        Keyboard currentKeyboard = kv.getKeyboard();
        try {
            List<Keyboard.Key> keys = currentKeyboard.getKeys();


            //DO THIS WHEN RECEIVING ENCRYPTED TEXT
            //FIRST DECRYPT
        //    String text = "Helo there what is your name my name is brian what is yours lol?";

            String[]parts = text.split(" ");

            for (int j = 0; j < keys.size() -1; j++){
                Keyboard.Key currentKey = keys.get(j+1);    //skip the first key (clipboard)
                //if (currentKey.codes[0] == Keyboard.KEYCODE_SHIFT) {

                //reset to default key labels
             /*   if (i == Keyboard.KEYCODE_DONE){ //if press done
                    System.out.println("Pressed done");
                    for (int k = 0; k < keys.size() -1; k++) {

                        Keyboard.Key currKey = keys.get(k);
                        kv.invalidateAllKeys();
                        currKey.label = defaultKeyLabels.get(k);
                        //if (keys.get(k).codes[0] != Keyboard.KEYCODE_SHIFT)kv.invalidateKey(k);
                    }
                }*/

                if (j < parts.length) {
                    currentKey.label = parts[j];

                    kv.invalidateKey(j+1);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void resetKeyLabels() {
        List<Keyboard.Key> keys = kv.getKeyboard().getKeys();
        for (int k = 0; k < keys.size() -1; k++) {  //loop through all keys
            Keyboard.Key currKey = keys.get(k);     //get each key
            kv.invalidateAllKeys();
            currKey.label = defaultKeyLabels.get(k);//reset key to default label
        }
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {
        resetKeyLabels();
    }

    @Override
    public void swipeRight() {
        resetKeyLabels();
    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    public void dispatchTakePictureIntent() {

    }
}
