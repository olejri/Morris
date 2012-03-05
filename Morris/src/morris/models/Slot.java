package morris.models;

public class Slot {
	
	private boolean taken;
	private boolean enabled;
	
	public Slot(){
		enabled = false;
		taken = false;
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	public boolean isTaken(){
		return taken;
	}
	
	public void setTaken(boolean taken){
		this.taken = taken;
	}
}
