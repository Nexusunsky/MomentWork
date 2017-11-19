package com.lh.nexusunsky.baselib.mvp.contract;

/**
 * @author Nexusunsky
 */
public interface IModel<I, O> {
    /**
     * 网络拉去数据
     */
    void fetchDataFromRemote();

    /**
     * 本地数据库缓存
     *
     * @param taskBeen
     */
    void loadDataFromLocal(I i);

    /**
     * 网络数据请求成功
     */
    O onFetchDataSuccess(I i);

    /**
     * 网络数据请求失败
     */
    void onFetchDataFailed(String o);

    /**
     * 本地缓存加载
     */
    O onLoadDataFinished(I i);
}
