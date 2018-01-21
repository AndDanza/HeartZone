package air1715.pillcare.Utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * The type Pop up utils.
 */
public class PopUpUtils {

    /**
     * Send message.
     *
     * @param context the context
     * @param text    the text
     */
    public static void sendMessage(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}

