package dao;

import java.io.IOException;
import java.io.InputStream;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import pojos.Image;
import pojos.Pool;
import pojos.User;
import utility.RideSharingUtil;

import com.google.common.io.ByteStreams;
import com.sun.jersey.core.util.Base64;

public class ImageDAOImpl implements ImageDao{
	SessionFactory sessionFactory = RideSharingUtil.getSessionFactoryInstance();

@Override
	public Boolean uploadImage(String userId,InputStream imgStream, String imageType, String descripion)
	{
		Boolean result=false;
		byte[] imageFile=null;
		try {
			imageFile=ByteStreams.toByteArray(imgStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Image image = new Image();
		User user= RideSharingUtil.getDaoInstance().getUserDetails(userId);
		image.setDescription(descripion);
		image.setImageBinary(imageFile);
		image.setUser(user);
		image.setUpdated(System.currentTimeMillis());
		image.setIsVerified(false);
		image.setImageType(imageType);
		Session session =sessionFactory.openSession();
		try
		{
		Transaction tx =session.beginTransaction();
		session.save(image);
		tx.commit();
		return true;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally
		{session.close();
		}
				
	}

@Override
public byte[] downloadImage(String userId) {
	// TODO Auto-generated method stub
	Session session = sessionFactory.openSession();
	Image image=null;
	Criteria cr = session.createCriteria(Image.class);
	User user = new User();
	user.setId(userId);
	cr.add(Restrictions.eq("user", user));
	if (cr.list() != null && cr.list().size() > 0) {
		image = (Image) cr.list().get(0);
		byte[] arr=(Base64.encode(image.getImageBinary()));
		return arr;
	}
	return null;
	
}
}
