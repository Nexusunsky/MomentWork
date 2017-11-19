package com.lh.nexusunsky.baselib.network.response;

import okhttp3.Response;

/**
 * @author Nexusunsky
 */
public interface IResponse {

    void onSuccess(Response response);

    void onFailure(int statusCode, String error_msg);

    void onProgress(long currentBytes, long totalBytes);
}
