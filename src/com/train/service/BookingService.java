package com.train.service;

import java.util.List;

import com.train.beans.HistoryBean;
import com.train.beans.TrainException;

public interface BookingService {

	public List<HistoryBean> getAllBookingsByCustomerId(String customerEmailId) throws TrainException;

	public HistoryBean createHistory(HistoryBean bookingDetails) throws TrainException;

}
