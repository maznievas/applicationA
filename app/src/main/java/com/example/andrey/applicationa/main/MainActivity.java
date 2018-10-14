package com.example.andrey.applicationa.main;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.andrey.applicationa.R;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "mLog";
    private MenuItem sortByDate, sortByStatus;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        mainFragment = (MainFragment) fm.findFragmentById(R.id.fragmentContainer);

        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, mainFragment)
                    .commit();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        sortByDate = menu.findItem(R.id.sortByDate);
        sortByStatus = menu.findItem(R.id.sortByStatus);
        setMenuVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortByDate:
                Log.d(TAG, "sort by date");
                mainFragment.sortByDate();
                return true;
            case R.id.sortByStatus:
                Log.d(TAG, "sort by status");
                mainFragment.sortByStatus();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setMenuVisible(boolean flag) {
        sortByDate.setVisible(flag);
        sortByStatus.setVisible(flag);
    }
}
