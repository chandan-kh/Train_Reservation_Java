package com.train.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.text.ParseException;

import com.train.beans.HistoryBean;
import com.train.beans.TrainException;
import com.train.constant.ResponseCode;
import com.train.service.BookingService;
import com.train.utility.DBUtil;

public class BookingServiceImpl implements BookingService {

    @Override
    public List<HistoryBean> getAllBookingsByCustomerId(String customerEmailId) throws TrainException {
        List<HistoryBean> transactions = null;
        String query = "SELECT * FROM HISTORY WHERE MAILID=?";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, customerEmailId);
            ResultSet rs = ps.executeQuery();
            transactions = new ArrayList<>();
            while (rs.next()) {
                HistoryBean transaction = new HistoryBean();
                transaction.setTransId(rs.getString("TRANSID"));
                transaction.setFrom_stn(rs.getString("FROM_STN"));
                transaction.setTo_stn(rs.getString("TO_STN"));
                transaction.setDate(rs.getString("DATE"));
                transaction.setMailId(rs.getString("MAILID"));
                transaction.setSeats(rs.getInt("SEATS"));
                transaction.setAmount(rs.getDouble("AMOUNT"));
                transaction.setTr_no(rs.getString("TR_NO"));
                transactions.add(transaction);
            }

            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new TrainException(e.getMessage());
        }
        return transactions;
    }

    @Override
    public HistoryBean createHistory(HistoryBean details) throws TrainException {
        HistoryBean history = null;
        String query = "INSERT INTO HISTORY (TRANSID, MAILID, TR_NO, DATE, FROM_STN, TO_STN, SEATS, AMOUNT) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            String transactionId = UUID.randomUUID().toString();
            ps.setString(1, transactionId);
            ps.setString(2, details.getMailId());
            ps.setString(3, details.getTr_no());

            // Convert date to java.sql.Date
            java.sql.Date sqlDate = null;
            try {
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                java.util.Date utilDate = inputDateFormat.parse(details.getDate());
                sqlDate = new java.sql.Date(utilDate.getTime());
            } catch (ParseException e) {
                throw new TrainException("Invalid date format: " + details.getDate());
            }

            ps.setDate(4, sqlDate);
            ps.setString(5, details.getFrom_stn());
            ps.setString(6, details.getTo_stn());
            ps.setLong(7, details.getSeats());
            ps.setDouble(8, details.getAmount());
            int response = ps.executeUpdate();
            if (response > 0) {
                history = new HistoryBean();
                history.setTransId(transactionId);
                history.setMailId(details.getMailId());
                history.setTr_no(details.getTr_no());
                history.setDate(sqlDate.toString()); // Store as string for consistency
                history.setFrom_stn(details.getFrom_stn());
                history.setTo_stn(details.getTo_stn());
                history.setSeats(details.getSeats());
                history.setAmount(details.getAmount());
            } else {
                throw new TrainException(ResponseCode.INTERNAL_SERVER_ERROR);
            }
            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new TrainException(e.getMessage());
        }
        return history;
    }


}