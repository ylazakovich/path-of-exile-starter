# PATH OF EXILE STARTER

![Build Status](https://github.com/ylazakovich/path-of-exile-starter/actions/workflows/test.yml/badge.svg)

## Overview

- [What the service can do?](#what-is-about-service)
- [Roadmap](#roadmap)
    - [Telegram](#telegram)
    - [Aggregator](#aggregator)
- [Quick start](#quick-start)
- [FAQ](#faq)

## What is about Service

1) Easy start for trading in Path of Exile
2) Service is able to provide you positions for trade
3) You are able to manage it over Telegram

## Roadmap

### Telegram

Realized Telegram bot over implementation with Spring Webhook

| **Features**                                             |     **Status**     | **Units** | **Integration Tests** |
|----------------------------------------------------------|:------------------:|:---------:|:---------------------:|
| Database + Migration                                     | :white_check_mark: |           |                       |
| Subscribe Webhook after application Start                | :white_check_mark: |           |                       |
| Save user info in Database                               | :white_check_mark: |           |                       |
| Integration with Aggregator                              | :white_check_mark: |           |                       |
| Menu with buttons: Start, Settings, Feedback             | :white_check_mark: |           |                       |
| Submenu for Start: Skills, Blessing items                | :white_check_mark: |           |                       |
| Functionality for button Start                           | :white_check_mark: |           |                       |
| Functionality for button Skills                          | :white_check_mark: |           |                       |
| Functionality for button Blessing Items                  |                    |           |                       |
| Functionality for button Settings                        |                    |           |                       |
| Functionality for button Feedback                        |                    |           |                       |
| Telegram channel with <br/>Updates/News/Market positions |                    |           |                       |

### Aggregator

Spring service which aggregate data from the 3rd party places

| **Features**                                  |     **Status**     |     **Units**      |
|-----------------------------------------------|:------------------:|:------------------:|
| Database + Migration                          | :white_check_mark: |                    |
| Load data to Database after application start | :white_check_mark: |                    |
| Update data in Database by CRON               | :white_check_mark: |                    |
| Integration with PoeNinja                     | :white_check_mark: | :white_check_mark: |
| Swagger                                       |                    |                    |
| Analyzer                                      | :white_check_mark: |                    |
| Analyze skill positions                       | :white_check_mark: | :white_check_mark: |
| Analyze blessing positions                    |                    |                    |

## Quick start

![preview](https://github.com/ylazakovich/path-of-exile-starter/blob/main/config/demo.jpg)

// TODO: Later will be updated

## FAQ

// TODO: Later will be added
