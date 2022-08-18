package com.kenaridjaja.rcss;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {

    @SuppressLint("RCSSApi")
    public Connection ConnectionClass() {
        String ip = "193.68.114.34", port = "1433", dbname = "RCSS", dbuser = "sa", dbpass = "!Q2w#E4r";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = null;
        String connectionUrl;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + dbname + ";User=" + dbuser + ";password=" + dbpass + ";";
            connection = DriverManager.getConnection(connectionUrl);
        } catch (Exception ex) {
            Log.e("Set Error", ex.getMessage());
        }
        return connection;


    }
}
