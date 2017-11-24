package com.lh.nexusunsky.impl;

import com.google.gson.Gson;
import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.mvp.BasePresenter;
import com.lh.nexusunsky.baselib.mvp.contract.IModel;
import com.lh.nexusunsky.baselib.mvp.contract.IView;
import com.lh.nexusunsky.baselib.network.EasyHttp;
import com.lh.nexusunsky.baselib.network.response.JsonResponse;
import com.lh.nexusunsky.baselib.utils.GsonUtil;
import com.lh.nexusunsky.baselib.utils.MessageHelper;
import com.lh.nexusunsky.cache.MomentsInfoCache;
import com.lh.nexusunsky.domain.MineInfo;
import com.lh.nexusunsky.domain.MomentsInfo;
import com.lh.nexusunsky.item.moments.MomentItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: Nexusunsky
 */
public class MomentPresenter extends BasePresenter<MomentPresenter.MomentView, MomentPresenter.MomentInfoModel> {

    private static final String TAG = MomentPresenter.class.getSimpleName();
    private static final int EACH_PAGE = 5;
    private static final int DEFAULT_INDEX = 1;
    private static final Void VOID = null;

    private List<MomentsInfo> mMemoryCache;
    private List<MomentsInfo> mTemp;
    private int currentPage = DEFAULT_INDEX;
    private int mTotalCount;
    private int mTotalPage;

    public interface MomentView extends IView {
        void refreshMineInfo(MineInfo hostInfo);

        void loadMoreMoments(List<MomentsInfo> infos);

        void refreshMoments(List<MomentsInfo> infos);

        void loadMomentsCache(List<MomentsInfo> infos);

        void loadDataComplete();

        void loadDataFinished();
    }

    public MomentPresenter(MomentView view) {
        super(view);
    }

    @Override
    protected void bindDataSource() {
        Logger.d(TAG, "bindDataSource");
        mMemoryCache = new ArrayList<>();
        mTemp = new ArrayList<>(EACH_PAGE);
        dataModel = new MomentInfoModel();
        dataModel.loadDataFromLocal(VOID);
    }

    @Override
    public void presenting() {
        final MomentView view = checkThenGet();
        if (view == null) {
            return;
        }
        Logger.d(TAG, "MomentView.presenting");
        view.showLoading();
        currentPage = DEFAULT_INDEX;
        dataModel.fetchDataFromRemote();
    }

    public void loadMore() {
        Logger.d(TAG, "MomentView.loadMore");
        final MomentView view = checkThenGet();
        if (view == null) {
            Logger.d(TAG, "loadMore VIEW is Empty");
            return;
        }
        if (mMemoryCache == null || mMemoryCache.isEmpty()) {
            presenting();
            return;
        }
        synchronized (MomentPresenter.class) {
            if (mTotalPage > currentPage) {
                int destIndex;
                if (mTotalPage - currentPage > 1) {
                    destIndex = (currentPage + 1) * EACH_PAGE;
                } else {
                    destIndex = mTotalCount;
                }
                Logger.d(TAG, " currentPage :" + currentPage
                        + "  mTotalCount :" + mTotalCount + " destIndex : " + destIndex);
                handleMomentsInfo(currentPage * EACH_PAGE, destIndex);
                view.loadMoreMoments(mTemp);
                currentPage++;
            } else {
                view.loadDataFinished();
            }
        }
    }

    public class MomentInfoModel implements IModel<Void, Void> {

        private static final String HTTP_TWEETS = "http://thoughtworks-ios.herokuapp.com/user/jsmith/tweets";
        private static final String HTTP_USER_JSMITH = "http://thoughtworks-ios.herokuapp.com/user/jsmith";
        private MomentsInfoCache mCache;

        MomentInfoModel() {
            mCache = new MomentsInfoCache();
        }

        @Override
        public void fetchDataFromRemote() {
            final MomentView view = checkThenGet();
            if (view == null) {
                return;
            }
            fetchMineData(view);
            fetchTweetsData(view);
        }

