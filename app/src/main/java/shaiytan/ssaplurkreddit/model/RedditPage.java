package shaiytan.ssaplurkreddit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

//pojo for parsing json from reddit
public class RedditPage {
    private String after;
    private List<RedditPost> children;

    public RedditPage(String after, List<RedditPost> children) {
        this.after = after;
        this.children = children;
    }

    public String getAfter() {
        return after;
    }

    public List<RedditPost> getChildren() {
        return children;
    }

    public static class Deserializer implements JsonDeserializer<RedditPage> {
        @Override
        public RedditPage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement data = jsonObject.get("data");
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(RedditPost.class, new RedditPost.Deserializer())
                    .create();
            return gson.fromJson(data, RedditPage.class);
        }
    }
}
