package np.com.softwarica.foodforall.models;

import java.io.Serializable;

public class Comment implements Serializable {
    private String name;
    private String image;
    private String comment;
    private String timestamp;

    public Comment() {
    }

    public Comment(String name, String image, String comment, String timestamp) {
        this.name = name;
        this.image = image;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
