package rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import pojos.Coupons;
import pojos.RedeemedCoupons;
import utility.GlobalConstants;
import utility.RideSharingUtil;
import vo.ApplyCouponVO;
import vo.GenericResponse;
@Path("coupons")
public class CouponService {

@POST
@Path("/apply")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public GenericResponse checkCouponValidity(ApplyCouponVO applyCouponVO){
	
	String userId = applyCouponVO.getUserId();
	String metaData = applyCouponVO.getMetaData();
	String couponCode = applyCouponVO.getCouponCode();
	GenericResponse response = new GenericResponse();
	Session session = RideSharingUtil.getSessionFactoryInstance().openSession();
	Criteria cr = session.createCriteria(Coupons.class);
	List<Coupons> coupons = cr.add(Restrictions.eq("couponCode", couponCode)).list();
	if(coupons!=null && coupons.size() > 0){
		Coupons coupon = coupons.get(0);
		if(!coupon.isValid()){
			response.setSuccess(false);
			response.setErrorMsg("Sorry, coupon already expired !");
		}
		else if(coupon.isApplicableMultipleTimes()){
			redeemCoupon(response,userId,couponCode,metaData,session);
		}
		else {
		cr = session.createCriteria(RedeemedCoupons.class);
		List<RedeemedCoupons> redemptions = cr.add(Restrictions.eq("couponCode",couponCode))
				.add(Restrictions.eq("userId", userId)).list();
		if(redemptions != null && redemptions.size() > 0){
			response.setSuccess(false);
			response.setErrorMsg("Coupon already redeemed !");
		}else{
			redeemCoupon(response,userId,couponCode,metaData,session);
		}
	}
	}
	else{
		response.setErrorMsg("invalid coupon");
	}
	session.close();
	return response;
}

private void redeemCoupon(GenericResponse response, String userId, String couponCode, String metaData,Session session) {
	// TODO Auto-generated method stub
	Transaction tx = session.beginTransaction();
	RedeemedCoupons redemption = new RedeemedCoupons();
	redemption.setUserId(userId);
	redemption.setCouponCode(couponCode);
	redemption.setMetaData(metaData);
	redemption.setSetlled(false);
	redemption.setTimeOfRedemption(System.currentTimeMillis());
	if(GlobalConstants.FREE_RECHARGE.equals(couponCode)){
		// metadata should be in format - <mobileNo> , <carrier> , <connectionType>
		String args[] = metaData.split(",");
		if(!(args != null && args.length == 3)){
			response.setSuccess(false);
			response.setErrorMsg("Please provide complete mobile information");
			return;
		}
	}
	session.save(redemption);
	tx.commit();
	response.setSuccess(true);
}
}
