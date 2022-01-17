package customs;

import controllers.ChromeController;
import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.picocontainer.PicoFactory;
import pages.TwilioVoiceClientPageImpl;
import utils.ReadConfigHelper;

public class CustomPicoFactory implements ObjectFactory {
    private PicoFactory delegate = new PicoFactory();

    public CustomPicoFactory()
    {
        addClass(ChromeController.class);
        addClass(ReadConfigHelper.class);
        addClass(TwilioVoiceClientPageImpl.class);
    }

    public void start() {
        delegate.start();
    }

    public void stop() {
        delegate.stop();
    }

    public boolean addClass(Class<?> aClass) {
        return delegate.addClass(aClass);
    }

    public <T> T getInstance(Class<T> aClass) {
        return delegate.getInstance(aClass);
    }
}
