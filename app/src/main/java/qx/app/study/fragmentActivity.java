package qx.app.study;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import liys.click.AClick;

public class fragmentActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private Button button1;
    private Fragment firstFragment;
    private Fragment secondFragment;
    private Fragment nowFragment;
    private ScreenStateReciver screenStateReciver;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        button = findViewById(R.id.button_first);
        button1 = findViewById(R.id.button_second);
        button.setOnClickListener(this);
        button1.setOnClickListener(this);

        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_fragment, firstFragment)
                .add(R.id.container_fragment, secondFragment)
                .commitAllowingStateLoss();
        nowFragment = firstFragment;
        switchFragment(firstFragment);
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .hide(firstFragment)
                .hide(secondFragment);
        nowFragment = fragment; //替换当前fragment
        /**
         * 隐藏国内手持清库 2021 .3.23
         */
        transaction.show(nowFragment).commit();

    }


    //        @OnClick(R.id.button_first, R.id.button_second)
    @Override
    public void onClick(View v) {
        view = v;
        Log.v("eeeeeeee", v.getId() + "eeeeeeee");
        switch (v.getId()) {
            case R.id.button_first:
                Log.v("qqqqqqq", v.getId() + "qqqqqqq");
//                screenStateReciver.onReceive();
                switchFragment(firstFragment);
                break;
            case R.id.button_second:
                Log.v("wwwwww", v.getId() + "wwwwwww");
                switchFragment(secondFragment);
                break;

        }
    }



    /**
     * 屏幕监听
     */
    class ScreenStateReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                Log.e("屏幕监听:", "屏幕亮了");
            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                Log.e("屏幕监听:", "屏幕黑了");
            } else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                Log.e("屏幕监听:", "屏幕解锁了");
//                checkPush(pushList);//解锁后去读取消息队列的推送列表
            }
        }
    }

}
