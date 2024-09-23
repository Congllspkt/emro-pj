package smartsuite.app.common.excel.data;

import java.util.List;

public class SimpleExcelRowHeader {

	// TextAlign
	public static final String HEADER_TEXTALIGN_CENTER = "center";
	public static final String HEADER_TEXTALIGN_LEFT = "left";
	public static final String HEADER_TEXTALIGN_RIGHT = "right";

	//boldText
	public static final boolean HEADER_BOLD_TEXT_TRUE = true;
	public static final boolean HEADER_BOLD_TEXT_FALSE = false;

	//Editable
	public static final boolean HEADER_EDITABLE_TRUE = true;
	public static final boolean HEADER_EDITABLE_FALSE = false;

	//visible
	public static final boolean HEADER_VISIBLE_TRUE = true;
	public static final boolean HEADER_VISIBLE_FALSE = false;

	//width
	public static final int HEADER_WIDTH_DEFUALT = 200;




	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isBoldText() {
		return boldText;
	}

	public void setBoldText(boolean boldText) {
		this.boldText = boldText;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}

	public List<String> getCombo() {
		return combo;
	}

	public void setCombo(List<String> combo) {
		this.combo = combo;
	}

	private String label;
	
	private String key;
	
	private boolean visible = true;
	
	private boolean editable = false;
	
	private boolean boldText = false;
	
	private int width = -1;
	
	private String textAlign;
	
	private List<String> combo;

	public static SimpleExcelRowHeader setExcelHedader(String label , String key , boolean boldText  ,
                                                       boolean editable , String textAlignType , boolean visible , int width, List<String> comboList){
		SimpleExcelRowHeader header = new SimpleExcelRowHeader();

		header.setLabel(label);
		header.setKey(key);
		header.setBoldText(boldText);
		header.setCombo(comboList);
		header.setEditable(editable);
		header.setTextAlign(textAlignType);
		header.setVisible(visible);
		header.setWidth(width);

		return header;
	}

	@Override
	public String toString() {
		return "{" +
				"label='" + label + '\'' +
				", key='" + key + '\'' +
				", visible=" + visible +
				", editable=" + editable +
				", boldText=" + boldText +
				", width=" + width +
				", textAlign='" + textAlign + '\'' +
				", combo=" + combo +
				'}';
	}
}
