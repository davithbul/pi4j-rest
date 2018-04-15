Pi4J-rest allows to control your motors and to manage your raspberry pi digital input output pins using rest API. Pi4J-rest enhances pi4j library allowing thread safe control of motors.

## Setup Raspberry PI
   *  In order to connect to pi without password we need to execute following steps
    Generate key (note please enter instead of setting password):
                    ssh-keygen

   *  Execute:
        ssh-copy-id -i ~/.ssh/id_rsa.pub pi@{RASPBERRY_PI_HOST}

   *  Verify that you are able to ssh pi without password
    ssh pi@{RASPBERRY_PI_HOST}

   *  (Optional) To connect Raspberry to wifi network
    Follow the instructions here: https://wiki.debian.org/WiFi/HowToUse


## Install mandatory libraries

   *  install pigpio
       rm pigpio.zip
       sudo rm -rf PIGPIO
       wget abyz.co.uk/rpi/pigpio/pigpio.zip
       unzip pigpio.zip
       cd PIGPIO
       make
       sudo make install


## Deploy project on Raspberry PI

   *  install pi4j in raspberry pi machine
    a. login to pi
        To install/update follow the instructions here: http://pi4j.com/install.html

   *  Navigate to project source code and execute
    ./gradlew deploy

Now if you open browser and navigate to http://{RASPBERRY_PI_HOST}:8080/health you should see {"status":"UP"}

## Setup pi4-rest as a service to run on Raspberry PI start
   *  copy from project scripts/pi4j-rest to /etc/init.d/

   *  Give execution permission:
    sudo chmod +x /etc/init.d/pi4j-rest

   *  Restart pi and test that it runs:
    sudo service pi4j-rest start

   *  Test that it stops:
    sudo service pi4j-rest stop

   *  Register service to run on startup
    a. open /etc/rc.local
    b. add following line before 'exit 0' line
        /etc/init.d/pi4j-rest start