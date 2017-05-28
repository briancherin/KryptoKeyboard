package joseph.com.antinasakeyboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Brian on 5/28/2017.
 */

public class CameraHelper {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    ImageView mImageView;

    public static void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(EncryptInputIME.context.getPackageManager()) != null) {
         //   startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            mImageView = (ImageView)((Activity)EncryptInputIME.context).findViewById(R.id.image);
            mImageView.setImageBitmap(imageBitmap);

            Drawable d = new BitmapDrawable(EncryptInputIME.context.getResources(), imageBitmap);

            List<Keyboard.Key> keys = EncryptInputIME.kv.getKeyboard().getKeys();
            for (int k = 0; k < keys.size() -1; k++) {  //loop through all keys
                Keyboard.Key currKey = keys.get(k);     //get each key
                //EncryptInputIME.kv.invalidateAllKeys();
                currKey.icon = d;
            }
        }
    }

}
