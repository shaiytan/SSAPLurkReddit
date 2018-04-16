package shaiytan.ssaplurkreddit.view;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import shaiytan.ssaplurkreddit.R;
import shaiytan.ssaplurkreddit.app.LurkRedditApplication;
import shaiytan.ssaplurkreddit.db.User;
import shaiytan.ssaplurkreddit.db.UsersDAO;

public class LoginActivity extends AppCompatActivity {
    public static final String RESULT_LOGIN = "login";
    public static final String RESULT_ID = "uid";

    private static final String REGISTER = "Register";
    private static final String SIGN_IN = "Sign In";

    private EditText usernameView;
    private EditText passwordView;
    private String action;
    private Button submit;
    private UsersDAO users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        submit = findViewById(R.id.btn_sign_in);
        usernameView = findViewById(R.id.et_username);
        passwordView = findViewById(R.id.et_password);
        SwitchCompat registerSwitch = findViewById(R.id.register_switch);
        setMode(registerSwitch.isChecked());
        registerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> setMode(isChecked));
        users = LurkRedditApplication.getDB().usersDAO();
    }

    private void setMode(boolean register) {
        action = register ? REGISTER : SIGN_IN;
        setTitle(action);
        submit.setText(action);
        usernameView.setText("");
        passwordView.setText("");
    }

    public void onSubmit(View view) {
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        if (username.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Don't leave fields empty", Toast.LENGTH_SHORT).show();
        else
            switch (action) {
                case REGISTER:
                    try {
                        long id = users.insertUser(new User(username, password));
                        finishSuccess(id, username);
                    } catch (SQLiteException e) {
                        Toast.makeText(
                                this,
                                "User with such login exists. Choose a different one.",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
                case SIGN_IN:
                    User user = users.findUserByLogin(username);
                    if (user != null && password.equals(user.getPassword()))
                        finishSuccess(user.getId(), username);
                    else
                        Toast.makeText(this, "Wrong username or password.", Toast.LENGTH_SHORT).show();
                    break;
            }
    }

    private void finishSuccess(long id, String login) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_LOGIN, login);
        intent.putExtra(RESULT_ID, id);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
