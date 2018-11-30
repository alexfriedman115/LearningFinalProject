package engine.display;

public class Piece extends DisplayObject {

	private int start;
	private int end;
	private boolean hasVoltage;
	private boolean hasResistance;
	private int voltage;
	private int resistance;
	
	public Piece(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public Piece(int start, int end)
	{
		super("");
		this.start = start;
		this.end = end;
		this.hasVoltage = false;
		this.hasResistance = false;
	}
	
	public Piece(int start, int end, boolean volt, boolean res)
	{
		super("");
		this.start = start;
		this.end = end;
		this.hasVoltage = volt;
		this.hasResistance = res;
	}
	
	public Piece(int start, int end, int volt, int res)
	{
		super("");
		this.start = start;
		this.end = end;
		this.hasVoltage = true;
		this.hasResistance = true;
		this.voltage = volt;
		this.resistance = res;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public boolean isHasVoltage() {
		return hasVoltage;
	}

	public void setHasVoltage(boolean hasVoltage) {
		this.hasVoltage = hasVoltage;
	}

	public boolean isHasResistance() {
		return hasResistance;
	}

	public void setHasResistance(boolean hasResistance) {
		this.hasResistance = hasResistance;
	}

	public int getVoltage() {
		return voltage;
	}

	public void setVoltage(int voltage) {
		this.voltage = voltage;
	}

	public int getResistance() {
		return resistance;
	}

	public void setResistance(int resistance) {
		this.resistance = resistance;
	}

}
