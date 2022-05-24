package com.rfid.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rfid.app.filebrowser.FileManagerActivity;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class RFIDViewActivity extends Activity {
    public static final int REQUESTCODE_CHOOSE_FILE = 3;
    private TextView textViewHeader;
    private TextView textView;
    private String fCurrentFile = "";
    private Boolean fFormLoading = true;
    Url url = new Url();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_rfid_view);

            Button btnBack          = findViewById(R.id.btnBack);
            Button btn_chooseFile   = findViewById(R.id.btn_chooseFile);
            Button btn_report       = findViewById(R.id.btn_report);
            Button btn_push         = findViewById(R.id.btn_push);
            textViewHeader          = findViewById(R.id.textViewHeader);
            textView                = findViewById(R.id.textView);



            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            btn_chooseFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewScannedFile();
                }
            });
            btn_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(RFIDViewActivity.this, RFIDReportActivity.class));
                }
            });
            btn_push.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddProducts(url.InsertReal());
                }
            });

            fCurrentFile = getIntent().getStringExtra("IntentObject");
            LoadData(fCurrentFile);
            fFormLoading = false;
        } catch (Exception ex) {
            onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private void ViewScannedFile(){
        Intent in = new Intent(RFIDViewActivity.this, FileManagerActivity.class);
        in.putExtra("IntentObject", fCurrentFile);
        startActivityForResult(in, REQUESTCODE_CHOOSE_FILE);
    }

    public void LoadData(String filePath) {
        if (filePath != null && !filePath.equals("")) {
            textViewHeader.setText(FilenameUtils.getName(filePath));
            List<String> lst = FileImport.LoadFileScan(fCurrentFile);
            textView.setText(String.join("\n", lst));

        }
        else{
            if (fFormLoading){
                ViewScannedFile();
            }
        }
    }

    public int push(String list, String id){
        int count = 0;
        String[] s = list.split(id);
        for(String item: s){
            count++;
        }
        return count-1;
    }

    private void AddProducts(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Success")){
                            Toast.makeText(RFIDViewActivity.this, "Push success", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RFIDViewActivity.this, "Push error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RFIDViewActivity.this, "Connect Error in RFIDViewActivity", Toast.LENGTH_SHORT).show();
                        Log.d("Error","Maybe Connect Error!\n" + error.toString());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                List<String> lst = FileImport.LoadFileScan(fCurrentFile);
                StringTokenizer st = new StringTokenizer( String.join("\n", lst),"\n");

                int count;
                while(st.hasMoreTokens()){
                    count = 1;
                    params.put("item_code", st.nextToken().trim());
//                    params.put("item_code", "001E".trim());
                    params.put("real_value", String.valueOf(count).trim());

                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO Auto-generated method stub
        switch (requestCode) {
            case REQUESTCODE_CHOOSE_FILE:
                if(resultCode == Activity.RESULT_OK) {
                    fCurrentFile = data.getStringExtra(FileManagerActivity.EXTRA_FILE_PATH);
                    LoadData(fCurrentFile);
                }
                break;

            default:
                break;
        }
    }
}
