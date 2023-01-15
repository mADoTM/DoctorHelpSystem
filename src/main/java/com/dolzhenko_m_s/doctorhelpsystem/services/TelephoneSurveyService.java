package com.dolzhenko_m_s.doctorhelpsystem.services;

import com.dolzhenko_m_s.doctorhelpsystem.dao.TelephoneSurveyDAO;
import com.dolzhenko_m_s.doctorhelpsystem.models.TelephoneSurvey;

import java.util.List;

public class TelephoneSurveyService {
    public List<TelephoneSurvey> getByPatientId(int patientId) {
        var list = new TelephoneSurveyDAO().all();
        return list.stream().filter(x -> x.getPatientId() == patientId).toList();
    }
}
