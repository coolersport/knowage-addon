# knowage-addon
Dropin addon code for Knowage BI

## How to:

Drop all classes under webapps/knowage/WEB-INF/classes keeping their respective package folder structure. They will be loaded first and used instead of those come with Knowage.

## Purposes

1. Using BCrypt for internal password encryption.
2. Secure implementation of SsoServiceInterface (instead of default FakeSsoService)

## Application

See https://github.com/coolersport/docker-secure-knowage-server.
