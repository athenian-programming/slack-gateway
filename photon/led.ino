int led = D0;
int ledValue = -1;

void setup() {
    pinMode(led, OUTPUT);

	// Set up the API endpoints
    Spark.variable("getLed", &ledValue, INT);
    Spark.function("setLed", ledToggle);
}

void loop() {
	// Read the current value
    ledValue = digitalRead(led);
}

int ledToggle(String command) {
    if (command == "on") {
        digitalWrite(led, HIGH);
        return 1;
    }

    if (command == "off") {
        digitalWrite(led, LOW);
        return 0;
    }

    return -1;
}


