package com.s1h.in.theextrastep;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class TopicPaymentRequest extends StringRequest{

    private static final String LOGIN_REQUEST_URL="http://theextrastep.in/pay/check_note.php";
  //  private static final String LOGIN_REQUEST_URL="http://disel.site/theextrastep/check_note.php";
    private Map<String,String> params;

    public TopicPaymentRequest(String full_name, String email_id,String topic, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("full_name",full_name);
        params.put("email_id",email_id);
        params.put("topic",topic);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
