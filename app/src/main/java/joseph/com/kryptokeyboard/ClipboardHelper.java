package joseph.com.kryptokeyboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Brian on 5/27/2017.
 */

public class ClipboardHelper {
    static ClipboardManager clipboard = (ClipboardManager) EncryptInputIME.context.getSystemService(Context.CLIPBOARD_SERVICE);
    static String pasteData = "";

    static String filename = "claipeee.txt";

    public static String getClipboardText() {
        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
        pasteData = item.getText().toString();

        return pasteData;

    }

    public static void clearFile() {
        try {
            FileOutputStream fOut2 = EncryptInputIME.context.openFileOutput(filename, Context.MODE_PRIVATE);
            try {
                fOut2.write("".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut2.write(System.getProperty("line.separator").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String str){
        try {
            FileOutputStream fOut = EncryptInputIME.context.openFileOutput(filename,Context.MODE_APPEND);
            //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOut));
            //OutputStreamWriter os = new OutputStreamWriter(fOut);

            try {
                fOut.write(str.getBytes());
                fOut.write(System.getProperty("line.separator").getBytes());
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }



         /*   System.out.println("Str = " + str);

            //String str = "jdklsfblajfgklasfhgksljfhgslfhg";
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("paste.txt", true));

                bw.write(str);
                bw.newLine();

                *//*os.write(str);
                os.write("\n");*//*


                *//*fOut.write(str.getBytes());
                fOut.write('\n');
                fOut.close();*//*
            } catch (IOException e) {
                e.printStackTrace();
            }*/

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void readFile(){
        try {
            FileInputStream fin = EncryptInputIME.context.openFileInput(filename);
            //FileInputStream fin = new FileInputStream(new File("paste.txt"));
            int c;
            String temp="";
            try {
                while( (c = fin.read()) != -1){
                    temp = temp + Character.toString((char)c);
                }
                fin.close();
                System.out.println(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static String readLineFromFile(int line){
        String c = "";
        try {
            FileInputStream fin = EncryptInputIME.context.openFileInput(filename);
            BufferedReader bf = new BufferedReader((new InputStreamReader(fin)));

            String temp="";
            try {
                int count = 0;
                while((c = bf.readLine()) != null && count < line){
                    System.out.println("line: " + c);
                    count++;
                }
                fin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }

}
