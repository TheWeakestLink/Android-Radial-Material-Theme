package com.example.mobile1.mypesoesense;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    // floating action button
    FloatingActionButton btnFab;
    // pop up dialog
    public Dialog dialog;
    // database
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;

    // var for spinner
    private String[] sendTo;
    private String[] currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //setContentView(R.layout.login);
        //setContentView(R.layout.sign_up);
        setContentView(R.layout.activity_sign_up_more2);
        //setContentView(R.layout.pay_bills);
        //initSpinners();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* SPINNER SAMPLE :
     * Just comment out the function call if changing layouts
     */

    public void initSpinners() {
        Spinner spinnerSendTo = (Spinner) findViewById(R.id.spinner_send_to);
        Spinner spinnerCurrency = (Spinner) findViewById(R.id.spinner_currency);
        this.sendTo = new String[] {"Meralco", "Manila Water"};
        this.currency = new String[] {"PHP", "USD"};

        ArrayAdapter<String> adapterSendTo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sendTo);
        ArrayAdapter<String> adapterCurrency = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currency);
        spinnerSendTo.setAdapter(adapterSendTo);
        spinnerCurrency.setAdapter(adapterCurrency);
    }

}
