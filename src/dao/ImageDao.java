package dao;

import java.io.InputStream;

public interface ImageDao {

	public Boolean uploadImage(String userId,InputStream imgStream, String imageType, String descripion);
	public byte[] downloadImage(String userId);
}
