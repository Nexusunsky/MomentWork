package com.lh.nexusunsky.activity;


import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.network.EasyHttp;
import com.lh.nexusunsky.baselib.network.response.JsonResponse;

import org.json.JSONArray;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void requestTest() throws Exception {
        doGet();
    }

    /**
     * GET请求 + Raw String返回
     */
    private void doGet() {
        String url = "http://thoughtworks-ios.herokuapp.com/user/jsmith/tweets";
        EasyHttp.getHttp().get()
                .setUrl(url)
                .addParam("name", "tsy")
                .addParam("id", "5")
                .tag(this)
                .enqueue(new JsonResponse() {
                    @Override
                    public void onSuccess(int statusCode, JSONArray response) {
                        super.onSuccess(statusCode, response);
                        Logger.d("onSuccess", response.toString());
                        System.out.println(response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, String errorMsg) {
                        System.out.println(errorMsg.toString());
                    }
                });
    }
}