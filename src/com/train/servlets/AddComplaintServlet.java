package com.train.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.train.beans.TrainException;
import com.train.beans.UserBean;
import com.train.constant.UserRole;
import com.train.service.UserService;
import com.train.service.impl.UserServiceImpl;
import com.train.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/addcomplaint")
public class AddComplaintServlet extends HttpServlet {

	private UserService userService = new UserServiceImpl(UserRole.CUSTOMER);

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();
		try {
			TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

			String emailId = TrainUtil.getCurrentUserEmail(req);
			long trainNo = Long.parseLong(req.getParameter("trainNo"));
			String fromStn = req.getParameter("fromStn");
			String toStn = req.getParameter("toStn");
			String description = req.getParameter("description");

			String message = userService.addComplaint(emailId, trainNo, fromStn, toStn, description);
			if ("SUCCESS".equalsIgnoreCase(message)) {
				RequestDispatcher rd = req.getRequestDispatcher("UserViewTrains.html");
				rd.include(req, res);
				pw.println("<div class='tab'><p1 class='menu'>Complaint Added Successfully !</p1></div>");

			} else {
				RequestDispatcher rd = req.getRequestDispatcher("UserViewTrains.html");
				rd.include(req, res);
				pw.println("<div class='tab'><p1 class='menu'>" + message + "</p1></div>");

			}

		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
		}
	}

}
