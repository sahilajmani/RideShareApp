package vo;

import java.io.Serializable;

public class GenericResponse implements Serializable{
 private Object data;
 private String errorMsg;
 private boolean isSuccess=false;
public Object getData() {
	return data;
}
public void setData(Object data) {
	this.data = data;
}
public String getErrorMsg() {
	return errorMsg;
}
public void setErrorMsg(String errorMsg) {
	this.errorMsg = errorMsg;
}
public boolean isSuccess() {
	return isSuccess;
}
public void setSuccess(boolean isSuccess) {
	this.isSuccess = isSuccess;
}
 
}
