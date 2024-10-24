/*
 * 这个类用来管理本地Json文件
 * 属性：
 *      1、name：以name为索引
 * 方法：
 *      1、add(JSONObject json, String time, String content)
 *          参数：json对象、时间、内容
 *          描述：这个方法以name为索引，向json对象中加入新内容
 */

package log;

import org.json.JSONArray;
import org.json.JSONObject;

public class Manager {
    private String name;
    private JSONObject json;

    public Manager(String name, JSONObject json) {
        this.name = name;
        this.json = json;
    }

    public JSONObject add(String time, String content) {
        JSONArray data = json.getJSONArray("data");
        JSONObject user;
        boolean flag = true;
        for (int i = 0; i < data.length(); i++) {
            user = new JSONObject(data.get(i).toString());
            if (user.getString("userName").equals(name)) {
                user.getJSONArray("log")
                        .put(new JSONObject("{\"time\":\"" + time + "\",\"content\":\"" + content + "\"}"));
                flag = false;
                data.remove(i);
                data.put(user);
                break;
            }
        }
        if (flag) {
            user = new JSONObject(
                    "{\"userName\": \"" + name + "\",\"log\": [{\"time\": \"" + time
                            + "\",\"content\": \"" + content + "\"}]}");
            data.put(user);
        }
        return json;
    }
}