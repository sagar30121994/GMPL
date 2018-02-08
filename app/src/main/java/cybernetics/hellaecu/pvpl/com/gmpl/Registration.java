package cybernetics.hellaecu.pvpl.com.gmpl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



/*
 Class-Registration
 Author - Sagar Thorat
 Date - 30-jan-18
*/


public class Registration extends AppCompatActivity {

    EditText username, pass, confirm_Pass, email;
    boolean flag = false;
    Button submit,btn_Login;
    Boolean isRegister = false;
    private FirebaseAuth mAuth;
    private String TAG = getClass().getSimpleName();
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.et_email);
        username = (EditText) findViewById(R.id.et_username);
        pass = (EditText) findViewById(R.id.et_password);
        confirm_Pass = (EditText) findViewById(R.id.et_et_confirm_password);
        submit = (Button) findViewById(R.id.btn_submit);
        editor = getSharedPreferences("Main", MODE_PRIVATE).edit();
        getSupportActionBar().setTitle("Registration");
        btn_Login=(Button)findViewById(R.id.btn_login);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //Befor Registration Validate Fields
                if (validateFields()) {


                    registerUser();

                }
            }
        });

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registration.this, Login.class);
                startActivity(i);

            }
        });

    }


    /*
      After validatation register the user to firebase

    */

    private boolean registerUser() {

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(Registration.this, "Authentication Success.", Toast.LENGTH_SHORT).show();

                            sendRegistrationLink();
                            isRegister= true;


                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Registration.this, "User Already exist.", Toast.LENGTH_SHORT).show();
                            isRegister= false;
                        }

                    }
                });
        return isRegister;
    }

/*
      After Registration send the authentication link to mail using firebase auth
    */

    private void sendRegistrationLink() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(Registration.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Registration.this,
                                    "Verification email sent to " + user.getEmail(),

                                    Toast.LENGTH_SHORT).show();
                            validateUsername();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(Registration.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*
      I am saving username in-order to use it after login
    */

    private void validateUsername() {
        editor.putString("name", username.getText().toString().trim());
        editor.commit();
        Intent i = new Intent(Registration.this, Login.class);
        startActivity(i);
        finish();
    }

    /*
      Vadidating all field
    */
    private boolean validateFields() {


        if (username.getText().toString().trim().equalsIgnoreCase("")
                || username.getText().toString().trim().equalsIgnoreCase(null)) {
            Toast.makeText(Registration.this, "Plaese Enter valid username", Toast.LENGTH_LONG).show();
            flag = false;
        } else if (email.getText().toString().trim().equalsIgnoreCase("")
                || username.getText().toString().trim().equalsIgnoreCase(null)) {
            Toast.makeText(Registration.this, "Plaese Enter valid email", Toast.LENGTH_LONG).show();
            flag = false;
        } else if (pass.getText().toString().trim().equalsIgnoreCase("")
                || username.getText().toString().trim().equalsIgnoreCase(null)) {
            Toast.makeText(Registration.this, "Password should not be blank", Toast.LENGTH_LONG).show();

            flag = false;
        } else if (!pass.getText().toString().equals(confirm_Pass.getText().toString())) {
            Toast.makeText(Registration.this, "Password does not match ", Toast.LENGTH_LONG).show();
            flag = false;
        }else if (pass.getText().toString().length()<6) {
            Toast.makeText(Registration.this, "Password must be 6 digit long ", Toast.LENGTH_LONG).show();
            flag = false;
        } else {
            flag = true;
        }

        return flag;

    }

}
