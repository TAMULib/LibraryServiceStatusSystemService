package edu.tamu.app.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.tamu.framework.util.HttpUtility;

@Service
public class AppHttpUtility extends HttpUtility {

    @Value("${app.http.timeout}")
    private int defaultTimeout;

    @Override
    public String makeHttpRequest(String urlString, String method) throws IOException, MalformedURLException {
        URL url = new URL(urlString);
        return makeHttpRequest(url.toString(), method, Optional.empty(), Optional.empty(), defaultTimeout);
    }

}
