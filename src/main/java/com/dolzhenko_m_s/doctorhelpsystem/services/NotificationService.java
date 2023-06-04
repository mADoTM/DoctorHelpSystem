package com.dolzhenko_m_s.doctorhelpsystem.services;

import com.dolzhenko_m_s.doctorhelpsystem.dao.NotificationDAO;
import com.dolzhenko_m_s.doctorhelpsystem.models.Notification;

import java.sql.Date;
import java.util.List;

public class NotificationService {
    public List<Notification> getByPatientId(int patientId) {
        return new NotificationDAO()
                .all()
                .stream()
                .filter(n -> n.getPatientId() == patientId)
                .filter(n -> !n.isExecuted())
                .toList();
    }

    public List<Notification> getByMonthAfter(Date from) {
        return new NotificationDAO()
                .all()
                .stream()
                .filter(n -> n.getDate().toLocalDate().getMonth() == from.toLocalDate().getMonth()
                        && n.getDate().toLocalDate().getYear() == from.toLocalDate().getYear())
                .filter(n -> !n.isExecuted())
                .toList();
    }
}
