package marianilga.projecttest;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "myLogs";
    private TextView textResult;
    private WebView webView;
    private LinearLayout layout;

    private final String yourText = "apptest.com/i?id=";
    private final String address = "http://devtest.ad-sys.com/c/apptest?id=";
    private String id = "";

    private List<String> messages  = new ArrayList<String>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button butStart = (Button)findViewById(R.id.butStart);
        butStart.setOnClickListener(this);
        textResult = (TextView)findViewById(R.id.result);
        layout = (LinearLayout)findViewById(R.id.layout);
        webView = new WebView(this);

    }


    @Override
    public void onClick(View v) {
        // Get all messages.
        messages = getMessages();

        //  Find a necessary text into a message.
            for (String sms : messages) {
                id = getValue(sms, yourText);
                if (!id.isEmpty()) {
                    break;
                }
            }

         if (id.isEmpty()) {
            textResult.setText("Not found");
        }
        else {
             toCall(address + id);
         }
    }


    // Method returns a list of all messages.
    private List<String> getMessages(){

        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor c = this.getContentResolver().query(uriSms, null, null, null, null);
        startManagingCursor(c);

        List<String> mess = new ArrayList<String>();

        if (c.moveToFirst()){
            do {
                mess.add(c.getString(c.getColumnIndex("body")));
            }
            while(c.moveToNext());
        }
        return mess;
    }



    // Method finds a value after required text in the string.
    private String getValue(String textsms, String needText){

        String value = "";

        // Check if there is required text in our text.
        int indexFirst = textsms.indexOf(needText);
        if (indexFirst == -1) return "";

        indexFirst += needText.length();

        // Check if the char is alphanumeric.
        for (int i = indexFirst; i < textsms.length(); i++){
            char ch = textsms.charAt(i);
            boolean ifAlphanumeric = ((ch >= 65)&& (ch <= 90))  ||
                                     ((ch >= 97)&& (ch <= 122)) ||
                                     ((ch >= 48)&& (ch <= 57));

            if (ifAlphanumeric){
                value = value + ch;
            }
            else {
                break;
            }
        }

        return value;
    }


    // Method calls to address
    private void  toCall(String pageAddress) {

        webView.loadUrl(pageAddress);
        layout.addView(webView);
    }
}
