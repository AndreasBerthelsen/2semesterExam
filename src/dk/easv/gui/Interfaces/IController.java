package dk.easv.gui.Interfaces;

import dk.easv.be.User;
/**
 * Denne klasse sætter en users info i de klasser hvor vi har behov for at sætte en bestemt user
 */
public interface IController {
    void setUserInfo(User user);
}
