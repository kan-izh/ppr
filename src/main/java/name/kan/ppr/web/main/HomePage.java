package name.kan.ppr.web.main;

import name.kan.ppr.web.csv.UploadCsvFormPanel;
import org.apache.wicket.markup.html.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kan
 * @since 2013-01-24 21:37
 */
public class HomePage extends WebPage
{
	private static final Logger log = LoggerFactory.getLogger(HomePage.class);
	private static final long serialVersionUID = 992503113234757275L;

	public HomePage()
	{
		add(new UploadCsvFormPanel("upload"));
	}

}
