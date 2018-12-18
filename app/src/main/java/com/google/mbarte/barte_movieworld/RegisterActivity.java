package com.google.mbarte.barte_movieworld;

import android.os.PatternMatcher;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText editFullName, editEmail, editPassword, editRePassword;
    Button signupButtonHere;

    private FirebaseAuth mAuth;

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

        mAuth = FirebaseAuth.getInstance();

        databaseUsers = FirebaseDatabase.getInstance().getReference();

        signupButtonHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            RegisterUser();
            }
        });
    }

    private void RegisterUser()
    {
        String fullname = editFullName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String repassword = editRePassword.getText().toString().trim();

        if(!TextUtils.isEmpty(fullname)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)||!TextUtils.isEmpty(repassword))
        {
            if(password.equals(repassword)) {
                String id = databaseUsers.push().getKey();

                Users user = new Users(id, fullname, email, password, repassword);

                databaseUsers.child(id).setValue(user);

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
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
        }
    }
}
