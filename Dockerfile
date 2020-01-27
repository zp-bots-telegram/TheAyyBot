FROM java:8-alpine
MAINTAINER Zack Pollard <zackpollard@ymail.com>

VOLUME /bot
WORKDIR /bot

ADD target/bot.jar /bot/bot.jar

CMD java -jar ./bot.jar