        @Override
        public void loadDataFromLocal(Void v) {
            final MomentView view = checkThenGet();
            if (view == null) {
                return;
            }
            loadMomentCache(view);
            loadMineCache(view);
        }

        private void loadMineCache(MomentView view) {
            final MineInfo info = mCache.loadMineInfoCache();
            if (info != null) {
                view.refreshMineInfo(info);
            } else {
                MessageHelper.showMessage("No MineInfo Cache Found !");
            }
        }

        private void loadMomentCache(MomentView view) {
            mMemoryCache = mCache.loadMomentsInfoCache();
            if (mMemoryCache != null && !mMemoryCache.isEmpty()) {
                refreshMomentInfo();
                view.loadMomentsCache(mTemp);
            } else {
                MessageHelper.showMessage("No MomentInfo Cache Found !");
            }
        }

        @Override
        public Void onFetchDataSuccess(Void v) {
            return null;
        }

        @Override
        public void onFetchDataFailed(String o) {
            final MomentView view = checkThenGet();
            if (view == null) {
                return;
            }
            view.loadDataComplete();
            Logger.d("onFailure", o);
            MessageHelper.showMessage(o);
        }

        @Override
        public Void onLoadDataFinished(Void v) {
            return null;
        }

        private void fetchTweetsData(final MomentView view) {
            EasyHttp.getHttp().get().setUrl(HTTP_TWEETS).tag(view)
                    .enqueue(new JsonResponse() {
                        @Override
                        public void onSuccess(int statusCode, JSONArray response) {
                            super.onSuccess(statusCode, response);
                            final String json = response.toString();
                            Logger.d("onSuccess", json);
                            cacheMoments(GsonUtil.INSTANCE.toList(json, MomentsInfo.class));
                            refreshMomentInfo();
                            view.refreshMoments(mTemp);
                        }

                        @Override
                        public void onFailure(int statusCode, String errorMsg) {
                            onFetchDataFailed(errorMsg);
                        }
                    });
        }

        private void fetchMineData(final MomentView view) {
            EasyHttp.getHttp().get().setUrl(HTTP_USER_JSMITH).tag(view)
                    .enqueue(new JsonResponse() {
                        @Override
                        public void onSuccess(int statusCode, JSONObject response) {
                            super.onSuccess(statusCode, response);
                            final String jsonPair = response.toString();
                            Logger.d("onSuccess", jsonPair);
                            mCache.saveMineInfo(jsonPair);
                            view.refreshMineInfo(new MineInfo(jsonPair));
                        }

                        @Override
                        public void onFailure(int statusCode, String errorMsg) {
                            onFetchDataFailed(errorMsg);
                        }
                    });
        }

        private void refreshMomentInfo() {
            calculateTotal();
            handleMomentsInfo(0, currentPage * EACH_PAGE);
        }

        private void cacheMoments(List<MomentsInfo> beanOnes) {
            if (mMemoryCache == null) {
                mMemoryCache = new ArrayList<>();
            }
            if (!mMemoryCache.isEmpty()) {
                mMemoryCache.clear();
            }
            for (MomentsInfo info : beanOnes) {
                if (MomentItem.Type.EMPTY_CONTENT == info.getMomentType()) {
                    continue;
                }
                mMemoryCache.add(info);
            }
            mCache.saveMomentInfo(new Gson().toJson(mMemoryCache));
        }

        private void calculateTotal() {
            currentPage = DEFAULT_INDEX;
            mTotalCount = mMemoryCache.size();
            mTotalPage = mTotalCount / EACH_PAGE;
            if (mTotalPage > 0 && mTotalCount % EACH_PAGE != 0) {
                mTotalPage++;
            }
        }
    }

    private void handleMomentsInfo(int startIndex, int destIndex) {
        if (!mTemp.isEmpty()) {
            mTemp.clear();
        }
        for (int i = startIndex; i < destIndex; i++) {
            mTemp.add(mMemoryCache.get(i));
        }
    }
}
