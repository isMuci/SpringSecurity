package com.example.provider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;

public class IWebAuthenticationDetails extends WebAuthenticationDetails {

    public boolean VERIFICATION_CODE_CORRECT;

    public boolean isVERIFICATION_CODE_CORRECT(){
        return VERIFICATION_CODE_CORRECT;
    }

    public IWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        String requestCode = request.getParameter("kaptcha");
        HttpSession session = request.getSession();
        String sessionCode = (String) session.getAttribute("kaptcha");
        if(sessionCode!=null){
            session.removeAttribute("kaptcha");
            if(sessionCode.equals(requestCode)){
                this.VERIFICATION_CODE_CORRECT=true;
            }
        }
    }

    public IWebAuthenticationDetails(String remoteAddress, String sessionId) {
        super(remoteAddress, sessionId);
    }
}
