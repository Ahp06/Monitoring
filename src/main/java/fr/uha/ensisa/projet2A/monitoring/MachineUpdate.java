package fr.uha.ensisa.projet2A.monitoring;

import java.sql.Timestamp;
import java.util.Date;

public class MachineUpdate {

	private int machineID;
	private String machineName;
	private int state;
	private String stateLabel;
	private Timestamp time;

	public int getMachineID() {
		return machineID;
	}

	public void setMachineID(int machineID) {
		this.machineID = machineID;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateLabel() {
		return stateLabel;
	}

	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return "MachineUpdate [machineID=" + machineID + ", machineName=" + machineName + ", state=" + state
				+ ", stateLabel=" + stateLabel + ", time=" + time + "]";
	}

}
