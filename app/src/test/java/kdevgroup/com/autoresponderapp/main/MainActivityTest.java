package kdevgroup.com.autoresponderapp.main;

import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaScannerConnection;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowService;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private MainActivity activity;
    private MainCallService service;
    private ShadowService shadow;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
                .get();
        service = Robolectric.setupService(MainCallService.class);
        shadow = shadowOf(service);
    }

    @Test
    public void activityNotNull() {
        assertNotNull(activity);
    }

    @Test
    public void serviceNotNull() {
        assertNotNull(service);
    }

    @Test
    public void shouldUnbindServiceAtShadowApplication() {
        ShadowApplication shadowApplication = shadowOf(RuntimeEnvironment.application);
        ServiceConnection conn = Shadow.newInstanceOf(MediaScannerConnection.class);
        service.bindService(new Intent("dummy"), conn, 0);
        assertThat(shadowApplication.getUnboundServiceConnections().size(), Matchers.equalTo(0));
        service.unbindService(conn);
        assertThat(shadowApplication.getUnboundServiceConnections(), hasSize(1));
    }

    @Test
    public void shouldUnbindServiceSuccessfully() {
        ServiceConnection conn = Shadow.newInstanceOf(MediaScannerConnection.class);
        service.unbindService(conn);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldUnbindServiceWithExceptionWhenRequested() {
        ShadowApplication.getInstance().setUnbindServiceShouldThrowIllegalArgument(true);
        ServiceConnection conn = Shadow.newInstanceOf(MediaScannerConnection.class);
        service.unbindService(conn);
    }
}
