Pi4J-rest allows to control your motors and to manage your raspberry pi digital input output pins using rest API. Pi4J-rest enhances pi4j library allowing thread safe control of motors.

## Setup Raspberry PI
In order to connect to pi without password we need to execute following steps:

   1. Generate key (use empty password)
   ```sh
   $ ssh-keygen
   $ ssh-copy-id -i ~/.ssh/id_rsa.pub pi@<YOUR_PI_HOST>
   ```                        
        
                   
   2.  Verify that you are able to ssh pi without password
    Generate key:
   ```sh
   $ ssh pi@<YOUR_PI_HOST>
   ```   
    
   3.  (Optional) [Connect Raspberry to WiFi network]


## Install Required Libraries

1. install pigpio

   ```sh
   $ rm pigpio.zip
   $ sudo rm -rf PIGPIO
   $ wget abyz.co.uk/rpi/pigpio/pigpio.zip
   $ unzip pigpio.zip
   $ cd PIGPIO
   $ make
   $ sudo make install
   ```                           


## Deploy project on Raspberry PI

   *  install pi4j in raspberry pi machine
        1. login to pi
        2. To install/update follow the instructions here: http://pi4j.com/install.html

   *  Navigate to project source code and execute

   ```sh
   $ ./gradlew deploy
   ```   

Now if you open browser and navigate to http://<YOUR_PI_HOST>:8080/health you should see {"status":"UP"}

## Setup pi4-rest as a service to run on Raspberry PI start
   *  copy from project scripts/pi4j-rest to /etc/init.d/

   *  Give execution permission:

   ```sh
   $ sudo chmod +x /etc/init.d/pi4j-rest
   ```   

   *  Restart pi and test that it runs:

   ```sh
   $ sudo service pi4j-rest start
   ```   

   *  Test that it stops:

   ```sh
   $ sudo service pi4j-rest stop
   ```   

   *  Register service to run on startup
    a. open /etc/rc.local
    b. add following line before 'exit 0' line
        /etc/init.d/pi4j-rest start
        
        
   [Connect Raspberry to WiFi network]: <https://raspberrypihq.com/how-to-connect-your-raspberry-pi-to-wifi/>        