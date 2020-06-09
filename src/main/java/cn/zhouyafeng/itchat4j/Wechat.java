package cn.zhouyafeng.itchat4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhouyafeng.itchat4j.controller.LoginController;
import cn.zhouyafeng.itchat4j.core.MsgCenter;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;

import java.util.concurrent.atomic.AtomicBoolean;

public class Wechat {
	private static final Logger LOG = LoggerFactory.getLogger(Wechat.class);
	private IMsgHandlerFace msgHandler;

	private AtomicBoolean isRunning = new AtomicBoolean(true);

	private LoginController loginController;
	private String qrPath;

	public Wechat(IMsgHandlerFace msgHandler, String qrPath) {
		System.setProperty("jsse.enableSNIExtension", "false"); // 防止SSL错误
		this.msgHandler = msgHandler;
		this.qrPath = qrPath;
		// 登陆
		loginController = new LoginController();

	}

	public void login() {
		loginController.login(qrPath);
	}

	/**
	 * cancel getting login qr code
	 * @return true if cancel success , else false
	 */
	public boolean cancelLogin() {
		return loginController.cancelLogin();
	}

	public void start() {
		LOG.info("+++++++++++++++++++开始消息处理+++++++++++++++++++++");
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(isRunning.get()) {
					MsgCenter.handleMsg(msgHandler);
				}
			}
		}).start();
	}

	/**
	 * 关闭所有线程
	 */
	public void stop() {
		isRunning.set(false);
	}

}
