// (c)2016 Flipboard Inc, All Rights Reserved.

package com.collectioncar.network.api;


import com.collectioncar.model.CarListModel;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ZhuangbiApi {
    @GET("search")
    Observable<List<CarListModel>> search(@Query("q") String query);
}
