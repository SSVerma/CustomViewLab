package com.ssverma.customviewlab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpStatusView();
    }

    private void setUpStatusView() {
        StatusView statusView = findViewById(R.id.status_view);

        int totalStatus = 10;
        int doneStatus = 4;

        boolean[] status = new boolean[totalStatus];

        for (int i = 0; i < doneStatus; i++) {
            status[i] = true;
        }

        statusView.setStatusDone(status);
    }
}
