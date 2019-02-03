package fi.esupponen.lottomachine;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

public class LottoSearch extends Service implements Runnable {
    boolean serviceOn;
    boolean gameOn;
    int weeks;
    ArrayList<Integer> chosenNumbers;

    public void displayWinNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("You won the lotto!")
                        .setContentText("Congratulations! It took " + weeks + " weeks to get a match.");
        int NOTIFICATION_ID = 12345;

        /**Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);*/
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }

    public ArrayList<Integer> drawLotto() {
        ArrayList<Integer> lotto = new ArrayList<>();

        while (lotto.size() < 7) {
            Integer random = new Integer((int)(Math.random()*40)+1);

            if (!lotto.contains(random)) {
                lotto.add(random);
            }
        }

        Debug.print("LottoSearch", "drawLotto", "numbers: " + lotto, 2);

        return lotto;
    }

    public boolean lottoMatch(ArrayList<Integer> lotto) {
        boolean match = true;

        for (Integer i : chosenNumbers) {
            if (!lotto.contains(i)) {
                match = false;
                break;
            }
        }

        return match;
    }

    @Override
    public void run() {
        while (gameOn) {
            weeks++;
            Debug.print("LottoSearch", "run", "week: " + weeks, 1);
            if (lottoMatch(drawLotto())) {
                Debug.print("LottoSearch", "run", "WINNER FOUND", 1);
                displayWinNotification();
                gameOn = false;
            }

            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!serviceOn) {
            Debug.print("LottoSearch", "onStartCommand", "service starting", 1);
            chosenNumbers = (ArrayList<Integer>) intent.getExtras().getSerializable("chosenNumbers");
            gameOn = true;
            Thread t = new Thread(this);
            t.start();
        } else {
            Debug.print("LottoSearch", "onStartCommand", "service already on", 1);
        }

        return START_STICKY;
    }

    public void onCreate() {
        serviceOn = false;
        gameOn = false;
        weeks = 0;
    }

    public void onDestroy() {
        serviceOn = false;
        gameOn = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
