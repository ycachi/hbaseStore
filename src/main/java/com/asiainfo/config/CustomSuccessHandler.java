package com.asiainfo.config;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.asiainfo.base.entity.system.SysUser;
import com.asiainfo.service.system.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private Logger logger = Logger.getLogger(getClass());
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	@Autowired
    private UserService userService;
	@Override
	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {
		MyUserDetail userDetail = (MyUserDetail)authentication.getPrincipal();
		SysUser currentUser = new SysUser();
		currentUser.setId(userDetail.getUserId());
		currentUser.setUsername(userDetail.getUsername());
		currentUser.setOperateDate(new Date());
		currentUser.setRoles(userDetail.getRoleList());
		currentUser.setCharacterName(userDetail.getCharactorName());
		HttpSession session = request.getSession();
		session.setAttribute("currentUser", currentUser);
		if (response.isCommitted()) {
			logger.info("Can't redirect");
			return;
		}
		userService.updateUser(currentUser);
		redirectStrategy.sendRedirect(request, response, "/index");
	}
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

}