package awesome.app.data;

public class HelpItem {

	private String name = "";
	private String info = "";
	
	public HelpItem()
	{
		
	}
	
	public HelpItem(String name, String info){
		this.name = name;
		this.info = info;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public String getInfo(){
		return this.info;
	}
}
