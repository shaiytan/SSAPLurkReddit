package shaiytan.ssaplurkreddit.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;

//post data to show in feed activity
public class RedditPost implements Serializable {
    private String id;
    private String title;
    private String postHint;
    private boolean isVideo;
    private String imageLink;

    public RedditPost(String id, String title, String postHint, String imageLink, boolean isVideo) {
        this.id = id;
        this.title = title;
        this.postHint = postHint;
        this.imageLink = imageLink;
        this.isVideo = isVideo;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isImage() {
        return postHint.equals("image") && !isVideo;
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
            String title = data.get("title").getAsString();

            JsonObject preview = data.getAsJsonObject("preview");
            String url = null;
            boolean isVideo = true;
            if (preview != null) {
                JsonObject image = preview.getAsJsonArray("images")
                        .get(0)
                        .getAsJsonObject();
                JsonObject source = image.getAsJsonArray("resolutions")
                        .get(1)
                        .getAsJsonObject();

                url = source.get("url").getAsString();
                isVideo = image.getAsJsonObject("variants").size() > 0;
            }

            JsonElement postHintElement = data.get("post_hint");
            String postHint = postHintElement != null ? postHintElement.getAsString() : "no_image";
            return new RedditPost(id, title, postHint, url, isVideo);
        }
    }
}
