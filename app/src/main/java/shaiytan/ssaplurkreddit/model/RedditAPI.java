package shaiytan.ssaplurkreddit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RedditAPI {
    String BASE_URL = "https://www.reddit.com/";

    static RedditAPI getAPI() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(RedditPage.class, new RedditPage.Deserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build();
        return retrofit.create(RedditAPI.class);
    }

    @GET("r/aww.json?raw_json=1")
    Call<RedditPage> getPosts(@Query("after") String after, @Query("limit") int limit);
}
