package com.example.chatgpt_api;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/*
* @Desc :
* @Author : xiaoyun
* @Created_Time : 2023/2/15 11:23
* @Project_Name : MainActivity.java
* @PACKAGE_NAME : com.example.chatgpt_api
* @Params : 
*/
public class MainActivity extends AppCompatActivity {
    public EditText input_content;
    public Button bt;
    public TextView tx;

    private ListView list;
    private List<Car> carList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_content = this.findViewById(R.id.shuru);
        bt = this.findViewById(R.id.chaxun);
        tx=this.findViewById(R.id.ans);
        list = this.findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Car car = carList.get(position);
                Toast.makeText(MainActivity.this, car.getCarname(), Toast.LENGTH_SHORT).show();
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cha = input_content.getText().toString();
                String da = decode(cha);
                Toast.makeText(MainActivity.this,da+"",Toast.LENGTH_SHORT).show();
                stream();
            }
        });
    }
    
    /*
    * @Desc : 封装请求函数
    * @Author : xiaoyun
    * @Created_Time : 2023/2/15 11:23
    * @Project_Name : MainActivity.java
    * @PACKAGE_NAME : com.example.chatgpt_api
    * @Params :
    */
    public void stream(){
        String url = "https://chatgpt.devbit.cn/conversation.php?q=" + input_content.getText().toString() + "&id=d742a891-22c0-449c-83ae-4a9e9173209b";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url1 = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    connection.setConnectTimeout(100000);
                    connection.setReadTimeout(100000);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    StringBuilder stringBuilder = new StringBuilder();
                    BufferedReader bufferedReader = null;
                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "GBK"));
                    String str=null;
                    while ((str = bufferedReader.readLine()) != null) {
                        stringBuilder.append(str);
                        String[] le = stringBuilder.toString().split("ata:");
                        JSONObject jsonObject = new JSONObject(le[le.length - 1]);
                        JSONObject mess = jsonObject.getJSONObject("message");
                        JSONObject content = mess.getJSONObject("content");
                        String parts = content.getJSONArray("parts").getString(0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                carList.clear();
                                Car car = new Car(decode(parts));
                                carList.add(car);
                                CarAdapter aaa = new CarAdapter(MainActivity.this, R.layout.car_item, carList);
                                list.setAdapter(aaa);
                            }
                        });
                    }
                    inputStream.close();
                    connection.disconnect();
                } catch (ProtocolException e) {
                    throw new RuntimeException(e);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }


    public void http(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Car car = new Car("思考中......");
                        carList.add(car);
                        CarAdapter carAdapter = new CarAdapter(MainActivity.this, R.layout.car_item, carList);
                        list.setAdapter(carAdapter);
                    }
                });
                String URL = "https://chatgpt.devbit.cn/conversation.php?q=" + input_content.getText().toString() + "&id=d742a891-22c0-449c-83ae-4a9e9173209b";
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(URL)
                        .get()
                        .build();
                JSONObject json=null;
                int i=0;
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String data = response.body().string();
                    JSONObject jsonObject = new JSONObject();
                    if (data.length() > 11) {
                        String[] split_data = data.split("data:");
                        Log.d("len123", split_data.length + "");
                        for (int j = 0; j < split_data.length; j++) {
                            Log.d("len123", split_data[j] + "j=" + j);
                        }
                        JSONObject jsonObject1 = new JSONObject(split_data[split_data.length - 2]);
                        JSONObject mess = jsonObject1.getJSONObject("message");
                        JSONObject content = mess.getJSONObject("content");
                        Log.d("len123", content + "");
                        String parts = content.getJSONArray("parts").getString(0);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                carList.clear();
                                Car car = new Car(decode(parts));
                                carList.add(car);
                                CarAdapter carAdapter = new CarAdapter(MainActivity.this, R.layout.car_item, carList);
                                list.setAdapter(carAdapter);
                            }
                        });

                    } else {
                        Log.d("123123",data.length()+"");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String cookie="";
            }
        }).start();
    }

    /*
    * @Desc : 输入流转输出流
    * @Author : xiaoyun
    * @Created_Time : 2023/2/15 16:10
    * @Project_Name : MainActivity.java
    * @PACKAGE_NAME : com.example.chatgpt_api
    * @Params :
    */
    private static String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        模板代码，必须熟练
        byte[] buffer = new byte[1024];
        int len=-1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer,0,len);
        }
        is.close();
        String state = os.toString();   //把流中的数据转换成字符串，采用的编码是utf-8(模拟器默认编码)
        os.close();
        return state;
    }

    /*
    * @Desc : Unicode转中文函数
    * @Author : xiaoyun
    * @Created_Time : 2023/2/15 13:51
    * @Project_Name : MainActivity.java
    * @PACKAGE_NAME : com.example.chatgpt_api
    * @Params :
    */
    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }

        StringBuffer retBuffer = new StringBuffer();
        int maxLoop = unicodeStr.length();

        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'u'))) {
                    try {
                        retBuffer.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuffer.append(unicodeStr.charAt(i));
                    }
                } else {
                    retBuffer.append(unicodeStr.charAt(i));
                }
            } else {
                retBuffer.append(unicodeStr.charAt(i));
            }
        }
        return retBuffer.toString();
    }


    /*
     * @Desc : 字符串转JSONObject
     * @Author : xiaoyun
     * @Created_Time : 2023/2/15 15:43
     * @Project_Name : MainActivity.java
     * @PACKAGE_NAME : com.example.chatgpt_api
     * @Params : str 传入的json格式字符串
     */
    public static JSONObject stringToJSONObject(String str) {
//        通过org.json.JSONObject类进行String转JSONObject
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }
}