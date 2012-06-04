package informatikforum.chatbox.gui;

import informatikforum.chatbox.business.BusinessLogic;
import android.text.style.ClickableSpan;
import android.view.View;

public class LinkSpan extends ClickableSpan {
	private String url;
	private BusinessLogic bl;

	public LinkSpan(BusinessLogic bl, String url) {
		this.bl = bl;
		this.url = url;
	}

	@Override
	public void onClick(View widget) {
		bl.openLink(url);
	}
}
