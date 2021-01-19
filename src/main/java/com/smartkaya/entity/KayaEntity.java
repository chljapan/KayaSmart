package com.smartkaya.entity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.smartkaya.constant.Constant;
/**
 * KaYa
 * @author LiangChen　2018/4/30
 * @version 1.0.0
 */
public class KayaEntity {
	private String kayaModelId;
	private String kindType;
	private String name;
	private String kind;
	private String relid;
	private String kindValue;
	private String parentId;
	private String businessId;
	private String businessSubId;
	private String orientationKey;
	private boolean mainFlg;
	private Date startDate;
	private Date endDate;
	private Date withdrawalDate;
	private String dataType;
	private int dataLength;
	private Date createdate;
	private String createuser;
	private Date updatedate;
	private String updateuser;
	private boolean lockflg;
	private String lockuser;
	private String format;
	public String getKayaModelId() {
		return kayaModelId;
	}
	public void setKayaModelId(String kayaModelId) {
		this.kayaModelId = kayaModelId;
	}

	public String getKindType() {
		return kindType;
	}
	public void setKindType(String kindType) {
		this.kindType = kindType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getKindValue() {
		return kindValue;
	}
	
	public String getFormat() {
		return format;
	}
	public void setFormate(String format) {
		this.format = format;
	}
	public void setKindValue(String kindValue) {
		this.kindValue = kindValue;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getOrientationKey() {
		return orientationKey;
	}
	public void setOrientationKey(String orientationKey) {
		this.orientationKey = orientationKey;
	}
	public boolean isMainFlg() {
		return mainFlg;
	}
	public void setMainFlg(boolean mainFlg) {
		this.mainFlg = mainFlg;
	}
	public String getBusinessSubId() {
		return businessSubId;
	}
	public void setBusinessSubId(String businessSubId) {
		this.businessSubId = businessSubId;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getWithdrawalDate() {
		return withdrawalDate;
	}
	public void setWithdrawalDate(Date withdrawalDate) {
		this.withdrawalDate = withdrawalDate;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public int getDataLength() {
		return dataLength;
	}
	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public Date getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}
	public String getUpdateuser() {
		return updateuser;
	}
	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}
	public boolean isLockflg() {
		return lockflg;
	}
	public void setLockflg(boolean lockflg) {
		this.lockflg = lockflg;
	}
	public String getLockuser() {
		return lockuser;
	}
	public void setLockuser(String lockuser) {
		this.lockuser = lockuser;
	}

	public String getRelid() {
		return relid;
	}
	public void setRelid(String relid) {
		this.relid = relid;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String toString() {
		return 	"kayaModelId:[" + this.kayaModelId + "],"
				+ "kindType:[" + this.kindType + "],"
				+ "name:[" + this.name + "],"
				+ "kind:[" + this.kind + "],"
				+ "kindValue:[" + this.kindValue + "],"
				+ "parentId:[" + this.parentId + "],"
				+ "businessId:[" + this.businessId	 + "],"
				+ "businessSubId:[" + this.businessSubId + "],"
				+ "mainFlg:[" + this.mainFlg + "],"
				+ "startDate:[" + this.startDate + "],"
				+ "endDate:[" + this.endDate + "],"
				+ "withdrawalDate:[" + this.withdrawalDate + "],"
				+ "dataType:[" + this.dataType + "],"
				+ "createdate:[" + this.createdate + "],"
				+ "createuser:[" + this.createuser + "],"
				+ "updatedate:[" + this.updatedate + "],"
				+ "updateuser:[" + this.updateuser + "],"
				+ "lockflg:[" + this.lockflg + "],"
				+ "lockuser:[" + this.lockuser + "]";
	}
	@SuppressWarnings("unchecked")
	public <V> V getValue(String key){
		V value=null;
		switch (dataType) {
		case "Byte":
			value = (V)(Byte.valueOf(kindValue));
		break;
		case "Short":
			value = (V)(Short.valueOf(kindValue));
			break;
		case "Integer":
			value = (V)(Integer.valueOf(kindValue));
			break;
		case "Long":
			value =  (V)(Long.valueOf(kindValue));
			break;
		case "Char":
			value =  (V)(Character.valueOf(kindValue.toCharArray()[0]));
			break;
		case "Float":
			value =  (V)(Float.valueOf(kindValue));
			break;
		case "Double":
			value =  (V)(Double.valueOf(kindValue));
		case "Boolean":
			value =  (V)(Boolean.valueOf(kindValue));
			break;
		case "BigDecimal":
			value =  (V)(BigDecimal.valueOf(Double.valueOf(kindValue)));
			break;
		case "Date":
	        SimpleDateFormat simpleDateFormat= null;
	        if (null == format) {
	        	simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //默认时间格式
	        } else {
	        	simpleDateFormat=new SimpleDateFormat(format);
	        }
	        //必须捕获异常
	        try {
	            Date date=simpleDateFormat.parse(kindValue);
				value =  (V)date;
	        } catch(ParseException px) {
	            px.printStackTrace();
	        }
			break;
			default:
				value = (V) Constant.EMPTY;
				break;		
		}
		return value;
    }
	

}
