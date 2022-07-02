package kao.frm.swing;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

class HeaderRendererKA extends DefaultTableCellRenderer
		{

			private static final long serialVersionUID = -5118552842765135758L;

			// Метод возвращает визуальный компонент для прорисовки
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column)
			{
				// Надпись из базового класса
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				// Выравнивание строки заголовка
				// label.setHorizontalAlignment(SwingConstants.CENTER);
				// Размещение изображения в заголовке
//				if (column == 1) label.setText(ResKA.getResourceBundleValue("FORM_SETTINGS_HEADERVALUE"));
//				else if (column == 0) label.setText(ResKA.getResourceBundleValue("FORM_SETTINGS_HEADERNAME"));

				// Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED);
				Border border = BorderFactory.createRaisedBevelBorder();
				label.setBorder(border);

				label.setBackground(UIManager.getColor("TableHeader.background"));
				label.setForeground(UIManager.getColor("TableHeader.foreground"));

				return label;
			}
		}
