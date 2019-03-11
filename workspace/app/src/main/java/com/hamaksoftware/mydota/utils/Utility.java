package com.hamaksoftware.mydota.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.text.DecimalFormat;

import java.util.HashMap;
import java.util.Map;

public class Utility {
    private static Utility obj = null;
    private static Context ctx;
    private static PrettyTime prettyTime;
    private static Map<Integer,Integer> expTable;

    private Utility() {
        //Experience Table
        //http://dota2.gamepedia.com/Experience

        expTable = new HashMap<Integer, Integer>(0);

        expTable.put(1,0);
        expTable.put(2,200);
        expTable.put(3,300);
        expTable.put(4,400);
        expTable.put(5,500);
        expTable.put(6,600);
        expTable.put(7,600);
        expTable.put(8,600);
        expTable.put(9,1200);
        expTable.put(10,1000);
        expTable.put(11,600);
        expTable.put(12,2200);
        expTable.put(13,800);
        expTable.put(14,1400);
        expTable.put(15,1500);
        expTable.put(16,1600);
        expTable.put(17,1700);
        expTable.put(18,1800);
        expTable.put(19,1900);
        expTable.put(20,2000);
        expTable.put(21,2100);
        expTable.put(22,2200);
        expTable.put(23,2300);
        expTable.put(24,2400);
        expTable.put(25,2500);

        prettyTime = new PrettyTime();

    }

    public static Map<Integer,Integer> getExpTable(){
        return expTable;
    }


    public static Utility getInstance(Context context) {
        if (obj == null) {
            obj = new Utility();
            ctx = context;

        }
        ctx = context;

        return obj;
    }

    public static PrettyTime getPrettytime(){
        return prettyTime;
    }


    public static void showDialog(Context c, String title, String msg,
                                  String positiveBtnCaption, String negativeBtnCaption,
                                  boolean isCancelable, final IDialog target) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(isCancelable)
                .setPositiveButton(positiveBtnCaption,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                if (target != null)
                                    target.PositiveMethod(dialog, id);
                            }

                        }
                )
                .setNegativeButton(negativeBtnCaption,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (target != null)
                                    target.NegativeMethod(dialog, id);
                            }
                        }
                );

        AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.show();
        if (isCancelable) {
            alert.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    if (target != null)
                        target.NegativeMethod(null, 0);
                }
            });
        }
    }


    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static String streamToString(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    public static String getPrettyNum(double ttl) {
        final long KILOBYTE = 1000L;
        final long MEGABYTE = 1000L * 1000L;
        final long GIGABYTE = 1000L * 1000L * 1000L;

        String sd = "";
        DecimalFormat Currency = new DecimalFormat("#0.0");
        if (ttl / KILOBYTE < KILOBYTE) {
            sd = Currency.format((double) (ttl / KILOBYTE)) + "K";
        } else if ((ttl / MEGABYTE) > KILOBYTE) {
            sd = Currency.format((double) (ttl / GIGABYTE)) + "G";
        } else {
            sd = Currency.format((double) (ttl / MEGABYTE)) + "M";
        }
        return sd;
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public String getPrettyMin(String time){
        if(time.contains(".")){
            String[] s = time.split("\\.");
            double r = Double.parseDouble("." + s[1]);
            String m = ((int)(r * 60))+"";
            return (s[0].length() > 1? s[0] : "0" + s[0]) + ":"+ (m.length() > 1 ? m: ("0"+ m));
        }else{
            return time + ":00";
        }
    }

}
