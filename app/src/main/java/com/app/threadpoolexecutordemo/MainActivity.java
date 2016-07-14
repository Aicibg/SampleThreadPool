package com.app.threadpoolexecutordemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Button btStart,btStop;
    private TextView tvTime;
    private boolean isPause=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btStart= (Button) findViewById(R.id.btn_start);
        btStop= (Button) findViewById(R.id.btn_stop);
        tvTime= (TextView) findViewById(R.id.tv_time);

        final PausableThreadPoolExecutor pausThread=new PausableThreadPoolExecutor(1,1,0L, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=1;i<=100;i++){
                    final int priority=i;
                    pausThread.execute(new PriorityRunnable(priority) {
                        @Override
                        public void doSth() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvTime.setText(String.valueOf(priority));
                                }
                            });
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }
                //isPause=true;
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(isPause){
                     pausThread.resume();
                     isPause=false;
                 }else{
                     pausThread.paush();
                     isPause=true;
                 }
            }
        });

    }


}
