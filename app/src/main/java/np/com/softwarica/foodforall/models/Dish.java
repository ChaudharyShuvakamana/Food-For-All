package np.com.softwarica.foodforall.models;

import java.io.Serializable;
import java.util.List;

public class Dish implements Serializable {
    private String name;
    private String image;
    private String origin;
    private String duration;
    private String difficulty;
    private String ingredance;
    private String instruction;
    private List<Like> likeList;
    private List<Comment> commentList;
    private String key;
    private String uid;
    private String fullName;
    private String userImage;

    public Dish() {
    }

    public Dish(String name, String image, String origin, String duration, String difficulty, String ingredance, String instruction) {
        this.name = name;
        this.image = image;
        this.origin = origin;
        this.duration = duration;
        this.difficulty = difficulty;
        this.ingredance = ingredance;
        this.instruction = instruction;
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getIngredance() {
        return ingredance;
    }

    public void setIngredance(String ingredance) {
        this.ingredance = ingredance;
    }

    public List<Like> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<Like> likeList) {
        this.likeList = likeList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
