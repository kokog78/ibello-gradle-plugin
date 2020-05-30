package hu.ibello.gradle;

import java.util.List;

public abstract class IbelloTestTask extends IbelloTask {

	private List<String> tags;
	private boolean headless;
	private String browser;
	private int[] size;
	private int repeat = 0;
	
	public List<String> getTags() {
		return tags;
	}
	
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public boolean isHeadless() {
		return headless;
	}
	
	public void setHeadless(boolean headless) {
		this.headless = headless;
	}
	
	public String getBrowser() {
		return browser;
	}
	
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	
	public int[] getSize() {
		return size;
	}
	
	public void setSize(int x, int y) {
		this.size = new int[] {x, y};
	}
	
	public int getRepeat() {
		return repeat;
	}
	
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	
	@Override
	protected List<String> getCalculatedCommand(String command) {
		List<String> result = super.getCalculatedCommand(command);
		if (headless) {
			result.add("--headless");
		}
		if (tags != null) {
			for (String tag : tags) {
				appendArgument(result, "--tag", tag);
			}
		}
		appendArgument(result, "--browser", browser);
		if (size != null) {
			String value = String.format("%dx%d", size[0], size[1]);
			appendArgument(result, "--size", value);
		}
		if (repeat > 1) {
			appendArgument(result, "--repeat", Integer.toString(repeat));
		}
		return result;
	}
}
