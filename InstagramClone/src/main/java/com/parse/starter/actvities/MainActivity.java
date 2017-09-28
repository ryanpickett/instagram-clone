/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter.actvities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.starter.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Boolean signUpModeActive = true;

    TextView tv_changeSignupMode;
    EditText et_username;
    EditText et_password;
    RelativeLayout rl_background;
    ImageView iv_logo;
    Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Instagram");

        tv_changeSignupMode = (TextView) findViewById(R.id.tv_changeSignupMode);
        tv_changeSignupMode.setOnClickListener(this);

        et_username = (EditText) findViewById(R.id.et_username);

        et_password = (EditText) findViewById(R.id.et_password);
        et_password.setOnKeyListener(this);

        rl_background = (RelativeLayout) findViewById(R.id.rl_background);
        rl_background.setOnClickListener(this);

        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        iv_logo.setOnClickListener(this);

        btn_signup = (Button) findViewById(R.id.btn_signup);

        if (ParseUser.getCurrentUser() != null) {

            showUserList();

        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

            signUp(view);

        }

        return false;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.tv_changeSignupMode) {

            if (signUpModeActive) {

                signUpModeActive = false;
                btn_signup.setText("Login");
                tv_changeSignupMode.setText("Signup");

            } else {

                signUpModeActive = true;
                btn_signup.setText("Signup");
                tv_changeSignupMode.setText("Login");

            }

        } else if (view.getId() == R.id.rl_background || view.getId() == R.id.iv_logo) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }
    }

    public void signUp(View view) {

        if (et_username.getText().toString().matches("") || et_password.getText().toString().matches("")) {

            Toast.makeText(this, "A username and password are required.", Toast.LENGTH_SHORT).show();

        } else {

            if (signUpModeActive) {

                ParseUser user = new ParseUser();

                user.setUsername(et_username.getText().toString());
                user.setPassword(et_password.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            Log.i("Signup", "Successful");
                            showUserList();

                        } else {

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            } else {

                ParseUser.logInInBackground(et_username.getText().toString(), et_password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if (user != null) {

                            Log.i("Signup", "Login successful");
                            showUserList();

                        } else {

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        }
    }

    public void showUserList() {

        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);

    }

}
