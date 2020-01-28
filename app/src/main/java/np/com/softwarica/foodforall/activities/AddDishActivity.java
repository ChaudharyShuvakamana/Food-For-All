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

import np.com.softwarica.foodforall.R;
import np.com.softwarica.foodforall.models.Dish;
import np.com.softwarica.foodforall.utils.FirebaseUtils;
import np.com.softwarica.foodforall.utils.ImageUtils;
import np.com.softwarica.foodforall.utils.SharedUtils;

public class AddDishActivity extends AppCompatActivity {

    private final int GALLERY_REQUEST_CODE = 1;
    private final int CAMERA_REQUEST_CODE = 2;
    private SimpleDraweeView imgDish;
    private EditText etDishName, etOrigin, etDuration, etIngredance, etInstruction;
    private Uri dishUri;
    private Dish dish;
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);

        dish = (Dish) getIntent().getSerializableExtra("dish");

        imgDish = findViewById(R.id.imgDish);
        etDishName = findViewById(R.id.etDishName);
        etOrigin = findViewById(R.id.etOrigin);
        etDuration = findViewById(R.id.etDuration);
        etIngredance = findViewById(R.id.etIngredance);
        etInstruction = findViewById(R.id.etInstruction);

        findViewById(R.id.imgBack).setOnClickListener(v -> super.onBackPressed());

        imgDish.setOnClickListener(v -> choosePhotoOption());

        if (dish != null) {
            fill();
            isUpdate = true;
        }
    }

    private void fill() {
        etDishName.setText(dish.getName());
        etOrigin.setText(dish.getOrigin());
        etDuration.setText(dish.getDuration());
        etIngredance.setText(dish.getIngredance());
        etInstruction.setText(dish.getInstruction());
        imgDish.setImageURI(dish.getImage());
    }

    public void addDish(View view) {
        if (!validate()) return;
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Adding dish...");
        dialog.show();

        if (isUpdate) {
            dish.setName(etDishName.getText().toString().trim());
            dish.setDuration(etDuration.getText().toString().trim());
            dish.setOrigin(etOrigin.getText().toString().trim());
            dish.setIngredance(etIngredance.getText().toString().trim());
            dish.setInstruction(etInstruction.getText().toString().trim());
            FirebaseUtils.database.child("dishes").child(dish.getKey()).setValue(dish);
            Toast.makeText(this, "Dish updated successfully!!!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            String fileName = System.currentTimeMillis() + ".jpg";
            FirebaseUtils.storage.child("dishes").child(fileName).putFile(dishUri).addOnCompleteListener(task -> {
                FirebaseUtils.storage.child("dishes").child(fileName).getDownloadUrl().addOnSuccessListener(uri -> {
                    dialog.dismiss();
                    String image = uri.toString();
                    Dish dish = new Dish();
                    dish.setName(etDishName.getText().toString().trim());
                    dish.setDuration(etDuration.getText().toString().trim());
                    dish.setOrigin(etOrigin.getText().toString().trim());
                    dish.setIngredance(etIngredance.getText().toString().trim());
                    dish.setInstruction(etInstruction.getText().toString().trim());
                    dish.setImage(image);
                    dish.setFullName(SharedUtils.getString(AddDishActivity.this, "name"));
                    dish.setUserImage(SharedUtils.getString(AddDishActivity.this, "image"));
                    dish.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    FirebaseUtils.database.child("dishes").push().setValue(dish);
                    Toast.makeText(this, "Dish added successfully!!!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(etDishName.getText().toString())) {
            etDishName.setError("Dish name is required!!!");
            etDishName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etOrigin.getText().toString())) {
            etOrigin.setError("Origin is required!!!");
            etOrigin.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etDuration.getText().toString())) {
            etDuration.setError("Duration is required!!!");
            etDuration.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etIngredance.getText().toString())) {
            etIngredance.setError("Ingredients are required!!!");
            etIngredance.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etInstruction.getText().toString())) {
            etInstruction.setError("Instructions are required!!!");
            etInstruction.requestFocus();
            return false;
        } else if (dishUri == null && !isUpdate) {
            Toast.makeText(this, "Please choose recipe image.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
                dishUri = data.getData();
                imgDish.setImageURI(dishUri);
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                final Bitmap photo = (Bitmap) data.getExtras().get("data");
                imgDish.setImageBitmap(photo);
                dishUri = ImageUtils.getImageUri(AddDishActivity.this, photo);
            }
        }
    }
}
