import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Main {

	public Main() {
		new loginPage().setVisible(true);
	}

	public static void main(String[] args) {
		
		//SET UI LOOK
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		new Main();
	}
}