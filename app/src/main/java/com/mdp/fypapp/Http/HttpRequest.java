package com.mdp.fypapp.Http;

import com.google.gson.Gson;
import com.mdp.fypapp.Model.Chat;
import com.mdp.fypapp.Model.Result;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

public class HttpRequest {
    public static Chat sendMessage(String message){
        Chat chatMessage = new Chat();
        String gsonResult = doGet(message);
        if (gsonResult != null){
            try{
                JSONObject object = new JSONObject(gsonResult);
                String result = object.getString("response");
                System.out.println(result);
                chatMessage.setMessage(result);
            }
            catch (Exception e){
                chatMessage.setMessage("Request error...");
            }
        }
        chatMessage.setDate(new Date());
        chatMessage.setType(Chat.Type.INCOUNT);
        return chatMessage;
    }

    public static String doGet(String message){
        String result = "";
        String url = setParmat(message);
        System.out.println("---url="+url);
        InputStream input = null;
        ByteArrayOutputStream output = null;
        try{
            URL urls = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urls.openConnection();

            connection.setReadTimeout(20*1000);
            connection.setConnectTimeout(20*1000);
            connection.setRequestMethod("GET");
            input = connection.getInputStream();
            output = new ByteArrayOutputStream();
            int len = -1;
            byte[] buff = new byte[1024];
            while ((len = input.read(buff)) != -1) {
                output.write(buff, 0, len);
            }
            output.flush();
            result = new String(output.toByteArray());

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output .close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private static String setParmat(String message) {
        String url = "";
        try {
            url =BotInfo.url + "/" + BotInfo.action_type[0] + "?key=" + BotInfo.bot_key + "&ask="
                    + URLEncoder.encode(message, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
