package com.example.phoneverification;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

public class MainActivity extends AppCompatActivity {
    public static int APP_REQUEST_CODE=0;
    TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        number=findViewById(R.id.tvnumber);

    }
    public void phoneLogin(final View view) {
        final Intent intent = new Intent(getApplicationContext(), AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE){
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if (loginResult.getError()!=null){
                Toast.makeText(this, ""+loginResult.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
//                showErrorActivity(loginResult.getError());
            }
            else if (loginResult.wasCancelled()){
                Toast.makeText(this, "Login Cancelled", Toast.LENGTH_SHORT).show();

            }else{
                if (loginResult.getAccessToken()!=null){
                    Toast.makeText(this, "Success"+loginResult.getAccessToken().getAccountId(), Toast.LENGTH_SHORT).show();

                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(Account account) {
                            number.setText(account.getPhoneNumber().toString());
                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {

                        }
                    });
                }else{
                    Toast.makeText(this, ""+String.format("Success:%s...", loginResult.getAuthorizationCode().substring(0,10)), Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    public void verify(View view) {
        phoneLogin(view);
    }


}
