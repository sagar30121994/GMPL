package cybernetics.hellaecu.pvpl.com.gmpl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/*
 Class-Login
 Author - Sagar Thorat
 Date - 30-jan-18
*/

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String TAG = getClass().getSimpleName();

    EditText email, password;

    Button login;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        login = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateEmailPassword()) {
                    mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        if (user.isEmailVerified()) {
                                            Toast.makeText(Login.this, "Verfied Email", Toast.LENGTH_LONG).show();
                                            activateIntent();
                                        } else {
                                            Toast.makeText(Login.this, "Please Verfied Email First", Toast.LENGTH_LONG).show();

                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(Login.this, "Authentication failed. Please check Email/Password",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }             }
        });
    }

    private boolean validateEmailPassword() {
        Boolean flag = false;

        if (email.getText().toString().equalsIgnoreCase("")
                | email.getText().toString().equalsIgnoreCase(null)) {
            Toast.makeText(Login.this, "Please Enter Valid Details.", Toast.LENGTH_LONG).show();
            flag = false;
        } else if (password.getText().length() < 6) {
            Toast.makeText(Login.this, "Please Enter Valid Password.", Toast.LENGTH_LONG).show();
            flag = false;
        } else {
            flag = true;
        }


        return flag;
    }

    private void activateIntent() {
        Intent i = new Intent(Login.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
