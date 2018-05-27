package techit.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import techit.model.User;
import techit.model.User.Type;
import techit.model.dao.UserDao;
import techit.rest.error.RestException;

@RestController
public class UserController {

	@Autowired
	private UserDao userDao;

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> getUsers() {
		return userDao.getUsers();
	}

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public User addUser(@ModelAttribute("currentUser") User currentUser, @RequestBody User user) {
		if (user.getUsername() == null || user.getPassword() == null)
			throw new RestException(400, "Missing username and/or password.");

		if (currentUser == null || currentUser.getType() != Type.ADMIN)
			throw new RestException(403, "Unauthorized: Insufficient Priviledge");

		if (StringUtils.isEmpty(user.getEmail())) {
			user.setEmail(user.getUsername() + "@calstatela.edu");
		}

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
		user.setHash(encoder.encode(user.getPassword()));
		return userDao.saveUser(user);
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public User getUser(@PathVariable Long id, @ModelAttribute("currentUser") User requester) {

		User user = userDao.getUser(id);
		if (user == null)
			throw new RestException(404, "No such User found");

		String reqUsername = user.getUsername();
		if (requester == null || (requester.getType() != Type.ADMIN && !requester.getUsername().equals(reqUsername)))
			throw new RestException(403, "Unauthorized");

		return user;
	}

}
