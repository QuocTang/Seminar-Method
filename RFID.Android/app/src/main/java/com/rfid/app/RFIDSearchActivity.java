package com.rfid.app;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.UHFTAGInfo;

import java.util.ArrayList;
import java.util.HashMap;


public class RFIDSearchActivity extends AppCompatActivity {
    private Button btnFind, btnBack, btnClear;
    private TextView tv_count;
    private EditText edtSearch;
    private ImageView imgArrow;
    private boolean loopFlag = false;
    private HashMap<String, String> map;
    private ArrayList<HashMap<String, String>> tagList;
    private ListView LvTags;
    private Handler handler;

    private SimpleAdapter adapter;

    private RFIDWithUHFUART mReader;
    private int inventoryFlag = 1;
    private boolean fIsEmulator = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfid_search);

        tagList = new ArrayList<HashMap<String, String>>();

        btnFind     = findViewById(R.id.BtFindS);
        btnBack     = findViewById(R.id.BtBack);
        btnClear    = findViewById(R.id.BtClearS);
        tv_count    = findViewById(R.id.tv_countS);
        LvTags      = findViewById(R.id.LvTagsS);
        edtSearch   = (EditText) findViewById(R.id.searchId);
        imgArrow    =  findViewById(R.id.imgArrow);



        btnFind.setOnClickListener(new BtFindClickListener());
        btnBack.setOnClickListener(new BtBackClickListener());
        btnClear.setOnClickListener(new BtClearClickListener());

        adapter = new SimpleAdapter(this, tagList, R.layout.listtag_items,
                new String[]{"tagUii", "tagLen", "tagCount"},
                new int[]{R.id.TvTagUii, R.id.TvTagLen, R.id.TvTagCount});

        LvTags.setAdapter(adapter);
        clearData();

        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                String result = msg.obj + "";
                String[] strs = result.split("@");
                if (addEPCToList(strs[0], strs[1], String.valueOf(edtSearch.getText())))
                    UIHelper.playSoundSuccess();
            }
        };
        fIsEmulator = UIHelper.isEmulator();
        initUHF();
    }

    private class BtFindClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            readTag();
        }
    }

    private  class BtBackClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            finish();
        }
    }

    private class BtClearClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            imgArrow.setImageResource(R.drawable.nothing);
            imgArrow.clearAnimation();
            clearData();
        }
    }

    private void clearData() {
        tv_count.setText("0");
        tagList.clear();
        edtSearch.setText("");
        adapter.notifyDataSetChanged();
    }

    private boolean addEPCToList(String epc, String rssi, String id) {
        if (!TextUtils.isEmpty(epc)) {

            int index = checkIsExist(epc, id);

            map = new HashMap<String, String>();
            map.put("tagUii", epc);
            map.put("tagCount", String.valueOf(1));
            map.put("tagRssi", rssi);

            if (index == -1) {
                tagList.add(map);
                LvTags.setAdapter(adapter);
                tv_count.setText("" + adapter.getCount());
            } else {
                int tagcount = Integer.parseInt(tagList.get(index).get("tagCount"), 10) + 1;

                map.put("tagCount", String.valueOf(tagcount));
                tagList.set(index, map);
            }

            adapter.notifyDataSetChanged();
            if (index >= 0)
                return false;

            return true;
        }
        return false;
    }

    public void initUHF() {
        // temporary check this, on emulator device mReader InitTask cause crash application
        if (!fIsEmulator) {
            if (mReader == null) {
                try {
                    mReader = RFIDWithUHFUART.getInstance();
                } catch (Exception ex) {
                    UIHelper.showExceptionError(RFIDSearchActivity.this, ex);
                    return;
                }

                if (mReader != null) {
                    new InitTask().execute();
                }
            }
        }
    }
    private class InitTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                return mReader.init();
            }
            catch (Exception ex){
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            mypDialog.cancel();

            if (!result) {
                Toast.makeText(RFIDSearchActivity.this, "init fail", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            try {
                super.onPreExecute();

                mypDialog = new ProgressDialog(RFIDSearchActivity.this);
                mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mypDialog.setMessage("init...");
                mypDialog.setCanceledOnTouchOutside(false);
                mypDialog.show();

            } catch (Exception ex) {
                UIHelper.showExceptionError(RFIDSearchActivity.this, ex);
                return;
            }
        }
    }

    private void readTag() {
        if (btnFind.getText().equals("FIND"))
        {
            if (mReader == null) {
                UIHelper.ToastMessage(RFIDSearchActivity.this, R.string.uhf_msg_sdk_open_fail);
                return;
            }

//            switch (inventoryFlag) {
//                case 0:
//                {
//                    UHFTAGInfo strUII = mReader.inventorySingleTag();
//                    if (strUII != null) {
//                        String strEPC = strUII.getEPC();
//                        addEPCToList(strEPC, strUII.getRssi());
//                        UIHelper.playSoundSuccess();
//                        tv_count.setText("" + adapter.getCount());
//                    } else {
//                        UIHelper.ToastMessage(RFIDSearchActivity.this, R.string.uhf_msg_inventory_fail);
//                    }
//                }
//                break;
//                case 1://  .startInventoryTag((byte) 0, (byte) 0))
//                {
                    if (mReader.startInventoryTag()) {
                        btnFind.setText(getString(R.string.title_stop_Inventory));
                        loopFlag = true;
//                        setViewEnabled(false);
                        new TagThread().start();
                    } else {
                        mReader.stopInventory();
                        UIHelper.ToastMessage(RFIDSearchActivity.this, R.string.uhf_msg_inventory_open_fail);
                    }
//                }
//                break;
//                default:
//                    break;
//            }
        } else {
            stopInventory();
        }
    }

    private void stopInventory() {
        if (loopFlag) {
            loopFlag = false;
//            setViewEnabled(true);
            if (mReader.stopInventory()) {
                btnFind.setText("Start");
            } else {
                UIHelper.ToastMessage(RFIDSearchActivity.this, R.string.uhf_msg_inventory_stop_fail);
            }
        }
    }

    private class TagThread extends Thread {
        public void run() {
            String strTid;
            String strResult;
            UHFTAGInfo res = null;
            while (loopFlag) {
                res = mReader.readTagFromBuffer();
                if (res != null) {
                    strTid = res.getTid();
                    if (strTid.length() != 0 && !strTid.equals("0000000" + "000000000") && !strTid.equals("000000000000000000000000")) {
                        strResult = "TID:" + strTid + "\n";
                    } else {
                        strResult = "";
                    }

                    Message msg = handler.obtainMessage();
                    msg.obj = strResult + res.getEPC() + "@" + res.getRssi();

                    handler.sendMessage(msg);
                }
            }
        }
    }



    public int checkIsExist(String strEPC, String id) {
        int existFlag = -1;
        if (strEPC == null || strEPC.length() == 0) {
            return existFlag;
        }

        String tempStr = "";
        for (int i = 0; i < tagList.size(); i++) {
            HashMap<String, String> temp = new HashMap<String, String>();
            temp = tagList.get(i);
            tempStr = temp.get("tagUii");
            Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
            if (id.equals(tempStr)){
                Toast.makeText(this, "Found "+id, Toast.LENGTH_SHORT).show();
                UIHelper.playSoundSuccess();
                imgArrow.setImageResource(R.drawable.black_arrow);
                imgArrow.startAnimation(animAlpha);
            }else{
                imgArrow.clearAnimation();
                imgArrow.setImageResource(R.drawable.nothing);
            }

            if (strEPC.equals(tempStr)) {
                existFlag = i;
                break;
            }
        }
        return existFlag;
    }
}

