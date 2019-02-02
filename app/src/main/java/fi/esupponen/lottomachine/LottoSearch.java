package fi.esupponen.lottomachine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class LottoSearch extends Service implements Runnable {
    boolean serviceOn;
    boolean gameOn;
    int weeks;
    ArrayList<Integer> chosenNumbers;

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
