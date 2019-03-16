package com.tca.service.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.tca.service.IProfileService;

@Service
@Profile("test")
public class ProfileTestServiceImpl implements IProfileService {

	@Override
	public String profileName() {
		return "test";
	}

}
