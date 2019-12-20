package np.com.softwarica.foodforall.models;

import java.io.Serializable;

public class Like implements Serializable {
    private String uid;

    public Like() {
    }

    public Like(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
