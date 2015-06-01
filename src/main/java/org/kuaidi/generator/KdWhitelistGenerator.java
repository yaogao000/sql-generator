package org.kuaidi.generator;

import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class KdWhitelistGenerator extends BatchGenerator {

	@Override
	protected Resource getResource() {
		return new ClassPathResource("t_kd_whitelist.txt");
	}

	@Override
	protected int getBatchSize() {
		return 100;
	}

	@Override
	protected int getLineNum() {
		return 5000;
	}

	@Override
	protected String getSql(List<String[]> bulks) {
		StringBuilder builder = new StringBuilder("INSERT INTO t_kd_whitelist(phone, reason_type) values");
		for (String[] bulk : bulks) {
			builder.append("('" + bulk[0] + "'," + bulk[1] + "),");
		}
		return builder.substring(0, builder.length() - 1) + ";";
	}

	@Override
	protected String getBaseTemplateFileName(int subfix) {
		return String.format("src/main/java/org/kuaidi/generator/t_kd_whitelist_%d.sql", subfix);
	}
}
