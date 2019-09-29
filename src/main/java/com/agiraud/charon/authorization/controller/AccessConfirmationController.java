package com.agiraud.charon.authorization.controller;

import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.agiraud.charon.core.config.ClientDetailsServiceImpl;
import com.agiraud.charon.core.dto.ClientDetail;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller for retrieving the model for the approvals and displaying the confirmation page
 * for accessing to a protected resource.
 */
@Controller
@SessionAttributes(types = AuthorizationRequest.class)
@Slf4j
public class AccessConfirmationController {
	/* ************************************************************************* */
	// ATTRIBUTES
	/* ************************************************************************* */
	@Autowired
	private ClientDetailsServiceImpl clientDetailsService;

	/* ************************************************************************* */
	// REQUEST MAPPING
	/* ************************************************************************* */
	@RequestMapping("/oauth/confirm_access")
	public ModelAndView getAccessConfirmation(HttpServletRequest request, @ModelAttribute AuthorizationRequest clientAuth) throws Exception {
		log.debug("[getAccessConfirmation] Info for: "+clientAuth.getClientId());
		
		ClientDetail client = (ClientDetail) clientDetailsService.loadClientByClientId(clientAuth.getClientId());
		TreeMap<String, Object> model = new TreeMap<String, Object>();
		model.put("client", client);
		return new ModelAndView("form-approval.html", model);
	}

}
