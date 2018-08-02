package com.aries.graphics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by JiangYiDong on 2018/18/2.
 * 用于测试泄漏。。。
 */
public class NormalThreadLeakActivity extends AppCompatActivity {

//    static Demo sInstance2Dome = null;
//    public final LeakBean mLeakBean = new LeakBean();
//    public static LeakBean mLeakBean = new LeakBean();

    public TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak);
        name = findViewById(R.id.activity_leak_tv);
//        if (sInstance2Dome == null)
//        {
//            sInstance2Dome= new Demo();
//        }
        MyThread myThread = new MyThread();
        myThread.start();
        name.setText(myThread.getName());
//        mLeakBean.setmContex(this);
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                dosomthing();
//            }
//        }.start();
    }

    class Demo
    {
        void doSomething()
        {
            System.out.print("dosth.");
        }
    }

    /**
     * Thread Leak
     */
    private class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            dosomthing();
        }
    }
    private void dosomthing(){
        try {
            Log.e("MyThread", "dosomthing start");
            Thread.sleep(10000);
            for (int i = 0; i < 10000000; i++){
                // TODO: 2018/3/23 耗时操作 
            }
            Log.e("MyThread", "dosomthing end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
