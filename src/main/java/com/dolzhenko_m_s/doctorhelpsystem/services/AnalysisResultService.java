package com.dolzhenko_m_s.doctorhelpsystem.services;

import com.dolzhenko_m_s.doctorhelpsystem.dao.AnalysisResultDAO;
import com.dolzhenko_m_s.doctorhelpsystem.models.AnalysisResult;

import java.util.List;

public class AnalysisResultService {
    public List<AnalysisResult> getByPatientId(int patientId) {
        return new AnalysisResultDAO()
                .all()
                .stream()
                .filter(a -> a.getPatientId() == patientId)
                .toList();
    }
}
