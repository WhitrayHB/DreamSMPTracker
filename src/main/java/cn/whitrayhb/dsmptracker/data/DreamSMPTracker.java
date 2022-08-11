package cn.whitrayhb.dsmptracker.data;

import cn.whitrayhb.dsmptracker.DSMPTrackerMain;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DreamSMPTracker {
    static int[] id;
    static String[] streamer;
    static int[] viewers;
    static String[] type;

    public static int getID(int i) {
        return id[i];
    }
    public static int getViewers(int i) {
        return viewers[i];
    }
    public static String getStreamer(int i){
        return  streamer[i];
    }
    public static String getType(int i){
        return type[i];
    }
    public static String fetchStreams(){
        HttpURLConnection conn = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder result = new StringBuilder();
        try{
            java.net.URL url = new URL("https://dreamsmp.com/streams.json");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(60000);
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();
            String l;
            if(conn.getResponseCode()==200){
                is = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                while ((l=br.readLine())!=null){
                    result.append(l);
                    return l;
                }
            }
            else{
                DSMPTrackerMain.INSTANCE.getLogger().info("Exception occured with response code "+conn.getResponseCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static int decodeJSON(){
        String l = fetchStreams();
        if(l==null) return -1;//fetchStream时出错
        int count = 0;
        id = new int[30];
        streamer = new String[30];
        viewers = new int[30];
        type = new String[30];
        DSMPTrackerMain.INSTANCE.getLogger().debug(l);
        try{
            JsonReader reader = new JsonReader(new StringReader(l));
            reader.beginArray();
            while(reader.hasNext()){
                reader.beginObject();
                while(reader.hasNext()){
                    String tagName = reader.nextName();
                    switch (tagName){
                        case "id" :
                            id[count] = reader.nextInt();
                            break;
                        case "username" :
                            streamer[count] = reader.nextString();
                            break;
                        case "viewers" :
                            viewers[count] = reader.nextInt();
                            break;
                        case "type" :
                            type[count] = reader.nextString();
                            break;
                    }
                }
                reader.endObject();
                count++;
            }
            reader.endArray();
        }catch(Exception e){
            e.printStackTrace();
            return -2;//解码失败
        }
        return count;//成功解码
    }
}
