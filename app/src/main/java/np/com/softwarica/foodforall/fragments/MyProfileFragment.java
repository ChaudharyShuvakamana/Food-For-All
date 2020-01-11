package np.com.softwarica.foodforall.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import np.com.softwarica.foodforall.R;
import np.com.softwarica.foodforall.utils.FirebaseUtils;
import np.com.softwarica.foodforall.utils.ImageUtils;
import np.com.softwarica.foodforall.utils.SharedUtils;

import static android.app.Activity.RESULT_OK;

public class MyProfileFragment extends Fragment {

    private final int GALLERY_REQUEST_CODE = 1;
    private final int CAMERA_REQUEST_CODE = 2;
    private SimpleDraweeView imgProfile;
    private TextView tvFullName, tvEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        imgProfile = v.findViewById(R.id.imgProfile);
        tvFullName = v.findViewById(R.id.tvFullName);
        tvEmail = v.findViewById(R.id.tvEmail);

        imgProfile.setImageURI(SharedUtils.getString(getActivity(), "image"));
        tvFullName.setText(SharedUtils.getString(getActivity(), "name"));
        tvEmail.setText(SharedUtils.getString(getActivity(), "email"));

        imgProfile.setOnClickListener(view -> choosePhotoOption());
        tvFullName.setOnClickListener(view -> openUpdateDialog());

        return v;
    }

    private void openUpdateDialog() {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_name, null, false);
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setView(v);
        EditText etFullName = v.findViewById(R.id.etFullName);
        Button btnUpdate = v.findViewById(R.id.btnUpdate);
        etFullName.setText(tvFullName.getText().toString());
        btnUpdate.setOnClickListener(view -> {
            String fullName = etFullName.getText().toString().trim();
            if (TextUtils.isEmpty(fullName)) {
                etFullName.setError("Please enter full name.");
                etFullName.requestFocus();
                return;
            }
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("fullname").setValue(fullName);
            Toast.makeText(getActivity(), "Profile name updated successfully!!!", Toast.LENGTH_SHORT).show();
            tvFullName.setText(fullName);
            dialog.dismiss();
        });
        dialog.show();
    }

    public void choosePhotoOption() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
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
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                Uri profileUri = data.getData();
                updateProfileImage(profileUri);
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                final Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri profileUri = ImageUtils.getImageUri(getActivity(), photo);
                updateProfileImage(profileUri);
            }
        }
    }

    private void updateProfileImage(Uri profileUri) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Updating Profile Picture...");
        dialog.show();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUtils.storage.child("profile_pictures/" + uid + ".jpg").putFile(profileUri).addOnCompleteListener(task -> {
            FirebaseUtils.storage.child("profile_pictures/" + uid + ".jpg").getDownloadUrl().addOnSuccessListener(uri -> {
                FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("image").setValue(uri.toString());
                imgProfile.setImageURI(profileUri);
                Toast.makeText(getActivity(), "Profile updated successfully!!!", Toast.LENGTH_SHORT).show();
            });
            dialog.dismiss();
        });

    }

}
