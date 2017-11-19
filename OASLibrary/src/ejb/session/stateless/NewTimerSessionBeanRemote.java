package ejb.session.stateless;

import java.util.Date;


public interface NewTimerSessionBeanRemote {
    
    public void cancelStartTimer(Long auctionId);

    public void cancelEndTimer(Long auctionId);

    public void createEndTimer(Date end, Long auctionId);

    public void createStartTimer(Date start, Long auctionId);
}
