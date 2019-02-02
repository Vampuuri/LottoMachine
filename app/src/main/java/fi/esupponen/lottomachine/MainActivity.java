package fi.esupponen.lottomachine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Integer> chosenNumbers;
    boolean serviceOn;

    public void faster(View v) {
        Toast.makeText(this, "+", Toast.LENGTH_SHORT).show();
    }

    public void slower(View v) {
        Toast.makeText(this, "-", Toast.LENGTH_SHORT).show();
    }

    public void numberClicked(View v) {
        if (!serviceOn) {
            Integer chosen = new Integer(((Button)v).getText().toString());

            if (chosenNumbers.contains(chosen)) {
                chosenNumbers.remove(chosen);
            } else if (chosenNumbers.size() < 7) {
                chosenNumbers.add(chosen);
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                Toast.makeText(this, "+", Toast.LENGTH_LONG).show();
                return true;
        }
        switch (item.getItemId()) {
            case (R.id.clickSlower):
                Toast.makeText(this, "-", Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }
}
