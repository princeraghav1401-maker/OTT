package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.HomeResponse;

public interface HomeService {

    HomeResponse getHome(String userEmail);
}