package com.akl.livetelecastgiftdemo;

import retrofit2.Response;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author:gtf
 * @date: 2016/9/23 16:09
 * @company:yj
 * @description:
 * @version:1.0
 */
public interface RetrofitService {
    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: RetrofitBean-Sample-App",
            "If-None-Match:ljd"
    })
    @POST("room/giftList")//获取直播间直播间礼物列表
    rx.Observable <ServerGiftBean> getGift(@Query("version") String version,@Query("device") String device);
}
