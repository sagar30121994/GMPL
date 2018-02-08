package cybernetics.hellaecu.pvpl.com.gmpl;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
SharedPreferences sharedPreferences;
String name;
TextView hello;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=getSharedPreferences("Main",MODE_PRIVATE);
       name= sharedPreferences.getString("name","");
        getSupportActionBar().setTitle("Home");

        hello=(TextView)findViewById(R.id.text_hello);

    hello.setText("Hello "+name);
    }
}
