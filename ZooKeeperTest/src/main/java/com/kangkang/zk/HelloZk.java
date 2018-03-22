package com.atguigu.zk;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import com.atguigu.config.BaseConfig;

/**
 * 1 通过java程序，新建链接zk，类似jdbc的connection，open.session 2
 * 新建一个znode节点/atguigu并设置为hello1018 等同于create /atguigu hello1018 3
 * 获得当前节点/atguigu的最新值 get /atguigu 4 关闭链接
 */
public class HelloZk extends BaseConfig {

	// 声明Log4j，创建Log4j对象Logger
	private static final Logger logger = Logger.getLogger(HelloZk.class);


	public static void main(String[] args)
			throws IOException, KeeperException, InterruptedException {
		HelloZk hk = new HelloZk();
		ZooKeeper zk = hk.startZk();

		if (zk.exists(PATH, false) == null) {
			// 创建节点
			hk.createZnode(PATH, "hello_1018");
			String result = hk.getZnode(PATH);
			logger.info("main***********String result:" + result);
		} else {
			logger.info("This node has existed");
		}
		// 停止Zookeeper服务
		hk.stopZK(zk);
	}
}
