package br.edu.ifpb.pweb2.sayxis.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class AccessService {

    public String checkPermission(HttpSession session, RedirectAttributes ra) {
        if (session.getAttribute("user_id") == null) {
            ra.addFlashAttribute("httpStatusCode", "401");
            return "redirect:/admin/access-denied";
        } else if ((session.getAttribute("is_adm").equals(Boolean.FALSE))) {
            ra.addFlashAttribute("httpStatusCode", "403");
            return "redirect:/admin/access-denied";
        }
        return null;
    }
}
