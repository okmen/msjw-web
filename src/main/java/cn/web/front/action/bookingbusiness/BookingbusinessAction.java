package cn.web.front.action.bookingbusiness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.booking.business.service.IBookingBusinessService;

/**
 * 预约类Action
 * @author Mbenben
 *
 */
@Controller
@RequestMapping(value="/bookingbusiness/")
@SuppressWarnings(value="all")
public class BookingbusinessAction {
	
	@Autowired
    @Qualifier("bookingBusinessService")
    private IBookingBusinessService bookingBusinessService;
	
}
	