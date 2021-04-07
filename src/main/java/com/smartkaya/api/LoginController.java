package com.smartkaya.api;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.smartkaya.constant.Constant;
import com.smartkaya.exception.ExceptionEnum;
import com.smartkaya.utils.VerifyCodeUtil;
/**
 * 权限验证处理
 * @author LiangChen
 * @version 1.0.0 2021/4/2
 */
@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@GetMapping("/index")
	public ModelAndView index(ModelAndView modelAndView) {
		return modelAndView;
	}

	@GetMapping("/index1")
	public ModelAndView index1(ModelAndView modelAndView) {
		return modelAndView;
	}

	@GetMapping("cms/login")
	public String login(HttpServletRequest request) {
		logger.info("当前的路径为：" + request.getRequestURI());
		Subject s = SecurityUtils.getSubject();
		logger.info("是否记住登录--》" + s.isRemembered() + "; 是否有权限登录" + s.isAuthenticated());
		logger.info("cms/login  sessionId = " + request.getSession() + "        " + request.getSession().getId());
		if (s.isAuthenticated()) {
			return "/cms/toIndex";
		} else {
			return "cms/login";
		}
	}

	@RequestMapping(value = "/login/main", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String,Object> loginMain(final HttpServletRequest request, final HttpServletResponse response) {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String rememberMe = request.getParameter("rememberMe");
		String code = request.getParameter("captcha");
		RestHelper helper = new RestHelper();
		Map<String, Object> ret = new HashMap<>();
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			helper.setMessage("ユーザー名称とパスワードは必須項目です.");
			return helper.getFailed(ExceptionEnum.BUSINESS_ERROR.getCode());
		}
		if (StringUtils.isBlank(code)) {
			helper.setMessage("認証コードは必須項目です.");
			return helper.getFailed(ExceptionEnum.BUSINESS_ERROR.getCode());
		}
		String error = null;
		HttpSession session = request.getSession();
		if (session == null) {
			helper.setMessage("セッションタイムアウト.");
			return helper.getFailed(ExceptionEnum.BUSINESS_ERROR.getCode());
		}
		logger.info("login       sessionId = " + session + "        " + request.getSession().getId());
		String trueCode = (String) session.getAttribute(Constant.VALIDATE_CODE);
		if (StringUtils.isBlank(trueCode)) {
			helper.setMessage("認証コードタイムアウト.");
			return helper.getFailed(ExceptionEnum.BUSINESS_ERROR.getCode());
		}
		logger.info("code     = " + code);
		logger.info("trueCode = " + trueCode);
		if (StringUtils.isBlank(code) || !trueCode.toLowerCase().equals(code.toLowerCase())) {
			error = "認証コードエラー.";
		} else {
			/* 就是代表当前的用户. */
			Subject user = SecurityUtils.getSubject();
			//UsernamePasswordToken token = new UsernamePasswordToken(username, password, Boolean.valueOf(rememberMe));
			AuthenticationToken token=new UsernamePasswordToken(username,password,Boolean.valueOf(rememberMe));
			try {
				user.login(token);
				//SysUser principal = (SysUser) user.getPrincipal();
				session.setAttribute("user", user.getPrincipal());
				if (user.isAuthenticated()) {
					ret = helper.getSimpleSuccess();
					ret.put("url", "../index.html");
				}
			} catch (IncorrectCredentialsException e) {
				error = "パスワード不正です.";
			} catch (ExcessiveAttemptsException e) {
				error = "失敗したログインが多すぎます.";
			} catch (LockedAccountException e) {
				error = "アカウントがロックされました.";
			} catch (DisabledAccountException e) {
				error = "アカウントが無効になりました.";
			} catch (ExpiredCredentialsException e) {
				error = "アカウントの有効期限が切れています.";
			} catch (UnknownAccountException e) {
				error = "アカウントは存在しません.";
			} catch (UnauthorizedException e) {
				error = "アクセス権限がありません.";
			} catch (Exception e) {
				error = "システムエラー.";
			}
		}
		if (StringUtils.isBlank(error)) {
			return ret;
		} else {
			helper.setMessage(error);
			return helper.getFailed(ExceptionEnum.BUSINESS_ERROR.getCode());
		}
	}

	/**
	 * 获取验证码图片和文本(验证码文本会保存在HttpSession中)
	 */
	@GetMapping("/genCaptcha")
	public void genCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_ALL_MIXED, 4, null);
		HttpSession session = request.getSession();
		session.setAttribute(Constant.VALIDATE_CODE, verifyCode);
		logger.info("本次生成的验证码为[" + verifyCode + "],已存放到HttpSession中 sessionId = " + request.getSession().getId());
		logger.info("genCaptcha  sessionId = " + request.getSession() + "        " + request.getSession().getId());
		response.setContentType("image/jpeg");
		BufferedImage bufferedImage = VerifyCodeUtil.generateImageCode(verifyCode, 116, 36, 5, true,
				new Color(249, 205, 173), null, null);
		ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
	}

	@GetMapping("systemLogout")
	public String logOut() {
		SecurityUtils.getSubject().logout();
		return "/cms/toLogin";
	}
}
