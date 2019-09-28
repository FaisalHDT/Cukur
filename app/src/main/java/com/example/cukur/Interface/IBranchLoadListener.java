package com.example.cukur.Interface;

import com.example.cukur.Model.Salon;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSucces(List<Salon> salonList);
    void onBranchLoadFailed(String message);
}
