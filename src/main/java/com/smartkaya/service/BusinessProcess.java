package com.smartkaya.service;

import java.util.List;

import com.smartkaya.bean.Message;
import com.smartkaya.bean.Message.Lever;
import com.smartkaya.bean.Paramater;
import com.smartkaya.bean.Paramaters;
import com.smartkaya.dao.KayaModelUtils;
import com.smartkaya.dao.KayaSQLExecute;
import com.smartkaya.dao.KayaWorkFlow;
import com.smartkaya.exception.KayaException;

/**
 * KaYamodel实际执行类
 * 
 * @author Wuzy 2020/9/17
 * @version 1.0.0
 */
public class BusinessProcess {

	/**
	 * 模型check
	 * 
	 * @param paramater
	 */
	public boolean checkModel(Paramater paramater) {
		boolean checkOK = true;
		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramater)) {
			checkOK = false;

			paramater.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("10009");
			message.setMesage("表不存在！");
			paramater.setMessages(message);
		}
		return checkOK;
	}

	/**
	 * 自定义的业务check
	 * 
	 * @param paramater
	 */
	public boolean checkInput(Paramater paramater) {
		return true;
	}

	/**
	 * SQL的组成和实际实行
	 * 
	 * @param paramater
	 */
	public void exeSQL(Paramater paramater) {
		try {
			KayaSQLExecute dao = new KayaSQLExecute();
			dao.execute(paramater);
		} catch (KayaException e) {
			paramater.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("XXXX");
			message.setMesage(e.getMessage());
			paramater.setMessages(message);
		}
	}

	/**
	 * 模型check
	 * 
	 * @param paramaters
	 */
	public boolean checkModel(Paramaters paramaters) {
		boolean checkOK = true;
		// Table存在确认
		if (!KayaModelUtils.checkTableId(paramaters)) {
			checkOK = false;

			paramaters.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("10009");
			message.setMesage("表不存在！");
			paramaters.setMessages(message);
		}
		return checkOK;
	}

	/**
	 * 自定义的业务check
	 * 
	 * @param paramaters
	 */
	public boolean checkInput(Paramaters paramaters) {
		return true;
	}

	/**
	 * SQL的组成和实际实行
	 * 
	 * @param paramatersList
	 */
	public void exeSQL(Paramaters paramaters) {
		try {
			KayaSQLExecute dao = new KayaSQLExecute();
			dao.execute(paramaters);
		} catch (KayaException e) {
			paramaters.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("XXXX");
			message.setMesage(e.getMessage());
			paramaters.setMessages(message);
		}
	}

	/**
	 * 模型check
	 * 
	 * @param paramatersList
	 */
	public boolean checkModel(List<Paramaters> paramatersList) {
		boolean checkOK = true;
		for (Paramaters paramaters : paramatersList) {
			// Table存在确认
			if (!KayaModelUtils.checkTableId(paramaters)) {
				checkOK = false;

				paramaters.setError(true);
				Message message = new Message();
				message.setLever(Lever.ERROR);
				message.setCode("10009");
				message.setMesage("表不存在！");
				paramaters.setMessages(message);
			}
		}
		return checkOK;
	}

	/**
	 * 自定义的业务check
	 * 
	 * @param paramatersList
	 */
	public boolean checkInput(List<Paramaters> paramatersList) {
		return true;
	}

	/**
	 * SQL的组成和实际实行
	 * 
	 * @param paramatersList
	 */
	public void exeSQL(List<Paramaters> paramatersList) {
		KayaSQLExecute dao = new KayaSQLExecute();
		dao.execute(paramatersList);
	}

	/**
	 * SQL的组成和实际实行
	 * 
	 * @param paramatersList
	 */
	@SuppressWarnings("unchecked")
	public <T> T searchSQL(Paramater paramater) {
		T resultDetail = null;
		try {
			KayaSQLExecute dao = new KayaSQLExecute();
			switch (paramater.getType()) {
			case FULLTEXT:
				resultDetail = (T) dao.selectByFullText(paramater);
				break;
			case BUSINESSKEY:
				resultDetail = (T) dao.selectByBusinessKeys(paramater);
				break;
			case MUILTKEYBYORI:
				resultDetail = (T) dao.selectMuiltKindByOrientationkey(paramater);
				break;
			case MUILTKEYBYBUS:
				resultDetail = (T) dao.selectMuiltKindByBusiness(paramater);
				break;
			case MUILTKEYINSUB:
				resultDetail = (T) dao.selectMuiltKindAllInfo(paramater);
				break;
			case CUSTOM:
				// TODO
				break;
			default:
				resultDetail = (T) dao.selectByFreeKind(paramater);
			}
		} catch (KayaException e) {
			paramater.setError(true);
			Message message = new Message();
			message.setLever(Lever.ERROR);
			message.setCode("XXXX");
			message.setMesage(e.getMessage());
			paramater.setMessages(message);
		}
		return resultDetail;
	}

	/**
	 * SQL的组成和实际实行
	 * 
	 * @param paramater
	 */
	public void exeWorkFlow(Paramater paramater) {
		KayaWorkFlow dao = new KayaWorkFlow();
		dao.excuteKayaWorkFlow(paramater);
	}

	/**
	 * SQL的组成和实际实行
	 * 
	 * @param paramaters
	 */
	public void exeWorkFlow(Paramaters paramaters) {
		KayaWorkFlow dao = new KayaWorkFlow();
		dao.excuteKayaWorkFlow(paramaters);
	}
}
