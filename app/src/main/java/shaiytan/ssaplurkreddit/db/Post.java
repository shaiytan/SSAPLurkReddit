package shaiytan.ssaplurkreddit.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "posts",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "id_user",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
)
public class Post {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "id_reddit")
    private String idReddit;
    @ColumnInfo(name = "id_user", index = true)
    private long idUser;
    private String title;
    @ColumnInfo(name = "image_link")
    private String imageLink;
    private boolean approved;

    Post() {
    }

    @Ignore
    public Post(String idReddit, long idUser, String title, String imageLink, boolean approved) {
        this.idReddit = idReddit;
        this.idUser = idUser;
        this.title = title;
        this.imageLink = imageLink;
        this.approved = approved;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdReddit() {
        return idReddit;
    }

    public void setIdReddit(String idReddit) {
        this.idReddit = idReddit;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
