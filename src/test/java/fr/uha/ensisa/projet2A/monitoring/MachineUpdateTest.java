package fr.uha.ensisa.projet2A.monitoring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class MachineUpdateTest {
	
	MachineUpdate sut; //System Under Test
        
    @Before
    public void createTest() {
        sut = new MachineUpdate(); 
    }
    
    @Test
    public void setMachineID() {
    	assertEquals(0,sut.getMachineID()); 
    	int ID = 1; 
    	sut.setMachineID(ID);
    	assertEquals(ID, sut.getMachineID());
    }
    
    @Test
    public void setMachineName() {
    	assertNull(sut.getMachineName()); 
    	String name = "Machine name"; 
    	sut.setMachineName(name); 
    	assertEquals(name, sut.getMachineName());
    }
    
    @Test
    public void setState() {
    	assertEquals(0,sut.getState()); 
    	int state = 0; 
    	sut.setState(state); 
    	assertEquals(state, sut.getState());
    }
    
    @Test
    public void setStateLabel() {
    	assertNull(sut.getStateLabel()); 
    	String stateLabel = "Off"; 
    	sut.setStateLabel(stateLabel); 
    	assertEquals(stateLabel, sut.getStateLabel());
    }

}
