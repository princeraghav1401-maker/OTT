package com.streamverse.backend.service;

import com.streamverse.backend.entity.Banner;

import java.util.List;

public interface BannerService {

    Banner createBanner(Banner banner);

    List<Banner> getAllBanners();

    List<Banner> getActiveBanners();

    void deleteBanner(Long id);
}