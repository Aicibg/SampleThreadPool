package com.app.threadpoolexecutordemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btStart,btStop,btThread1,btThread2,btThread3,btThread4,btRemove;
    private TextView tvTime,tvThread1,tvThread2,tvThread3,tvThread4;
    private boolean isPause=false;
    private Thread thread4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btStart= (Button) findViewById(R.id.btn_start);
        btStop= (Button) findViewById(R.id.btn_stop);
        tvTime= (TextView) findViewById(R.id.tv_time);
        tvThread1= (TextView) findViewById(R.id.tv_thread1);
        tvThread2= (TextView) findViewById(R.id.tv_thread2);
        tvThread3= (TextView) findViewById(R.id.tv_thread3);
        tvThread4= (TextView) findViewById(R.id.tv_thread4);
        btThread1= (Button) findViewById(R.id.btn_thread1);
        btThread2= (Button) findViewById(R.id.btn_thread2);
        btThread3= (Button) findViewById(R.id.btn_thread3);
        btThread4= (Button) findViewById(R.id.btn_thread4);
        btRemove= (Button) findViewById(R.id.btn_remove);

        btThread1.setOnClickListener(this);
        btThread2.setOnClickListener(this);
        btThread3.setOnClickListener(this);
        btThread4.setOnClickListener(this);
        btRemove.setOnClickListener(this);

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

        thread4=new MyThread(tvThread4);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_thread1:
                DefaultThreadPool.execute(new MyThread(tvThread1));
                break;
            case R.id.btn_thread2:
                DefaultThreadPool.execute(new MyThread(tvThread2));
                break;
            case R.id.btn_thread3:
                DefaultThreadPool.execute(new MyThread(tvThread3));
                break;
            case R.id.btn_thread4:
                DefaultThreadPool.execute(thread4);
                break;
            case R.id.btn_remove:
                DefaultThreadPool.shutDownNow();
                break;
        }
    }

    class MyRunnabl implements Runnable{
        private TextView tvText;

        public MyRunnabl(TextView view) {
            this.tvText=view;
        }

        @Override
        public void run() {
            for(int i=0;i<=100;i++){
                final int a=i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvText.setText(a+"");
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyThread extends Thread{
        private TextView tvText;

        public MyThread(TextView view) {
            this.tvText=view;
        }
        @Override
        public void run() {
            for(int i=0;i<=100;i++){
                final int a=i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvText.setText(a+"");
                    }
                });
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
