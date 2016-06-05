## Slack Gateway

[Heroku](https://www.heroku.com/) server for routing [slash command](https://api.slack.com/slash-commands)
requests to Particle.io and Blynk.cc devices.

### Setup

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


### Slack Configuration

The Slack /led command is mapped to https://slack-gateway.herokuapp.com/led


### Photon Configuration

The [sketch for the Photon](https://github.com/pambrose/slack-gateway/blob/master/photon/led.ino) sets the LED to D0

The device name is assigned in [application.conf](https://github.com/pambrose/slack-gateway/blob/master/src/main/resources/application.conf)


### Heroku Configuration

The *slack.token* and *particle.token* values can be assigned as Config Vars on Heroku. They can
also be assigned in [application.conf](https://github.com/pambrose/slack-gateway/blob/master/src/main/resources/application.conf)