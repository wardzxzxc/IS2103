/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timer;

import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author Edward
 */
@Local
public interface NewTimerSessionBeanLocal {
    


    public void cancelStartTimer(Long auctionId);

    public void cancelEndTimer(Long auctionId);

    public void createEndTimer(Date end, Long auctionId);

    public void createStartTimer(Date start, Long auctionId);
}
