package org.kuaidi.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;

/**
 * 生成批量插入, 更行的sql语句
 * 
 * @author yaogaolin
 *
 */
public abstract class BatchGenerator extends AbstractGeneator {
	/**
	 * 设置sql 批量插入或者更新sql语句的变量大小
	 * 
	 * @return
	 */
	protected abstract int getBatchSize();

	/**
	 * 根据批量插入或者更新sql语句的变量 获取到的 sql语句
	 * 
	 * @param bulks
	 * @return
	 */
	protected abstract String getSql(List<String[]> bulks);

	@Override
	public void generator() {
		int lineNum = getLineNum();
		if (lineNum <= 0) {
			throw new IllegalArgumentException("lineNum can not be less than 0");
		}

		int batchSize = getBatchSize();
		if (batchSize <= 0) {
			throw new IllegalArgumentException("batch size can not be less than 0");
		}

		List<String> sqls = new ArrayList<String>(lineNum == Integer.MAX_VALUE ? 10000 : lineNum);
		List<String[]> bulks = new ArrayList<String[]>(batchSize == 0 ? 4 : batchSize);

		Resource resouce = getResource();
		String line = null;
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(resouce.getInputStream()));
			while (null != (line = bufferedReader.readLine())) {
				if (StringUtils.isBlank(line)) {
					System.out
							.println(String.format("line number %d is empty, please ensure this waring", sqls.size()));
					continue;
				}
				String[] values = line.split("\t");
				bulks.add(values);

				if (bulks.size() >= batchSize) {
					String sql = getSql(bulks);
					bulks.clear();

					sqls.add(sql);
					if (sqls.size() >= lineNum) {
						// 保存
						saveToFile(sqls);
						sqls.clear();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != bufferedReader) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				bufferedReader = null;
			}
		}
		if (bulks.size() > 0) {
			String sql = getSql(bulks);
			bulks.clear();

			sqls.add(sql);
			if (sqls.size() >= lineNum) {
				saveToFile(sqls);
				sqls.clear();
			}
		}

		if (sqls.size() > 0) {
			saveToFile(sqls);
		}
	}
}
