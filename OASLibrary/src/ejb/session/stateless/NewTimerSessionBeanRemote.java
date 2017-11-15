/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.util.Date;
import javax.ejb.Remote;

/**
 *
 * @author Edward
 */
@Remote
public interface NewTimerSessionBeanRemote {
    
    public void cancelStartTimer(Long auctionId);

    public void cancelEndTimer(Long auctionId);

    public void createEndTimer(Date end, Long auctionId);

    public void createStartTimer(Date start, Long auctionId);
}
