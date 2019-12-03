package com.s1h.in.theextrastep;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class TopicRequest extends StringRequest{

    private static final String LOGIN_REQUEST_URL="http://disel.site/theextrastep/topicfee.php";
    private Map<String,String> params;

    public TopicRequest(String std, String subject,String topicnew, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("std",std);
        params.put("subject",subject);
        params.put("topic",topicnew);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
