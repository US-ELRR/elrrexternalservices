package com.deloitte.elrr;

import fr.spacefox.confusablehomoglyphs.Confusables;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Iterator;

@Component
public class HomoGlyphFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestBody = IOUtils.toString(request.getInputStream());
        Confusables confusables = Confusables.fromInternal();

        JSONObject jsonObject = new JSONObject(requestBody);
        Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = (String) jsonObject.get(key);
            boolean dangerousKey = confusables.isDangerous(key);
            boolean dangerousValue = confusables.isDangerous(value);
            if (dangerousKey || dangerousValue) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Request body contains homoglyphs.");
                return;
            }

        }

        filterChain.doFilter(request, response);
    }


}
