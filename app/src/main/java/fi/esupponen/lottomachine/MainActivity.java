package fi.esupponen.lottomachine;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    class IterationListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Debug.print("IterationListener", "onReceive", "broadcast reveiced", 2);

            for (Button b : numberButtons) {
                if (chosenNumbers.contains(new Integer(b.getText().toString()))) {
                    b.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                } else {
                    b.getBackground().clearColorFilter();
                }
            }

            ArrayList<Integer> lottoNumbers = intent.getExtras().getIntegerArrayList("lottoNumbers");
            int weeks = intent.getExtras().getInt("passedWeeks");

            TextView tv = (TextView) findViewById(R.id.text);
            tv.setText(weeks + " weeks passed.");

            for (Integer i : lottoNumbers) {
                numberButtons.get(i.intValue() - 1).getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
            }
        }
    }

    // Game related variables
    ArrayList<Integer> chosenNumbers;
    ArrayList<Button> numberButtons;
    boolean serviceOn;
    IterationListener iListener;
    int skillLevel;

    // Binding related variables
    ServiceConnection connectionToService;
    LottoSearch lottoSearch;
    boolean isBounded = false;


    public void faster() {
        if (isBounded) {
            lottoSearch.faster();
        }
    }

    public void slower() {
        if (isBounded) {
            lottoSearch.slower();
        }
    }

    public void numberClicked(View v) {
        if (!serviceOn) {
            Integer chosen = new Integer(((Button)v).getText().toString());

            if (chosenNumbers.contains(chosen)) {
                chosenNumbers.remove(chosen);
                ((Button)v).getBackground().clearColorFilter();
            } else if (chosenNumbers.size() < 7) {
                chosenNumbers.add(chosen);
                ((Button)v).getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            }

            if (chosenNumbers.size() == 7) {
                ((Button)findViewById(R.id.lucky)).setEnabled(true);
            } else {
                ((Button)findViewById(R.id.lucky)).setEnabled(false);
            }
        }
    }

    public void feelLucky(View v) {
        if (isBounded) {
            if (serviceOn) {
                serviceOn = false;
                lottoSearch.stop();
                ((Button)findViewById(R.id.lucky)).setText("I feel lucky");
            } else {
                Debug.print("MainActivity", "feelLucky", "pushed", 1);

                serviceOn = true;

                Intent intent = new Intent(this, LottoSearch.class);
                intent.putExtra("chosenNumbers", chosenNumbers);
                intent.putExtra("skillLevel", skillLevel);
                startService(intent);

                ((Button)findViewById(R.id.lucky)).setText("I give up");
            }
        }
    }

    public void createNumberButtonsList() {
        numberButtons = new ArrayList<Button>();

        numberButtons.add((Button) findViewById(R.id.number1));
        numberButtons.add((Button) findViewById(R.id.number2));
        numberButtons.add((Button) findViewById(R.id.number3));
        numberButtons.add((Button) findViewById(R.id.number4));
        numberButtons.add((Button) findViewById(R.id.number5));
        numberButtons.add((Button) findViewById(R.id.number6));
        numberButtons.add((Button) findViewById(R.id.number7));
        numberButtons.add((Button) findViewById(R.id.number8));
        numberButtons.add((Button) findViewById(R.id.number9));
        numberButtons.add((Button) findViewById(R.id.number10));
        numberButtons.add((Button) findViewById(R.id.number11));
        numberButtons.add((Button) findViewById(R.id.number12));
        numberButtons.add((Button) findViewById(R.id.number13));
        numberButtons.add((Button) findViewById(R.id.number14));
        numberButtons.add((Button) findViewById(R.id.number15));
        numberButtons.add((Button) findViewById(R.id.number16));
        numberButtons.add((Button) findViewById(R.id.number17));
        numberButtons.add((Button) findViewById(R.id.number18));
        numberButtons.add((Button) findViewById(R.id.number19));
        numberButtons.add((Button) findViewById(R.id.number20));
        numberButtons.add((Button) findViewById(R.id.number21));
        numberButtons.add((Button) findViewById(R.id.number22));
        numberButtons.add((Button) findViewById(R.id.number23));
        numberButtons.add((Button) findViewById(R.id.number24));
        numberButtons.add((Button) findViewById(R.id.number25));
        numberButtons.add((Button) findViewById(R.id.number26));
        numberButtons.add((Button) findViewById(R.id.number27));
        numberButtons.add((Button) findViewById(R.id.number28));
        numberButtons.add((Button) findViewById(R.id.number29));
        numberButtons.add((Button) findViewById(R.id.number30));
        numberButtons.add((Button) findViewById(R.id.number31));
        numberButtons.add((Button) findViewById(R.id.number32));
        numberButtons.add((Button) findViewById(R.id.number33));
        numberButtons.add((Button) findViewById(R.id.number34));
        numberButtons.add((Button) findViewById(R.id.number35));
        numberButtons.add((Button) findViewById(R.id.number36));
        numberButtons.add((Button) findViewById(R.id.number37));
        numberButtons.add((Button) findViewById(R.id.number38));
        numberButtons.add((Button) findViewById(R.id.number39));
        numberButtons.add((Button) findViewById(R.id.number40));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        chosenNumbers = new ArrayList<>();
        Debug.loadDebug(this);
        serviceOn = false;
        skillLevel = 7;
        iListener = new IterationListener();
        connectionToService = new MyServiceConnection();
        LocalBroadcastManager.getInstance(this).registerReceiver(iListener, new IntentFilter("passedIteration"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNumberButtonsList();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, LottoSearch.class);
        bindService(intent, connectionToService, Context.BIND_AUTO_CREATE);
    }

     @Override
     protected void onStop() {
        super.onStop();

        if (isBounded) {
            unbindService(connectionToService);
            isBounded = false;
        }
     }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            ArrayList<Integer> numbers = (ArrayList<Integer>) savedInstanceState.get("chosenNumbers");

            if (numbers != null) {
                chosenNumbers = numbers;
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putIntegerArrayList("numbers", chosenNumbers);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case (R.id.clickFaster):
                faster();
                return true;
        }
        switch (item.getItemId()) {
            case (R.id.clickSlower):
                slower();
                return true;
        }
        switch (item.getItemId()) {
            case (R.id.difficulty5):
                if (!serviceOn) {
                    item.setChecked(true);
                    skillLevel = 5;
                }
                return true;
        }
        switch (item.getItemId()) {
            case (R.id.difficulty6):
                if (!serviceOn) {
                    item.setChecked(true);
                    skillLevel = 6;
                }
                return true;
        }
        switch (item.getItemId()) {
            case (R.id.difficulty7):
                if (!serviceOn) {
                    item.setChecked(true);
                    skillLevel = 7;
                }
                return true;
        }
        return false;
    }

    class MyServiceConnection implements android.content.ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            lottoSearch = binder.getService();
            isBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBounded = false;
        }
    }
}
