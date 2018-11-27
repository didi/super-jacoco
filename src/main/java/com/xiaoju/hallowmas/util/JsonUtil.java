package com.xiaoju.hallowmas.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;


/**
 * @ClassName: JsonUtil
 * @Description: 获取json字符串：将指定对象转换成json字符串；
 * 				   解析json字符串：从json字符串中解析出需要的key的值
 * 				  说明：考虑到性能测试多线程情况下的使用，该工具类的方法都定义为非static的
 */
public class JsonUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtil.class);

	/**
	 * 将BaseTaxiParameters对象转换成json字符串
	 * @param parameters 自定义的BaseTaxiParameters类型的对象（包括其子类对象）
	 * @return json字符串
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * */
	private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // 为保持对象版本兼容性,忽略未知的属性
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 序列化的时候，跳过null值
        mapper.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
    }

    /**
     * 从某个指定对象转成JSON字符串
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String toJSONStr(T t) {
        String json = "";
        if (t != null) {
            try {
                json = mapper.writeValueAsString(t);
            } catch (IOException e) {
                LOG.warn("Failed to convert json : " + e.getMessage());
            }
        }

        return json;
    }
    /**
     * 将一个对象编码为json字符串
     *
     * @param obj
     *            ,if null return "null" 要编码的字符串
     * @return json字符串
     * @throws RuntimeException
     *             若对象不能被编码为json串
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("error encode json for " + obj, e);
        }
    }

    /**
     * 将一个对象编码成字节
     *
     * @param obj
     * @return
     */
    public static byte[] toBytes(Object obj) {

        try {
            return mapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            throw new RuntimeException("error encode json for " + obj, e);
        }
    }

    /**
     * 将一个json字符串解码为java对象
     *
     * 注意：如果传入的字符串为null，那么返回的对象也为null
     *
     * @param json
     *            json字符串
     * @param cls
     *            对象类型
     * @return 解析后的java对象
     * @throws RuntimeException
     *             若解析json过程中发生了异常
     */
    public static <T> T toObject(String json, Class<T> cls) {
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, cls);
        } catch (Exception e) {
            throw new RuntimeException("error decode json to " + cls, e);
        }
    }

    /**
     * 将json字节解码为java对象
     *
     * @param jsonBytes
     *            json字节
     * @param cls
     *            对象类型
     * @return 解码后的对象
     */
    public static <T> T toObject(byte[] jsonBytes, Class<T> cls) {
        try {
            return mapper.readValue(jsonBytes, cls);
        } catch (Exception e) {
            throw new RuntimeException("error decode json to " + cls);
        }
    }

    /**
     * 将json字节解码为java对象
     *
     * @param json
     * @param typeReference
     * @param <T>
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static <T> T toObject(String json, TypeReference typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("error decode json to " + typeReference);
        }
    }

	/**
	 * 将Object对象转换成json字符串
	 * @param object  可以是map，list，或其他对象
	 * @return json字符串
	 *
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * */
	public static String  getJsonStrFromObject(Object object)
			throws JsonGenerationException, JsonMappingException, IOException {
		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, object);
		return writer.toString();
	}	
	
	/**
	 * 解析json字符串中指定key的value
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param key
	 *            json字符串中的key
	 * 
	 * @return List<Object> key对应的value列表 （有时候相同key对应的value不止一个）
	 * 
	 */
    public static List<Object> parseJsonStr(String jsonString, String key) {
        //存放jsonStr中，指定key的value
        List<Object> values = new ArrayList<Object>();
		if (jsonString != null || key != null) {
			if (jsonString.startsWith("[")) {
				JSONArray jsonArr = JSONArray.fromObject(jsonString);
                getValueFromJSONArray(values, jsonArr, key);
			} else if(jsonString.startsWith("{")){//非json数组
				JSONObject jsonObj = JSONObject.fromObject(jsonString);
                getValueFromJSONObject(values, jsonObj, key);
			}
		}
		return values;
	}

	/**
	 * 递归解析JSONArray中的指定key
	 * */
    private static void getValueFromJSONArray(List<Object> values, JSONArray jsonArr, String key) {
		for (int i = 0; i < jsonArr.size(); i++) {
			Object obj = jsonArr.get(i);
			if (obj.toString().startsWith("[")) {
				JSONArray subArr = JSONArray.fromObject(obj);
                getValueFromJSONArray(values, subArr, key);
			} else if(obj.toString().startsWith("{")) {//非json数组
				JSONObject subObj = JSONObject.fromObject(obj);
                getValueFromJSONObject(values, subObj, key);
			}
		}
	}

	/**
	 * 递归解析JSONObject中的指定key
	 * */
    private static void getValueFromJSONObject(List<Object> values, JSONObject jsonObj, String key) {
        //存放jsonStr中，指定key的value
        Object value = null;
		if (jsonObj.containsKey(key)) {//json字符串的查找，终结于所有的JSONObject,找到后即可返回
			value = jsonObj.get(key);
			values.add(value);
			return;
		}
		Set keys = jsonObj.keySet();
		Iterator<String> keyIt = keys.iterator();
		while (keyIt.hasNext()) {
			String onekey = keyIt.next();
			Object vObj = jsonObj.get(onekey);
			if (vObj.toString().startsWith("[")) {
				JSONArray subArr = JSONArray.fromObject(vObj);
                getValueFromJSONArray(values, subArr, key);
			} else if(vObj.toString().startsWith("{")){//非json数组
				
				JSONObject subJs = JSONObject.fromObject(vObj);
                getValueFromJSONObject(values, subJs, key);
			}
		}
	}

    public static Map parserToMap(String s){
        Map map=new HashMap();
        JSONObject json=JSONObject.fromObject(s);
        Iterator keys=json.keys();
        while(keys.hasNext()){
            String key=(String) keys.next();
            String value=json.get(key).toString();
            if(value.startsWith("{")&&value.endsWith("}")){
                map.put(key, parserToMap(value));
            }else{
                map.put(key, value);
            }

        }
        return map;
    }

}