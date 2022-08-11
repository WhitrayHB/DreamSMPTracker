package cn.whitrayhb.dsmptracker.command;

import cn.whitrayhb.dsmptracker.DSMPTrackerMain;
import cn.whitrayhb.dsmptracker.data.DreamSMPTracker;
import com.google.gson.stream.JsonReader;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Stream extends JRawCommand {
    public static final Stream INSTANCE = new Stream();

    private Stream() {
        super(DSMPTrackerMain.INSTANCE,"stream", "查询直播", "直播状态");
        setUsage("/stream"); // 设置用法，这将会在 /help 中展示
        setDescription("获取DreamSMP成员直播状态"); // 设置描述，也会在 /help 中展示
        setPrefixOptional(false); // 设置指令前缀是可选的，即使用 `test` 也能执行指令而不需要 `/test`
    }
    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {
        sender.sendMessage(buildMessage());
    }
    public String buildMessage(){
        StringBuilder message = new StringBuilder();
        int num = DreamSMPTracker.decodeJSON();
        switch (num){
            case -1:
                return "Error occurred when fetching streams";
            case -2:
                return "Error occurred when decoding JSON";
            case 0:
                message.append("现在没有主播在直播");
                return message.toString();
            case 1:
                message.append("现在只有"+DreamSMPTracker.getStreamer(0)+"在"+DreamSMPTracker.getType(0)+"上直播，有"+DreamSMPTracker.getViewers(0)+"人观看\n");
                return message.toString();
        }
        if(num > 1){
            message.append("现在有"+num+"位主播在直播,分别是:\n");
            for(int i=0;i<num;i++){
                message.append(DreamSMPTracker.getStreamer(i)+"，在"+DreamSMPTracker.getType(i)+"上有"+DreamSMPTracker.getViewers(i)+"人观看\n");
            }
        return message.toString();
        }else{
            return null;
        }
    }
    public void initialize(){
        DreamSMPTracker.decodeJSON();
    }
}
