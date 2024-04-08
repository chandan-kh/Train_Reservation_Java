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

import com.train.constant.UserRole;
import com.train.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/food")
public class Food extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		PrintWriter pw = res.getWriter();
		res.setContentType("text/html");
		TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

		String emailId = TrainUtil.getCurrentUserEmail(req);
		long trainNo = Long.parseLong(req.getParameter("trainNo"));
		String fromStn = req.getParameter("fromStn");
		String toStn = req.getParameter("toStn");
		RequestDispatcher rd = req.getRequestDispatcher("UserViewTrains.html");
		rd.include(req, res);
		pw.println("<div class='main'><p1 class='menu'>File your Complaint</p1></div>");

		pw.println("<div class='tab'>" +
		        "<form action='addfood' method='post'>" +
		            "<table>" +
		                "<tr><td>USER ID:</td><td><input type='text' name='emailId' value='" + emailId + "' readonly></td></tr>" + 
		                "<tr><td>Train NO:</td><td>" + trainNo + "</td></tr>" +
		                "<tr><td>From Station:</td><td><input type='text' name='fromStn' value='" + fromStn + "' readonly></td></tr>" + 
		                "<tr><td>To Station :</td><td><input type='text' name='toStn' value='" + toStn + "' readonly></td></tr>" + 
		                "<tr><td>Journey Date:</td><td>" +
		                    "<input type='hidden' name='trainNo' value='" + trainNo + "'>" +
		                    "<input type='date' name='journeydate' value='" + LocalDate.now() + "'></td></tr>" +
		                "<tr><td>Select Indian Food:</td><td colspan='3'>" +
		                    "<select name='food'>" +
		                        "<option value='Biryani'>Biryani</option>" +
		                        "<option value='Masala Dosa'>Masala Dosa</option>" +
		                        "<option value='Butter Chicken'>Butter Chicken</option>" +
		                        "<option value='Paneer Tikka'>Paneer Tikka</option>" +
		                        "<option value='Chole Bhature'>Chole Bhature</option>" +
		                        "<option value='Pav Bhaji'>Pav Bhaji</option>" +
		                        "<option value='Tandoori Roti'>Tandoori Roti</option>" +
		                        "<option value='Chicken Curry'>Chicken Curry</option>" +
		                        "<option value='Fish Fry'>Fish Fry</option>" +
		                        "<option value='Samosa'>Samosa</option>" +
		                    "</select></td></tr>" +
		                "<tr></tr>" +
		            "</table></div>" +
		            "<div class='tab'><p1 class='menu'><input type='submit'value='Order Food'></p1></div>" +
		        "</form>");


	}

}