package com.kenaridjaja.rcss;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kenaridjaja.rcss.Connection.ConnectionHelper;

import org.w3c.dom.Text;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        custnumber = findViewById(R.id.actvCustNumber);
        custname = findViewById(R.id.tvCustName);
        siteid = findViewById(R.id.actvSiteID);
        itemnumber = findViewById(R.id.actvItemNumber);
        itemname = findViewById(R.id.tvItemName);
        TextView quantity = findViewById(R.id.tvQty);
        TextView pricelist = findViewById(R.id.tvPrice);
        Button process = findViewById(R.id.btnSelect);
        TextView msg = findViewById(R.id.msg);

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
                        siteid.setText(result.getString(1));
                        custnumber.setText(result.getString(2));
                        custname.setText(result.getString(3));
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

            String Q = "SELECT [ITEM_NUMBER], [ITEM_NAME] FROM dbo.rcssmasterdb WHERE CUST_NUMBER ='" + custnumber.getText() + "';";
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(Q);

            ArrayList<String> data = new ArrayList<>();
            ArrayList<String> data2 = new ArrayList<>();

            while (result.next()) {
                String itemnumber = result.getString("ITEM_NUMBER");
                String itemname = result.getString("ITEM_NAME");
                data.add(itemnumber);
                data2.add(itemname);
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
                hideKeyPad();
            });

        } catch (Exception ex) {
            Log.e("Set Error", ex.getMessage());
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