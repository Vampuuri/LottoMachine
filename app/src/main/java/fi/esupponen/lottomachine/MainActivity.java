package fi.esupponen.lottomachine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
            tv.setText("Chosen numbers:\n" + chosenNumbers + "\n\n" + weeks + " weeks passed.");

            for (Integer i : lottoNumbers) {
                numberButtons.get(i.intValue() - 1).getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
            }
        }
    }

    ArrayList<Integer> chosenNumbers;
    ArrayList<Button> numberButtons;
    boolean serviceOn;
    IterationListener iListener;

    public void faster() {
        Toast.makeText(this, "+", Toast.LENGTH_SHORT).show();
    }

    public void slower() {
        Toast.makeText(this, "-", Toast.LENGTH_SHORT).show();
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

            TextView tv = (TextView) findViewById(R.id.text);
            tv.setText("Chosen numbers:\n" + chosenNumbers);

            if (chosenNumbers.size() == 7) {
                ((Button)findViewById(R.id.lucky)).setEnabled(true);
            } else {
                ((Button)findViewById(R.id.lucky)).setEnabled(false);
            }
        }
    }

    public void feelLucky(View v) {
        if (serviceOn) {
            serviceOn = false;
            Intent intent = new Intent(this, LottoSearch.class);
            stopService(intent);
            ((Button)findViewById(R.id.lucky)).setText("I feel lucky");
        } else {
            Debug.print("MainActivity", "feelLucky", "pushed", 1);

            serviceOn = true;

            Intent intent = new Intent(this, LottoSearch.class);
            intent.putExtra("chosenNumbers", chosenNumbers);
            startService(intent);

            ((Button)findViewById(R.id.lucky)).setText("I give up");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        chosenNumbers = new ArrayList<>();
        Debug.loadDebug(this);
        serviceOn = false;
        iListener = new IterationListener();
        LocalBroadcastManager.getInstance(this).registerReceiver(iListener, new IntentFilter("passedIteration"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNumberButtonsList();
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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            ArrayList<Integer> numbers = (ArrayList<Integer>) savedInstanceState.get("chosenNumbers");

            if (numbers != null) {
                chosenNumbers = numbers;

                TextView tv = (TextView) findViewById(R.id.text);
                tv.setText("Chosen numbers:\n" + chosenNumbers);
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
        return false;
    }
}
