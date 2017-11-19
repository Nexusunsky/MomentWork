package com.lh.nexusunsky.baselib.network.builder;


import com.lh.nexusunsky.baselib.network.EasyHttp;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Nexusunsky
 */
public abstract class RequestWithParamBuilder<T extends RequestWithParamBuilder> extends RequestBuilder<T> {

    protected Map<String, String> mParams;

    public RequestWithParamBuilder(EasyHttp easyHttp) {
        super(easyHttp);
    }

    /**
     * 参数对
     */
    public T params(Map<String, String> params) {
        this.mParams = params;
        return (T) this;
    }

    /**
     * 单个参数
     */
    public T addParam(String key, String val) {
        if (this.mParams == null) {
            mParams = new LinkedHashMap<>();
        }
        mParams.put(key, val);
        return (T) this;
    }
}
