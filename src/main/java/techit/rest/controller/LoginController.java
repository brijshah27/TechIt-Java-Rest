package techit.rest.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import techit.model.User;
import techit.model.dao.UserDao;
import techit.rest.error.RestException;
import techit.util.JwtSignatureUtil;
import techit.util.ResponseMap;

@RestController
public class LoginController {

	@Autowired
	UserDao userDao;

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> login(@RequestBody User user) {
		if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword()))
			throw new RestException(400, "Missing username and/or password.");

		User userObj = userDao.getUser(user.getUsername());
		if (userObj == null)
			throw new RestException(400, "Wrong username and/or password.");

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
		if (encoder.matches(user.getPassword(), userObj.getHash()))
			return new ResponseMap<String, Object>().put("success", true)
					.put("jwt", JwtSignatureUtil.generateToken(userObj)).getMap();
		else
			throw new RestException(400, "Wrong username and/or password.");
	}
}
