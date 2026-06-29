package com.streamverse.backend.service.impl;

import com.streamverse.backend.entity.Banner;
import com.streamverse.backend.repository.BannerRepository;
import com.streamverse.backend.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;

    @Override
    public Banner createBanner(Banner banner) {
        return bannerRepository.save(banner);
    }

    @Override
    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    @Override
    public List<Banner> getActiveBanners() {
        return bannerRepository.findByActiveTrueOrderByDisplayOrderAsc();
    }

    @Override
    public void deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner not found"));

        bannerRepository.delete(banner);
    }
}