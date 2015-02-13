package work;

import java.util.List;

import model.ApplicationDAO;
import model.Model;

import org.genericdao.RollbackException;

import thirdPartyAPI.Instagram;
import util.Constants;
import util.Util;
import databeans.ApplicationData;
import databeans.Photo;

public class DefaultAccountsUpdateTask implements Runnable {
	Model model;

	public static boolean needToUpdate(ApplicationDAO applicationDAO) {
		ApplicationData appData = applicationDAO.getApplicationData();
		long nextUpdateTime = appData.getNextUpdateTime();
		long currentTime = System.currentTimeMillis();
		if (nextUpdateTime > currentTime) {
			Util.i("no need to update, skip");
			return false;
		}
		appData.setNextUpdateTime(nextUpdateTime + Constants.UPDATE_INTERVAL);
		Util.i("need to execute update, nextUpdateTime = ", nextUpdateTime,
				", currentTime = ", currentTime);
		return true;
	}

	public static String accessToken;

	public static void setValidToken(String accessToken) {
		DefaultAccountsUpdateTask.accessToken = accessToken;
	}

	public DefaultAccountsUpdateTask(Model model) {
		this.model = model;
	}

	@Override
	public void run() {
		if (accessToken == null) {
			Util.e("accessToken is null");
			return;
		}
		if (!needToUpdate(model.applicationDAO)) {
			return;
		}
		for (int i = 0; i < Constants.defaultUserTag.length; i++) {
			String userName = Constants.defaultUser[i];
			String tag = Constants.defaultUserTag[i];

			List<Photo> photos = Instagram.getPictureOf(accessToken, tag);
			for (Photo photo : photos) {
				photo.setUserName(userName);
				try {
					model.getPhotoDAO().create(photo);
				} catch (RollbackException e) {
					Util.e(e);
				}
			}
		}

	}

}
