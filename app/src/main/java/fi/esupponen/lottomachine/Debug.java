package fi.esupponen.lottomachine;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Debug {
    private static int DEBUG_LEVEL;
    private static boolean UI_DISPLAY;
    private static Context HOST;


    public static void loadDebug(Context host) {
        DEBUG_LEVEL = host.getResources().getInteger(R.integer.debug_level);
        UI_DISPLAY = host.getResources().getBoolean(R.bool.ui_display);
        HOST = host;
    }

    public static void print(String className, String methodName, String message, int level) {
        if (BuildConfig.DEBUG && level <= DEBUG_LEVEL) {
            if (UI_DISPLAY) {
                CharSequence toastMessage = className + ": " + methodName + ": " + message;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(HOST, toastMessage, duration);
                toast.show();
            } else {
                Log.d(className + ": " + methodName, message);
            }
        }
    }
}