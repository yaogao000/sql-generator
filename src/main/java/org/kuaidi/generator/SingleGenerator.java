package org.kuaidi.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;

/**
 * 根据输入文件的每行 生成一条sql语句
 * 
 * @author yaogaolin
 *
 */
public abstract class SingleGenerator extends AbstractGeneator {

	/**
	 * 根据输入文件的每行 生成一条sql语句
	 * 
	 * @param values
	 *            输入文件每行数据(已经按\t split了)
	 * @return
	 */
	protected abstract String getSql(String[] values);

	@Override
	public void generator() {
		int lineNum = getLineNum();
		if (lineNum <= 0) {
			throw new IllegalArgumentException("lineNum can not be less than 0");
		}

		List<String> sqls = new ArrayList<String>(lineNum == Integer.MAX_VALUE ? 10000 : lineNum);

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
				String sql = getSql(values);
				sqls.add(sql);

				if (sqls.size() >= lineNum) {
					// 保存
					saveToFile(sqls);
					sqls.clear();
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

		if (sqls.size() > 0) {
			saveToFile(sqls);
		}
	}
}
