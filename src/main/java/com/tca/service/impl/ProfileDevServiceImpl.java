package com.tca.service.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.tca.service.IProfileService;

@Service
@Profile("dev")
public class ProfileDevServiceImpl implements IProfileService {

	@Override
	public String profileName() {
		return "dev";
	}
	
}
