package util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.example.administrator.iteam_fragment.Constant.MEDIA_TYPE_PNG;

/**
 * Created by jarvis on 2017/2/27.
 */

@SuppressWarnings("ALL")
public class HttpUtil {
    private CookieHandler cookieHandler;
    private static OkHttpClient client;
    private HttpUtil() {
        initOkHttpFunction();
    }
    private static class OkHttpSingle{
        private static HttpUtil sCommonOkhttpUtils = new HttpUtil();
    }
    public static HttpUtil getUtilsInstance(){
        return  OkHttpSingle.sCommonOkhttpUtils;
    }
    private void initOkHttpFunction() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        //设置缓存 以及超时间 以及获取OkHttpClient对象
        client = builder.writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public OkHttpClient getInstance() {
        if (client == null) {
            synchronized (HttpUtil.class) {
                if (client == null)

                    client = new OkHttpClient.Builder()
                            .cookieJar(new CookieJar() {
                                private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
                                @Override
                                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                                    cookieStore.put(url, cookies);
                                }

                                @Override
                                public List<Cookie> loadForRequest(HttpUrl url) {
                                    List<Cookie> cookies = cookieStore.get(url);
                                    return cookies != null ? cookies : new ArrayList<Cookie>();
                                }
                            })
                            .build();
            }
        }
        return client;
    }
    private  static String cookieHeader(List<Cookie> cookies) {
        StringBuilder cookieHeader = new StringBuilder();
        for (int i = 0, size = cookies.size(); i < size; i++) {
            if (i > 0) {
                cookieHeader.append("; ");
            }
            Cookie cookie = cookies.get(i);
            cookieHeader.append(cookie.name()).append('=').append(cookie.value());
        }
        return cookieHeader.toString();
    }

    public  static void sendOkHttpRequest(String address, String cookie, Callback callback){
        Request request;
        if(!TextUtils.isEmpty(cookie)) {
            request = new Request.Builder().addHeader("cookie", cookie).url(address).build();
        }
        else {
            request = new Request.Builder().url(address).build();
        }

        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    /**
     * Post请求发送JSON数据
     *
     * @param url
     * @param jsonParams
     * @param callback
     */
    public static void doPost(String url, String cookie, String jsonParams, Callback callback) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , jsonParams);
        Request request;
        if(!TextUtils.isEmpty(cookie)) {
            request = new Request.Builder().addHeader("cookie", cookie).url(url)
                    .post(body)
                    .build();
        }
        else {
            request = new Request.Builder().url(url)
                    .post(body)
                    .build();
        }
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    public static void sendMultipart(String url,String cookie,String jsonParams,ArrayList<File>fileList, Callback callback) throws JSONException {
//        int cacheSize = 10 * 1024 * 1024;
        //设置超时时间及缓存
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .writeTimeout(20, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS)
//                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));


//        OkHttpClient mOkHttpClient=builder.build();

        MultipartBody.Builder mbody=new MultipartBody.Builder().setType(MultipartBody.FORM);

//        List<File> fileList=new ArrayList<File>();
//        File img1=new File("/sdcard/wangshu.jpg");
//        fileList.add(img1);
//        File img2=new File("/sdcard/123.jpg");
//        fileList.add(img2);
//        int i=0;
//        mbody.addFormDataPart("jsonparam","json",RequestBody.create(MediaType.parse("application/json; charset=utf-8")
//                , jsonParams));
//        mbody.addPart( RequestBody.create(MediaType.parse("application/json; charset=utf-8")
//                , jsonParams));
        if(!jsonParams.equals("")) {
            JSONObject jsonObject = new JSONObject(jsonParams);
            Iterator<String> sIterator = jsonObject.keys();
            while (sIterator.hasNext()) {
                // 获得key
                String key = sIterator.next();
                // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
                String value = jsonObject.getString(key);
                mbody.addFormDataPart(key, value);
            }
        }
        for(File file:fileList){
            if(file.exists()){
                Log.i("imageName:",file.getName());//经过测试，此处的名称不能相同，如果相同，只能保存最后一个图片，不知道那些同名的大神是怎么成功保存图片的。
                mbody.addFormDataPart("file[]",file.getName(),RequestBody.create(MEDIA_TYPE_PNG,file));
//                i++;
            }
        }

        RequestBody requestBody =mbody.build();
        Request request = new Request.Builder()
                .addHeader("cookie", cookie)
//                .header("Authorization", "Client-ID " + "...")
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
