package org.kuaidi.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;

public abstract class AbstractGeneator implements Generator {
	/**
	 * 文件后缀序列
	 */
	private int postfix = 0;

	/**
	 * 待解析文件的资源路径
	 * 
	 * @return
	 */
	protected abstract Resource getResource();

	/**
	 * 设置生成文件的行数, 默认所有sql生成到一个文件
	 * 
	 * @return
	 */
	protected int getLineNum() {
		return Integer.MAX_VALUE;
	}

	/**
	 * 根据文件后缀序列获取文件名
	 * 
	 * @param postfix
	 * @return
	 */
	protected abstract String getBaseTemplateFileName(int postfix);

	/**
	 * 保存生成的sql到getBaseTemplateFileName文件
	 * 
	 * @param sqls
	 */
	protected void saveToFile(List<String> sqls) {
		File file = new File(getBaseTemplateFileName(postfix));

		BufferedWriter writer = null;
		try {
			file.createNewFile();

			System.out.println(String.format("create new File with name %s", file.getName()));

			writer = new BufferedWriter(new FileWriter(file));
			for (String sql : sqls) {
				writer.write(sql + "\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				writer = null;
			}
		}

		this.postfix++;
	}
}
