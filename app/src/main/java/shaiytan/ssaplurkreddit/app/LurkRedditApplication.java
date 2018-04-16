package shaiytan.ssaplurkreddit.app;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import shaiytan.ssaplurkreddit.db.LurkRedditDatabase;
import shaiytan.ssaplurkreddit.model.RedditAPI;
import shaiytan.ssaplurkreddit.model.RedditPage;

public class LurkRedditApplication extends Application {
    private static LurkRedditDatabase db;
    private static RedditAPI api;

    public static LurkRedditDatabase getDB() {
        return db;
    }

    public static RedditAPI getAPI() {
        return api;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(getApplicationContext(),
                LurkRedditDatabase.class,
                LurkRedditDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(RedditPage.class, new RedditPage.Deserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(RedditAPI.BASE_URL)
                .build();
        api = retrofit.create(RedditAPI.class);
    }
}
