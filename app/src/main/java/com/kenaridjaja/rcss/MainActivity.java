package com.kenaridjaja.rcss;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText custnumber = findViewById(R.id.editCustNumber);
        TextView custname = findViewById(R.id.tvCustName);
        EditText siteid = findViewById(R.id.editSiteID);
        EditText itemnumber = findViewById(R.id.editItemNumber);
        TextView itemname = findViewById(R.id.tvItemName);
        TextView quantity = findViewById(R.id.tvQty);
        TextView pricelist = findViewById(R.id.tvPrice);
        Button process = findViewById(R.id.btnSelect);
        TextView msg = findViewById(R.id.msg);

        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection con = ConnectionClass();
                try {
                    if (con != null) {
                        String Q = "Select * from dbo.rcssmasterdb Where CUST_NUMBER=" +
                                "'2200001'AND ITEM_NUMBER='010518002'";
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
            }
        });
    }

    @SuppressLint("RCSSApi")
    public Connection ConnectionClass() {
        String ip = "193.68.114.34", port = "1433", dbname = "RCSS", dbuser = "sa", dbpass = "!Q2w#E4r";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = null;
        String connectURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + dbname + ";User=" + dbuser + ";password=" + dbpass + ";";
            connection = DriverManager.getConnection(connectionUrl);
        } catch (Exception ex) {
            Log.e("Set Error", ex.getMessage());
        }
        return connection;


    }
}