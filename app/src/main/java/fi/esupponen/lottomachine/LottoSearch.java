package fi.esupponen.lottomachine;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

public class LottoSearch extends Service implements Runnable {
    IBinder myBinder;

    boolean serviceOn;
    boolean gameOn;
    int weeks;
    int sleepTime;
    int skillLevel;
    ArrayList<Integer> chosenNumbers;

    public void displayWinNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("You won the lotto!")
                        .setContentText("Congratulations! It took " + weeks + " weeks to get " + skillLevel + " right.");
        int NOTIFICATION_ID = 1;

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

        Debug.print("LottoSearch", "drawLotto", "numbers: " + lotto, 3);

        return lotto;
    }

    public boolean lottoMatch(ArrayList<Integer> lotto) {
        int rightNumbers = 0;

        for (Integer i : chosenNumbers) {
            if (lotto.contains(i)) {
                rightNumbers++;
            }
        }

        Debug.print("LottoSearch", "drawLotto", "rightNumbers: " + rightNumbers, 3);

        return rightNumbers >= skillLevel;
    }

    @Override
    public void run() {
        while (gameOn) {
            weeks++;
            Debug.print("LottoSearch", "run", "week: " + weeks, 2);
            ArrayList<Integer> lotto = drawLotto();

            if (lottoMatch(lotto)) {
                Debug.print("LottoSearch", "run", "WINNER FOUND", 1);
                displayWinNotification();
                gameOn = false;
            }

            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
            Intent i = new Intent("passedIteration");
            i.putExtra("lottoNumbers", lotto);
            i.putExtra("passedWeeks", weeks);
            manager.sendBroadcast(i);

            try {
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void faster() {
        if (sleepTime > 10) {
            sleepTime -= 10;
        }

        Debug.print("LottoSearch", "faster", "sleepTime: " + sleepTime, 2);
    }

    public void slower() {
        if (sleepTime <= 990) {
            sleepTime += 10;
        }

        Debug.print("LottoSearch", "slower", "sleepTime: " + sleepTime, 2);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!serviceOn) {
            Debug.print("LottoSearch", "onStartCommand", "service starting", 2);
            chosenNumbers = (ArrayList<Integer>) intent.getExtras().getSerializable("chosenNumbers");
            skillLevel = intent.getExtras().getInt("skillLevel");
            gameOn = true;
            Thread t = new Thread(this);
            t.start();
        } else {
            Debug.print("LottoSearch", "onStartCommand", "service already on", 2);
        }

        return START_STICKY;
    }

    public void stop() {
        Debug.print("LottoSearch", "stop", "stopped", 2);
        serviceOn = false;
        gameOn = false;
        weeks = 0;
    }

    public void onCreate() {
        myBinder = new LocalBinder(this);

        serviceOn = false;
        gameOn = false;
        weeks = 0;
        sleepTime = 250;
    }

    public void onDestroy() {
        serviceOn = false;
        gameOn = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }
}
