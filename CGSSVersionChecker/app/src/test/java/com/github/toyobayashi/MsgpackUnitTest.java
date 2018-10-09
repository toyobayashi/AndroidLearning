package com.github.toyobayashi;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MsgpackUnitTest {
    @Test
    public void testNull() throws Exception {
        byte[] buf = Msgpack.encode(null);
        Assert.assertTrue(JSONObject.NULL.equals(Msgpack.decode(buf)));
    }

    @Test
    public void testNumber() throws Exception {
        Integer a = 66;
        byte[] buf = Msgpack.encode(a);
        Integer d = Msgpack.decode(buf);
        Assert.assertEquals(a, d);
    }

    @Test
    public void testPrimitiveArray() throws Exception {
        int[] a = { 77, 88, 99 };
        byte[] buf = Msgpack.encode(a);
        JSONArray d = Msgpack.decode(buf);

        Assert.assertEquals(77, d.getInt(0));
        Assert.assertEquals(88, d.getInt(1));
        Assert.assertEquals(99, d.getInt(2));

        String[] b = { "hahaha", "hehehe" };
        byte[] buf2 = Msgpack.encode(b);
        d = Msgpack.decode(buf2);

        Assert.assertEquals("hahaha", d.getString(0));
        Assert.assertEquals("hehehe", d.getString(1));
    }

    @Test
    public void testJSONArray() throws Exception {
        JSONArray arr = new JSONArray();
        arr.put(1);
        arr.put("2");
        arr.put(JSONObject.NULL);
        byte[] buf = Msgpack.encode(arr);
        JSONArray d = Msgpack.decode(buf);

        Assert.assertEquals(1, d.getInt(0));
        Assert.assertEquals("2", d.getString(1));
        Assert.assertTrue(JSONObject.NULL.equals(d.get(2)));
    }

    @Test
    public void testJSONObject() throws Exception {
        JSONArray arr = new JSONArray();
        arr.put(1);
        arr.put("2");
        arr.put(JSONObject.NULL);
        JSONObject json = new JSONObject(new LinkedHashMap());

        JSONObject child = new JSONObject(new LinkedHashMap());
        child.put("arr", arr);

        json.put("child", child);
        json.put("abc", "123");
        byte[] buf = Msgpack.encode(json);
        JSONObject d = Msgpack.decode(buf);

        Assert.assertEquals("{\"child\":{\"arr\":[1,\"2\",null]},\"abc\":\"123\"}", d.toString());
    }
}