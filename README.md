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

* The Slack /led command is mapped to https://slack-gateway.herokuapp.com/led


### Photon Configuration

* The sketch for the Photon is [here](https://github.com/pambrose/slack-gateway/blob/master/photon/led.ino)

* The LED is connected to pin D0

