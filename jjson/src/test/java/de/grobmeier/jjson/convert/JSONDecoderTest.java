/*
 *  Copyright 2007 Christian Grobmeier 
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at 
 *  
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *  
 *  Unless required by applicable law or agreed to in writing, 
 *  software distributed under the License is distributed 
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 *  either express or implied. See the License for the specific 
 *  language governing permissions and limitations under the License.
 */
package de.grobmeier.jjson.convert;

import de.grobmeier.jjson.JSONObject;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import de.grobmeier.jjson.JSONValue;
import de.grobmeier.jjson.convert.JSONDecoder;

import java.util.Map;

public class JSONDecoderTest {
    @Test
    public final void testDecodeBoolean() {
        JSONDecoder decoder = 
            new JSONDecoder("[null,true,12]");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("[null,true,12]", result.toJSON());
    }
    
    @Test
    public final void testDecodeBoolean2() {
        JSONDecoder decoder = 
            new JSONDecoder("[null,false,12]");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("[null,false,12]", result.toJSON());
    }
    
    @Test
    public final void testDecodeBoolean3() {
        JSONDecoder decoder = 
            new JSONDecoder("[null,true,12,false,\"BLA\"]");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("[null,true,12,false,\"BLA\"]", result.toJSON());
    }
    
    @Test
    public final void testDecodeNull4() {
        JSONDecoder decoder = 
            new JSONDecoder("[null,12]");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("[null,12]", result.toJSON());
    }
    
    @Test
    public final void testDecodeNull() {
        JSONDecoder decoder = 
            new JSONDecoder("[null]");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("[null]", result.toJSON());
    }
    
    @Test
    public final void testDecodeNull2() {
        JSONDecoder decoder = 
            new JSONDecoder("null");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("null", result.toJSON());
    }
    
    
    @Test
    public final void testDecodeNull3() {
        JSONDecoder decoder = 
            new JSONDecoder("[null,null]");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("[null,null]", result.toJSON());
    }
    
    @Test
    public final void testDecodeSimpleString() {
        JSONDecoder decoder = new JSONDecoder("\"hello\"");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("\"hello\"",result.toJSON());
    }
    
    @Test
    public final void testDecodeString() {
        JSONDecoder decoder = new JSONDecoder("\"hello, its \\\"ME\\\" again!\"");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("\"hello, its \\\"ME\\\" again!\"",result.toJSON());
    }
    
    @Test
    public final void testDecodeObject() {
        JSONDecoder decoder = new JSONDecoder("{\"key\":\"value\"}");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("{\"key\":\"value\"}", result.toJSON());
    }

    @Test
    public final void testDecodeObject2() {
        JSONDecoder decoder = 
            new JSONDecoder("{\"key\":\"value\",\"key2\":\"value2\"}");
        JSONValue result = decoder.decode();
        TestCase.assertEquals(
                "{\"key2\":\"value2\",\"key\":\"value\"}", 
                result.toJSON());
    }
    
    @Test
    public final void testDecodeObject3() {
        JSONDecoder decoder = 
            new JSONDecoder("{\"key\":\"value\",\"key2\":{\"key3\":\"value2\"}}");
        JSONValue result = decoder.decode();
        TestCase.assertEquals(
                "{\"key2\":{\"key3\":\"value2\"},\"key\":\"value\"}", 
                result.toJSON());
    }
    
    @Test
    public final void testDecodeObject4() {
        JSONDecoder decoder = 
            new JSONDecoder("{\"key\":\"value\",\"key2\":{\"key3\":\"value2\"},\"key5\":true}");
        JSONObject result = (JSONObject)decoder.decode();

        Map<String, JSONValue> value = result.getValue();

        Assert.assertEquals("{\"key3\":\"value2\"}", value.get("key2").toJSON());
        Assert.assertEquals("true", value.get("key5").toJSON());
        Assert.assertEquals("\"value\"", value.get("key").toJSON());
    }
    
    @Test
    public final void testDecodeArray() {
        String orig = "[\"key\",\"key2\"]";
        JSONDecoder decoder = new JSONDecoder(orig);
        JSONValue result = decoder.decode();
        TestCase.assertEquals(orig, result.toJSON());
    }
    
