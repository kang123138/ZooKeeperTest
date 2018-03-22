package com.atguigu.config;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import com.atguigu.zk.WatchMore;
import com.atguigu.zk.WatchOne;

public class BaseConfig {
	// 定义常量
	public static final String CONNECTSTRING = "192.168.65.129:2181";
	public static final int SESSION_TIMEOUT = 20 * 1000;
	public static final String PATH = "/atguigu";
	public ZooKeeper zk;
	
	// 创建新值和旧值
	private String newValue = "";
	private String oldValue = "";

	// log
	private static final Logger logger1 = Logger.getLogger(WatchOne.class);
	// log
	private static final Logger logger2 = Logger.getLogger(WatchMore.class);

	// private ZooKeeper zk;
	// 创建连接zookeeper
	public ZooKeeper startZk() throws IOException {
		zk = new ZooKeeper(CONNECTSTRING, SESSION_TIMEOUT, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
			}
		});
		return zk;
	}

	// 新建一个znode节点/atguigu并设置为hello1018 等同于create /atguigu hello1018
	public void createZnode(String path, String data)
			throws KeeperException, InterruptedException {
		zk.create(PATH, data.getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
	}

	// 获得当前节点/atguigu的最新值 get /atguigu
	public String getZnode(String path)
			throws KeeperException, InterruptedException {
		byte[] result = zk.getData(path, null, new Stat());
		return new String(result);
	}

	// 获取节点值
	public String getZnodeByWatchOne(String path)
			throws KeeperException, InterruptedException {
		byte[] byteArray = zk.getData(path, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				try {
					triggerValue1(path);
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, new Stat());
		String result = new String(byteArray);
		return result;
	}
	
	// 获取节点值
	public String getZnodeByWatchMore(String path)
			throws KeeperException, InterruptedException {
		byte[] byteArray = zk.getData(path, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				try {
					triggerValue2(path);
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, new Stat());
		String result = new String(byteArray);
		// 把结果赋值给oldValue
		oldValue = result;
		return result;
	}

	// 触发观察者
	public void triggerValue1(String path)
			throws KeeperException, InterruptedException {
		byte[] byteArray = zk.getData(path, null, new Stat());
		String result = new String(byteArray);
		logger1.info(
				"***********888lwatcher after triggerValue result :" + result);
	}
	
	// 触发观察者
	public boolean triggerValue2(String path)
			throws KeeperException, InterruptedException {
		byte[] byteArray = zk.getData(path, new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				try {
					triggerValue2(path);
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, new Stat());
		String result = new String(byteArray);
		// 把触发观察者改变的结果赋值给newValue
		newValue = result;
		if (newValue.equals(oldValue)) {
			logger2.info("This value has no changes***********");
			return false;
		} else {
			logger2.info("******olderValue :" + oldValue
					+ "***********newValue: " + newValue);
			oldValue = newValue;
			return true;
		}
	}

	// 关闭链接
	public void stopZK(ZooKeeper zk) throws InterruptedException {
		if (zk != null) {
			zk.close();
		}
	}
}
