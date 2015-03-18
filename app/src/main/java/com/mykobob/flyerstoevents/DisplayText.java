package com.mykobob.flyerstoevents;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayText extends ActionBarActivity {

    private Event[] operate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_text);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        operate = new Event[bundle.size()];
        int index = 0;

        for(String str : bundle.keySet()) {
            Event e = (Event) bundle.get(str);
            operate[index++] = e;
        }

        TextView view = (TextView) findViewById(R.id.display_all_info);
        view.setText("Start: " + operate[0].toString() + " :End");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display__text, menu);
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
}
