package com.encryprion_decryption.encry_decry;

// Text encryption and decryption using AES algorithm

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Scene;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scottyab.aescrypt.AESCrypt;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    EditText et_key,et_message;
    Button btn_encrypt,btn_decrypt;
    TextView message,textView;
    String output,etkey,etmsg;
    String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_key = findViewById(R.id.et_key);
        et_message = findViewById(R.id.et_message);
        btn_encrypt = findViewById(R.id.btn_encrypt);
        btn_decrypt = findViewById(R.id.btn_decrypt);
        message = findViewById(R.id.message);
        textView = findViewById(R.id.textView);

        //encrypt button
        btn_encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    etkey = et_key.getText().toString();
                    etmsg = et_message.getText().toString();

                    // checking input is empty or not
                    if(TextUtils.isEmpty(etkey) || TextUtils.isEmpty(etmsg)){
                        Toast.makeText(MainActivity.this, "Enter everything", Toast.LENGTH_SHORT).show();
                    }else {

                        output = encrypt(etmsg,etkey);
                        message.setText(output);
                        textView.setText("Encrypted Text");
                        et_key.setText("");
                        et_message.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // decrypt button
        btn_decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    output = decrypt(output,et_key.getText().toString());
                    message.setText(output);
                    textView.setText("Decrypted Text");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //decrypt method
    private String decrypt(String output, String msg) throws Exception{
        SecretKeySpec key = generateKey(msg);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decode_val = Base64.decode(output,Base64.DEFAULT);
        byte[] decoded = c.doFinal(decode_val);
        String decrypted_value = new String(decoded);
        return decrypted_value;
    }

    //encrypt method
    private String encrypt(String data, String msg) throws Exception{
        SecretKeySpec key = generateKey(msg);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encrypt_val = c.doFinal(data.getBytes());
        String encrypted_value = Base64.encodeToString(encrypt_val,Base64.DEFAULT);
        return encrypted_value;
    }

    private SecretKeySpec generateKey(String msg) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = msg.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.blowfish:
                Toast.makeText(this, "Changed to BlowFish", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.rsa:
                Toast.makeText(this, "Changed to RSA", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.twofish:
                Toast.makeText(this, "Changed to TwoFish", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.about:
                setContentView(R.layout.about_app);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
}