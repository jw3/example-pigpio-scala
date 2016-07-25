pigpio-scala examples
-

### Flasher

The hello world of circuits, the flashing led.



### Building Examples

Two ways have been tested; Docker and Debian



### Docker and pigpio



```
docker run --rm \
    -u root --privileged \
    -v /sys:/sys -v /dev/mem:/dev/mem \
    -v /home/pi/pigpio:/pigpio -e LD_LIBRARY_PATH=/pigpio \
    192.168.0.10:5000/jwiii/example-pigpio-scala:0.1
    run-pigpio-image.sh
```
