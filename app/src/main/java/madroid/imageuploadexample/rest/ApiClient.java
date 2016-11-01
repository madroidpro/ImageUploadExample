package madroid.imageuploadexample.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by madroid on 03-08-2016.
 */
public class ApiClient{
    public static String BaseUrl="http://54.152.88.70/cake_pharma/webservices/";
    private static Retrofit retrofit=null;

    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
