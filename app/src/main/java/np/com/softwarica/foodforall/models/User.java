package np.com.softwarica.foodforall.models;

public class User {
    private String fullname;
    private String email;
    private String image;

    public User() {
    }

    public User(String fullname, String email, String image) {
        this.fullname = fullname;
        this.email = email;
        this.image = image;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
