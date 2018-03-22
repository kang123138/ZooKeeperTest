package com.atguigu.zk;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import com.atguigu.config.BaseConfig;

import lombok.Setter;

/**
 * *1 初始化ZK的多个操作 1.1 建立ZK的链接 1.2 创建/atguigu节点并赋值 1.3 获得该节点的值
 * 
 * 2 watchmore 2.1 获得值之后设置一个观察者watcher，如果/atguigu该节点的值发生了变化，要求通知Client端，一次性通知
 * 
 * 3 watchMore 3.1 获得值之后设置一个观察者watcher，如果/atguigu该节点的值发生了变化，要求通知Client端,继续观察 3.2
 * 又再次获得新的值的同时再新设置一个观察者，继续观察并获得值 3.3 又再次获得新的值的同时再新设置一个观察者，继续观察并获得值.。。。。。重复上述过程
 */
public class WatchMore extends BaseConfig{

	// log
	private static final Logger logger = Logger.getLogger(WatchMore.class);



	public static void main(String[] args)
			throws IOException, KeeperException, InterruptedException {
		WatchMore wo = new WatchMore();
		ZooKeeper zk = wo.startZk();
		if (zk.exists(PATH, false) == null) {
			wo.createZnode(PATH, "AAA");
			String result = wo.getZnodeByWatchMore(PATH);
			logger.info("************main init result:" + result);
		} else {
			logger.info("This node has exists");
		}

		Thread.sleep(Long.MAX_VALUE);
	}

}
