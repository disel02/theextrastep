package com.tes.in.theextrastep;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class VideoRequest extends StringRequest{

    private static final String LOGIN_REQUEST_URL="https://diselkamble9.000webhostapp.com/students/videos.php";
    private Map<String,String> params;

    public VideoRequest(String std_video, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("std_video",std_video);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
