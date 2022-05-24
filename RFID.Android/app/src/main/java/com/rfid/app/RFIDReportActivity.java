package com.rfid.app;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RFIDReportActivity extends AppCompatActivity{
    Url url = new Url();
    ListView lvProducts;
    ArrayList<Products> arrayProducts;
    ProductAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_list);

        //show Report
        lvProducts = findViewById(R.id.lvProducts);
        arrayProducts = new ArrayList<>();
        adapter = new ProductAdapter(this, R.layout.product_layout, arrayProducts);
        lvProducts.setAdapter(adapter);
        GetData(url.GetData());
    }
    //Getdata
    private void GetData(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        arrayProducts.clear();
                        for(int i=0; i<response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                arrayProducts.add(new Products(
                                        object.getString("item_code"),
//                                        object.getString("name"),
//                                        object.getString("color"),
//                                        object.getInt("price"),
                                        object.getInt("real_value"),
                                        object.getInt("theory_value"),
                                        object.getInt("gap"),
                                        object.getString("time")
                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RFIDReportActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    public void DeleteProducts(String itemcode){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.Delete(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Success")){
                            Toast.makeText(RFIDReportActivity.this, "Delete success", Toast.LENGTH_SHORT).show();
                            GetData(url.GetData());
                        }else{
                            Toast.makeText(RFIDReportActivity.this, "Delete error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RFIDReportActivity.this, "Error Delete in Php", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("item_code", itemcode);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
