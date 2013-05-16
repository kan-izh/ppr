package name.kan.wicket.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;

/**
* @author kan
* @since 2013-05-16 23:47
*/
public class HideNullModelBehavior extends Behavior
{
	private static final long serialVersionUID = 6661293458927167190L;

	public static final HideNullModelBehavior INSTANCE = new HideNullModelBehavior();

	private HideNullModelBehavior()
	{
	}

	@Override
	public void onConfigure(final Component component)
	{
		component.setVisible(component.getDefaultModelObject() != null);
	}
}
