package com.collectioncar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.collectioncar.baseapp.BaseAct;
import com.collectioncar.widget.StatusBarCompat;

public class MainActivity extends BaseAct {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //SetTranslanteBar();
    }
}
