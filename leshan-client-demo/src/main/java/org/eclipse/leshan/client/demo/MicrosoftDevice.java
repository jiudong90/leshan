package org.eclipse.leshan.client.demo;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;
import org.eclipse.leshan.util.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jiudongl on 10/17/2016.
 */
public class MicrosoftDevice extends BaseInstanceEnabler {
    private static final Logger LOG = LoggerFactory.getLogger(MicrosoftDevice.class);

    private String data = "init";

    public MicrosoftDevice() {
        // notify new date each 5 second
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                data = RandomStringUtils.random(4, new char[]{'a','b','c','d','e','f', '1', '2', '3'});
                fireResourcesChange(13);

            }
        }, 5000, 100);
    }

    @Override
    public ReadResponse read(int resourceid) {
        LOG.info("Read on Device Resource " + resourceid);
        switch (resourceid) {
            case 0:
                return ReadResponse.success(resourceid, getDescription());
            default:
                return super.read(resourceid);
        }
    }

    @Override
    public ExecuteResponse execute(int resourceid, String params) {
        LOG.info("Execute on Device resource " + resourceid);
        if (params != null && params.length() != 0)
            System.out.println("\t params " + params);
        return ExecuteResponse.success();
    }

    @Override
    public WriteResponse write(int resourceid, LwM2mResource value) {
        LOG.info("Write on Device Resource " + resourceid + " value " + value);
        switch (resourceid) {
            case 0:
                return WriteResponse.notFound();
            default:
                return super.write(resourceid, value);
        }
    }

    private String getDescription() {
        return "Microsoft Demo Device " + data;
    }

    private long getErrorCode() {
        return 0;
    }

    private int getBatteryLevel() {
        final Random rand = new Random();
        return rand.nextInt(100);
    }

    private int getMemoryFree() {
        final Random rand = new Random();
        return rand.nextInt(50) + 114;
    }

    private Date getCurrentTime() {
        return new Date();
    }

    private String utcOffset = new SimpleDateFormat("X").format(Calendar.getInstance().getTime());;

    private String getUtcOffset() {
        return utcOffset;
    }

    private void setUtcOffset(String t) {
        utcOffset = t;
    }

    private String timeZone = TimeZone.getDefault().getID();

    private String getTimezone() {
        return timeZone;
    }

    private void setTimezone(String t) {
        timeZone = t;
    }

    private String getSupportedBinding() {
        return "U";
    }
}
