package qx.app.study;

import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @ProjectName: Study
 * @Package: qx.app.study
 * @ClassName: MyAdapter
 * @Description: java类作用描述
 * @Author: 张耀
 * @CreateDate: 2021/9/10 10:05
 * @UpdateUser: 更新者：
 * @UpdateDate: 2021/9/10 10:05
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class MyAdapter extends BaseQuickAdapter <TestBean, BaseViewHolder> {

    private OnBtnClickListener onBtnClickListener;

    public MyAdapter(@Nullable List <TestBean> data) {
        super(R.layout.item_xxx, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, TestBean item) {
        TextView tvName = helper.getView(R.id.tv_name);
        TextView tvAge = helper.getView(R.id.tv_age);
        Button btn = helper.getView(R.id.btn_1);

        if (helper.getAdapterPosition() == 0) {
            tvName.setText("姓名");
            tvAge.setText("年龄");
        } else {
            tvName.setText(item.getName());
            tvAge.setText("" + item.getAge());
        }
        btn.setOnClickListener(view -> {
            onBtnClickListener.onBtnClick(helper.getAdapterPosition());
        });
    }


    public interface OnBtnClickListener {
        void onBtnClick(int position);

    }

    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
        this.onBtnClickListener = onBtnClickListener;
    }


}
