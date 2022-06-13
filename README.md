Telemetry

Android application to display real time telemetry data from Dirt Rally 2.0
This is a POC. New features to be added

## Setup

Make sure that game is not running

### Android
1. `git clone git@github.com:gmt0/DirtRally2-Telemetry.git`
2. Build APK


### Dirt Rally 2.0
1. Open `Documents/My Games/DiRT Rally 2.0/hardwaresettings/hardware_settings_config.xml`
2. Go to `<motion_platform>`
3. Set `udp enabled="true"`
4. Set `extradata="3"`
5. Set `ip="your ip"`
6. Set `port="20777"`

### Play
1. Connect phone to usb and open the app
2. Run game
3. Drive the car an check real time data being displayed in the android activity

## To add
1. Network setup from UI
2. Save telemetry data to cloud
3. Diplay historical data and graphs
4. Car dashboard themes
5. UI customisation
