package qx.app.study;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class zhangyyshiSBActivity extends AppCompatActivity {
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhangyyshi_s_b);

        button = findViewById(R.id.zyysb);
        textView = findViewById(R.id.zyy);
        button.setOnClickListener(view -> {
            textView.setText("张YY是什么？");
        });
    }
}