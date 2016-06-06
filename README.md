# Slack Gateway for Reading and Writing Microcontroller Values

Gateway running on [Heroku](https://www.heroku.com/) for routing [Slash command](https://api.slack.com/slash-commands)
requests to Particle.io and Blynk.cc devices.

## Usage

From Slack:

```
/particle on
/particle off
/particle value

/blynk on
/blynk off
/blynk value
```

## Setup

Clone this repo:

```bash
$ git clone https://github.com/pambrose/slack-gateway.git
$ cd slack-gateway
```

Install [Heroku CLI](https://devcenter.heroku.com/articles/heroku-command) with:

```bash
$ brew install heroku
```

Create Heroku app with:

```bash
$ heroku create app-name
```

## Configuration

### Slack

The Slack **/blynk** command is mapped to a POST on https://slack-gateway.herokuapp.com/blynk
and the **/particle** command is mapped to a POST on https://slack-gateway.herokuapp.com/particle.


### Photon

The [sketch for the Photon](https://github.com/pambrose/slack-gateway/blob/master/photon/led.ino) sets the LED to D0.

## Blynk

The Blynk device is a [SparkFun Blynk Board](https://www.sparkfun.com/products/13794). The LED in this example is the
onboard LED at D5.

### Heroku

Assign *slack.token*, *particle.token*, and *blynk.token* values as Heroku Config Vars or in
[application.conf](https://github.com/pambrose/slack-gateway/blob/master/src/main/resources/application.conf).

## Deployment

Deploy the gateway server to Heroku with:

```bash
$ make deploy
```

## Debugging

You can view the request params of a /led command in Slack with:

```bash
/led debug
```

You can view the Heroku logs with:

```bash
$ make logs
```

