package utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Administrator on 12/12/2017.
 */

public class Utility {
    public static Bitmap converBitmap(String path) {
        Bitmap bitmap = null;
        if(path == null) {
            return BitmapFactory.decodeResource(Resources.getSystem(), android.R.drawable.ic_menu_camera);
        }

        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
        bfOptions.inDither = false; // Disable Dithering mode
        bfOptions.inPurgeable = true; // Tell to gc that whether it needs free
                                        // memory, the Bitmap can be cleared
        bfOptions.inInputShareable = true; // Which kind of reference will be
                                            // used to recover the Bitmap data
                                             // after being clear, when it will
                                                // be used in the future
        bfOptions.inTempStorage = new byte[32 * 1024];

        File file = new File(path);
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            if (fs != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  bitmap;
    }

    public static void MakeToast(Context context, String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }
}
