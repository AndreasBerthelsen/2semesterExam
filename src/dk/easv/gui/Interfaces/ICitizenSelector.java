package dk.easv.gui.Interfaces;

import dk.easv.be.Citizen;

/**
 * Denne klasse sætter en citizen info i de klasser hvor vi har behov for at sætte en bestemt ciizen
 */
public interface ICitizenSelector {
    void setCitizen(Citizen citizen);
}
