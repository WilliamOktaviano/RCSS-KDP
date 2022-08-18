package com.kenaridjaja.rcss;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Connection con;
    Spinner spinner;
    AutoCompleteTextView siteid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText custnumber = findViewById(R.id.editCustNumber);
        TextView custname = findViewById(R.id.tvCustName);
        siteid = findViewById(R.id.actvSiteID);
        EditText itemnumber = findViewById(R.id.editItemNumber);
        TextView itemname = findViewById(R.id.tvItemName);
        TextView quantity = findViewById(R.id.tvQty);
        TextView pricelist = findViewById(R.id.tvPrice);
        Button process = findViewById(R.id.btnSelect);
        TextView msg = findViewById(R.id.msg);
        spinner = findViewById(R.id.spinnerSiteID);

        FillSpinnerSiteID();


        process.setOnClickListener(v -> {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            con = connectionHelper.ConnectionClass();
            try {
                if (con != null) {
                    String Q = "Select * from dbo.rcssmasterdb Where CUST_NUMBER=" +
                            "'2200001'AND ITEM_NUMBER='951000000'";
                    Statement statement = con.createStatement();
                    ResultSet result = statement.executeQuery(Q);
                    msg.setText("Query Executed");

                    while (result.next()) {
                        custnumber.setText(result.getString(1));
                        custname.setText(result.getString(2));
                        siteid.setText(result.getString(3));
                        itemnumber.setText(result.getString(4));
                        itemname.setText(result.getString(5));
                        quantity.setText(result.getString(6));
                        pricelist.setText(result.getString(7));
                    }
                } else {
                    msg.setText("Error in Connection");
                }
            } catch (Exception ex) {
                Log.e("Set Error", ex.getMessage());
            }
        });
    }

    public void FillSpinnerSiteID() {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            con = connectionHelper.ConnectionClass();

            String Q = "SELECT DISTINCT [SITE] FROM dbo.rcssmasterdb";
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(Q);

            ArrayList<String> data = new ArrayList<>();
            data.add(0, "Pilih Site ID!");
            while (result.next()) {
                String siteid = result.getString("SITE");
                data.add(siteid);
            }
            ArrayAdapter<String> dataAdapter;
            dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, data);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            siteid.setThreshold(1);
            siteid.setAdapter(dataAdapter);
//            spinner.setAdapter(dataAdapter);
//            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    if(parent.getItemAtPosition(position).equals("Pilih Site ID!"))
//                    {
//
//                    }
//                    else{
//                        String item = parent.getItemAtPosition(position).toString();
//                        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent){
//                    //TODO Auto-generated method stub
//                }
//            });

            siteid.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(parent.getItemAtPosition(position).equals("Pilih Site ID!"))
                    {

                    }
                    else{
                        String item = parent.getItemAtPosition(position).toString();
                        Toast.makeText(parent.getContext(), "A: " + item, Toast.LENGTH_SHORT).show();
                    }

                }

//                @Override
//                public void onNothingSelected(AdapterView<?> parent){
//                    //TODO Auto-generated method stub
//                }
            });




        } catch (Exception ex) {
            Log.e("Set Error", ex.getMessage());
        }
    }

    public void hideKeyPad(Activity activity, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}