    @Test
    public final void testDecodeArrayWithObjects() {
        JSONDecoder decoder = 
            new JSONDecoder("[\"key1\",\"key2\",{\"key3\":\"value3\"}]");
        JSONValue result = decoder.decode();
        TestCase.assertEquals(
                "[\"key1\",\"key2\",{\"key3\":\"value3\"}]", 
                result.toJSON());
    }
    
    @Test
    public final void testDecodeArrayWithNumber() {
        JSONDecoder decoder = 
            new JSONDecoder("[\"key1\",\"key2\",12345]");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("[\"key1\",\"key2\",12345]", result.toJSON());
    }
    
    @Test
    public final void testDecodeArrayWithNumber2() {
        JSONDecoder decoder = 
            new JSONDecoder("[\"key1\",\"key2\",12345.56]");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("[\"key1\",\"key2\",12345.56]", result.toJSON());
    }
    
    @Test
    public final void testDecodeArrayWithNumber3() {
        JSONDecoder decoder = 
            new JSONDecoder("[\"key1\",\"key2\",+12345.56]");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("[\"key1\",\"key2\",12345.56]", result.toJSON());
    }
    
    @Test
    public final void testDecodeArrayWithNumber4() {
        JSONDecoder decoder = 
            new JSONDecoder("[\"key1\",\"key2\",-12345.56]");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("[\"key1\",\"key2\",-12345.56]", result.toJSON());
    }
    
    @Test
    public final void testDecodeArrayWithNumber5() {
        JSONDecoder decoder = 
            new JSONDecoder("[null,12,15]");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("[null,12,15]", result.toJSON());
    }
    
    @Test
    public final void testDecodeArrayWithNumber6() {
        JSONDecoder decoder = 
            new JSONDecoder("[\"key1\",\"key2\",-12345.56,\"bla\"]");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("[\"key1\",\"key2\",-12345.56,\"bla\"]", result.toJSON());
    }
    
    @Test
    public final void testDecodeArrayWithNumber7() {
        JSONDecoder decoder = 
            new JSONDecoder("[\"key1\",\"key2\",-12345.56,\"bla\",null]");
        JSONValue result = decoder.decode();
        TestCase.assertEquals("[\"key1\",\"key2\",-12345.56,\"bla\",null]", result.toJSON());
    }
    
    @Test
    public final void testEmptyArray() {
    	String json = "{\"total\":0,\"data\":[]}";
    	JSONDecoder decoder = new JSONDecoder(json);
    	JSONValue result = decoder.decode();
    	TestCase.assertEquals(json, result.toJSON());
    }

    @Test
    public final void testEmptySpace() {
    	String json = "{\"total\":123, \"data\":[{\"text\":\"test :-) \"},{\"a.name\":\"hello\"}]}";
    	JSONDecoder decoder = new JSONDecoder(json);
    	JSONValue result = decoder.decode();
    	// Attention: blanks between object members are not supported in plain json - there they are not generated in toJSON
    	TestCase.assertEquals("{\"total\":123,\"data\":[{\"text\":\"test :-) \"},{\"a.name\":\"hello\"}]}", result.toJSON());
    }
    
    @Test 
    public final void testNestedArraySymbols() {
    	String json ="\"test[]test\"";
    	JSONDecoder decoder = new JSONDecoder(json);
    	JSONValue result = decoder.decode();
    	TestCase.assertEquals(json, result.toJSON());
    }
    
    @Test 
    public final void testNestedObjectSymbols() {
    	String json ="\"test{}te{[]}st\"";
    	JSONDecoder decoder = new JSONDecoder(json);
    	JSONValue result = decoder.decode();
    	TestCase.assertEquals(json, result.toJSON());
    }
    
    @Test 
    public final void testSomething() {
    	String json ="{\"roles\":{}}";
    	JSONDecoder decoder = new JSONDecoder(json);
    	JSONValue result = decoder.decode();
    	TestCase.assertEquals(json, result.toJSON());
    }
}
