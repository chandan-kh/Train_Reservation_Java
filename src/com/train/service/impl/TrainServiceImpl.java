package com.train.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.train.beans.TrainBean;
import com.train.beans.TrainException;
import com.train.constant.ResponseCode;
import com.train.service.TrainService;
import com.train.utility.DBUtil;

public class TrainServiceImpl implements TrainService {

    @Override
    public String addTrain(TrainBean train) {
        String responseCode = ResponseCode.FAILURE.toString();
        String query = "INSERT INTO TRAIN VALUES(?,?,?,?,?,?)";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, train.getTr_no());
            ps.setString(2, train.getTr_name());
            ps.setString(3, train.getFrom_stn());
            ps.setString(4, train.getTo_stn());
            ps.setLong(5, train.getSeats());
            ps.setDouble(6, train.getFare());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                responseCode = ResponseCode.SUCCESS.toString();
            }
            ps.close();
        } catch (SQLException | TrainException e) {
            responseCode += " : " + e.getMessage();
        }
        return responseCode;
    }

    @Override
    public String deleteTrainById(String trainNo) {
        String responseCode = ResponseCode.FAILURE.toString();
        String query = "DELETE FROM TRAIN WHERE TR_NO=?";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, trainNo);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                responseCode = ResponseCode.SUCCESS.toString();
            }
            ps.close();
        } catch (SQLException | TrainException e) {
            responseCode += " : " + e.getMessage();
        }
        return responseCode;
    }

    @Override
    public String updateTrain(TrainBean train) {
        String responseCode = ResponseCode.FAILURE.toString();
        String query = "UPDATE TRAIN SET TR_NAME=?, FROM_STN=?, TO_STN=?, SEATS=?, FARE=? WHERE TR_NO=?";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, train.getTr_name());
            ps.setString(2, train.getFrom_stn());
            ps.setString(3, train.getTo_stn());
            ps.setLong(4, train.getSeats());
            ps.setDouble(5, train.getFare());
            ps.setLong(6, train.getTr_no());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                responseCode = ResponseCode.SUCCESS.toString();
            }
            ps.close();
        } catch (SQLException | TrainException e) {
            responseCode += " : " + e.getMessage();
        }
        return responseCode;
    }

    @Override
    public TrainBean getTrainById(String trainNo) throws TrainException {
        TrainBean train = null;
        String query = "SELECT * FROM TRAIN WHERE TR_NO=?";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, trainNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                train = new TrainBean();
                train.setFare(rs.getDouble("FARE"));
                train.setFrom_stn(rs.getString("FROM_STN"));
                train.setTo_stn(rs.getString("TO_STN"));
                train.setTr_name(rs.getString("TR_NAME"));
                train.setTr_no(rs.getLong("TR_NO"));
                train.setSeats(rs.getInt("SEATS"));
            }
            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new TrainException(e.getMessage());
        }
        return train;
    }

    @Override
    public List<TrainBean> getAllTrains() throws TrainException {
        List<TrainBean> trains = null;
        String query = "SELECT * FROM TRAIN";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            trains = new ArrayList<>();
            while (rs.next()) {
                TrainBean train = new TrainBean();
                train.setFare(rs.getDouble("FARE"));
                train.setFrom_stn(rs.getString("FROM_STN"));
                train.setTo_stn(rs.getString("TO_STN"));
                train.setTr_name(rs.getString("TR_NAME"));
                train.setTr_no(rs.getLong("TR_NO"));
                train.setSeats(rs.getInt("SEATS"));
                trains.add(train);
            }

            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new TrainException(e.getMessage());
        }
        return trains;
    }

    @Override
    public List<TrainBean> getTrainsBetweenStations(String fromStation, String toStation) throws TrainException {
        List<TrainBean> trains = null;
        String query = "SELECT * FROM TRAIN WHERE UPPER(FROM_STN) LIKE UPPER(?) AND UPPER(TO_STN) LIKE UPPER(?)";

        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, "%" + fromStation + "%");
            ps.setString(2, "%" + toStation + "%");
            ResultSet rs = ps.executeQuery();
            trains = new ArrayList<>();
            while (rs.next()) {
                TrainBean train = new TrainBean();
                train.setFare(rs.getDouble("FARE"));
                train.setFrom_stn(rs.getString("FROM_STN"));
                train.setTo_stn(rs.getString("TO_STN"));
                train.setTr_name(rs.getString("TR_NAME"));
                train.setTr_no(rs.getLong("TR_NO"));
                train.setSeats(rs.getInt("SEATS"));
                trains.add(train);
            }

            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new TrainException(e.getMessage());
        }
        return trains;
    }
}
