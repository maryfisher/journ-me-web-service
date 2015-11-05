| Branch        | Build Status  |
| ------------- | ------------- |
| *develop*     | ![Build Status](https://img.shields.io/shippable/563a7c681895ca4474229806/develop.svg) |
| *production*  | ![Build Status](https://img.shields.io/shippable/563a7c681895ca4474229806/production.svg) |

# How to run the client app #

1. Install JDK 8 on PC (including setup of relevant environment variables)
2. Install Java Cryptographic Extension
3. Install Maven on PC (including setup of relevant environment variables)
4. Prepare app.properties at ../journ-me-web-service (staticResource.locations = file:///[Path to]/journ-me-client-app/lib/,file:///[Path to]/journ-me-client-app/.temp/)
5. Run launch.bat
6. Check if REST service is live at http://localhost:8080/api/internal/monitoring/healthchecks
7. The app is deployed at "localhost:8080/index.html"