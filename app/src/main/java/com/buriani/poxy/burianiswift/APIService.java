package com.buriani.poxy.burianiswift;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Poxy on 5/16/2017.
 */

public interface APIService {

    @FormUrlEncoded
    @POST("getResp/")
    Call<MSG> userData(@Field("name") String name,@Field("phone") String phone,@Field("client") String client,@Field("obid") String oid);



    @FormUrlEncoded
    @POST("getContacts/")
    Call<MSG> cleanData(@Field("email") String email,@Field("obid") String obid);

}
