package cn.web.front.common;

public final class DeviceUtils {

    public static int findSourceId(final String deviceType) {

        if (deviceType.matches("^(?i)IPHONE|IPAD|IPOD.*")) {
            return 1;
        } else if (deviceType.matches("^(?i)ANDROID.*")) {
            return 2;
        } else if (deviceType.matches("^(?i)WECHAT.*")) {
            return 3;
        } else {
            return 0;
        }
    }

    public static int findDeviceId(final String deviceType) {

        int deviceId = findSourceId(deviceType);

        return deviceId == 1 ? 2 : (deviceId == 2 ? 1 : deviceId);
    }
}
