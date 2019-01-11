package com.google.mbarte.barte_movieworld;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    Button photoHERE, updateHERE;
    ImageView imageView;
    EditText firstName, lastName, emailAddress, dateHERE;
    String stringImageURL;
    FirebaseAuth mAuth;

    Uri uriProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        photoHERE = findViewById(R.id.addPhotoButton);
        updateHERE = findViewById(R.id.updateButton);
        imageView = findViewById(R.id.imageHERE);
        firstName = findViewById(R.id.firstNameProfile);
        lastName = findViewById(R.id.lastNameProfile);
        emailAddress = findViewById(R.id.emailProfile);

        mAuth = FirebaseAuth.getInstance();

        photoHERE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });
        updateHERE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });
    }

    private void saveUserInfo() {
        String firstN = firstName.getText().toString();
        String lastN = lastName.getText().toString();
        String emailN = emailAddress.getText().toString();
        String dateN = dateHERE.getText().toString();

        if(firstN.isEmpty() && lastN.isEmpty() && emailN.isEmpty())
        {
            Toast.makeText(ProfileActivity.this, "Fill up all inputs.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null && stringImageURL != null)
        {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(firstN)
                    .setDisplayName(lastN)
                    .setDisplayName(emailN)
                    .setDisplayName(dateN)
                    .setPhotoUri(Uri.parse(stringImageURL))
                    .build();
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            uriProfileImage = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);
                uploadImageToFirebase();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebase() {
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+ ".jpg");
        if(uriProfileImage != null)
        {
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            stringImageURL = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }
}
