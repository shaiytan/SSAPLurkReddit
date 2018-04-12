package shaiytan.ssaplurkreddit.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import shaiytan.ssaplurkreddit.R;

public class LoginActivity extends AppCompatActivity {
    public static final String REGISTER = "Register";
    public static final String SIGN_IN = "Sign In";
    private final Map<String, String> users = new HashMap<>();
    private EditText usernameView;
    private EditText passwordView;
    private SwitchCompat registerSwitch;
    private String action;
    private Button submit;
    private String username;
    private String password;

    {
        users.put("vasya", "1234");
        users.put("petya", "qwerty");
        users.put("slavka", "jsforever");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        submit = findViewById(R.id.btn_sign_in);
        usernameView = findViewById(R.id.et_username);
        passwordView = findViewById(R.id.et_password);
        registerSwitch = findViewById(R.id.register_switch);
        registerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> checkMode());
        checkMode();
    }

    private void checkMode() {
        action = registerSwitch.isChecked() ? REGISTER : SIGN_IN;
        setTitle(action);
        submit.setText(action);
        usernameView.setText("");
        passwordView.setText("");
    }

    public void onSubmit(View view) {
        username = usernameView.getText().toString();
        password = passwordView.getText().toString();
        switch (action) {
            case REGISTER:
                if (users.containsKey(username))
                    Toast.makeText(this, "Choose another username", Toast.LENGTH_SHORT).show();
                else {
                    users.put(username, password);
                    finishSuccess();
                }
                break;
            case SIGN_IN:
                String pass = users.get(username);
                if (pass != null && password.equals(pass)) finishSuccess();
                else Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void finishSuccess() {
        Intent intent = new Intent();
        intent.putExtra("username", username);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
