package techit.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import techit.model.Ticket;
import techit.model.User;
import techit.model.dao.TicketDao;
import techit.rest.error.RestException;

@RestController
public class TicketController {

	@Autowired
	private TicketDao ticketDao;

	@RequestMapping(value = "/tickets", method = RequestMethod.GET)
	public List<Ticket> getTickets() {
		return ticketDao.getTickets();
	}

	@RequestMapping(value = "/tickets/{id}", method = RequestMethod.GET)
	public Ticket getTicket(@PathVariable Long id) {
		return ticketDao.getTicket(id);
	}

	@RequestMapping(value = "/tickets", method = RequestMethod.POST)
	public Ticket addTicket(@ModelAttribute("currentUser") User currentUser, @RequestBody Ticket ticket) {

		if (currentUser == null)
			throw new RestException(403, "Unauthorized: Insufficient Priviledge");

		if(ticket==null || StringUtils.isEmpty(ticket.getCreatedForEmail()) || StringUtils.isEmpty(ticket.getSubject()) || ticket.getUnit()==null) {
			throw new RestException(400, "Bad Request: createdForEmail, subject, unit, or ticket is missing");
		}
		ticket.setCreatedBy(currentUser);
		return ticketDao.saveTicket(ticket);
	}
}
