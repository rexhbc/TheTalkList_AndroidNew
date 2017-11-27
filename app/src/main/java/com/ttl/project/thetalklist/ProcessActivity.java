package com.ttl.project.thetalklist;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ttl.project.thetalklist.Services.LoginService;

public class ProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);


    /*    Dialog dialog=new Dialog(getApplicationContext());
        dialog.setContentView(R.layout.threedotprogressbar);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();*/
        SharedPreferences pref111=getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        LoginService loginService=new LoginService();
        loginService.login(pref111.getString("email",""),pref111.getString("pass",""),getApplicationContext());


        Toast.makeText(getApplicationContext(), "role "+ getIntent().hasExtra("role"), Toast.LENGTH_SHORT).show();

        SharedPreferences pref=getSharedPreferences("roleChange",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();



    Intent i=getIntent();
        if (i.hasExtra("role")){
            Intent ix=new Intent(getApplicationContext(),SettingFlyout.class);
            ix.putExtra("role",0);
            editor.putInt("roleId",0).apply();
            Toast.makeText(getApplicationContext(), "set role", Toast.LENGTH_SHORT).show();
            ix.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(ix);
        }else
        {
            Intent ix=new Intent(getApplicationContext(),SettingFlyout.class);
            ix.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(ix);
        }
//            dialog.dismiss();

    }
}
