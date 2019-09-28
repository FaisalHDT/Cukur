package com.example.cukur.Interface;

import com.example.cukur.Model.Banner;

import java.util.List;

public interface ILookbookLoadlistner {
    void onLookbookLoadSuccess (List<Banner> banners);
    void onLookbookLoadFailed (String message);
}
