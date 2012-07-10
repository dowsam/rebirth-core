package cn.com.rebirth.core.poi.editor;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import cn.com.rebirth.core.poi.ExcelContext;
import cn.com.rebirth.core.poi.style.AlignDecorator;
import cn.com.rebirth.core.poi.style.BorderStyleDecorator;
import cn.com.rebirth.core.poi.style.ColorDecorator;
import cn.com.rebirth.core.poi.style.FillPatternDecorator;
import cn.com.rebirth.core.poi.style.ValignDecorator;

/**
 * The Class AbstractRegionEditor.
 *
 * @param <T> the generic type
 */
@SuppressWarnings("all")
public abstract class AbstractRegionEditor<T> extends AbstractEditor {

	/**
	 * Instantiates a new abstract region editor.
	 *
	 * @param context the context
	 */
	protected AbstractRegionEditor(ExcelContext context) {
		super(context);
	}

	/**
	 * New cell editor.
	 *
	 * @return the cell editor
	 */
	abstract protected CellEditor newCellEditor();

	/**
	 * New top cell editor.
	 *
	 * @return the cell editor
	 */
	abstract protected CellEditor newTopCellEditor();

	/**
	 * New bottom cell editor.
	 *
	 * @return the cell editor
	 */
	abstract protected CellEditor newBottomCellEditor();

	/**
	 * New left cell editor.
	 *
	 * @return the cell editor
	 */
	abstract protected CellEditor newLeftCellEditor();

	/**
	 * New right cell editor.
	 *
	 * @return the cell editor
	 */
	abstract protected CellEditor newRightCellEditor();

	/**
	 * Gets the cell range.
	 *
	 * @return the cell range
	 */
	abstract protected CellRangeAddress getCellRange();

	/**
	 * Border outer.
	 *
	 * @param borderStyle the border style
	 * @param borderColor the border color
	 * @return the t
	 */
	public T borderOuter(BorderStyleDecorator borderStyle, ColorDecorator borderColor) {
		this.borderBottom(borderStyle, borderColor);
		this.borderLeft(borderStyle, borderColor);
		this.borderRight(borderStyle, borderColor);
		this.borderTop(borderStyle, borderColor);
		return (T) this;
	}

	/**
	 * Border full.
	 *
	 * @param borderStyle the border style
	 * @param borderColor the border color
	 * @return the t
	 */
	public T borderFull(BorderStyleDecorator borderStyle, ColorDecorator borderColor) {
		CellEditor cellEditor = newCellEditor();
		cellEditor.border(borderStyle, borderColor);
		return (T) this;
	}

	/**
	 * Border left.
	 *
	 * @param borderStyle the border style
	 * @param borderColor the border color
	 * @return the t
	 */
	public T borderLeft(BorderStyleDecorator borderStyle, ColorDecorator borderColor) {
		//左边框
		CellEditor cellEditorLeft = this.newLeftCellEditor();
		cellEditorLeft.borderLeft(borderStyle, borderColor);
		return (T) this;
	}

	/**
	 * Border right.
	 *
	 * @param borderStyle the border style
	 * @param borderColor the border color
	 * @return the t
	 */
	public T borderRight(BorderStyleDecorator borderStyle, ColorDecorator borderColor) {
		//右边框
		CellEditor cellEditorRight = this.newRightCellEditor();
		cellEditorRight.borderRight(borderStyle, borderColor);
		return (T) this;
	}

	/**
	 * Border top.
	 *
	 * @param borderStyle the border style
	 * @param borderColor the border color
	 * @return the t
	 */
	public T borderTop(BorderStyleDecorator borderStyle, ColorDecorator borderColor) {
		//上边框
		CellEditor cellEditorTop = this.newTopCellEditor();
		cellEditorTop.borderTop(borderStyle, borderColor);
		return (T) this;
	}

	/**
	 * Border bottom.
	 *
	 * @param borderStyle the border style
	 * @param borderColor the border color
	 * @return the t
	 */
	public T borderBottom(BorderStyleDecorator borderStyle, ColorDecorator borderColor) {
		//下边框
		CellEditor cellEditorBottom = this.newBottomCellEditor();
		cellEditorBottom.borderBottom(borderStyle, borderColor);
		return (T) this;
	}

	/**
	 * Font.
	 *
	 * @param fontEditor the font editor
	 * @return the t
	 */
	public T font(FontOutEditor fontEditor) {
		CellEditor cellEditor = newCellEditor();
		cellEditor.font(fontEditor);
		return (T) this;
	}

