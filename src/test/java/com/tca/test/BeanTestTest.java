package com.tca.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tca.beans.BeanTest;

public class BeanTestTest extends BaseTest {
	
	@Autowired
	private BeanTest beanTest;
	
	@Test
	public void test01() {
		System.out.println(beanTest);
	}
}
