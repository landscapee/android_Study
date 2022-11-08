package qx.app.study;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements aaaaa {

    @BindView(R.id.tv_hello)
    TextView tvHello;

    @BindView(R.id.rv_data)
    RecyclerView rcData;


    private ImageView ivH;
    private Button button;

    private Context mContext;

    private TestBean testBean;


    private List <TestBean> mList = new ArrayList <>();


    /**
     * 创建的调用 该生命周期
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ButterKnife.bind(this);
        initView();
        initData();
    }

    /**
     * 一般可以 数据获取
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 可以与用户交互
     */
    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    /**
     * 界面 暂停
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 停止
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        testBean = null;
        super.onDestroy();
    }

    private void initData() {


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                testBean = new TestBean("lty", 8);

                Intent intent = new Intent();
                intent.setClass(mContext, zhangyyshiSBActivity.class);
//                intent.putExtra("a",8);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", testBean);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    private void initView() {
        for (int i = 0; i < 20; i++) {
            TestBean testBean = new TestBean("zyy", i);
            mList.add(testBean);
        }
        MyAdapter myAdapter = new MyAdapter(mList);
        rcData.setLayoutManager(new LinearLayoutManager(this));
//        rcData.setLayoutManager(new GridLayoutManager(this, 2));
        rcData.setAdapter(myAdapter);

//        myAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                Toast.makeText(MainActivity.this, "" + mList.get(position).getName(), Toast.LENGTH_SHORT).show();
//            }
//        });
        myAdapter.setOnBtnClickListener(new MyAdapter.OnBtnClickListener() {
            @Override
            public void onBtnClick(int position) {
                Toast.makeText(MainActivity.this, "" + mList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }

        });


        ivH = findViewById(R.id.iv_hhhh);
        button = findViewById(R.id.button);
        tvHello.setText("婚纱店看");


    }

    @Override
    public void fun() {
        Toast.makeText(mContext, "sdadasd", Toast.LENGTH_SHORT).show();
    }
}