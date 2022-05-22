JavaMPD
=======

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.inthebacklop/javampd/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.inthebacklop/javampd)
[![Build Status](https://travis-ci.org/finnyb/javampd.svg?branch=develop)](https://travis-ci.org/finnyb/javampd)
[![Coverage Status](https://coveralls.io/repos/github/finnyb/javampd/badge.svg?branch=develop)](https://coveralls.io/github/finnyb/javampd?branch=develop)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=finnyb_javampd&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=finnyb_javampd)

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/dashboard?id=finnyb_javampd)

Java API for controlling the Music Player Daemon (MPD)

Maven Dependency:

```
<dependency>
  <groupId>com.inthebacklog</groupId>
  <artifactId>javampd</artifactId>
  <version>7.0.0</version>
</dependency>
```

To connect to mpd using the defaults of localhost and port 6600 
```
MPD mpd = MPD.builder().build();
```

or build to your environment

```
MPD mpd = MPD.builder()
        .server("yourserver")
        .port(yourport)
        .password(yourpassword)
        .build();
```

Full documentation can be found [here](http://finnyb.github.io/javampd/7.0.0)
