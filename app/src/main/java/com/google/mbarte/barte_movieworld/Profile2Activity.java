package com.google.mbarte.barte_movieworld;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile2Activity extends AppCompatActivity {

    private ImageView imageView;
    private EditText fullname1, emailaddress, dateHERE;
    Button updateButton, photoButton;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    private String currentUserId;
    final DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(currentUserId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        imageView = (ImageView)findViewById(R.id.imageHERE1);
        photoButton = (Button) findViewById(R.id.addPhotoButton1);
        updateButton = (Button) findViewById(R.id.updateButton2);

        fullname1 = (EditText) findViewById(R.id.fullNameProfile1);
        emailaddress = (EditText) findViewById(R.id.emailProfile1);
        dateHERE = (EditText) findViewById(R.id.birthdateProfile1);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
                finish();
            }
        });

    }
    public void saveUserInfo()
    {
        String fullNameHERE = fullname1.getText().toString();
        String emailAddressHERE = emailaddress.getText().toString();
        String dateSave = dateHERE.getText().toString();
        String pass = "123456";
        String repassword = "123456";

        Users userProfile = new Users(fullNameHERE, emailAddressHERE, pass, repassword, dateSave);
        databaseReference.setValue(userProfile);
    }
    public void retrieveData()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Users userProfile = dataSnapshot.getValue(Users.class);
                    fullname1.setText(userProfile.getFullname());
                    emailaddress.setText(userProfile.getEmail());
                    dateHERE.setText(userProfile.getBirth());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile2Activity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
