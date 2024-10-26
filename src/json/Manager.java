/*
 * 这个类用来管理本地Json文件
 * 属性：
 *      1、name：以name为索引
 * 方法：
 *      1、add(JSONObject json, String time, String content)
 *          参数：json对象、时间、内容
 *          描述：这个方法以name为索引，向json对象中加入新内容
 */

package json;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;

public class Manager {
    public JSONObject json;

    public Manager() {

    }

    public Manager(String name, String time, String content) {
        json = new JSONObject(
                "{\"data\":[{\"log\":[{\"time\":\"" + time + "\",\"content\":\"" + content + "\"}],\"userName\":\""
                        + name + "\"}]}");
    }

    // 检查Json
    public void check(String fileName) {
        // 文件操作相关的类
        File folder = new File("resources");
        File log = new File("resources/" + fileName);
        FileOutputStream writeJson;
        try {
            if (!log.exists()) {
                synchronized (this) {
                    if (!log.exists()) {
                        folder.mkdir();
                        log.createNewFile();
                        writeJson = new FileOutputStream("resources/" + fileName);
                        writeJson.write("{\"data\":[]}".getBytes());
                        writeJson.close();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    // 提取时间
    public void extract() {
        // 原始数据，data数组
        JSONArray data = json.getJSONArray("data");

        // 临时data数组
        JSONArray tempDataArray = new JSONArray();

        // 提取
        for (int i = 0; i < data.length(); i++) {

            // 保存单条data数据
            tempDataArray.put(
                    (new JSONObject()
                            .put("userName", new JSONObject(data.getJSONObject(i).toString()).getString("userName")))
                            .put("log", new JSONArray()
                                    .put(data.getJSONObject(i).getJSONArray("log").getJSONObject(
                                            data.getJSONObject(i).getJSONArray("log").length() - 1))));
        }
        JSONObject resultJson = new JSONObject();
        resultJson.put("data", tempDataArray);
        json = resultJson;
    }

    // 合并两个json
    public void merge(JSONObject j) {
        JSONArray jData = j.getJSONArray("data");
        JSONArray data = json.getJSONArray("data");
        JSONObject user, jUser;
        boolean flag = true;
        for (int i = 0; i < jData.length(); i++) {
            jUser = new JSONObject(jData.getJSONObject(i).toString());
            for (int k = 0; k < data.length(); k++) {
                user = new JSONObject(data.getJSONObject(k).toString());
                if (jUser.getString("userName").equals(user.getString("userName"))) {
                    for (int l = 0; l < jUser.getJSONArray("log").length(); l++) {
                        user.getJSONArray("log").put(jUser.getJSONArray("log").getJSONObject(l));
                    }
                    data.remove(k);
                    data.put(user);
                    k--;
                    flag = false;
                    break;
                }
            }
            if (flag) {
                data.put(jUser);
            } else {
                flag = true;
            }
        }
    }

    // 读Json
    public JSONObject read(String fileName) {
        FileInputStream readJson;
        byte[] buf = new byte[10000];
        try {
            readJson = new FileInputStream("resources/" + fileName);
            readJson.read(buf);
            readJson.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        this.json = new JSONObject(new String(buf));
        return json;
    }

    // 写Json
    public void write(String fileName) {
        FileOutputStream writeJson;
        try {
            writeJson = new FileOutputStream("resources/" + fileName);
            writeJson.write(json.toString(4).getBytes());
            writeJson.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}