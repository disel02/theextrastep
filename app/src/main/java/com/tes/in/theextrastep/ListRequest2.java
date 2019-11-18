package com.tes.in.theextrastep;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class ListRequest2 extends StringRequest{

    private static final String LOGIN_REQUEST_URL="https://diselkamble9.000webhostapp.com/students/topics.php";
    private Map<String,String> params;

    public ListRequest2(String std,String subject, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("std",std);
        params.put("subject",subject);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}