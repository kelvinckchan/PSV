package app.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of userInfos. This is used for saving the list of
 * userInfos to XML.
 * 
 * @author Marco Jakob
 */
@XmlRootElement(name = "userInfos")
public class UserInfoWrapper {

	private List<UserInfo> userInfos;

	@XmlElement(name = "userInfo")
	public List<UserInfo> getUserInfos() {
		return userInfos;
	}

	public void setUserInfos(List<UserInfo> userInfos) {
		this.userInfos = userInfos;
	}
}