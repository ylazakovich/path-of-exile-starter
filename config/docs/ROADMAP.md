## Status

### Telegram

Realized Telegram bot over implementation with Spring Webhook

| **Features**                                 |     **Status**     | **Integration Tests** |
|----------------------------------------------|:------------------:|:---------------------:|
| Database + Migration                         | :white_check_mark: |                       |
| Subscribe Webhook after application Start    | :white_check_mark: |                       |
| Save user info in Database                   | :white_check_mark: |                       |
| Integration with Aggregator                  | :white_check_mark: |                       |
| Welcome message                              | :white_check_mark: |                       |
| Menu with buttons: Start, Settings, Feedback | :white_check_mark: |                       |
| Submenu for Start: Skills, Blessing items    | :white_check_mark: |                       |
| Button Start                                 | :white_check_mark: |                       |
| Button Skills                                | :white_check_mark: |                       |
| Button Refresh Skills                        | :white_check_mark: |                       |
| Button Blessing Items                        |                    |                       |
| Button Settings                              |                    |                       |
| Button Feedback                              |                    |                       |

### Aggregator

Spring service which aggregate data from the 3rd party places

| **Features**                                  |     **Status**     |   **Unit Tests**   |
|-----------------------------------------------|:------------------:|:------------------:|
| Database + Migration                          | :white_check_mark: |                    |
| Load data to Database after application start | :white_check_mark: |                    |
| Update data in Database by CRON               | :white_check_mark: |                    |
| Integration with PoeNinja                     | :white_check_mark: | :white_check_mark: |
| Swagger                                       |                    |                    |
| Analyzer                                      | :white_check_mark: |                    |
| Analyze skill positions                       | :white_check_mark: | :white_check_mark: |
| Analyze blessing positions                    |                    |                    |
