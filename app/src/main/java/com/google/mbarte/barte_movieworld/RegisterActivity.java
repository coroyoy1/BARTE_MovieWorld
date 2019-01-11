package com.google.mbarte.barte_movieworld;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.PatternMatcher;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    EditText editFullName, editEmail, editPassword, editRePassword, editDate;
    Button signupButtonHere, addPhotoButton;
    ImageView imageView;
    FirebaseStorage storage;
    StorageReference storageReference;

    private FirebaseAuth mAuth;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "RegisterActivity";

    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        editFullName = (EditText)findViewById(R.id.editTextFullname);
        editEmail = (EditText)findViewById(R.id.editTextEmail);
        editPassword = (EditText)findViewById(R.id.editTextPassword);
        editRePassword = (EditText)findViewById(R.id.editTextRePassword);
        signupButtonHere = (Button)findViewById(R.id.signupButton);
        addPhotoButton = (Button)findViewById(R.id.createAddButton);
        imageView = (ImageView)findViewById(R.id.createImage);
        editDate = (EditText)findViewById(R.id.editTextBirthdate);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        databaseUsers = FirebaseDatabase.getInstance().getReference();

        signupButtonHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadImage();
                RegisterUser2();

            }
        });
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yy"+month+"/"+day+"/"+year);
                String date = month + "/" + day + "/" + year;
                editDate.setText(date);
            }
        };

    }

    private void uploadImage() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading image...");
            progressDialog.show();
            StorageReference ref = storageReference.child("images/"+UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          progressDialog.dismiss();
                          Toast.makeText(RegisterActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this,"Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Upload "+(int)progress+"%");
                        }
                    });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null)
        {

        }
    }

    private void RegisterUser2()
    {
        final String fullname = editFullName.getText().toString().trim();
        final String email = editEmail.getText().toString().trim();
        final String password = editPassword.getText().toString().trim();
        final String repassword = editRePassword.getText().toString().trim();
        final String birthdate = editDate.getText().toString().trim();

        if(!TextUtils.isEmpty(fullname)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)||!TextUtils.isEmpty(repassword))
        {
            if(password.equals(repassword)) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Users user = new Users(fullname, email, password, repassword, birthdate);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {

                                    }
                                }
                            });
                        }
                        else
                        {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                Toast.makeText(getApplicationContext(), "Already Registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
            else
            {
                Toast.makeText(this, "Password should be same", Toast.LENGTH_SHORT).show();
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                editEmail.setError("Please put valid email");
                editEmail.requestFocus();
                return;
            }
            if(password.isEmpty()&&repassword.isEmpty())
            {
                editPassword.setError("Password is empty");
                editPassword.requestFocus();
                return;
            }
            if(password.length() < 6)
            {
                editPassword.setError("Minimum password is 6");
                editPassword.requestFocus();
                return;
            }
        }
        else
        {
            Toast.makeText(this, "All input should be inputed", Toast.LENGTH_SHORT).show();
            return;
        }
    }

//    private void RegisterUser()
//    {
//        String fullname = editFullName.getText().toString().trim();
//        String email = editEmail.getText().toString().trim();
//        String password = editPassword.getText().toString().trim();
//        String repassword = editRePassword.getText().toString().trim();
//        String birthdate = editDate.getText().toString().trim();
//
//        if(!TextUtils.isEmpty(fullname)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)||!TextUtils.isEmpty(repassword))
//        {
//            if(password.equals(repassword)) {
//                String id = databaseUsers.push().getKey();
//
//                Users user = new Users(fullname, email, password, repassword, birthdate);
//
//                databaseUsers.child(id).setValue(user);
//
//                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful())
//                        {
//                            finish();
//                            startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
//                            Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
//                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
//                            {
//                                Toast.makeText(getApplicationContext(), "Already Registered", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//            }
//            else
//            {
//                Toast.makeText(this, "Password should be same", Toast.LENGTH_SHORT).show();
//            }
//            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
//            {
//                editEmail.setError("Please put valid email");
//                editEmail.requestFocus();
//                return;
//            }
//            if(password.isEmpty()&&repassword.isEmpty())
//            {
//                editPassword.setError("Password is empty");
//                editPassword.requestFocus();
//                return;
//            }
//            if(password.length() < 6)
//            {
//                editPassword.setError("Minimum password is 6");
//                editPassword.requestFocus();
//                return;
//            }
//        }
//        else
//        {
//            Toast.makeText(this, "All input should be inputed", Toast.LENGTH_SHORT).show();
//        }
//    }
}
