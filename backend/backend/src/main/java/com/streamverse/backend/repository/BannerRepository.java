package com.streamverse.backend.repository;

import com.streamverse.backend.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    List<Banner> findByActiveTrueOrderByDisplayOrderAsc();
}