package com.tca.service.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.tca.service.IProfileService;

@Service
@Profile("pro")
public class ProfileProServiceImpl implements IProfileService {

	@Override
	public String profileName() {
		return "pro";
	}

}
