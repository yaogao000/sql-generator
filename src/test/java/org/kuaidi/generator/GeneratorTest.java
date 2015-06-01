package org.kuaidi.generator;

import org.junit.Test;

public class GeneratorTest {

	@Test
	public void testCustomerWhitelist() {
		AbstractGeneator generator = new CustomerWhitelistGenerator();
		generator.generator();
	}

	@Test
	public void testKdCustomerWhitelist() {
		AbstractGeneator generator = new KdWhitelistGenerator();
		generator.generator();
	}
}
