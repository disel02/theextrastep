package com.s1h.in.theextrastep;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class ListRequest extends StringRequest{

    private static final String LOGIN_REQUEST_URL="http://disel.site/theextrastep/subjects.php";
    private Map<String,String> params;

    public ListRequest(String list_key, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("std",list_key);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
