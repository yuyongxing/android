package com.collectioncar.framgent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.collectioncar.R;
import com.collectioncar.adapter.CheShangQuanAdapter;
import com.collectioncar.baseapp.BaseFragment;
import com.collectioncar.model.CarListModel;
import com.collectioncar.util.JsonUtil;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jaydenxiao.common.commonwidget.LoadingTip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/20.
 */

public class NewsMainFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    Context ctx;
    @Bind(R.id.irc)
    IRecyclerView irc;

    private int currpage=1;

    private RequestQueue mRequestQueue;
    private CheShangQuanAdapter cheshangquanadapter;
    private List<CarListModel> datas = new ArrayList<>();
    private int cartotal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.framents_news, null);

        ctx = getActivity();
        mRequestQueue = Volley.newRequestQueue(ctx);

        ButterKnife.bind(this, view);
       /* subscription = Network.getZhuangbiApi(SystemConstant.HTTP_SERVICE_URL)
                .search("装逼")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);*/
        irc.setLayoutManager(new LinearLayoutManager(getContext()));
        cheshangquanadapter = new CheShangQuanAdapter(getContext(), datas);
        cheshangquanadapter.setAct(getActivity());
        cheshangquanadapter.openLoadAnimation(new ScaleInAnimation());

        irc.setAdapter(cheshangquanadapter);
        irc.setOnRefreshListener(this);
        irc.setOnLoadMoreListener(this);
        cheshangquanadapter.getPageBean().setRefresh(false);
            getdate();


        return view;
    }
/*    Observer<List<CarListModel>> observer = new Observer<List<CarListModel>>() {
        @Override
        public void onCompleted() {
            Log.e("observer","onCompleted");
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<CarListModel> images) {
            Log.e("observer","onNext"+images.size());

        }
    };*/
        public boolean isExisted(CarListModel car, List<CarListModel> carListtotal) {
             for (CarListModel model : carListtotal) {
        if (model.getId().equals(car.getId()))
            return true;
    }
    return false;
    }
     void getdate(){
        //检查网络是否可用
         if (isNetworkAvailable() == false) {
             setNetwork();
             return;
         }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://www.2schome.net/mobile/carman/storecars.json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject jsonobject = JsonUtil.jsonToGoogleJsonObject(response);
                        String carTotal=jsonobject.get("carTotal").toString();
                        cartotal=Integer.parseInt(carTotal);
                        String carlist = jsonobject.get("carList").toString();

                        List<CarListModel> carList = (List<CarListModel>) JsonUtil.jsonToList(
                                carlist, new TypeToken<List<CarListModel>>() {
                                }.getType());

                        if (carList != null) {

                            if (cheshangquanadapter.getPageBean().isRefresh()) {

                                irc.setRefreshing(false);
                                 cheshangquanadapter.replaceAll(carList);
                              /*  List<CarListModel> carListtotal=cheshangquanadapter.getAll();
                                Log.e("isRefresh","isRefresh"+carListtotal.size());
                                    for(int j=0;j<carList.size();j++){
                                        CarListModel car=carList.get(j);
                                        if (isExisted(car,carListtotal) == false) {
                                            cheshangquanadapter.addAt(0,car);
                                        }
                                 }
                                cheshangquanadapter.notifyDataSetChanged();*/
                            } else {
                                if (carList.size() > 0) {
                                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                                    cheshangquanadapter.addAll(carList);
                                } else {
                                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                                }
                            }
                        }else{
                            irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> params = new HashMap<String, String>();
                params.put("loginName", "18958106583");
                params.put("token","afd0a7b4b28bd05cf74444152203ca23");
                params.put("order", "1");
                params.put("league", "1");
                params.put("currPage",currpage+"" );
                return params;
            }
        };


        mRequestQueue.add(stringRequest);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.irc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.irc:
                break;

        }
    }

    @Override
    public void onLoadMore(View loadMoreView) {

        cheshangquanadapter.getPageBean().setRefresh(false);
        //发起请求
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        currpage += 1;
        getdate();
    }

    @Override
    public void onRefresh() {
        cheshangquanadapter.getPageBean().setRefresh(true);
        currpage = 1;
        //发起请求
        irc.setRefreshing(true);
        getdate();
    }
}
