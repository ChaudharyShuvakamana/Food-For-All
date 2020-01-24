package np.com.softwarica.foodforall.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import np.com.softwarica.foodforall.R;
import np.com.softwarica.foodforall.models.User;
import np.com.softwarica.foodforall.utils.FirebaseUtils;
import np.com.softwarica.foodforall.utils.ImageUtils;

public class RegisterActivity extends AppCompatActivity {

    private final int GALLERY_REQUEST_CODE = 1;
    private final int CAMERA_REQUEST_CODE = 2;
    private SimpleDraweeView imgProfile;
    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private Uri profileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imgProfile = findViewById(R.id.imgProfile);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        imgProfile.setOnClickListener(v -> choosePhotoOption());
    }

    public void openLogin(View view) {
        finish();
    }

    public void register(View view) {
        if (!validate()) return;
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Loading...");
        dialog.show();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                FirebaseUtils.storage.child("profile_pictures/" + task1.getResult().getUser().getUid() + ".jpg").putFile(profileUri).addOnCompleteListener(task -> {
                    FirebaseUtils.storage.child("profile_pictures/" + task1.getResult().getUser().getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(uri -> {
                        dialog.dismiss();
                        String image = uri.toString();
                        User user = new User(etFullName.getText().toString(), etEmail.getText().toString(), image);
                        FirebaseUtils.database.child("users").child(task1.getResult().getUser().getUid()).setValue(user);
                        Toast.makeText(this, "Register Successful!!!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                });
            } else {
                dialog.dismiss();
                Toast.makeText(this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void choosePhotoOption() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Upload Picture Option");
        myAlertDialog.setMessage("Please choose option to select image from?");

        myAlertDialog.setPositiveButton("Gallery", (arg0, arg1) -> {
            Intent pictureActionIntent = null;
            pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pictureActionIntent, GALLERY_REQUEST_CODE);
        });

        myAlertDialog.setNegativeButton("Camera", (arg0, arg1) -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        });
        myAlertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                profileUri = data.getData();
                imgProfile.setImageURI(profileUri);
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                final Bitmap photo = (Bitmap) data.getExtras().get("data");
                imgProfile.setImageBitmap(photo);
                profileUri = ImageUtils.getImageUri(RegisterActivity.this, photo);
            }
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(etFullName.getText().toString())) {
            etFullName.setError("Full name is required!!!");
            etFullName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError("Email is required!!!");
            etEmail.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Password is required!!!");
            etPassword.requestFocus();
            return false;
        } else if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            etConfirmPassword.setError("Password does not match!!!");
            etConfirmPassword.requestFocus();
            return false;
        } else if (profileUri == null) {
            Toast.makeText(this, "Please select profile image.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
