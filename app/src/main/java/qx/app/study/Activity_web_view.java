package qx.app.study;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.Date;

public class Activity_web_view extends AppCompatActivity {




    private static final String TAG = "WebViewActivity";
    private WebView webView;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.wv);
        //加载本地中的html文件
        //        webView.loadUrl("file:///android_asset/web.html");

        //设置支持js否则有些网页无法打开
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        //加载网络url
                webView.loadUrl("http://192.168.0.109:8099/#/webview?t=" +  String.format("%tc",new Date()));
//        webView.loadUrl("http:www.baidu.com");


        System.out.println("http://192.168.0.107:8099/#/webview?t=" +  String.format("%tc",new Date()));
//         长按点击事件
        webView.setOnLongClickListener(v -> {
            final WebView.HitTestResult hitTestResult = webView.getHitTestResult();
            // 如果是图片类型或者是带有图片链接的类型
            if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                    hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {


                // 弹出保存图片的对话框
                new AlertDialog.Builder(Activity_web_view.this)
                        .setItems(new String[]{"保存图片"}, (dialog, which) -> {
                            String pic = hitTestResult.getExtra();//获取图片
                            Log.e(TAG, " 获取到的图片地址为  ：" + pic);
                            MyDownloadManager myDownloadManager = new MyDownloadManager();
                            switch (which) {
                                case 0:
                                    myDownloadManager.addDownloadTask(pic,Activity_web_view.this);
                                    Toast.makeText(context,"图片保存图库成功",Toast.LENGTH_LONG).show();
                                     break;
                            }
                        })
                        .show();
                return true;
            }
            return false;//保持长按可以复制文字
        });

    }

    class MyClient extends WebViewClient {
        //监听到页面发生跳转的情况，默认打开web浏览器
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            //在webView中加载要打开的链接
            view.loadUrl(request.getUrl().toString());
            return true;
        }
        //页面开始加载
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e(TAG, "onPageStarted: ");
        }
        //页面加载完成的回调方法
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e(TAG, "onPageFinished: ");
            //在webView中加载js代码
//            webView.loadUrl("javascript:Alert('hello')");
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        //监听网页进度 newProgress进度值在0-100
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        //设置Activity的标题与 网页的标题一致
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //如果用户按的是返回键 并且WebView页面可以返回
        if (keyCode==event.KEYCODE_BACK&&webView.canGoBack()){
            //让WebView返回
            webView.goBack();
            return true;
        }
        //如果WebView不能返回 则调用默认的onKeyDown方法 退出Activity
        return super.onKeyDown(keyCode, event);
    }

}