package eu.boiled.android.zlog;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowLog.LogItem;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 23)
public class ZLogTest {

    @Test
    public void shouldShowErrorMessage() {
        ZLog.init(true);

        ZLog.e("Error message");

        assertLog()
                .hasErrorMessage(getClass().getName(), "Error message")
                .hasNoMoreMessages();
    }

    @Test
    public void shouldNotShowErrorMessageIfDisabled() {
        ZLog.init(false);

        ZLog.e("Error message");

        assertLog().hasNoMoreMessages();
    }

    @Test
    public void shouldShowDebugMessage() {
        ZLog.init(true);

        ZLog.d("Debug message");

        assertLog()
                .hasDebugMessage(getClass().getName(), "Debug message")
                .hasNoMoreMessages();
    }

    @Test
    public void shouldNotShowDebugMessageIfDisabled() {
        ZLog.init(false);

        ZLog.d("Debug message");

        assertLog().hasNoMoreMessages();
    }

    public LogAssert assertLog() {
        return new LogAssert(ShadowLog.getLogs());
    }

    private static class LogAssert {
        private final List<LogItem> items;
        private int index = 0;

        public LogAssert(final List<LogItem> items) {
            this.items = items;
        }

        public LogAssert hasDebugMessage(final String tag, final String message) {
            return hasMessage(Log.DEBUG, tag, message);
        }

        public LogAssert hasErrorMessage(final String tag, final String message) {
            return hasMessage(Log.ERROR, tag, message);
        }

        private LogAssert hasMessage(final int type, final String tag, final String message) {
            final LogItem item = items.get(index++);
            assertEquals(item.type, type);
            assertEquals(item.tag, tag);
            assertEquals(item.msg, message);
            return this;
        }

        public LogAssert hasNoMoreMessages() {
            assertEquals(index, items.size());
            return this;
        }
    }
}