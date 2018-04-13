package shaiytan.ssaplurkreddit.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RedditAPI {
    String BASE_URL = "https://www.reddit.com/";

    @GET("r/aww.json?raw_json=1")
    Call<RedditPage> getPosts(@Query("after") String after, @Query("limit") int limit);
}
