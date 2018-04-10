package shaiytan.ssaplurkreddit;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Response;
import shaiytan.ssaplurkreddit.model.RedditAPI;
import shaiytan.ssaplurkreddit.model.RedditPage;
import shaiytan.ssaplurkreddit.model.RedditPost;

import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void retrofit_works() throws IOException {
        RedditAPI redditAPI = RedditAPI.getAPI();
        Response<RedditPage> response = redditAPI.getPosts("", 100).execute();
        RedditPage body = response.body();
        assertNotNull(body);
        System.out.println("before=" + body.getBefore());
        System.out.println("after=" + body.getAfter());
        System.out.println("\nChildren:\n");
        for (RedditPost post : body.getChildren()) {
            String id = post.getId();
            assertNotNull(id);
            System.out.println("id=" + id);
            String title = post.getTitle();
            assertNotNull(title);
            System.out.println("title=" + title);
            System.out.println("is_image=" + post.isImage());
            String url = post.getImageLink();
            assertNotNull(url);
            System.out.println("url=" + url);
            System.out.println("-----------------");
        }
    }
}