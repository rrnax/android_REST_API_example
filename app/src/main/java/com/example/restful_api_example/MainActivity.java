package com.example.restful_api_example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private final String fakeApiUrl = "https://jsonplaceholder.typicode.com/";
    private Button showData;
    TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showData = (Button) findViewById(R.id.showData);
        data = (TextView) findViewById(R.id.data);

    }

    public void downloadDataFromApi(View view){
        ApiThread backThread = new ApiThread();
        new Thread(backThread).start();
    }

    class ApiThread implements Runnable{
        URL postEndpoint;
        String result;
        @Override
        public void run() {
            try {
                postEndpoint = new URL(new String(fakeApiUrl + "posts/1"));
                HttpsURLConnection connected = (HttpsURLConnection) postEndpoint.openConnection();

                connected.setRequestProperty("User-Agent", "rest-app-example-v1.0");
                connected.setRequestProperty("Contact", "rrnax@mat.umk.pl");
//                connected.setRequestMethod("POST");
//
//                String dataToSend = "message=hello";
//                connected.setDoOutput(true);
//                connected.getOutputStream().write(dataToSend.getBytes(StandardCharsets.UTF_8));

                if(connected.getResponseCode() == 200) {
                    InputStream responseBody = connected.getInputStream();
                    InputStreamReader responseReader = new InputStreamReader(responseBody, "UTF-8");
                    result = new BufferedReader(responseReader).lines().collect(Collectors.joining("\n"));
                    System.out.println(result);
//
//                    HttpResponseCache myCache = HttpResponseCache.install(getCacheDir(), 100000L);
//                    if (myCache.getHitCount() > 0) {
//                        System.out.println("Dziala");
//                    }

//                    InputStream in = connected.getInputStream();
//                    InputStreamReader isr = new InputStreamReader(in);
//                    int data = isr.read();
//                    while (data != -1) {
//                        result += (char) data;
//                        data = isr.read();
//                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            data.setText(result);
                        }
                    });
                }
//                connected.disconnect();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
