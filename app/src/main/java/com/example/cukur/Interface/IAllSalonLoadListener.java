package com.example.cukur.Interface;

import java.util.List;

public interface IAllSalonLoadListener {

    void onAllSalonLoadSucces(List<String>areaNameList);
    void onAllSalonLoadFailed(String message);
}
