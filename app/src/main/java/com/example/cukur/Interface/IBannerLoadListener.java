package com.example.cukur.Interface;

import com.example.cukur.Model.Banner;

import java.util.List;

public interface IBannerLoadListener {
    void onBannerLoadSuccess (List<Banner> banners);
    void onBannerLoadFailed (String message);
}
