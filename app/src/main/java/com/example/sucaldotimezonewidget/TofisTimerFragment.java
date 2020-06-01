package com.example.sucaldotimezonewidget;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class TofisTimerFragment extends Fragment implements View.OnClickListener {

    private int seconds = 0;
    private boolean running;

    private TextView timeView;
    private Button startBtn, stopBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_tofis_timer, container, false);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        timeView = rootView.findViewById(R.id.time_view);
        startBtn = rootView.findViewById(R.id.start_button);
        stopBtn = rootView.findViewById(R.id.stop_button);

        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);

        runTimer();

        return rootView;
    }

    private void onClickStart() {
        running = true;
    }

    private void onClickStop() {
        running = false;
        seconds = 0;
    }

    private void runTimer() {

        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time
                        = String
                        .format(Locale.getDefault(), "%02d:%02d", minutes, secs);

                timeView.setText(time);

                if (running & secs % 30 == 0) {
                    getActivity().startService(new Intent(getContext(), SoundService.class));
                }

                if (running) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_button:
                onClickStart();
                break;
            case R.id.stop_button:
                onClickStop();
                break;
        }
    }
}
