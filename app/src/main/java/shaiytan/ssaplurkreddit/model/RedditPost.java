package shaiytan.ssaplurkreddit.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class RedditPost {
    private String id;
    private String title;
    private String postHint;
    private boolean hasVariants;
    private String imageLink;

    public RedditPost(String id, String title, String postHint, String imageLink, boolean hasVariants) {
        this.id = id;
        this.title = title;
        this.postHint = postHint;
        this.imageLink = imageLink;
        this.hasVariants = hasVariants;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isImage() {
        return postHint.equals("image") && !hasVariants;
    }

    public String getImageLink() {
        return imageLink;
    }

    public static class Deserializer implements JsonDeserializer<RedditPost> {
        @Override
        public RedditPost deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject child = json.getAsJsonObject();
            JsonObject data = child.getAsJsonObject("data");
            String id = data.get("id").getAsString();
            String postHint = data.get("post_hint").getAsString();
            String title = data.get("title").getAsString();

            JsonObject preview = data.getAsJsonObject("preview");
            JsonObject image = preview.getAsJsonArray("images")
                    .get(0)
                    .getAsJsonObject();
            JsonObject source = image.getAsJsonObject("source");
            String url = source.get("url").getAsString();
            int variantsSize = image.getAsJsonObject("variants").size();

            return new RedditPost(id, title, postHint, url, variantsSize > 0);
        }
    }
}
