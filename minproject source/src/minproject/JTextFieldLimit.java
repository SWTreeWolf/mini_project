package minproject;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

//입력 글자수 제한하기 위한 내부클래스
@SuppressWarnings("serial")
public class JTextFieldLimit extends PlainDocument {
		private int limit;

		JTextFieldLimit(int limit) {
			super();
			this.limit = limit;
		}

		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null)
				return;
			if ((getLength() + str.length()) <= limit)
				super.insertString(offset, str, attr);
		}
	}
