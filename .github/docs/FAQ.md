# 📢 FAQ

#### Q: Does Telegram bot work over Webhook ?

Yes, bot uses webhooks implementation \
Application is realized over `Spring` framework

#### Q: Which resources do you use for checking actual prices ?

Currently, bot check prices over https://poe.ninja/

#### Q: How to up bot locally ?

So, for that purpose I would recommend you to use ngrok \
Then go into [BotConfiguration.class](https://github.com/ylazakovich/path-of-exile-starter/blob/main/modules/telegram/src/main/java/io/starter/telegram/config/BotConfiguration.java)

- webhook - links from ngrok
- token - BotFather provide this token

<details>
<summary>Example</summary>

```java

@Key("TELEGRAM_BOT_WEBHOOK")
@DefaultValue("https://1234-567-890-00-111.ngrok-free.app")
String webHook();

@Key("TELEGRAM_BOT_TOKEN")
@DefaultValue("1234567890:AABBCC-DDDDD")
String token();
```

</details>

Then

**Option 1**

1. run `docker-compose.yml`
2. start application from aggregator module
3. start application from telegram module

**Option 2** (not ready yet)

Run over console `app.sh`

Then \
Bot is ready for working

#### Q: Should I run api call for assigning webhook ?

No, you don't need to do it \
Bot has controller which assign webhook to bot after starting application

#### Q: Which database you have decided to use ?

I decided to use `mariadb` \
Also application use migrations flow over flyway mechanism

#### Q: Are you planning to make a documentation ?

It is possible, but at the moment I have no a lot of free time \
I haven't finished yet `1.0.0` version

#### Q: Are you controlling library versions ?

Repository is connected to renovate with \
regular checking and managing \
new versions of libs

#### Q: How do you control stability of your app ?

Repository is connected to github actions

1. Each pull request/merge into `main` triggers unit tests
2. Integrations tests are in progress now and later will be done

- Every release should run pipeline with running these tests
- Nightly builds will have a rule for running these tests only manually

#### Q: Are you planning to make a chat for communicate about this project ?

Yes, I have a plan for that in future \
Currently, I would recommend you to stay your feedback \
over `issues` or `discussion`
