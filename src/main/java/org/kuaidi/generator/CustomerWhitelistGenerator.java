package org.kuaidi.generator;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class CustomerWhitelistGenerator extends SingleGenerator {

	@Override
	protected Resource getResource() {
		return new ClassPathResource("t_customer.txt");
	}

	@Override
	protected String getSql(String[] values) {
		return String
				.format("update t_customer_blacklist set status=0,update_time=SYSDATE() where phone=(select mobile from t_customer where cid=%s) and type=2;",
						values[0]);
	}

	@Override
	protected String getBaseTemplateFileName(int subfix) {
		return String.format("src/main/java/org/kuaidi/generator/whiltelist_%d.sql", subfix);
	}
}
