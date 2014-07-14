package spicerequests;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by anhhnguyen on 7/14/2014.
 */
public class ScanRequest extends SpringAndroidSpiceRequest<String> {

    private String code;
    private String access_token;

    public ScanRequest(String code, String access_token) {
        super(String.class);
        this.code = code;
        this.access_token = access_token;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        String url = "https://sss-mobile-test.herokuapp.com/scan?code=" + code + "&access_token=" + access_token;

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);


        HttpEntity requestEntity = new HttpEntity("", requestHeaders);

        RestTemplate restTemplate = getRestTemplate();
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

        return restTemplate.postForObject(url, requestEntity, String.class);
    }

    public String createCacheKey(){
        return code;
    }
}
