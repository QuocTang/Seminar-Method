package com.rfid.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddProductsActivity extends AppCompatActivity {
    EditText edtItem_code, edtId, edtTheory;
    Button btnAdd, btnBack;

    Url url = new Url();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        AnhXa();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_code = edtItem_code.getText().toString().trim();
                String theoryValue = edtTheory.getText().toString().trim();
                String product_id = edtId.getText().toString().trim();
                if(item_code.isEmpty() || theoryValue.isEmpty()){
                    Toast.makeText(AddProductsActivity.this, "Please fill in all!", Toast.LENGTH_SHORT).show();
                }else{
                    AddProducts(url.Insert());
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AddProducts(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Success")){
                            Toast.makeText(AddProductsActivity.this, "Add success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddProductsActivity.this, RFIDScanActivity.class));
                        }else{
                            Toast.makeText(AddProductsActivity.this, "Add error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddProductsActivity.this, "Connect Error in AddProductsActivity", Toast.LENGTH_SHORT).show();
                        Log.d("Error","Maybe Connect Error!\n" + error.toString());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("item_code", edtItem_code.getText().toString().trim());
                params.put("theory_value", edtTheory.getText().toString().trim());
                params.put("product_id", edtId.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void AnhXa() {
        btnAdd = (Button) findViewById(R.id.buttonAdd);
        btnBack = (Button) findViewById(R.id.buttonBack);
        edtItem_code = (EditText) findViewById(R.id.editTextItemCode);
        edtTheory = (EditText) findViewById(R.id.editTextTheoryValue);
        edtId = (EditText) findViewById(R.id.editTextProductId);
    }
}