package com.kenaridjaja.rcss;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kenaridjaja.rcss.Connection.ConnectionHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Connection con;
    AutoCompleteTextView siteid;
    AutoCompleteTextView custnumber;
    AutoCompleteTextView itemnumber;
    TextView custname;
    TextView itemname;
    TextView quantity;
    TextView price;

    EditText inputqty;

    TextView statQty;

    TextView msg;
    Button process;

    int qtyInput;

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            try {
                qtyInput = Integer.parseInt(inputqty.getText().toString());
                float qtyAvailable = Float.parseFloat(quantity.getText().toString());

                if (qtyInput > qtyAvailable) {
                    statQty.setText("Qty\nKurang");
                } else if (qtyInput <= qtyAvailable) {
                    statQty.setText("OK");
                }
            } catch (NumberFormatException nfe) {
                Toast.makeText(getApplicationContext(), "Qty Input harus diisi!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        custnumber = findViewById(R.id.actvCustNumber);
        custname = findViewById(R.id.tvCustName);
        siteid = findViewById(R.id.actvSiteID);
        itemnumber = findViewById(R.id.actvItemNumber);
        itemname = findViewById(R.id.tvItemName);
        quantity = findViewById(R.id.tvQtyAvailable);
        statQty = findViewById(R.id.statusQty);
        inputqty = findViewById(R.id.editQtyOrdered);
        price = findViewById(R.id.tvPrice);
        process = findViewById(R.id.btnNext);
        msg = findViewById(R.id.msg);

        inputqty.addTextChangedListener(textWatcher);
//        Button process = findViewById(R.id.btnSelect);
//        TextView msg = findViewById(R.id.msg);

        FillSpinnerSiteID();

        init();

        process.setOnClickListener(v -> {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            con = connectionHelper.ConnectionClass();
            try {
                if (con != null) {
                    startActivity(new Intent(MainActivity.this, ResultActivity.class));
//                    String Q = "Select * from dbo.rcssmasterdb Where CUST_NUMBER=" +
//                            "'2200001'AND ITEM_NUMBER='951000000'";
//                    Statement statement = con.createStatement();
//                    ResultSet result = statement.executeQuery(Q);
//                    msg.setText("Query Executed");
//
//                    while (result.next()) {
//                        siteid.setText(result.getString(1));
//                        custnumber.setText(result.getString(2));
//                        custname.setText(result.getString(3));
//                        itemnumber.setText(result.getString(4));
//                        itemname.setText(result.getString(5));
//                        quantity.setText(result.getString(6));
//                        pricelist.setText(result.getString(7));
                }
                else {
                msg.setText("Error in Connection");
                }
                } catch(Exception ex) {
                Log.e("Set Error", ex.getMessage());
            }});
    }
//        process.setOnClickListener(v -> {
//            ConnectionHelper connectionHelper = new ConnectionHelper();
//            con = connectionHelper.ConnectionClass();
//            try {
//                if (con != null) {
//                    String Q = "Select * from dbo.rcssmasterdb Where CUST_NUMBER=" +
//                            "'2200001'AND ITEM_NUMBER='951000000'";
//                    Statement statement = con.createStatement();
//                    ResultSet result = statement.executeQuery(Q);
//                    msg.setText("Query Executed");
//
//                    while (result.next()) {
//                        siteid.setText(result.getString(1));
//                        custnumber.setText(result.getString(2));
//                        custname.setText(result.getString(3));
//                        itemnumber.setText(result.getString(4));
//                        itemname.setText(result.getString(5));
//                        quantity.setText(result.getString(6));
//                        pricelist.setText(result.getString(7));
//                    }
//                } else {
//                    msg.setText("Error in Connection");
//                }
//            } catch (Exception ex) {
//                Log.e("Set Error", ex.getMessage());
//            }
//        });


    public void FillSpinnerSiteID() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            con = connectionHelper.ConnectionClass();

            String Q = "SELECT DISTINCT [SITE] FROM dbo.rcssmasterdb";
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(Q);
            ArrayList<String> data = new ArrayList<>();
            while (result.next()) {
                String siteid = result.getString("SITE");
                data.add(siteid);
            }
            ArrayAdapter<String> dataAdapter;
            dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, data);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            siteid.setThreshold(1); //start from first character of typing
            siteid.setAdapter(dataAdapter);
            siteid.setOnItemClickListener((parent, view, position, id) -> {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "A: " + item, Toast.LENGTH_SHORT).show();
                hideKeyPad();
                FillCustomerNumberChoice();

            });
        } catch (Exception ex) {
            Log.e("Set Error", ex.getMessage());
        }
    }

    public void FillCustomerNumberChoice() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            con = connectionHelper.ConnectionClass();

            String Q = "SELECT DISTINCT a.[CUST_NUMBER], a.[CUST_NAME] FROM (SELECT [SITE], [CUST_NUMBER], [CUST_NAME] FROM dbo.rcssmasterdb) a WHERE SITE = '" + siteid.getText() + "' ORDER BY [CUST_NUMBER];";
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(Q);
            ArrayList<String> data = new ArrayList<>();
            ArrayList<String> data2 = new ArrayList<>();
            while (result.next()) {
                String custnumber = result.getString("CUST_NUMBER");
                String custname = result.getString("CUST_NAME");
                data.add(custnumber);
                data2.add(custname);
            }

            ArrayAdapter<String> dataAdapter;
            dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, data);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            custnumber.setThreshold(1);
            custnumber.setAdapter(dataAdapter);
            custnumber.setOnItemClickListener((parent, view, position, id) -> {

                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "B: " + item, Toast.LENGTH_SHORT).show();
                custname.setText(data2.get(position));
                hideKeyPad();
                FillItemNumber();

            });
        } catch (Exception ex) {
            Log.e("Set Error", ex.getMessage());
        }
    }

    public void FillItemNumber() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            con = connectionHelper.ConnectionClass();

            String Q = "SELECT [ITEM_NUMBER], [ITEM_NAME], [QTY_AVAILABLE], [PRICE] FROM dbo.rcssmasterdb WHERE CUST_NUMBER ='" + custnumber.getText() + "';";
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(Q);

            ArrayList<String> data = new ArrayList<>();
            ArrayList<String> data2 = new ArrayList<>();
            ArrayList<String> data3 = new ArrayList<>();
            ArrayList<String> data4 = new ArrayList<>();

            while (result.next()) {
                String itemnumber = result.getString("ITEM_NUMBER");
                String itemname = result.getString("ITEM_NAME");
                String qtyavailable = result.getString("QTY_AVAILABLE");
                String price = result.getString("PRICE");
                data.add(itemnumber);
                data2.add(itemname);
                data3.add(qtyavailable);
                data4.add(price);
            }
            ArrayAdapter<String> dataAdapter;
            dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, data);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            itemnumber.setThreshold(1);
            itemnumber.setAdapter(dataAdapter);
            itemnumber.setOnItemClickListener((parent, view, position, id) -> {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "C: " + item, Toast.LENGTH_SHORT).show();
                itemname.setText(data2.get(position));
                quantity.setText(data3.get(position));
                price.setText(data4.get(position));
                hideKeyPad();

            });

        } catch (Exception ex) {
            Log.e("Set Error", ex.getMessage());
        }
    }

    public void init() {
        TableLayout stk = findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(" Sl.No ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" Product ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" Unit Price ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText(" Stock Remaining ");
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);
        for (int i = 0; i < 25; i++) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("" + i);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText("Product " + i);
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText("Rs." + i);
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            TextView t4v = new TextView(this);
            t4v.setText("" + i * 15 / 32 * 10);
            t4v.setTextColor(Color.WHITE);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);
            stk.addView(tbrow);
        }
    }

    public void hideKeyPad() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}