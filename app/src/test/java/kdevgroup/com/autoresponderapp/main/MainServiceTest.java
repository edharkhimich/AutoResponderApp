package kdevgroup.com.autoresponderapp.main;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowService;

import kdevgroup.com.autoresponderapp.BuildConfig;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainServiceTest {

    private MainCallService service;
    private ShadowService shadow;
    private Context context;

    @Before
    public void setUp() {
        service = Robolectric.setupService(MainCallService.class);
        shadow = shadowOf(service);
        context = RuntimeEnvironment.application;
    }

    @Test
    public void serviceNotNull() {
        assertNotNull(service);
    }

    @Test
    public void stopSelfService() {
        service.stopSelf();
        assertTrue(shadow.isStoppedBySelf());
    }

}