	/**
	 * Bg color.
	 *
	 * @param bg the bg
	 * @return the t
	 */
	public T bgColor(ColorDecorator bg) {
		CellEditor cellEditor = newCellEditor();
		cellEditor.bgColor(bg);
		return (T) this;
	}

	/**
	 * Bg color.
	 *
	 * @param bg the bg
	 * @param fillPattern the fill pattern
	 * @return the t
	 */
	public T bgColor(ColorDecorator bg, FillPatternDecorator fillPattern) {
		CellEditor cellEditor = newCellEditor();
		cellEditor.bgColor(bg, fillPattern);
		return (T) this;
	}

	/**
	 * Align.
	 *
	 * @param align the align
	 * @return the t
	 */
	public T align(AlignDecorator align) {
		CellEditor cellEditor = newCellEditor();
		cellEditor.align(align);
		return (T) this;
	}

	/**
	 * V align.
	 *
	 * @param align the align
	 * @return the t
	 */
	public T vAlign(ValignDecorator align) {
		CellEditor cellEditor = newCellEditor();
		cellEditor.vAlign(align);
		return (T) this;
	}

	/**
	 * Warp text.
	 *
	 * @param autoWarp the auto warp
	 * @return the t
	 */
	public T warpText(boolean autoWarp) {
		CellEditor cellEditor = newCellEditor();
		cellEditor.warpText(autoWarp);
		return (T) this;
	}

	/**
	 * Merge.
	 *
	 * @return the t
	 */
	public T merge() {
		workingSheet.addMergedRegion(getCellRange());
		return (T) this;
	}

	/**
	 * Style.
	 *
	 * @param style the style
	 * @return the t
	 */
	public T style(CellStyle style) {
		CellEditor cellEditor = newCellEditor();
		cellEditor.style(style);
		return (T) this;
	}

	/**
	 * Hidden.
	 *
	 * @param hidden the hidden
	 * @return the t
	 */
	public T hidden(boolean hidden) {
		CellEditor cellEditor = newCellEditor();
		cellEditor.hidden(hidden);
		return (T) this;
	}

	/**
	 * Indent.
	 *
	 * @param indent the indent
	 * @return the t
	 */
	public T indent(int indent) {
		CellEditor cellEditor = newCellEditor();
		cellEditor.indent(indent);
		return (T) this;
	}

	/**
	 * Lock.
	 *
	 * @param locked the locked
	 * @return the t
	 */
	public T lock(boolean locked) {
		CellEditor cellEditor = newCellEditor();
		cellEditor.lock(locked);
		return (T) this;
	}

	/**
	 * Rotate.
	 *
	 * @param rotation the rotation
	 * @return the t
	 */
	public T rotate(int rotation) {
		CellEditor cellEditor = newCellEditor();
		cellEditor.rotate(rotation);
		return (T) this;
	}

	/**
	 * Width.
	 *
	 * @param width the width
	 * @return the t
	 */
	public T width(int width) {
		CellEditor cellEditor = newTopCellEditor();
		cellEditor.width(width);
		return (T) this;
	}

	/**
	 * Adds the width.
	 *
	 * @param width the width
	 * @return the t
	 */
	public T addWidth(int width) {
		CellEditor cellEditor = newTopCellEditor();
		cellEditor.addWidth(width);
		return (T) this;
	}

	/**
	 * Height.
	 *
	 * @param height the height
	 * @return the t
	 */
	public T height(float height) {
		CellEditor cellEditor = newLeftCellEditor();
		cellEditor.height(height);
		return (T) this;
	}

	/**
	 * Adds the height.
	 *
	 * @param height the height
	 * @return the t
	 */
	public T addHeight(float height) {
		CellEditor cellEditor = newLeftCellEditor();
		cellEditor.addHeight(height);
		return (T) this;
	}

	/**
	 * Data format.
	 *
	 * @param format the format
	 * @return the t
	 */
	public T dataFormat(String format) {
		CellEditor cellEditor = newLeftCellEditor();
		cellEditor.dataFormat(format);
		return (T) this;
	}

	/**
	 * Value.
	 *
	 * @return the object[]
	 */
	public Object[] value() {
		CellEditor cellEditor = newCellEditor();
		if (cellEditor.getWorkingCell().size() == 1) {
			return new Object[] { cellEditor.value() };
		} else {
			return (Object[]) cellEditor.value();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		CellEditor cellEditor = newCellEditor();
		return cellEditor.toString();
	}

}
