package spicerequests;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import models.AuthenticatedUser;
/**
 * Created by anhhnguyen on 7/13/2014.
 */
public class LoginRequest extends SpringAndroidSpiceRequest<AuthenticatedUser> {

    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        super(AuthenticatedUser.class);
        this.username = username;
        this.password = password;
    }

    @Override
    public AuthenticatedUser loadDataFromNetwork() throws Exception {
        String url = "https://sss-mobile-test.herokuapp.com/login?username=" + username + "&password=" + password;

        MultiValueMap<String, Object> headers = new LinkedMultiValueMap<String, Object>();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");

        HttpEntity request = new HttpEntity("", headers);

        return getRestTemplate().postForObject(url, request, AuthenticatedUser.class);
    }

    /**
     * This method generates a unique cache key for this request. In this case
     * our cache key depends just on the keyword.
     * @return
     */
    public String createCacheKey() {
        return username+password;
    }
}
